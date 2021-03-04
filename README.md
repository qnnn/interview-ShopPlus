# interview-ShopPlus

## 说明
    该项目并未完成



## 云组件

| 服务 |                       简介                        |                            地址                             |
| :--: | :-----------------------------------------------: | :---------------------------------------------------------: |
| COS  | COS是腾讯云提供的一种存储海量文件的分布式存储服务 | [官网](https://cloud.tencent.com/document/product/436/6222) |



## 后端技术选型

|          技术          |                说明                 |     版本      |
| :--------------------: | :---------------------------------: | :-----------: |
|      Spring  Boot      |              MVC 框架               | 2.3.4.RELEASE |
| Spring Security Oauth2 |            认证授权框架             | 2.2.4.RELEASE |
|      MyBatisPlus       |        基于mybatis的ORM框架         |     3.4.0     |
|        HikariCP        |            数据库连接池             |     3.4.5     |
|         lombok         |          简化对象封装工具           |    1.18.12    |
|        sentinel        |         轻量级流量控制框架          | 2.2.3.RELEASE |
|         dubbo          |           分布式服务框架            |     2.7.3     |
|         redis          |           Key-Value数据库           | 2.1.0.RELEASE |
|         mysql          |               数据库                |    8.0.16     |
|         nacos          |         服务注册与发现中心          |     1.3.1     |
|       openfeign        | 声明式的Web服务客户端，实现负载均衡 | 2.2.5.RELEASE |
|        cos_api         |            腾讯云存储桶             |    5.6.35     |
|         okhttp         |          轻量级的HTTP框架           |    3.14.9     |
|         docker         |             容器化引擎              |   19.03.13    |
|     docker-compose     |            容器编排工具             |    1.27.4     |



## 前端技术选型
|         技术          |          说明          |  版本  |
| :-------------------: | :--------------------: | :----: |
|          Vue          |   前端框架，MVVM模式   | 2.6.10 |
|        vue/cli        |       项目脚手架       | 4.4.4  |
|         Vuex          |      全局状态管理      | 3.1.0  |
|         axios         | HTTP客户端，ajax的封装 | 0.18.1 |
| vue-image-crop-upload |    图片裁剪上传组件    | ^2.5.0 |
|      element-ui       |         UI框架         | 2.13.2 |



## 服务


|      服务名      |       说明       | 端口 |
| :--------------: | :--------------: | :--: |
|   business-reg   |     注册服务     | 9000 |
| business-oauth2  |     认证服务     | 9001 |
| business-profile | 个人信息管理服务 | 9002 |
|   cloud-upload   |   文件上传服务   | 9999 |
|     gateway      |     默认网关     | 8888 |
|     provider     |  管理员提供服务  | null |


## Docker Compose 配置

### nacos+mysql

```yaml
version: "2"
services:
  nacos:
    image: nacos/nacos-server:1.3.1
    container_name: nacos-standalone-mysql
    env_file:
      - ../env/nacos-standlone-mysql.env
    volumes:
      - ./standalone-logs/:/home/nacos/logs
      - ./init.d/custom.properties:/home/nacos/init.d/custom.properties
    ports:
      - "8848:8848"
      - "9555:9555"
    depends_on:
      - mysql
    restart: always
  mysql:
    container_name: mysql
    image: nacos/nacos-mysql:8.0.16
    env_file:
      - ../env/mysql.env
    volumes:
      - ./mysql:/var/lib/mysql
    ports:
      - "3306:3306"
    restart: always
```



### redis

```yaml
version: '3'
services:
        redis:
                restart: always
                image: redis
                container_name: redis
                ports:
                        - 6379:6379
```

## Sentinel + nacos 限流规则持久化
```json
[
# 只是简单试验，所以暂时只列了一个规则
    {
        "resource": "getInfo", 
        "count": 1,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0
    }
]
```






