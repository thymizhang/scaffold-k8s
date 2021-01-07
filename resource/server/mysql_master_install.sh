#!/bin/bash

######################################################################################


# 关闭防火墙
systemctl stop firewalld.service
systemctl disable firewalld.service

# 主机名
sed -i "s/localhost.localdomain/mysql-master/g"  /etc/hostname
# ip映射
echo "192.168.213.180 mysql-master" >> /etc/hosts

# 开机启动权限
chmod +x /etc/rc.d/rc.local

# 添加EPEL源
dnf -y install https://dl.fedoraproject.org/pub/epel/epel-release-latest-8.noarch.rpm

# 安装工具
dnf install -y screen
dnf install -y make automake
dnf install -y gcc gcc-c++
dnf install -y openssl openssl-devel


######################################################################################


# MySQL slave安装
# MySQL8.0默认已经是utf8mb4字符集，所以字符集不再修改
dnf install -y @mysql
systemctl enable --now mysqld

# 修改配置
# 禁用ONLY_FULL_GROUP_BY, 支持非聚合列进行GROUP BY.
sed -i "4asql_mode = \"STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION\"" /etc/my.cnf
# 关闭MySQL主机查询dns
sed -i "4askip-name-resolve" /etc/my.cnf
# 主数据库id, 需要在全局唯一
sed -i "4aserver-id=1" /etc/my.cnf
sed -i "4alog-bin=mysql-bin" /etc/my.cnf
sed -i "4a[mysqld]" /etc/my.cnf


######################################################################################
# 手动配置以下内容
######################################################################################

clear

echo "###########################################################"
echo "# 重启服务器后,手动配置以下内容,建议提前复制以下内容"
echo "###########################################################"
echo 
echo "# MySQL-Master安全配置"
echo "# 第一步 : 账号安全"
echo "mysql_secure_installation"
echo
echo "# 1 要求你配置VALIDATE PASSWORD component（验证密码组件）： 输入y ，回车进入该配置"
echo "# 2 选择密码验证策略等级， 我这里选择0 （low），回车"
echo "# 3 输入新密码两次(test2020)"
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
echo
echo "# 第三步 : 配置主从复制账号"
echo "set global validate_password.policy=0;"
echo "set global validate_password.length=1;"
echo "CREATE USER 'replica'@'192.168.213.181' IDENTIFIED WITH mysql_native_password BY 'test2020';"
echo "GRANT REPLICATION SLAVE ON *.* TO 'replica'@'192.168.213.181';"
echo "flush privileges;"
echo 
echo "# 第四步 : 获取主节点当前binary log文件名和位置（position），然后进行从数据库的第四步配置====>>"
echo "SHOW MASTER STATUS;"
echo
echo "# 第五步 : 等待主从同步后，创建数据库, 导入表结构"
echo "CREATE DATABASE scaffold_user DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
echo "CREATE DATABASE scaffold_company DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
echo "CREATE DATABASE scaffold_project_0 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
echo "CREATE DATABASE scaffold_project_1 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
echo