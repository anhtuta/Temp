## Java
### OOP
- Tính đóng gói (Encapsulation):
  + Là việc đóng gói dữ liệu lại với nhau vào 1 đơn vị duy nhât (private class). Việc truy cập data chỉ được thông qua các method public (getter, setter)
  + Lợi ích:
    + Cho phép **che giấu thông tin** và những **tính chất** bên trong của đối tượng
    + Các đối tượng khác **không** thể **tác động trực tiếp** đến **dữ liệu bên trong** và làm thay đổi trạng thái của đối tượng (các biến, method private) mà bắt buộc phải **thông qua các phương thức công khai** do đối tượng đó cung cấp (các method public như getter, setter...)
    + Bằng việc cung cấp các method getter, setter, ta có thể biến 1 class thành *read-only* hoặc *write-only*
- Tính kế thừa (Inheritance): cho phép xây dựng một lớp mới (lớp Con), **kế thừa và tái sử dụng** các thuộc tính, phương thức dựa trên lớp cũ (lớp Cha) đã có trước đó. Chẳng hạn, 1 class có thể implements 1 ```interface``` hoặc extends từ 1 ```abstract class``` khác
- Tính đa hình (Polymorphism = many forms)
  + Nghĩa là 1 action (method) có thể được thực hiện bằng **nhiều cách khác nhau**
  + Tính kế thừa cho phép tái sử dụng lại các attribute, method. Tính đa hình cho phép thực hiện method đó dưới những cách khác nhau
  + Có 2 cách thể hiện tính đa hình:
    + Overload (compile time polymorphism): nhiều method cùng tên nhưng khác tham số
    + Override (run time polymorphism): nhiều class cùng implements/extends từ 1 interface/class cha
  + VD1 (Overload): 2 method tính max:
  ```java
  int max(int a, int b) {}
  int max(int a, int b, int c) {}
  ```
  + VD2 (Override): có 1 interface Sort, interface này có 1 method sort(). Có thêm 2 class Mergesort và Quicksort cùng implements interface Sort đó, mỗi class sẽ override method sort theo cách khác nhau
  + ```final``` keyword: dùng để *restrict* (hạn chế) người dùng:
    + final variable ko thể bị thay đổi giá trị sau khi khởi tạo
    + final method: ko thể bị override ở class con (nhưng class con vẫn dùng được method này của class cha)
    + final class: ko thể extends nó được
- Tính trừu tượng (Abstraction):
  + Là việc ẩn các implementation details (triển khai chi tiết) và chỉ hiển thị chức năng cho người dùng. Tính trừu tượng tập trung vào việc object đó sẽ làm gì thay vì object đó làm như thế nào (focus on what the object does instead of how it does it)
  + Có 2 cách thể hiện tính trừu tượng: Abstract class (0 to 100%) và Interface (100%)

## Abstract class và Interface
- Is-a và Can-do
- Abstract class:
  + Là một class cha cho tất cả các class có cùng bản chất
  + Class khi extend (thừa kế) một abstract class được gọi là ```Is-a```. VD: class Dog extends Animal, thì có thể nói **Dog Is-a Animal**
  + Không thể tạo thể hiện
  + Variable: như class bình thường (final, non-final, static, non-static variable)
  + Method: normal method và abstract method
  + Đơn thừa kế: tức là 1 class chỉ có cùng bản chất ```is-a``` với 1 class cha thôi
- Interface:
  + Là một chức năng có thể thêm và bất kì class nào
  + Class khi implement (thực hiện) một interface được gọi là ```can-do```
  + Không thể tạo thể hiện
  + Variable: chỉ có static và final variable
  + Method: chỉ gồm abstract method (tức là method ko có thân hàm) (tuy nhiên Java8 có thêm ```default method``` và ```static method``` giống normal method trong abstract class, tức là có thân hàm)
  + Đa thừa kế: 1 class có thể ```can-do``` nhiều thứ
- Nhiều class ko cùng bản chất với nhau nhưng có thể cùng implement 1 interface

// trie
với trường hợp ứng dụng từ điển muốn có cả partial search, infix search (giống kiểu: LIKE '%infix%'), hay autocompletion, thì có thể dùng TRIE được bằng cách insert các string con của từ đó, chẳng hạn với từ banana, thì ta insert vào TRIE các từ: banana, anana, nana, ana, na, a. Bởi vì bộ nhớ dùng cho TRIE ko phụ thuộc vào số lượng từ, mà chỉ là độ dài từ, nên cách này khá ok. Mỗi tội khi search sẽ chậm hơn vì có nhiều từ có chung infix hơn

(java notes)
## Spring security
- Spring security thực ra chỉ gồm các servlet filter dùng để authentication và authorization
- Có thể tích hợp với Spring MVC hay Spring boot, và cũng có các chuẩn OAuth2 hay SAML
- Nó tự động tạo các page login/logout và tránh được những vulnerability (lỗ hổng web) như CSRF
- Các filter của Spring security làm 4 việc chính sau:
  + Extract (bóc tách) token, username/password từ request
  + Check token đó đã authenticate chưa
  + Check token đó đã authorize chưa
  + Cho phép request đi tới servlet, controller
- Nếu cả 4 bước trên được gói gọn trong 1 filter, thì nó khá phức tạp với 1 đống code. Có thể split nhỏ thành nhiều filter như sau:
  + First, go through a LoginMethodFilter
  + Then, go through an AuthenticationFilter
  + Then, go through an AuthorizationFilter
  + Finally, hit your servlet
- Khái niệm trên gọi là filter chain. Method cuối cùng cho request đi qua filter đó là ```chain.doFilter(request, response);```
- Filter chain của Spring security gồm khoảng 15 filter, sau đây là những filter quan trọng:
  + **BasicAuthenticationFilter**: Tries to find a Basic Auth HTTP Header on the request and if found, tries to authenticate the user with the header's username and password.
  + **UsernamePasswordAuthenticationFilter**: Tries to find a username/password request parameter/POST body and if found, tries to authenticate the user with those values.
  + **DefaultLoginPageGeneratingFilter**: Generates a login page for you, if you don’t explicitly disable that feature. THIS filter is why you get a default login page when enabling Spring Security.
  + **DefaultLogoutPageGeneratingFilter**: Generates a logout page for you, if you don't explicitly disable that feature.
  + **FilterSecurityInterceptor**: Does your authorization
- Các filter sẽ làm mọi thứ, việc của developer là chỉ cần config bằng cách ```extends WebSecurityConfigurerAdapter```
- Authentication with Spring Security: có 3 trường hợp phổ biến sau:
  + Default: backend lưu username, hashed password trong database
  + Less common: backend ko lưu password, thay vào đó sẽ dựa vào 1 bên thứ 3 (third party) lưu trữ, và backend sẽ gọi rest service gửi username/password tới bên thứ 3 này để bên đó authenticate
  + Also popular: Dùng OAuth2 hoặc login with social network account (Google, fb, github...) (OpenID), có thể là dùng JWT

### UserDetailsService: Having access to the user's password
Với trường hợp đầu tiên, ta cần cung cấp cho Spring security 3 bean sau:
- ```UserDetailsService```: bean này sẽ load UserDetails với param username từ request gửi tới, ta cần Override lại hàm sau:
```java
@Override
public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
  userRepository.findByUsername(name);
  List<GrantedAuthority> grantList;
  ...

  return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantList);
}

```
- ```PasswordEncoder```: chỉ rõ thuật toán hash password được lưu trong database là gì, VD:
```java
@Bean
public PasswordEncoder passwordEncoder() {
  return PasswordEncoderFactories.createDelegatingPasswordEncoder();
}
```

Nếu ko muốn implement UserDetailsService thì có thể dùng những implement có sẵn của Spring security:
- JdbcUserDetailsManager: config column lưu trữ user
- InMemoryUserDetailsManager: keeps all userdetails in-memory and is great for testing.

Trường hợp secure app dùng Basic Auth, flow sẽ như sau:
- Extract username/password từ HTTP Basic Auth header trong filter, cụ thể là filter ```BasicAuthenticationFilter``` có sẵn của Spring
- Dùng UserDetailsService để load User từ database và return UserDetails, object này bao gồm hashed password
- Compare password trong database với password từ header lấy được ở trên. Nếu match thì authenticate thành công (Thuật toán hash lấy từ bean PasswordEncoder)

### AuthenticationProvider: Not having access to the user’s password
Trường hợp thứ 2, backend ko lưu password trong database mà dùng chẳng hạn Atlassian Crowd, để quản lý credentials. Tất nhiên ta ko thể request tới Atlassian Crowd để hỏi password của user là gì, nhưng Atlassian Crowd sẽ cung cấp 1 REST endpoint giúp ta authenticate user

Trong trường hợp này thay vì implement UserDetailsService, ta sẽ phải implement AuthenticationProvider để override lại method authenticate(). VD:
```java
public class AtlassianCrowdAuthenticationProvider implements AuthenticationProvider {

  // Có thể làm bất cứ điều gì để authenticate user, chẳng hạn như gọi REST service.
  // Nếu authenticate fail thì cần throw an exception
  // Nếu authenticate success thì cần return 1 object ```UsernamePasswordAuthenticationToken```,
  // và cần có field ```authenticated``` = true. Vậy là xong
  @Override
  Authentication authenticate(Authentication authentication)  throws AuthenticationException {
      String username = authentication.getPrincipal().toString();
      String password = authentication.getCredentials().toString();

      User user = callAtlassianCrowdRestService(username, password);
      if (user == null) {
          throw new AuthenticationException("could not login");
      }
      return new UserNamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
  }
  // other methods ignored
}
```

### Using OAuth2 to authenticate: sẽ xét sau

### Authorization
Authority chính là role với prefix "ROLE_", chẳng hạn: role = "ADMIN" thì authority tương ứng là "ROLE_ADMIN"

Trong UserDetailsService hay AuthenticationProvider, ta cần chỉ rõ authority của user (get from database)
