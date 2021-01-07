#!/bin/bash

######################################################################################
# 服务安装环境：CentOS 8.x 

# 安装包准备:
# 1 jdk-11.0.8_linux-x64_bin.rpm
# 2 erlang-23.1.1-1.el8.x86_64.rpm
# 3 rabbitmq-server-3.8.9-1.el8.noarch.rpm
# 4 redis-6.0.8.tar.gz
# 5 nacos-server-1.4.0.tar.gz
# 6 sentinel-dashboard-1.8.0.jar

######################################################################################

# 关闭防火墙
#systemctl stop firewalld.service
#systemctl disable firewalld.service

# 网络访问配置
setsebool -P httpd_can_network_connect 1
setsebool -P httpd_setrlimit 1

# 关闭SELINUX
sed -i "s/SELINUX=enforcing/SELINUX=disabled/g" /etc/selinux/config

# 主机名
sed -i "s/localhost.localdomain/gcesev-dev/g"  /etc/hostname
# ip映射
echo "192.168.213.140 gcesev-dev" >> /etc/hosts
echo "192.168.213.140 www.gceasy.cc" >> /etc/hosts
echo "192.168.213.140 mysql.gceasy.cc" >> /etc/hosts
echo "192.168.213.140 mysqlslave.gceasy.cc" >> /etc/hosts
echo "192.168.213.140 redis.gceasy.cc" >> /etc/hosts
echo "192.168.213.140 rabbitmq.gceasy.cc" >> /etc/hosts
echo "192.168.213.140 nacos.gceasy.cc" >> /etc/hosts
echo "192.168.213.140 sentinel.gceasy.cc" >> /etc/hosts

# 文件句柄
echo '# 打开文件句柄数量' >> /etc/sysctl.conf
echo fs.file-max = 655360 >> /etc/sysctl.conf
echo \* soft nofile 65535 >> /etc/security/limits.conf
echo \* hard nofile 65535 >> /etc/security/limits.conf
echo \* soft nproc 65535 >> /etc/security/limits.conf
echo \* hard nproc 65535 >> /etc/security/limits.conf
# 内存
echo '# 防止redis启动报错' >> /etc/sysctl.conf
echo net.core.somaxconn = 1024 >> /etc/sysctl.conf
echo vm.overcommit_memory = 1 >> /etc/sysctl.conf
sysctl -p

# 开机启动权限
chmod +x /etc/rc.d/rc.local
echo '# 防止redis启动报错' >> /etc/rc.d/rc.local
echo 'echo never > /sys/kernel/mm/transparent_hugepage/enabled' >> /etc/rc.d/rc.local

# 编辑器设置显示行号
echo set number >> /etc/vimrc

# 添加EPEL源
#dnf -y install https://dl.fedoraproject.org/pub/epel/epel-release-latest-8.noarch.rpm

# 安装工具
dnf install -y screen
dnf install -y make automake
dnf install -y gcc gcc-c++
dnf install -y openssl openssl-devel


######################################################################################


# JDK 安装，tomcat、nacos、maven运行需要
cd /usr/local
rpm -ivh /usr/local/jdk-11.0.8_linux-x64_bin.rpm
rm -rf /usr/local/jdk-11.0.8_linux-x64_bin.rpm


######################################################################################


# MySQL 安装
# MySQL8.0默认已经是utf8mb4字符集，所以字符集不再修改
dnf install -y @mysql
systemctl enable --now mysqld

# 修改配置
# 禁用ONLY_FULL_GROUP_BY, 支持非聚合列进行GROUP BY.
sed -i "4asql_mode = \"STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION\"" /etc/my.cnf
# 关闭MySQL主机查询dns
sed -i "4askip-name-resolve" /etc/my.cnf
sed -i "4a[mysqld]" /etc/my.cnf


######################################################################################


# Redis 安装
cd /usr/local
# redis安装需要
dnf install -y tcl

tar -zxvf /usr/local/redis-6.0.8.tar.gz
mv /usr/local/redis-6.0.8 /usr/local/redis
cd /usr/local/redis
make install
# 检查安装问题 make test

cd /usr/local
rm -rf /usr/local/redis-6.0.8.tar.gz

# 创建日志和数据目录
mkdir -pv /usr/local/redis/logs /usr/local/redis/data

# 守护进程启动
sed -i "s/daemonize no/daemonize yes/g" /usr/local/redis/redis.conf
# 支持systemctl启动
sed -i "s/supervised no/supervised systemd/g" /usr/local/redis/redis.conf
# 日志文件
sed -i "s/logfile \"\"/logfile \"\/usr\/local\/redis\/logs\/redis.log\"/g" /usr/local/redis/redis.conf
sed -i "s/dir .\//dir \"\/usr\/local\/redis\/data\"/g" /usr/local/redis/redis.conf
# 开启AOF持久化, 文件持久化方式, 如果同时使用RDB和AOF两种持久化机制，那么redis重启的时候，会使用AOF来构建数据，因为AOF的数据更加完整。AOF恢复慢, 占用空间大, RDB恢复快, 占用空间小. 
#sed -i "s/appendonly no/appendonly yes/g"  /usr/local/redis/redis.conf
# 修改以下两项, 让redis可以远程访问
sed -i "s/protected-mode yes/protected-mode no/g"  /usr/local/redis/redis.conf
sed -i "s/bind 127.0.0.1/#bind 127.0.0.1/g"  /usr/local/redis/redis.conf

# redis系统开机启动文件
echo [Unit] >> /usr/lib/systemd/system/redis.service
echo Description=redis-server >> /usr/lib/systemd/system/redis.service
echo After=network.target >> /usr/lib/systemd/system/redis.service
echo  >> /usr/lib/systemd/system/redis.service
echo [Service] >> /usr/lib/systemd/system/redis.service
echo ExecStart=/usr/local/bin/redis-server /usr/local/redis/redis.conf --supervised systemd >> /usr/lib/systemd/system/redis.service
echo ExecStop=/usr/local/bin/redis-cli shutdown >> /usr/lib/systemd/system/redis.service
echo Restart=always >> /usr/lib/systemd/system/redis.service
echo Type=forking >> /usr/lib/systemd/system/redis.service
echo  >> /usr/lib/systemd/system/redis.service
echo [Install] >> /usr/lib/systemd/system/redis.service
echo WantedBy=multi-user.target >> /usr/lib/systemd/system/redis.service


######################################################################################


#  rabbitmq安装
cd /usr/local

# erlang安装, 下载地址: https://github.com/rabbitmq/erlang-rpm/releases/
dnf install -y socat
rpm -ivh /usr/local/erlang-23.1.1-1.el8.x86_64.rpm
rm -rf /usr/local/erlang-23.1.1-1.el8.x86_64.rpm

# RabbitMQ 安装, 下载地址https://www.rabbitmq.com/download.html
rpm -ivh /usr/local/rabbitmq-server-3.8.9-1.el8.noarch.rpm
rm -rf /usr/local/rabbitmq-server-3.8.9-1.el8.noarch.rpm

# 安装后提示要更换的路径
sed -i "s/\/var\/run/\/run/g"  /usr/lib/tmpfiles.d/rabbitmq-server.conf
sed -i "s/\/var\/run/\/run/g"  /usr/lib/tmpfiles.d/subscription-manager.conf

# 启用管理插件
rabbitmq-plugins enable rabbitmq_management


######################################################################################


# Nacos安装, 下载地址: https://github.com/alibaba/nacos/releases
cd /usr/local
tar -zxvf /usr/local/nacos-server-1.4.0.tar.gz
rm -rf /usr/local/nacos-server-1.4.0.tar.gz

# 日志配置
sed -i "s/2GB/1GB/g" /usr/local/nacos/conf/nacos-logback.xml
sed -i "s/7GB/2GB/g" /usr/local/nacos/conf/nacos-logback.xml
sed -i "s/>7</>3</g" /usr/local/nacos/conf/nacos-logback.xml
# 以下地址根据实际情况修改
sed -i "s/# nacos.inetutils.ip-address=/nacos.inetutils.ip-address=192.168.213.140/g" /usr/local/nacos/conf/application.properties


######################################################################################


# Sentinel-dashboard安装
cd /usr/local
mkdir -pv /usr/local/sentinel/bin
mv /usr/local/sentinel-dashboard-1.8.0.jar /usr/local/sentinel/sentinel-dashboard-1.8.0.jar
echo screen -dmS sentinel java -Dserver.port=9090 -jar /usr/local/sentinel/sentinel-dashboard-1.8.0.jar >> /usr/local/sentinel/bin/startup.sh
chmod +x /usr/local/sentinel/bin/*.sh


######################################################################################


# 配置mysql开机启动
systemctl enable mysqld.service

# 配置redis开机启动
systemctl enable redis.service

# 配置rabbitmq开机启动
systemctl enable rabbitmq-server.service

# 配置nacos开机启动
echo >> /etc/rc.d/rc.local
echo bash /usr/local/nacos/bin/startup.sh -m standalone >> /etc/rc.d/rc.local

# 配置sentinel开机启动
echo >> /etc/rc.d/rc.local
echo bash /usr/local/sentinel/bin/startup.sh >> /etc/rc.d/rc.local


######################################################################################


# 清理安装脚本
rm -rf /usr/local/installl.sh 


######################################################################################
# 手动配置以下内容
######################################################################################

clear

echo "###########################################################"
echo "# 重启服务器后,手动配置以下内容,建议提前复制以下内容"
echo "###########################################################"
echo 
echo "# MySQL安全配置"
echo "# 第一步 : 账号安全"
echo "mysql_secure_installation"
echo
echo "# 1 要求你配置VALIDATE PASSWORD component（验证密码组件）： 输入y ，回车进入该配置"
echo "# 2 选择密码验证策略等级， 我这里选择0 （low），回车"
echo "# 3 输入新密码两次(GCeasy1818.)"
echo "# 4 确认是否继续使用提供的密码？输入y ，回车"
echo "# 5 移除匿名用户？ 输入y ，回车"
echo "# 6 不允许root远程登陆？ 我这里需要远程登陆，所以输入n ，回车"
echo "# 7 移除test数据库？ 输入y ，回车"
echo "# 8 重新载入权限表？ 输入y ，回车"
echo
echo "# 第二步 : 设置远程登录"
echo "mysql -uroot -p"
echo "use mysql;"
echo "update user set host='%' where user='root';"
echo "flush privileges;"
echo
echo 
echo "# 为RabbitMQ设置账号密码, 以下命令需要逐行执行: "
echo "rabbitmqctl add_user root runhui802"
echo "rabbitmqctl set_user_tags root administrator"
echo "rabbitmqctl set_permissions -p \"/\" root \".*\" \".*\" \".*\""
echo "rabbitmqctl delete_user guest"
echo "# 设置镜像队列策略"
echo "rabbitmqctl set_policy ha-all \"^\" '{\"ha-mode\":\"all\"}'"
echo "# 重启服务后生效"
echo "systemctl restart rabbitmq-server"
echo 
echo "# 创建数据库并导入表结构"
echo "1 创建业务数据库: gceasy_demo , 导入OAuth2相关表: oauth2.sql"
echo
echo "# 创建nacos命名空间并导入配置"
echo "1 创建命名空间: GCEASY_DEV, GCEASY_TEST, GCEASY_PROD"
echo "2 在public空间导入sentinel配置"
echo "3 在GCEASY_DEV空间导入业务配置"
echo
echo
echo "# 安装检查"
echo "JDK检查： java -version"
echo "Redis检查：redis-cli，连接成功后输入ping，返回pong表示正常"
echo "RabbitMQ检查：在浏览器访问 http://192.168.213.140:15672"
echo "Nacos检查：在浏览器访问 http://192.168.213.140:8848/nacos ，用户和密码：nacos"
echo "Sentinel检查：在浏览器访问 http://192.168.213.140:9090 ，用户和密码：sentinel"


# MySQL8修改密码
#ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '新密码';
#FLUSH PRIVILEGES;