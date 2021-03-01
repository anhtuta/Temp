```git reflog```: restore lost commit. Giả sử hiện tại HEAD ở lastest commit là commit10, và các commit trước đó lần lượt là commit9, commit8... Lỡ tay ```reset --hard``` về commit 8, làm sao để lấy lại commit 10? Dùng ```git reflog```, sẽ show ra các HEAD gần nhất, sau đó tìm commit10 và reset HEAD về đó

Recover staged changes: những file vừa được thêm vào Staging area (bằng lệnh git add), sau đó ``reset --hard``, thì recover như nào?
- ```git fsck --lost-found```
- Vào thư mục .git/lost-found sẽ tìm thấy file muốn recover

Recover uncommitted changes: các file ở working directory chưa được add vào staging area: KHÔNG thể recover được.
- Giả sử mới thêm 1 file mới, edit rồi lỡ tay xóa nó mà chưa add vào staging, thì có thể dùng IDE để recover (Eclipse, VS Code, Intellij đều hỗ trợ việc này)

=== Did you do any internship?
Yeah, I did 2 internships when I was a student. My first internship happend at my fourth year university, at that time, I submitted internship form to Fsoft company, and they accepted me. My position was web developer. I've never been an intern before, so I was so excited about it. At that time, I didn't have any knowledge or experience about web development, so I was taught about the most basic things, such as how to make a simple web page using HTML, CSS and Javascript. The internship inspired me to pursue Web Development career!
My second internship happend at my last year in university, I've got Samsung scholarship and started my intership for 3 months at Samsung R&D center. I was assigned to Backend team and they helped me to complete my graduation thesis. And after my graduation, I came back to that team and worked with them as an official employee.

=== Having
HAVING dùng để filter kết quả trên các phép toán aggregates, do WHERE không dùng được với aggregates.

VD: đếm các customer ở từng quốc gia, và lọc chỉ lấy những quốc gia có số lượng customer > 10:
```sql
-- Query SAI:
SELECT country_id, COUNT(customer_id) AS totalCustomer
FROM customers
WHERE COUNT(customer_id) > 10   -- Không thể dùng hàm aggregate với WHERE
GROUP BY country_id;

-- Query đúng:
SELECT country_id, COUNT(customer_id) AS totalCustomer
FROM customers
GROUP BY country_id
HAVING COUNT(customer_id) > 10;   -- Hoặc: HAVING totalCustomer > 10
```
Bản chất của HAVING là filter, tức là vẫn query ra hết, sau đó mới lọc các row thỏa mãn điều kiện

HAVING clause chỉ dùng được với SELECT statement, và KHÔNG thể dùng mà không có GROUP BY clause

So sánh HAVING clause và WHERE clause: https://www.geeksforgeeks.org/difference-between-where-and-having-clause-in-sql/

===
jwt nhược điểm:
- Cái token ấy nó ko đặc trưng cho 1 phiên làm việc của user. Nếu user bị đổi role hoặc logout, thì cái token ấy nó vẫn giữ cái phiên làm việc trước khi mà có sự kiện nào đó liên quan đến user xảy ra. Cái này ko khắc phục được. Nên sau khi update role của user, thì phía FE cần logout và xóa cái token đó đi, rồi login lại để bên BE gen lại token mới (mặc dù token cũ bị xóa nhưng nếu ai đó lấy được thì vẫn xài được nó với role cũ, bởi vì server ko lưu jwt trong database, mà chỉ verify jwt thông qua signature, nên token cũ thì signature vẫn đúng. Có thể lưu những jwt đã bị invoke trong database, nhưng như vậy ko hay lắm, vì bản chất của jwt là stateless, ko lưu trong database. Do đó nên đặt timeout của jwt ngắn thôi)


jwt ưu điểm:
- nhiều server chạy trên nhiều instance khác nhau vẫn có thể check token, do token ko lưu trong database (các server ko dùng chung database)
- Do ko lưu trong database nên lúc verify signature (check valid token), authorize user từ token ko cần request vào database => tăng performance

Nhược điểm của session authen:
- Do sessionID lưu trong memory (RAM) nên việc scale từ 1 server lên nhiều server sẽ khó khăn
=> có thể dùng server Redis để lưu sessionID, và các server backend khác dùng chung Redis đó
- Cần bảo vệ user khỏi tấn công CSRF (thêm _csrf token là được)
- Authen các thiết bị mobile như nào? Vì nó ko có cookie nên ko thể dùng được
- Cross domain cũng ko dùng được, vì cookie chỉ hoạt động trên domain hoặc subdomain đó. Khác domain là browser sẽ ko gửi kèm được cookie sessionID


=== Stateless vs Stateful trong Java servlet, spring
- Stateful: server có lưu dữ liệu của client, VD: authen = session
- Stateless: server ko lưu dữ liệu của client, vD: authen = JWT, access_token

Nếu dùng session để authen, thì từ sessionID của server có thể thêm, sửa, đọc data của session hiện tại:
```java
session.setAttribute("abc", "123");
session.getAttribute("abc");
```
Tức Java servlet đã có sẵn method để lưu và đọc data từ session của request rồi. Nghĩa là nó được thiết kế theo chuẩn stateful

Nếu dùng JWT để authen, thì spring security sẽ KHÔNG có các method để thêm hay đọc data từ access_token hay jwt, đó là stateless. Spring oauth2 được xây dựng theo chuẩn stateless. Tất nhiên có thể tự custom token và thêm data vào từng token đó rồi lưu lại, như thế sẽ thành stateful

=== OKRs
Mục tiêu (Objective): Tôi muốn làm điều gì?
Kết quả then chốt (Key Result): Tôi đến đó bằng cách nào?

