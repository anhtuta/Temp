trie chỉ phụ thuộc vào độ dài từ thôi chứ có phụ thuộc vào số từ đâu

## Run Spring Boot project in terminal
- Giả sử project Spring Boot build thành file tên là HelloSpringBoot.war. Spring boot đã có tomcat nhúng nên ko cần phải deploy trên tomcat nữa, mà có thể run trực tiếp trên terminal như sau:<br/>
```
java -server -Xms2g -Xmx2g -jar -Dspring.profiles.active=staging,log -Dspring.datasource.url=jdbc:mysql://localhost:3306/database_name?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false HelloSpringBoot.war
```
- Trong đó ```Dspring...``` là các tham số config cho project (giống như trong file application.properties). Các tham số này sẽ ghi đè giá trị đã được config trong file application.properties
