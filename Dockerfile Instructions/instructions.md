## 指令说明

指令格式一般为`<instruction><argument>`,包括`FROM`、`MAITNTAINER`、`RUN`等，简列如下：

```shell
FROM	#指定所创建镜像的基础镜像
MAINTAINER	#指定维护者信息
RUN	#运行命令
CMD	#定启动容器时默认执行的命令
LABEL	#指定生成镜像的元数据标签信息
EXPOSE	#声明镜像内服务所监听的端口
ENV	#指定环境变量
ADD	#复制指定的路径下的内容到容器下
COPY	#复制本地主机的路径下的内容到镜像中
ENTRYPOINT	#指定镜像的默认入口
VOLUME	#创建数据卷挂载点
USER	#指定运行容器时的用户名或UID
WORKDIR	#配置工作目录
ARG	#指定镜像内使用的参数（如版本号信息等）
ONBUILD	#配置当前所创建的镜像作为其他镜像的基础镜像时，所执行的创建操作指令
STOPSIGNAL	#容器退出的信号值
HEALTHCHECK	#如何进行健康检查
SHELL	#指定使用shell时的默认shell类型
```

下面分别进行介绍。

### FROM
功能为指定基础镜像，并且必须是第一条指令。如果本地镜像不存在或者镜像仓库中不存在，则回去DockerHub下载指定镜像。 如果在同一个Dockerfile中创建多个镜像，可以使用多个FROM指令，可用于分阶段构建，例如：（每个镜像一次）。
```shell
#基础阶段
FROM xxxx AS base
RUN xxxx
WORKDIR /app
EXPOSE 8080

#build阶段
FROM xxxxxxx/build AS build
RUN xxxxxxx
WORKDIR /src

COPY [xxx,xxx,xxx]
COPY [xxx,xxx,xxx]

RUN xxxxx
COPY . .
RUN donet build "xxxxxx" -c Release -o /app/build

#publish阶段
FROM build AS publish
RUN donet publish "xxxxxx" -c Release -o /app/publish

#
FROM base AS final
```

语法：

`FROM <image>`
`FROM <image>:<tag>`
`FROM <image>:<digest>` 

三种写法，其中和 是可选项，如果没有选择，那么默认值为latest。

###RUN
功能为运行指定的命令。
语法：

`RUN <command>`
`RUN ["executable", "param1", "param2"]`

第一种后边直接跟shell命令，在linux操作系统上默认为`/bin/sh –c`。
第二种是类似于函数调用。可将executable理解成为可执行文件，后面就是两个参数。
示例：
```shell
RUN /bin/bash -c 'source $HOME/.bashrc; echo $HOME
RUN ["/bin/bash", "-c", "echo hello"]
```
注意：多行命令不要写多个`RUN`，原因是Dockerfile中每一个指令都会建立一层。
多少个RUN就构建了多少层镜像，会造成镜像的臃肿、多层，不仅仅增加了构件部署的时间，还容易出错。

RUN命令较长书写较长时可以使用换行符\。示例：
```shell
RUN apt-get update \
     && apt-get install vim
```

###CMD
功能为容器启动时要运行的命令。
语法：
```shell
CMD ["executable","param1","param2"]
CMD ["param1","param2"]
CMD command param1 param2
```

每个`Dockerfile`只能有一条`CMD`命令。如果指定了多条命令，只有最后一条会被执行。用户在启动容器时手动指定了运行的命令，则会覆盖`CMD`指定的命令。
示例：
```shell
CMD [ "sh", "-c", "echo $HOME" 
CMD [ "echo", "$HOME" ]
```

补充细节：这里边包括参数的一定要用双引号，就是",不能是单引号。千万不能写成单引号。

###LABEL
功能是为镜像指定生成镜像的元数据标签信息。
语法：

`LABEL <key>=<value> <key>=<value> <key>=<value> ...`

示例：
```shell
LABEL "com.example.vendor"="ACME Incorporated"
LABEL com.example.label-with-value="foo"
LABEL version="1.0"
LABEL description="This text illustrates \
that label-values can span multiple lines."
```

说明：`LABEL`会继承基础镜像种的`LABEL`，如遇到`key`相同，则值覆盖。

###MAINTAINER
功能指定作者
语法：

`MAINTAINER <name>`


示例：
```shell
MAINTAINER  "li@latelee.org"

#该信息会写入生成镜像的Author属性域中。
```

###EXPOSE
功能为暴漏容器运行时的监听端口给外部。

语法：

`EXPOSE <PORT> [<PORT>…]`

示例：
```shell
EXPOSE 22 80 443

#注意，该指令只是起到声明作用，并不会自动完成端口映射，需要在启动容器时加-p。
```
###ENV
功能为设置环境变量
语法：
```shell
ENV <key> <value>
ENV <key>=<value> ...
```

示例：
```shell
ENV PATH /usr/local/bin:$PATH

```
###ADD
功能把文件复制到镜像中。
如果把虚拟机与容器想象成两台linux服务器的话，那么这个命令就类似于scp，只是scp需要加用户名和密码的权限验证，而ADD不用。
语法：
```shell
ADD <src>... <dest>
ADD ["<src>",... "<dest>"]
```
`<dest>`路径的填写可以是容器内的绝对路径，也可以是相对于工作目录的相对路径
`<src>`可以是一个本地文件或者是一个本地压缩文件，还可以是一个url
如果把`<src>`写成一个url，那么`ADD`就类似于`wget`命令

示例：
```shell
ADD test relativeDir/ 
ADD test /relativeDir
ADD http://example.com/foobar /
```

尽量不要把`<scr>`写成一个文件夹，如果`<src>`是一个文件夹了，复制整个目录的内容,包括文件系统元数据

###COPY
功能把文件复制到镜像中。
语法：
```shell
COPY <src>... <dest>
COPY ["<src>",... "<dest>"]
```

与ADD的区别，`COPY`的`<src>`只能是本地文件，其他用法一致

###ENTRYPOINT
功能是启动时的默认入口命令。该入口命令会在启动容器时作为根命令执行，所有传入值作为该命令的参数。
语法：
```shell
ENTRYPOINT ["executable", "param1", "param2"]
ENTRYPOINT command param1 param2
```

只能写一条，如果写了多条，那么只有最后一条生效。

###VOLUME
功能可以将文件夹或者其他容器中的文件夹挂在到这个容器中。
语法：
```shell
VOLUME ["/data"]
```

说明：
`["/data"]`可以是一个JsonArray ，也可以是多个值。
例如：
```shell
VOLUME ["/var/log/"]
VOLUME /var/log
VOLUME /var/log /var/db
```

一般的使用场景为需要持久化存储数据时，当数据需要持久化时用这个命令。

###USER
功能设置启动容器的用户，可以是用户名或UID。
语法：
```shell
USER daemo
USER UID
```
注意：如果设置了容器以daemon用户去运行，那么`RUN`、`CMD`和`ENTRYPOINT`都会以这个用户去运行

###WORKDIR
功能为后续的`RUN`、`CMD`、`ENTRYPOINT`指令配置工作目录。
语法：
```shell
WORKDIR /path/to/workdir
```
如果不存在则会创建，也可以设置多次。
示例：
```shell
WORKDIR /a
WORKDIR b
WORKDIR c
RUN pwd
```

pwd执行的结果是`/a/b/c`

`WORKDIR`也可以解析环境变量。
示例：
```shell
ENV DIRPATH /path
WORKDIR $DIRPATH/$DIRNAME
RUN pwd
```

`pwd`的执行结果是`/path/$DIRNAME`

###ARG
功能指定镜像内使用的参数（版本号信息等）。
语法：
```shell
ARG <name>[=<default value>]
```

设置变量命令，`ARG`命令定义了一个变量，在`docker build`创建镜像的时候，使用`--build-arg <varname>=<value>`来指定参数。
示例：
```shell
FROM busybox
ARG user1
ARG buildno=1
```
