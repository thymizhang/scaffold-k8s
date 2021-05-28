## 目录说明
本目录包含项目在两种kubernetes环境的部署方式，本地部署（local）和阿里云部署（aliyun）。


### Docker镜像构建及上传到仓库
1. 准备镜像构建文件  
1.1. 在微服务模块根目录下创建镜像构建文件Dockerfile，本项目Dockerfile文件参考springboot官方格式编写；  
2. 构建微服务镜像并上传到远程仓库  
2.1. 在idea安装`Alibaba Cloud`插件；  
2.2. 在idea选择：Tools->Alibaba Cloud->Deploy to Registry / Kubernetes->Deploy to Registry；  
2.3. 在Deploy to Registry界面中填写以下内容：
```text
name: 填写构建配置的名称
Image:Build Image:Context Directory: 选择需要构建的模块目录
Image:Build Image:Dockerfile: 选择模块的镜像构建文件，通常放在模块的根目录下
Image:Build Image:Version(tag): 填写镜像的版本号，建议与pom.xml文件中一致
Image Repositories: 选择镜像仓库，使用阿里巴巴镜像仓库，选择Alibaba Cloud Container Registry，在下方选择区域和命名空间后，再在列表中选择需要上传镜像的仓库
Before launch: 创建镜像前的操作，这里添加一个maven命令，再次编译打包后构建镜像，注意要选择模块的目录
```   
2.4. 在idea的`Services`界面中(Alt+8)，将`Deploy to Registry / Kubernetes`添加进来，右键选择刚才添加的构建配置，在右键菜单中选择`Run`，等待构建并上传镜像到阿里云，中间会提示输入阿里云账号密码。  


### 本地部署
1. 本地环境搭建  
1.1. 需要安装的软件包括：Docker desktop、minikube或kubernetes(Docker Desktop版)；  
1.2. 在idea安装`kubernetes`插件；  
2. 编写yaml部署文件  
2.1. 部署策略：由于服务注册与发现采用Nacos，所有微服务都不需要使用k8s的service来做服务发现，所以只需将gateway通过service暴露即可；  
2.2. 具体yaml参见[`scaffold-server-gateway`](/resource/k8s/local/scaffold-server-gateway.yaml)等文件；  
3. 将项目部署到本地minikube环境   
3.1. 启动minikube，在idea的`Terminal`(Alt+F12)运行`start-minikube.ps1`；   
3.2. 切换docker环境，在idea的`Terminal`(Alt+F12)运行`switch-docker.ps1`；  
3.3. 添加运行监控，在idea的`Services`(Alt+8)界面中，将`kubernetes`添加进来；  
3.4. 部署方式一：在yaml部署文件(/resource/k8s/local/*.yaml)右键，菜单中选择Kubernetes->Apply to Content...；  
3.5. 部署方式二：在idea的`Terminal`(Alt+F12)，进入目录`/resource/k8s/local`，执行`kubectl apply -f .`；  
3.6. 监控方式一：在idea的`Services`(Alt+8)界面中选择minikube，观察deployment、pod、service是否运行；  
3.7. 监控方式二：在idea的`Terminal`(Alt+F12)执行`kubectl get all`，查看k8s当前所有pod；  
3.8. 测试：需要先将minikube中k8s的service暴露出来，在idea的`Terminal`(Alt+F12)执行`minikube service server-gateway`；  
3.9. 测试：通过以上方式可以得到server-gateway的对外访问地址(通常是http://127.0.0.1:xxxxx)，然后使用postman等工具开始测试；  
4. 将项目部署到本地kubernetes(Docker Desktop版)环境    
4.1. 在Docker Desktop中启动kubernetes，无需切换docker环境；  
4.2. 部署方式同上；  
4.3. 监控方式：如果安装了kubernetes dashboard，在PowellShell中执行`kubectl proxy`，然后在浏览器中访问（需要token）：http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/；  
4.4. 测试：`scaffold-server-gateway.yaml`中定义service的nodePort就是访问端口，测试地址为http://localhost:访问端口，然后使用postman等工具开始测试；  


### 阿里云部署
* 前提条件：已经购买阿里云kubernetes集群  
1. 开发工具安装插件  
1.1. 在idea安装`Alibaba Cloud`插件；  
1.2. 参考：https://help.aliyun.com/document_detail/100254.html；  
2. 本地kubectl连接测试  
2.1. 修改配置文件：$home/.kube/config，将阿里云ACK的公网访问连接信息拷贝过来（记得备份原文件）；  
2.2. 查看当前连接的k8s环境是否为阿里云：`kubectl config current-context`；  
2.3. 查看当前连接的k8s环境的集群信息：`kubectl cluster-info`；  
2.4. 查看当前连接的k8s环境的节点信息：`kubectl get nodes`；  
3. 阿里云ACK环境配置  
3.1. 在阿里云ACK环境安装免密插件，方便拉取私有仓库镜像，或者将仓库设为公开，并与ACK在同一个区域中（比如：杭州、深圳）；  
3.2. 免密插件参考：https://help.aliyun.com/document_detail/159703.html?spm=5176.smartservice_service_chat.0.0.7eb8709aUwU6lM；  
3.3. 网络配置（非常重要）：需要让安装了nacos的ECS安全组允许k8s集群的访问，通过修改k8s的coredns实现；  
3.3.1. 第一步：在安装了nacos的ECS安全组配置规则，允许k8s集群的PodCIDR以及集群交换机网段访问所有端口；  
3.3.2. 第二步：PodCIDR网段在集群基本信息中获取，集群交换机网段在集群资源-节点虚拟交换机-点击进入查看；  
3.3.2. 第三步：修改coredns，使用命令`kubectl edit configmap coredns -n kube-system`；     
3.3.3. 第四步：在打开coredns的yaml文件中添加需要解析的host；   
3.3.4. 第五步：保存coredns的yaml后，使其生效，使用命令`kubectl scale deployment coredns -n kube-system --replicas=n`，示例如下；  
```text
# 添加解析
kubectl edit configmap coredns -n kube-system
# 添加dns解析，添加在`kubernetes cluster.local in-addr.arpa ip6.arpa`之后
        hosts {
            192.168.0.79 mysql.gceasy.cc
            192.168.0.79 mysqlmaster.gceasy.cc
            192.168.0.79 mysqlslave.gceasy.cc
            192.168.0.79 redis.gceasy.cc
            192.168.0.79 rabbitmq.gceasy.cc
            192.168.0.79 nacos.gceasy.cc
            192.168.0.79 sentinel.gceasy.cc
            fallthrough
        }
# 应用configmap到coredns
kubectl scale deployment coredns -n kube-system --replicas=0
kubectl scale deployment coredns -n kube-system --replicas=2
```
4. 使用命令行将项目部署到阿里云ACK环境  
4.1. 登录docker镜像仓库(如果仓库公开，无需登录)，比如：`docker login --username=**** registry.cn-hangzhou.aliyuncs.com`；  
4.2. 暴露服务的service需要绑定SLB（重要），在service的yaml文件中添加参数：`service.beta.kubernetes.io/alibaba-cloud-loadbalancer-id`和``；  
4.3. 绑定SLB参考1：https://help.aliyun.com/document_detail/181518.html?spm=5176.smartservice_service_chat.0.0.66663f1bI7SCAO；  
4.4. 绑定SLB参考2：https://help.aliyun.com/document_detail/86531.html?spm=a2c4g.11186623.6.769.5ef3586ccNAtMb；  
4.5. 命令行部署：在命令行进入k8s部署文件目录（`/resouce/k8s/aliyun/`），执行`kubectl apply -f ****.yaml`；  
4.6. 查看容器运行情况，执行`kubectl get all`，检查`service/server-gateway`的地址和端口是否映射成功；  
4.7. 测试：`scaffold-server-gateway.yaml`中定义service的targetPort就是访问端口，测试地址为 [ http://SLB的ip:访问端口 ] ，然后使用postman等工具开始测试；  
  

参考：https://help.aliyun.com/document_detail/100254.html


### 本地安装kubernetes(Docker Desktop版)
* 本地如果不适用Minikube搭建Kubernetes环境，可以使用Docker Desktop自带的Kubernetes，配置过程如下  
1. 下载Kubernetes镜像  
1.1. 配置镜像加速器，打开Docker Desktop的Settings，切换到Docker Engine选项卡，将`registry-mirrors`的值设置为`https://nw6rhy9r.mirror.aliyuncs.com`（该值来自阿里云容器镜像服务）；  
1.2. 由于被墙，需要使用阿里云的项目来拉取镜像，git clone https://github.com/AliyunContainerService/k8s-for-docker-desktop.git；  
1.3. 然后运行目录中的`load_images.ps1`文件；  
1.4. 等待镜像下载完成，使用命令`docker images`查看镜像；  
2. 启动Kubernetes  
2.1. 打开Docker Desktop的Settings，切换到Kubernetes选项卡，勾选`Enable Kubernetes`开启kubernetes；  
2.2. 等待Kubernetes启动完成，使用命令`kubectl cluster-info`查看集群信息；  
2.3. 使用命令`kubectl get pods -n kube-system`查看pods运行状态；  
3. 安装Kubernetes控制台  
3.1. 在https://github.com/kubernetes/dashboard项目中找到最新版本的下载地址；  
3.2. 下载部署文件`recommended.yaml`，由于被墙，可以在阿里云服务器下载，使用命令wget https://raw.githubusercontent.com/kubernetes/dashboard/v2.2.0/aio/deploy/recommended.yaml；  
3.3. 部署Kubernetes dashboard，使用命令`kubectl apply -f recommended.yaml`；  
3.4. 开启 API Server 访问代理，使用命令`kubectl proxy`；  
3.5. 在浏览器中打开控制台，http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/；  
3.6. 使用下面的命令获取令牌；  
   ```
   $TOKEN=((kubectl -n kube-system describe secret default | Select-String "token:") -split " +")[1]
   kubectl config set-credentials docker-for-desktop --token="${TOKEN}"
   echo $TOKEN
   ```


### 容器日志采集
参考：https://help.aliyun.com/document_detail/87540.html?spm=5176.110652https://help.aliyun.com/document_detail/87540.html?spm=5176.11065259.1996646101.searchclickresult.54c369bdmNbEuo&aly_as=Mta9aHK4  
参考：https://blog.csdn.net/yangchao1125/article/details/103180832  


### 问题
* k8s容器时间和宿主机时间相差8小时  
> 