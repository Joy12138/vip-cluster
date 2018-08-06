运行环境

Java 8
Maven 3
环境配置

安装JDK 8
安装maven
maven安装及maven项目导入eclipse流程
使用前配置

       因为支付宝的sdk没有上传maven官方库,所以需要自己手动安装本地库才能运行，自行修改路径及文件名称。

       先下载平台sdk，地址如下：

             https://doc.open.alipay.com/doc2/detail.htm?treeId=193&articleId=103419&docType=1

       解压后,找到sdk的jar文件，如：alipay-sdk-java20160917220058.jar,将sdk安装到本地maven库,修改命令路径后 使用命令安装:

       mvn install:install-file -DgroupId=com.alipay -DartifactId=sdk -Dversion=java20160917220058 -Dfile=/toPath/alipay-sdk-java20160917220058.jar -

       Dpackaging=jar -DgeneratePom=true

      然后在pom.xml中修改以下依赖的版本

          <dependency>

              <groupId>com.alipay</groupId>

              <artifactId>sdk</artifactId>

              <version>java20160917220058</version>

         </dependency>

开始使用

     在eclipse导入已存在的maven工程，并且增加maven依赖的jar。

运行入口
           Application.java 中的main方法

配置文件
           src/main/resources/application-dev.yml

           配置文件中，可以去修改配置账号，网关地址，及异步通知公网回调地址等，

           具体参数解析见application-dev.yml样例：

     #支付宝标准参数

            alipay:

                 config:

                     gateway : https://openapi.alipay.com/gateway.do （支付宝网关）

                     alipay_public_key : （支付宝公钥）

                     auth_gateway : https://openauth.alipay.com/oauth2/publicAppAuthorize.htm 

                                         （第三方应用授权的地址）

     #开发／测试账号：

                     pid : 开发／测试支付宝账号Pid

                     appid : 开发／测试支付宝账号下用于开发会员营销的应用appid

                     private_key : 应用的私钥

            notify:

                     activity : 获取异步通知的地址。

                     例如：http://121.40.156.70:8888/alipay/notify/gateway/activity.do

            server:

                     host:

                         url: 服务的协议类型和服务器地址组成的url，例如： http://serverName（ip地址）

                     port: 服务的端口号。例如：8888

            spring:

            freemarker:

                     suffix: .html

            datasource:

                     type: com.zaxxer.hikari.HikariDataSource

                     url: jdbc:sqlite:data.db

                     driverClassName: org.sqlite.JDBC

           mybatis:

                     type-aliases-package: com.example.entity,com.example.dao

                     type-handlers-package: org.apache.ibatis.type.LocalDateTypeHandler

                     config-location: classpath:mybatis-config.xml

使用示例

页面访问入口

           运行Application.java的main方法后，默认是8080端口，可在浏览器中打开 http://localhost:8080 ,会看到会员营销Demo主页。

配置公网转发

           可以使用ngrok自行配置，通过公网访问Demo入口。此方法可作为开发调试阶段使用，配置方法可自行查阅资料。

部署服务器应用

           此方法一般作为长期应用使用。操作步骤：eclipse右击工程，Run as—maven install，系统会在target文件夹下生成相应的war文件。此文件包含应用所需的tomcat容  

           器，可以在任意装有JDK1.8的系统上运行。将war文件和工程目录下的data.db文件，上传至服务器相应目录，并运行如下命令：

           java –jar demo-0.0.1-SNAPSHOT.war （窗口打印日志，不可关掉本地窗口。）

           或 nohup java –jar demo-0.0.1-SNAPSHOT.war & （服务器后台运行，输出日志到服务器指定文件，可关掉本地窗口。）

           启动应用，对外提供稳定的服务。

          *注意：在更改服务器地址时，要同时更改支付宝应用的授权回调地址，否则系统会报无效页面的错误。

页面操作

           公网域名或扫码进入会员营销Demo主页后，会有四个按钮分别进入不同的业务场景。

                                          

页面功能

          商户会员卡开卡流程

                说明：点击“点此领取会员卡”按钮，进入用户授权页面。授权完毕，进入会员信息确认页面。用户填入必填信息，点击“点此开通会员卡”按钮，进入开卡成功页面，此页面为内部页面，可以隐藏也可以作为过度页面使用。最终跳转至会员卡详情页面，展示开卡结果。用户可以体验会员卡各功能，包括会员卡基本信息，卡片翻转，二级菜单，外链URL等。此场景包括两个子场景，线下二维码扫码：

                         

             口碑店铺页领卡：

                         

           商户会员卡开卡领券流程

                 说明：点击“点此领取会员卡并领券”按钮，进入用户授权页面。授权完毕，进入会员信息确认页面。用户填入必填信息，点击“开通会员卡并领券”按钮，进入口碑领券页面，点击“领取”按钮，提示领取成功，按钮变为“去消费”。点击进入卡券包，会员卡和券均会出现在卡券包。点击会员卡进入卡详情页（如场景1），点击代金券，进入券详情页，可以去买单或者分享给朋友。如果点击去买单，跳转至支付码页，完成新一轮支付。

                        

          消费送券流程

                说明：点击“点此领取会员卡并领券”按钮，进入用户授权页面。授权完毕，进入会员信息确认页面。用户填入必填信息，点击“开通会员卡并领券”按钮，进入口碑领券页面，点击“领取”按钮，提示领取成功，按钮变为“去消费”。点击进入卡券包，会员卡和券均会出现在卡券包。点击会员卡进入卡详情页（如场景1），点击代金券，进入券详情页，可以去买单或者分享给朋友。如果点击去买单，跳转至支付码页，完成新一轮支付。

                       

          退款功能

                可以输入交易号和金额进行退款。