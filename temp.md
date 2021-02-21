Sở thích:
One Piece, Conan
Marvel: thor
Game: AOV
Music: pop
podcast: all ear english, better@english

=== Mysql view
khi dữ liệu ở bảng chính thay đổi thì trong View cũng sẽ được thay đổi
- Ưu điểm
  + Giới hạn dữ liệu cho người sử dụng: chỉ cho phép user xem được 1 vài field thôi
  + Tăng bảo mật vì nó chỉ Read Only, cannot write
  + Che giấu đi sự phức tạp của mô hình dữ liệu, bởi những gì mà họ thấy chỉ là môt View rất đơn giản, chứ họ ko thể thấy được JOIN từ những bảng nào với nhau, phức tạp ra sao...
- Nhược điểm
  + Khi truy vấn trong View có thể sẽ chậm hơn trong table
  + Bị phụ thuộc vào Table gốc, nếu Table gốc thay đổi cấu trúc thì đòi hỏi View cũng phải thiết kế lại cho phù hợp

=== Mysql Stored Proccedure
Mỗi thủ tục sẽ có các mức độ truy cập, nghĩa là ta cũng có thể cấp quyền sử dụng cho một Uesr nào đó trong hệ quản trị (Lưu ý là user trong hệ quản trị chứ không phải là admin của ứng dụng website).
- Ưu điểm:
  + Nếu câu query rất dài và phức tạp, phải join nhiều hay là cần gọi nhiều query 1 lúc, thì dùng Proccedure sẽ giúp giảm thời gian truy vấn giữa ứng dụng và database.
  + Giúp các team làm việc tốt hơn: phân ra team Coder riêng và team DBA (Database Administrator) viết thủ tục riêng.
- Nhược điểm:
  + ...

=== Mysql index
prefix index? lưu dạng TRIE?
