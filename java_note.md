
## Run Spring Boot project in terminal
- Giả sử project Spring Boot build thành file tên là HelloSpringBoot.war. Spring boot đã có tomcat nhúng nên ko cần phải deploy trên tomcat nữa, mà có thể run trực tiếp trên terminal như sau:
```
java -server -Xms2g -Xmx2g -jar -Dspring.profiles.active=staging,log -Dspring.datasource.url=jdbc:mysql://localhost:3306/database_name?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false HelloSpringBoot.war
```
- Trong đó ```Dspring...``` là các tham số config cho project (giống như trong file application.properties). Các tham số này sẽ ghi đè giá trị đã được config trong file application.properties

## Remove IntelliJ item from right-click menu
Sau khi cài IntelliJ mà nhỡ chọn "Open folder as Project...", thì khi click chuột phải, nó sẽ hiện menu item: "Open Folder as IntelliJ IDEA Community Edition Project", muốn xóa đi thì phải sửa regedit:
- Windows + R, gõ ```regedit``` rồi enter
- Tìm đến ```Computer\HKEY_CLASSES_ROOT\Directory\Background\shell\IntelliJ IDEA Community Edition```
- Click chuột phải vào IntelliJ IDEA Community Edition, chọn ```New -> String Value```
  + Nhập giá trị cho string đó là ```Extended```, thì khi cái menu đó nó mới hiện khi thực hiện Shirt + right click
  + Nhập giá trị cho string đó là ```LegacyDisable```, thì nó sẽ mất hẳn
  + KHÔNG nên xóa hẳn thư mục đó đi, vì revert lại khá khó khăn!
- Tương tự với các chương trình khác, chú ý tùy vào chương trình mà việc sửa regedit có thể ở các thư mục sau:
  + ```HKEY_CLASSES_ROOT\Directory\Background\shell\```
  + ```HKEY_CLASSES_ROOT\Directory\shell\```
  + ```HKEY_CLASSES_ROOT\*\shellex\ContextMenuHandlers```
  + ```HKEY_CLASSES_ROOT\*\shell```
  + ...
