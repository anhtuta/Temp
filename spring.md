## Lỗi Spring boot ko tìm được bean được annotate bởi @Repository
Lỗi này xảy ra khi dùng nhiều datasource, chẳng hạn dùng cả Mongodb và MySQL. Trong file config đã khai báo như sau:
```properties
# ============= DataSource ==================
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/test?createDatabaseIfNotExist=true&verifyServerCertificate=false&useSSL=true&useUnicode=true&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=5555

spring.data.mongodb.port=27017
spring.data.mongodb.database=test
```
Trong file pom.xml cũng đủ các thư viện như spring-boot-starter-data-jpa, spring-boot-starter-data-mongodb, mysql-connector-java. Tuy vậy khi tạo 1 repository như sau:
```java
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}

// Bên service gọi tới bean này:
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

}
```
Khi run project thì bị lỗi:
```
Field userRepository in UserServiceImpl required a bean of type 'UserRepository' that could not be found.
The injection point has the following annotations:
	- @org.springframework.beans.factory.annotation.Autowired(required=true)
Action:
Consider defining a bean of type 'UserRepository' in your configuration.
```
Bật log level = DEBUG lên: ``` logging.level.root=DEBUG ```, thì thấy dòng này:
```
 HibernateJpaConfiguration:
      Did not match:
         - @ConditionalOnBean (types: javax.sql.DataSource; SearchStrategy: all) did not find any beans (OnBeanCondition)
```
Khả năng cao là do Spring chưa nhận được datasource của MySQL, thử thêm datasource như sau:
```java
@Primary
@Bean
public DataSource userDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
    dataSource.setUrl(env.getProperty("spring.datasource.url"));
    dataSource.setUsername(env.getProperty("spring.datasource.username"));
    dataSource.setPassword(env.getProperty("spring.datasource.password"));

    return dataSource;
}
```
Run lại thì thành công!
