============利用protoc生产java bean文件==============
一、下载 protoc-2.5.0-win32
二、解压 protoc-2.5.0-win32，例如解压到 D:\ProgramFiles\protoc\protoc-2.5.0-win32 目录（该目录下会有protoc.exe可执行文件）
三、在D:\ProgramFiles\protoc\protoc-2.5.0-win32 目录下创建文件夹src
四、cmd到D:\ProgramFiles\protoc\protoc-2.5.0-win32，编写.proto文件，例如编写SubscribeReq.proto
五、在D:\ProgramFiles\protoc\protoc-2.5.0-win32目录运行如下命令，指定输出目录，指定proto文件
protoc.exe --java_out=./src ./SearchRequest.proto
六、将生产的代码复制到项目中
七、加入maven依赖
<dependency>
  <groupId>com.google.protobuf</groupId>
  <artifactId>protobuf-java</artifactId>
  <version>2.5.0</version>
</dependency>
