## 云组件

| 服务 |                       简介                        |                            地址                             |
| :--: | :-----------------------------------------------: | :---------------------------------------------------------: |
| COS  | COS是腾讯云提供的一种存储海量文件的分布式存储服务 | [官网](https://cloud.tencent.com/document/product/436/6222) |



## 后端技术选型



| 技术                   | 说明                                | 版本          |
| ---------------------- | ----------------------------------- | ------------- |
| Spring Boot            | MVC 框架                            | 2.3.4.RELEASE |
| Spring Security Oauth2 | 认证授权框架                        | 2.2.4.RELEASE |
| MyBatisPlus            | 基于mybatis的ORM框架                | 3.4.0         |
| HikariCP               | 数据库连接池                        | 3.4.5         |
| lombok                 | 简化对象封装工具                    | 1.18.12       |
| sentinel               | 轻量级流量控制框架                  | 2.2.3.RELEASE |
| dubbo                  | 分布式服务框架                      | 2.7.3         |
| redis                  | Key-Value数据库                     | 2.1.0.RELEASE |
| mysql                  | 数据库                              | 8.0.16        |
| nacos                  | 服务注册与发现中心                  | 1.3.1         |
| openfeign              | 声明式的Web服务客户端，实现负载均衡 | 2.2.5.RELEASE |
| cos_api                | 腾讯云存储桶                        | 5.6.35        |
| okhttp                 | 轻量级的HTTP框架                    | 3.14.9        |
| docker                 | 容器化引擎                          | 19.03.13      |
| docker-compose         | 容器编排工具                        | 1.27.4        |
| easyExcel              | 阿里简易excel包                     |               |
| JPA                    | 数据操作框架                        |               |
| mapstruct              | dto与实体之间转换                   |               |
| rocketmq               | 消息队列                            |               |




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




| 服务名           | 说明                         | 端口 |
| ---------------- | ---------------------------- | ---- |
| business-reg     | 注册服务                     | 9000 |
| business-oauth2  | 认证服务                     | 9001 |
| business-profile | 个人信息管理服务             | 9002 |
| business-system  | 业务服务（内存不够暂不细分） |      |
| cloud-upload     | 文件上传服务                 | 9999 |
| gateway          | 默认网关                     | 8888 |
| provider         | 管理员提供服务               | null |
| Message          | 消息队列服务                 |      |




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

### rocketmq

```yaml
version: '3.5'
services:
  rmqnamesrv:
    restart: always
    image: foxiswho/rocketmq:server
    container_name: rmq-server
    ports:
      - 9876:9876
    volumes:
      - ./data/logs:/opt/logs
      - ./data/store:/opt/store
    networks:
        rmq:
          aliases:
            - rmqnamesrv

  rmqbroker:
    restart: always
    image: foxiswho/rocketmq:broker
    container_name: rmq-broker
    ports:
      - 10909:10909
      - 10911:10911
    volumes:
      - ./data/logs:/opt/logs
      - ./data/store:/opt/store
      - ./data/brokerconf/broker.conf:/etc/rocketmq/broker.conf
    environment:
        NAMESRV_ADDR: "rmqnamesrv:9876"
        JAVA_OPTS: " -Duser.home=/opt"
        JAVA_OPT_EXT: "-server -Xms128m -Xmx128m -Xmn128m"
    command: mqbroker -c /etc/rocketmq/broker.conf
    depends_on:
      - rmqnamesrv
    networks:
      rmq:
        aliases:
          - rmqbroker

  rmqconsole:
    restart: always
    image: styletang/rocketmq-console-ng
    container_name: rmqconsole
    ports:
      - 8080:8080
    environment:
        JAVA_OPTS: "-Drocketmq.namesrv.addr=rmqnamesrv:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false"
    depends_on:
      - rmqnamesrv
    networks:
      rmq:
        aliases:
          - rmqconsole

networks:
  rmq:
    name: rmq
    driver: bridge

```



## Sentinel + nacos 限流规则持久化
```json
[
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
> 只是简单试验，所以只写了一个规则

# 一些功能展示
> 若不显示，请点击
### 动态路由
![动态路由](https://github.com/qnnn/kimiAdmin/blob/main/example/gif/%E5%8A%A8%E6%80%81%E8%B7%AF%E7%94%B1.gif?raw=true)

### RBAC角色管理
![RBAC](https://github.com/qnnn/kimiAdmin/blob/main/example/gif/RBAC%E8%A7%92%E8%89%B2%E7%AE%A1%E7%90%86.gif?raw=true)

### Sentinel限流
![限流](https://github.com/qnnn/kimiAdmin/blob/main/example/gif/%E9%99%90%E6%B5%81.gif?raw=true)

### COS存储桶
![COS](https://github.com/qnnn/kimiAdmin/blob/main/example/gif/COS%E5%AD%98%E5%82%A8.gif?raw=true)

### 日志记录
> 记录用户的操作

![日志.png](https://i.loli.net/2021/04/10/VYfXhF8lqSaOjwZ.png)

### 无状态登录
#### cookies
![无状态登录.png](https://i.loli.net/2021/04/10/Aw64pL7kYosRiGD.png)
#### redis token
![redis存oauth.png](https://i.loli.net/2021/04/10/EUtAdrWF8Jnagcl.png)

### nacos配置和服务列表
#### nacos+sentinel限流配置
![nacos中sentinel流控规则配置.png](https://i.loli.net/2021/04/10/WTHLAfJqkNGFxI6.png)
#### 服务列表
![服务列表.png](https://i.loli.net/2021/04/10/8krITWJYSVONfs4.png)

### 消息队列
> 登录时通过消息队列记录登录日志，写入数据库

![消息队列.png](https://i.loli.net/2021/04/10/q8EQBM9JDOXh6RF.png)

### 使用easyExcel导出excel
![easyExcel.png](https://i.loli.net/2021/04/10/jQpDe7YzZauREHX.png)
