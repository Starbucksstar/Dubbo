# 基于Dubbo封装，提供@DubboService 、@DubboConsumer注解暴露服务和消费引用功能。

## 1.dubbo工程:
dubbo二次封装工程，其中暴露DubboService和DubboConsumer两个自定义注解，可以直接使用注解方式暴露服务和引用服务，引用方建议使用DubboFactory.getDubboService()获取相关服务实例

## 2.dubboprovider工程：
服务提供方工程，其中写了一个简单的暴露用户信息的两个服务，DubboAnnotationBeanParser的注入容器使用@Configuretion注解，进行注解配置

## 3.dubboconsumer工程：
服务消费方工程，其中写了如何引用刚才的暴露服务。

## 4.user.jar:
服务提供方公共接口，日常开发中，可以父工程下两个子工程，一个是api工程提供暴露的dubbo接口，另一个是实际工程接口服务实现

- 注意:application.properties文件里面的配置可以针对dubbo默认配置进行覆盖，建议不用修改。
- 坑：dubbo整合springboot出现很多问题，curator-framework包需要pom引入，启动类加@ComponentScan等
- 遗留问题：目前消费方的@DubboConsumer注解目前还无法使用，估计是注解注入顺序有问题。
