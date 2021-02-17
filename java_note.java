=== So sánh enum
Ko dùng equal() mà dùng ==

=== @NotNull, @NotEmpty
// Với số phải dùng kiểu wrapper, ko dùng kiểu nguyên thủy
// Với số ko dùng được @NotEmpty
@NotNull(message = "id cannot be null or empty")
private Integer id;

// Với string thì dùng @NotEmpty vẫn có thể bắt được lỗi = null,
// do đó ko cần @NotNull ở đây
@NotEmpty(message = "userName cannot be null or empty")
private String userName;

Chú ý PHẢI import như sau:
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

=== JPA
- Trong 1 entity, có thể dùng các annotation sau trước 1 method:
	- @PrePersist: chạy method trước khi tạo mới và lưu 1 entity vào database. (???)
	- @PreUpdate: chạy method trước khi update 1 entity (???)
- tên cột ko được trùng với từ khóa, nếu ko JPA sẽ bị lỗi
	VD: table: setting(id, option, value);
	2 cột option và value trùng từ khóa, lúc dùng JPA sẽ lỗi!
- Có 3 bảng user, role, user_role (tức là user và role quan hệ nhiều-nhiều)
  Tạo entity như sau:
@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @Size(max = 45)
    private String name;

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;
}
@Entity
@Table(name = "role")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @Size(max = 45)
    @Column(name = "role_name")
    private String roleName;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    private List<User> users;
}
// Khi update Role cho User thì có thể làm như sau:
List<Role> roles = new ArrayList<>();
roles.add(...);
// ...
user.setRoles(roles);
userRepository.save(user);
// Làm như vậy thì bảng user_role sẽ tự động được update đúng role!

===
- Repository: thao tác với database, trả về entity
- Service: Dùng Repository, bao gồm cả API trả về model và entity.
  Service có thêm phần check, valid input... Nếu get 1
  record trong database mà ko tồn tại thì sẽ throw exception
  tại Service, chứ ko nên throw ở Repository
- Controller, components khác muốn truy cập database: nên dùng Service,
  vì đề phòng trường hợp null sẽ được throw exception ở Service
  (Trong Repository ko throw gì)
- VD:
public interface SettingRepository extends JpaRepository<Setting, Integer> {
    Setting findBySettingOption(String option);
}

public interface SettingService {
    List<Setting> getAllSettings();
    Setting getSettingById(Integer settingId);
    Setting getSettingByOption(String name);
    Integer createNewSetting(SettingRequestVO request);
    Integer updateSetting(SettingRequestVO request);
    void deleteSetting(Integer settingId);
}

Trong method getSettingById() của SettingService, nó sẽ dùng SettingRepository
để truy cập database (dùng hàm findOne()). Nếu SettingRepository trả về null
thì getSettingById() sẽ throw exception.
Tương tự với các method còn lại.

=== tomcat error
Khi deploy 2 app.war lên 1 con tomcat thì có thể bị lỗi sau:
Unable to register MBean [HikariDataSource (HikariPool-2)] with key 'dataSource';
javax.management.InstanceAlreadyExistsException: com.zaxxer.hikari:name=dataSource,type=HikariDataSource
Lý do là vì 2 app đều có config database nên bị trùng nhau cái cc gì ko rõ lắm
cách xử lý:
https://stackoverflow.com/questions/34284984/tomcat-hikaricp-issue-when-deploying-two-applications-with-db-connection
Thêm vào file config dòng sau:
spring.jmx.default-domain=app_name
hoặc:
spring
  jmx:
    default-domain: app_name
	
=== Json pretty-print rendered Springboot REST API
Thêm vào file application.yaml:
# Pretty-print JSON responses
spring:
  jackson:
    serialization:
      indent-output: true
	  
=== config show SQL:
application.properties:
spring.jpa.properties.hibernate.show_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true
spring.jpa.properties.hibernate.format_sql=trues
spring.jpa.properties.hibernate.type=trace

application.yaml:
spring:
  jpa:
    properties:
      hibernate:
        show_sql: true
        format_sql: true

=== profile:
Trong file yaml config như sau:
spring:
  profiles:
    active: dev

#-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
# Local Profile
#-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
spring:
  profiles: local
  
  ...
---
#-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
# Staging Profile
#-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
spring:
  profiles: stg
  
  ...
---
#-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
# Dev Profile
#-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
spring:
  profiles: dev
  
  ...
---

- Lúc run thì: click chuột phải -> Run As -> Run configurations... -> Spring boot App (chứ ko phải
  Maven Build nhé)
- Sau đó nhập profile muốn run. Chú ý rằng config profile trong file yaml
  ko có tác dụng chọn profile lúc run:
  active: dev	// KHÔNG có tác dụng
- Lúc build: chỉ cần chỉnh profile muốn build ở trong file yaml, ko cần
  nhập rõ profile trong lúc build configurations:
  active: dev	// PHẢI dùng để build thành công
  
=== build file war with different profile
- Có thể build như ở trên, tức là trước lúc build ta active profile ở file config
- Có cách khác: build và lúc run thì chọn profile để run:
	+ Dùng STS: Click chuột phải > Run As > Maven clean
	+ Click chuột phải > Run As > Maven install
	+ Xong xuôi sẽ có file war trong thư mục target, mở terminal cd vào đó
	+ run lệnh (chọn 3 profile stg,log,swagger):
		java -jar -Dspring.profiles.active=stg,log,swagger app-name.war
		
	
=== Ép kiểu sang String
String str = map.get("abc").toString();
Nếu map.get("abc") == null thì sẽ bị lỗi ở chỗ này (null sao .toString() được)

=> Cách an toàn hơn:
String str = (String) map.get("abc");
Nếu map.get("abc") == null thì str = null => OK
Nhưng nếu map.get("abc") là 1 số Integer, thì ko thể ép sang kiểu String được => Lỗi

Dùng cách sau có thể giải quyết được việc ép số Integer sang String:
String str = map.get("abc") + "";
Nhưng nếu map.get("abc") == null thì str = "null"

=> nên check null trước như sau:
String str1 = (String) map.get("key1");	// nếu map.get("key1") có kiểu String
String str2 = map.get("key2") + "";		// nếu map.get("key2") có kiểu số
if(str1 != null && !str2.equal("null")) {
	// do something
}

=== JPA config
Trong file application.yaml, nên comment dòng sau để run project sẽ nhanh hơn (rất nhiều)
spring:
  jpa:
    database: mysql
    show-sql: false
    hibernate:
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    properties:
      hibernate:
        format_sql: false
#        hbm2ddl:
#          #auto: create
#          auto: update
#          #auto: validate
#          import_files: 

=== Hashmap hashCode và equals
Hàm hashCode dùng để tính toán giá trị index trong mảng table[]
Hàm equals dùng để so sánh 2 key, nếu 2 key = nhau (khi hashCode = nhau) thì value của key2 sẽ thay thế value của key1

Giả sử có class Key như sau:
public class Key {
  private String key;

  public Key(String key) {
    super();
    this.key = key;
  }

  @Override
  public int hashCode() {
    ...
  }

  @Override
  public boolean equals(Object obj) {
    ...
  }
}

Nếu hashCode luôn return 1 hằng số, chẳng hạn:
@Override
public int hashCode() {
  return 2021;
}
Thì với mọi key (dù giá trị khác nhau), hashFunction đều cho ra 1 giá trị index, tức là mọi key đều lưu vào 1 node của bảng table[]

Nếu equals luôn return true:
@Override
public boolean equals(Object obj) {
  return true;
}
Thì mọi key đều = nhau (dù hash ra index khác nhau), tức là nếu tồn tại 2 key mà hash ra cùng 1 giá trị index, thì việc put theo key2 sẽ update giá trị của key1

Nếu cả hashCode return 1 hằng số, và equals luôn return true: lúc này hashmap luôn chỉ có 1 phần tử, và việc put những key sau sẽ update value cho key đầu tiên

Demo: Nếu hashCode return 1 hằng số, thì giả sử insert 2 triệu phần tử vào HashMap, có thể tốn hơn 45 phút!

=== How Hashmap work
- Node và data table:
Bảng băm dùng để lưu data: Node<K,V>[] table;
Trong đó Node:
static class Node<K,V> implements Map.Entry<K,V> {
  final int hash;
  final K key;
  V value;
  Node<K,V> next;
}
Dễ thấy mỗi phần tử của bảng data table[] chính là HEAD của 1 danh sách liên kết, tức là HashMap trong Java xử lý collision dùng Separate chaining. Có thể gọi table[] này là array of linked lists (hay là buckets)

- Index calculating:
Kích thước của mảng table: luôn là lũy thừa của 2 (the array size is a power of two). Nếu init HashMap size = 37 thì nó tự động chọn 1 số nhỏ nhất là lũy thừa của 2 và >= 37, chính là số 64.
Lí do table.size luôn là power of two: để tính index từ hashCode: index = hashCode & (size - 1).
Giả sử size = 16 = 1000(2) (kí hiệu vậy ám chỉ cơ số 2). Giả sử key1 có hashCode1 = 952 => index1 = 952 & 15 = 8. Hoặc dễ hình dung, viết dưới dạng cơ số 2: 0..01110111000 & 0..01111 = 1000.
Nếu tự viết 1 HashMap, mà table.size ko phải là lũy thừa của 2, có thể tính index = cách chia module: nhưng việc chia ko nhanh bằng phép toán trên bit. Giả sử size = 20 => index1 = 952 % 20 = 12.

- Auto resizing:
HashMap tự động resize (tăng gấp đôi table.size, và nó tự động làm, ko cung cấp method để increase thủ công) khi số lượng phần tử trong map >= threshold, trong đó threshold = mapCapacity * loadFactor.
Hàm sau sẽ khởi tại giá trị 2 field đó: public HashMap(int initialCapacity, float loadFactor). Default thì initialCapacity = 16, loadFactor = 0.75.
Việc resize sẽ khiến hàm tính index bị thay đổi, vì hàm đó tính theo table.size: index = hash(key) AND (table.size - 1), dẫn tới sẽ phải tính toán lại toàn bộ các phần tử có trong Map

- Java8 cải tiến hơn: buckets là mảng của DSLK và Tree (dùng cây cân bằng Red-black tree). Tức là nếu 1 bucket chứa nhiều hơn 8 node lưu dạng DSLK, thì nó sẽ được convert sang dạng Tree. Tất nhiên điều này sẽ gây tốn bộ nhớ hơn

- Ref: http://coding-geek.com/how-does-a-hashmap-work-in-java/
