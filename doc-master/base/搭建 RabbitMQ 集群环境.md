# RabbitMQ 集群环境的安装和配置

## 配置信息

目前集群配置了两个节点，使用的是镜像模式

| 服务器地址 |  web | 账号密码|
| --- | --- | --- |
| 10.24.10.12 | [访问地址](http://10.24.10.12:15672/) | csp123321/csp#123321 |
| 10.24.10.15 | |  

## 集群环境的安装及配置


### 安装 Erlang

参考 [erlang 的安装](https://packages.erlang-solutions.com/erlang/)，注意集群安装时，最好每个集群机器上的 erlang 版本一致

``` bash
# Centos安装EPEL扩展源
rpm -ivh http://dl.fedoraproject.org/pub/epel/6/x86_64/epel-release-6-8.noarch.rpm
# 添加erlang yum源
vim /etc/yum.repos.d/erlang-solutions.repo
[erlang-solutions]
name=Centos $releasever - $basearch - Erlang Solutions
baseurl=http://binaries.erlang-solutions.com/rpm/centos/$releasever/$basearch
gpgcheck=1
gpgkey=http://binaries.erlang-solutions.com/debian/erlang_solutions.asc
enabled=1
# 导入key
rpm --import http://binaries.erlang-solutions.com/debian/erlang_solutions.asc
# 使用指定源安装
yum install erlang --enablerepo=erlang-solutions
# 安装指定版本的 erlang，如 19.3
yum install erlang-19.3 --enablerepo=erlang-solutions

# 卸载 erlang
yum remove erlang
# 查看 erlang-erts 的版本
yum list installed | grep erlang-erts
卸载对应版本的  erlang-erts
yum remove erlang-erts.x86_64
```

### 安装 RabbitMQ

- 下载包 ` curl http://www.rabbitmq.com/releases/rabbitmq-server/v3.6.9/rabbitmq-server-3.6.9-1.el6.noarch.rpm -o rabbitmq-server-3.6.9-1.el6.noarch.rpm`
- 安装：`rpm --import https://www.rabbitmq.com/rabbitmq-signing-key-public.asc`
- 安装：`sudo yum install -y rabbitmq-server-3.6.9-1.el6.noarch.rpm`
- 启动服务：
    - 先看下自己的主机名：`hostname`，我的主机名是：**FTPCU1**
    - 先修改一下 host 文件：`vim /etc/hosts`，添加一行：`127.0.0.1 FTPCU1`（必须这样做）
    - 启动：`service rabbitmq-server start`，启动一般都比较慢，所以别急
    - 停止：`service rabbitmq-server stop`
    - 重启：`service rabbitmq-server restart`
	- 设置开机启动：`chkconfig rabbitmq-server on`

#### 配置

- 查找默认配置位置：`find / -name "rabbitmq.config.example"`，我这边搜索结果是：`/usr/share/doc/rabbitmq-server-3.6.9/rabbitmq.config.example`
- 复制默认配置：`cp /usr/share/doc/rabbitmq-server-3.6.9/rabbitmq.config.example /etc/rabbitmq/`
- 修改配置文件名：`cd /etc/rabbitmq ; mv rabbitmq.config.example rabbitmq.config`
- 编辑配置文件，开启用户远程访问：`vim rabbitmq.config`
	- 在 64 行，默认有这样一句话：`%% {loopback_users, []},`，注意，该语句最后有一个逗号，等下是要去掉的
	- 我们需要改为：`{loopback_users, []}`
- 开启 Web 界面管理：`rabbitmq-plugins enable rabbitmq_management`
- 重启 RabbitMQ 服务：`service rabbitmq-server restart`
- 开放防火墙端口：
	- `sudo iptables -I INPUT -p tcp -m tcp --dport 15672 -j ACCEPT`
	- `sudo iptables -I INPUT -p tcp -m tcp --dport 5672 -j ACCEPT`
	- `sudo service iptables save`
	- `sudo service iptables restart`
- 浏览器访问：`http://10.24.10.224:15672`
	默认管理员账号：**guest**
	默认管理员密码：**guest**

### 集群搭建
1. 分别在 10.24.10.12、10.24.10.15 上安装 erlang、rabbitmq
2. 修改每个节点的 `/etc/hosts`，添加
    
    ```
    10.24.10.12 FTPCU1
    10.24.10.15 FTPCT2
    ```
3. 设置 Erlang Cookie，将 FTPCU1 的 cookie 文件拷贝到 FTPCT2 对应位置来，文件路径为 `/var/lib/rabbitmq/.erlang.cookie`
    
    ``` bash
    # 默认文件级别是 400，不能进行覆盖
    chmod 777 /var/lib/rabbitmq/.erlang.cookie
    # 将文件权限还原
    chmod 400 /var/lib/rabbitmq/.erlang.cookie
    chown rabbitmq /var/lib/rabbitmq/.erlang.cookie
    chgrp rabbitmq /var/lib/rabbitmq/.erlang.cookie
    ```
4. 使用 -detached 参数运行各节点

    ```
    rabbitmqctl stop
    rabbitmq-server -detached
    ```
5. 将 FTPCT2 与 FTPCU1 组成集群
 
    ```
    rabbitmqctl stop_app 
    rabbitmqctl join_cluster rabbit@FTPCU1
    rabbitmqctl start_app
    ```
6. 设置镜像队列策略，即队列会被复制到各个节点，各个节点状态保持一致。
    
    ```
    rabbitmqctl set_policy ha-all "^" '{"ha-mode":"all"}'
    ```

## 注意事项

1. 搭建集群时，设置完 FTPCT2 的 cookie，启动 rabbitmq-server 时，出现 `Authentication failed (rejected by the remote node), please check the Erlang cookie`
    
    可能是 rabbitmq 没有关闭干净，执行 `ps -ef|grep rabbitmq`，将所有的进程都杀掉

## 参考

[rabbitmq 配置](http://www.rabbitmq.com/configure.html)
[rabbitmq 集群配置](http://www.rabbitmq.com/clustering.html)
[rabbitmq 高可用配置](http://www.rabbitmq.com/ha.html)
[rabbitmq 集群和高可用配置](https://88250.b3log.org/rabbitmq-clustering-ha)

