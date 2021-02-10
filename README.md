# JUnit-Exam
요구사항의 id는 int였지만 프로젝트에서는 Long을 사용하고 있습니다.

### application.yml
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
