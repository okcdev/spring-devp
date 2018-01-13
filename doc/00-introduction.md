# 说明
## 平台说明

## 更新日志
### R0.1(初始版本)

* 实现工程基础信息的配置，可配置内容包括：
* 支持工程项目的初始化，生成内容包括：
  * pom.xml（工程POM文件）
  * application.properties（spring-boot配置文件）
  * Application.java（spring-boot启动文件）
* 实现数据库实际库表结构的读取
  * 对MySQL数据库的支持
* 实现当前库表结构的保存
* 实现根据数据库表生成对应的代码，具体包括：
  * _Entity类
  * Entity类（继承_Entity类）
  * Dao类
  * Mybatis Mapper文件
  * Service类