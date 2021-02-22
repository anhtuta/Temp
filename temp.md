Sở thích:
One Piece, Conan
Marvel: thor
Game: AOV
Music: pop
podcast: all ear english, better@english'

Kí hiệu O lớn: Điều quan trọng không phải là số lượng data mà là cách số lượng phép toán tăng lên khi lượng data tăng lên

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
- Ưu điểm:
  + Tăng tốc độ tìm kiếm records theo câu lệnh WHERE (Không chỉ giới hạn trong câu lệnh SELECT mà với cả xử lý UPDATE hay DELETE có điều kiện WHERE)
- Nhược điểm:
  + Tốc độ write (insert,update,delete) sẽ chậm hơn, do cần insert,update,delete cả index nữa
  + Tốn thêm bộ nhớ

- MySQL cung cấp 3 kiểu index khác nhau cho data đó là B+Tree, Hash và RTree index (Btree trong MySQL là B+Tree nhé, vì keyword BTREE ko thể dùng dấu +)
  + B+Tree index được sử dụng trong các biểu thức so sánh dạng: =, >, >=, <, <=, BETWEEN và LIKE (tìm kiếm 1 khoảng giá trị). Nhưng nếu LIKE '%abc' thì sẽ ko tận dụng được index, lúc này MySQL sẽ scan cả table
  + Hash index chỉ nên sử dụng trong các biểu thức toán tử là = và <> (Không sử dụng cho toán từ tìm kiếm 1 khoảng giá trị như >, <)
  + Hash có tốc độ nhanh hơn kiểu Btree
  + Không thể tối ưu hóa toán tử ORDER BY bằng việc sử dụng Hash index bởi vì nó không thể tìm kiếm được phần từ tiếp theo trong Order
  + RTREE: ???

- B-Tree
  + Ko phải là BST: 1 node có thể có nhiều hơn 2 node con
  + Mọi node đều lưu data, do đó có trường hợp time tìm kiếm là O(1) (tìm thấy ngay ở root), nhưng ko thể tìm kiếm theo khoảng
  + Các node lá ko link với nhau
- B+Tree
  + Ko phải là BST
  + Chỉ node lá lưu key và data, node none-leaf chỉ lưu key. Do đó thời gian tìm kiếm luôn là O(logn) (fixed)
  + Các node lá có link với nhau (mỗi dùng 2 pointer link với node trước và sau)
  + Phù hợp với external storage (storing disk data)
- MySQL dùng B+Tree, Mongodb dùng B-Tree: https://medium.com/@mena.meseha/what-is-the-difference-between-mysql-innodb-b-tree-index-and-hash-index-ed8f2ce66d69

- Prefix index? lưu dạng TRIE? => Sai nhé
  + Prefix index là index áp dụng trên cột có kiểu data là string, dùng ctdl B-Tree
  + Áp dụng cho phần đầu của column đó, tức là áp dụng cho substring(column, length)
- Index trên nhiều cột:
CREATE INDEX index_name ON table_name(c1,c2,c3);
  + nếu tạo index cho n cột thì những câu truy vấn có số cột ít hơn đều được tối ưu hóa, và phải tuân theo thứ tự từ trái sang phải. VD ở trên ta tạo index trên 3 cột, thì query như sau sẽ được tối ưu và tìm kiếm theo index:
    WHERE c1 = "abc" AND c2 = 123 AND c3 = "xyz";
    WHERE c1 = "abc" AND c2 = 123;
    WHERE c1 = "abc";
  + Query như sau thì ko tận dụng được index:
    WHERE c1 = "abc" AND c3 = "xyz";  // ko đúng thứ tự c1,c2,c3
    WHERE c3 = "xyz" AND c1 = "abc";  // ko đúng thứ tự c1,c2,c3
    WHERE c2 = 123; // ko đúng thứ tự
  
