## 链接

 - `ln -s`
 当我们需要在不同的目录，用到相同的文件时，我们不需要在每一个需要的目录下都放一个必须相同的文件，我们只要在某个固定的目录，放上该文件，然后在其它的 目录下用`ln`命令链接`(link)`它就可以，不必重复的占用磁盘空间。
 e.g.
 `ln -s /bin/less /usr/local/bin/less`

 `-s`(symbolic)参数是代号的意思。

 这里有两点要注意：
   - 1.`ln`命令会保持每一处链接文件的同步性，也就是说，不论你改动了哪一处，其它的文件都会发生相同的变化；
   - 2，`ln`的链接又分**软链接**和**硬链接**两种，**软链接**就是`ln -s ** **`,它只会在你选定的位置上生成一个文件的镜像，不会占用磁盘空间，**硬链接**`ln ** **`,没有参数`-s`, 它会在你选定的位置上生成一个和源文件大小相同的文件，无论是软链接还是硬链接，文件都保持同步变化。 

 如果你用`ls`察看一个目录时，发现有的文件后面有一个`@`的符号，那就是一个用`ln`命令生成的文件，用`ls -l`命令去察看，就可以看到显示的`link`的路径了。

 
 ## 配置Jenkins实例容器的Docker Host
  1) Firstly, install Docker plugin on Jenkins
 
  2) Go to Manage Jenkins -> System Configuration -> Add Cloud -> Docker
 
  3) Run another container that can mediate between host and jenkins container because that in the docker host uri field you have to enter unix or tcp address of the docker host. But since you are running jenkins as container, the container cant reach docker host unix port.
     Follow the instructions on [alpine socat](https://hub.docker.com/r/alpine/socat/) to create socat container.
     
  4) After the creating socat container, you can go back the docker configuration in jenkins and enter tcp://socat-container-ip:2375
  
  5) Used below two commands to find ip address of socat container : 
     ```shell
        docker ps
        docker inspect container_name | grep IPAddress
     ```