# JUnit-Exam

### application.yml
mysql 
```
server:
  servlet:
    encoding:
      charset: utf-8
      enabled: true

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/ssar?serverTimezone=Asia/Seoul
    username: ssar
    password: bitc5600
  
    
  jpa:
    hibernate:
      ddl-auto: create # create, update, none
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    show-sql: true
 ```
