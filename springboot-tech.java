file application.yml:
spring:
  profiles:
#    active: prd
    active: local,swagger
    
Tạo thêm 2 file
application-local.yml,
application-prd.yml
2 file này có data giống application.yml ban đầu

===
@Profile("swagger")
// Bean này Spring chỉ khởi tạo và quản lý khi môi trường là những môi trường không phải là `prd`
// @Profile("!prd")
public class SwaggerConfig {

