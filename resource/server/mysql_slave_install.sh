#!/bin/bash

######################################################################################
# 服务安装环境：CentOS 8.x 

# 关闭防火墙
systemctl stop firewalld.service
systemctl disable firewalld.service

# 主机名
sed -i "s/localhost.localdomain/mysql-slave/g"  /etc/hostname
# ip映射
echo "192.168.213.181 mysql-slave" >> /etc/hosts

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

# 修改配置文件
# 禁用ONLY_FULL_GROUP_BY, 支持非聚合列进行GROUP BY.
sed -i "4asql_mode = \"STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION\"" /etc/my.cnf
# 指定需要同步的数据库, 数据库名称根据需要修改, 多个数据库增加多行, 默认是同步所有的库( *重要 )
sed -i "4areplicate_do_db=scaffold_project_1" /etc/my.cnf
sed -i "4areplicate_do_db=scaffold_project_0" /etc/my.cnf
sed -i "4areplicate_do_db=scaffold_company" /etc/my.cnf
sed -i "4areplicate_do_db=scaffold_user" /etc/my.cnf
# 设为只读模式, 不影响主从同步
sed -i "4aread_only=1" /etc/my.cnf
# 关闭MySQL主机查询dns
sed -i "4askip-name-resolve" /etc/my.cnf
# 从数据库id, 需要在全局唯一
sed -i "4aserver-id=2" /etc/my.cnf
sed -i "4a[mysqld]" /etc/my.cnf


######################################################################################
# 手动配置以下内容
######################################################################################

clear

echo "###########################################################"
echo "# 重启服务器后,手动配置以下内容,建议提前复制以下内容"
echo "###########################################################"
echo 
echo "# MySQL-Slave安全配置"
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
echo "# 第二步 : 安全设置，添加只读远程访问账户"
echo "mysql -uroot -p"
echo "use mysql;"
echo "create user readman IDENTIFIED by 'test2020';"
echo "grant select on scaffold_user.* to readman;"
echo "grant select on scaffold_company.* to readman;"
echo "grant select on scaffold_project_0.* to readman;"
echo "grant select on scaffold_project_1.* to readman;"
echo "flush privileges;"
echo 
echo "# 第三步 : 检查my.cnf配置中replicate_do_db数据库是否正确(重要)"
echo
echo "# 第四步 : 设置同步参数, 获得MASTER数据库的参数(SHOW MASTER STATUS)"
echo "# 需要修改的参数 : MASTER_LOG_FILE, MASTER_LOG_POS"
echo "CHANGE MASTER TO"
echo "MASTER_HOST='192.168.213.180',"
echo "MASTER_USER='replica',"
echo "MASTER_PASSWORD='test2020',"
echo "MASTER_LOG_FILE='mysql-bin.000001',"
echo "MASTER_LOG_POS=2241;"
echo 
echo "# 第五步 : 开启主从同步, 请确保主数据库开启，检查Slave_IO_Running、Slave_SQL_Running同步状态"
echo "start slave;"
echo "show slave status\G;"
echo
echo "# 第六步 : 同步成功后，回到主数据库的第五步配置 <<===="
echo

######################################################################################

# 相关问题
# 如果遇到 Last_Error: Error , 可能从库没有主库的表, 可以跳过这条语句
#mysql> stop slave; 
#mysql> set global sql_slave_skip_counter=20;
#mysql> start slave;
#mysql> show slave status\G;
# 检查Slave_IO_Running和Slave_SQL_Running两个参数是否为yes


#非空主库主从设置
# 很多时候，设置主从复制的时候，主库已经运行了一段时间，有了一些业务数据，那么这个时候我们首先将主库设置为只读状态，不允许新数据写入，然后查看当前的归档日志状态，记录下来后将数据库备份出来，恢复从库的可写状态，最后把备份出来的数据库恢复到从库中，再设置为从刚才查询出来的归档日志和归档日志的位置开始进行同步即可。

# 将数据库设为只读,  注意该操作会导致数据库无法写入, 适用于特殊情况, 比如主从需要重新同步或数据库迁移时, 主数据库需要防止新数据写入时
#mysql> show global variables like "%read_only%";
#mysql> flush tables with read lock;
#mysql> set global read_only=1;
#mysql> show global variables like "%read_only%";

# 恢复数据库的读写状态
#mysql> unlock tables;
#mysql> set global read_only=0;
