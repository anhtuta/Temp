## cors filter
- Allow các Request header field sau: pragma, x-xsrf-token:
  ```response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me, pragma, x-xsrf-token");```
- Fix lỗi request với method=OPTIONS ko qua được các filter:
```java
    // Nếu có filter khác nữa thì khả năng filter đó sẽ chặn request OPTIONS này
    // Nên có thể return luôn với status = 200 (OK) để nó ko phải qua filter đó nữa
    // Trong CorsFilter, thêm đoạn code sau trước lệnh filterChain.doFilter
    if ("OPTIONS".equals(request.getMethod())) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("OK");
        return;
    }
```

## Import certificate file cho STS4:
Vào thư mục chứa file certificate SAPL.crt, mở cmd run lệnh sau: ```keytool -importcert -file SAPL.crt -keystore keystore.jks -alias "Alias"```

Lệnh trên sẽ import certificate với file = SAPL.crt vào keystore có tên là "keystore.jks", alias của certificate này là Alias

Sau khi run xong thì thư mục hiện tại sẽ có 1 file mới được tạo ra là file keystore.jks. Vào thư mục chứa STS (Eclipse), thêm vào file SpringToolSuite4.ini các param sau (Giả sử keystore file và certificate file đặt trong thư mục D:\Downloads\miscellaneous):
```
-vmargs
-Djavax.net.ssl.trustStore=D:\Downloads\miscellaneous\keystore.jks
-Djavax.net.ssl.trustStorePassword=changeit
-Djavax.net.ssl.trustStoreType=JKS
```
Xong rồi!
