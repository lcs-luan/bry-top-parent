## 系统顶级pom

**1、顶级公共依赖：bry-top-parent**

```
<parent>
    <groupId>com.bry.parent</groupId>
    <artifactId>bry-top-parent</artifactId>
    <version>2.0-jdk17-SNAPSHOT</version>
</parent>
```

统一管理依赖jar包的版本号，方便以后升级；

**2、工具的工具包：bry-common-utils**

```
<dependency>
    <groupId>com.bry.parent</groupId>
    <artifactId>bry-common-utils</artifactId>
    <version>${bry-parent.version}</version>
</dependency>
```

该模块包含：
(1)、系统错误码前缀配置：system.response.code
(2)、公共返回码接口、公共返回结果处理工具类
(3)、公共异常类定义，统一异常处理
(4)、分页工具类
