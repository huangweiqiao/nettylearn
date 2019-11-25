一、将jibx_1_3_1.zip解压（例如解压到 D:\ProgramFiles\jibx_1_3_1）

二、创建pojo类

三、cd 到 target目录下的classes目录 （cd D:\ProgramFiles\gitRepository\nettylearn\target\classes）

四、生产绑定文件(格式： java -cp  jibx解压后目录中的工具jar   工具类  -t  生成的绑定文件保存地址   -v   完整类名1 完整类名2 完整类名3)
java -cp D:\ProgramFiles\jibx_1_3_1\jibx\lib\jibx-tools.jar org.jibx.binding.generator.BindGen -t D:\ProgramFiles\gitRepository\nettylearn\src\main\resources\jibx -v com.hwq.httpxml.pojo.Student com.hwq.httpxml.pojo.Shipping com.hwq.httpxml.pojo.Address com.hwq.httpxml.pojo.Customer com.hwq.httpxml.pojo.Order

五、增强class，在classes目录下会生成Jibx_bingding_xxxx.class文件 (格式：java -cp jibx解压后目录中的工具jar   工具类  -v  第四步中生成的绑定文件)
java -cp D:\ProgramFiles\jibx_1_3_1\jibx\lib\jibx-bind.jar org.jibx.binding.Compile -v D:\ProgramFiles\gitRepository\nettylearn\src\main\resources\jibx\binding.xml

maven插件动态增强
如果用maven插件可以这样增强class
1、在pom.xml文件中加入配置
<dependency>
	<groupId>org.jibx</groupId>
	<artifactId>jibx-run</artifactId>
	<version>1.3.1</version>
</dependency>
<dependency>
	<groupId>org.jibx</groupId>
	<artifactId>jibx-extras</artifactId>
	<version>1.3.1</version>
</dependency>

<build>
	<plugins>
		<plugin>
			<!-- 生成jibx class信息 -->
			<groupId>org.jibx</groupId>
			<artifactId>jibx-maven-plugin</artifactId>
			<version>1.3.1</version>
			<configuration>
				<schemaBindingDirectory>${basedir}/src/main/resources/jibx</schemaBindingDirectory>
				<includeSchemaBindings>
					<includeSchemaBindings>*binding.xml</includeSchemaBindings>
				</includeSchemaBindings>
				<verbose>true</verbose>
			</configuration>
			<executions>
				<execution>
					<id>jibx-bind</id>
					<phase>compile</phase>
					<!-- 把jibx绑定到了comile编译阶段 -->
					<goals>
						<goal>bind</goal>
					</goals>
				</execution>
			</executions>
		</plugin>
	</plugins>
</build>

2、执行 install 命令
