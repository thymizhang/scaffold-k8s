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
1.1. 需要安装的软件包括：Docker desktop和minikube；  
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
4. 测试  
4.1. 需要先将minikube中k8s的service暴露出来，在idea的`Terminal`(Alt+F12)执行`minikube service server-gateway`；  
4.2. 通过以上方式可以得到server-gateway的对外访问地址(通常是http://127.0.0.1:xxxxx)，然后使用postman等工具开始测试；    


### 阿里云部署
1. 安装插件  
1.1. 在idea安装`Alibaba Cloud`插件；  


### 问题
* k8s容器时间和宿主机时间相差8小时  
> 