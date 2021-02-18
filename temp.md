<link rel="stylesheet" type="text/css" href="./temp.css"></link>

## So sánh HashMap, LinkedHashMap, TreeMap
<div id="table-compare-maps" class="demo">

| Thuộc tính                    | HashMap                                                                                                                  | LinkedHashMap                        | TreeMap                     |
|-------------------------------|--------------------------------------------------------------------------------------------------------------------------|--------------------------------------|-----------------------------|
| Implementation                | Buckets (mỗi bucket là 1 linkedlist, từ Java8, nếu bucket có >=8 phần tử thì linkedlist được chuyển thành balanced tree) | Buckets, double linked list          | Red-Black Tree              |
| Get, put, remove, containsKey | O(1) (Nhanh nhất)                                                                                                        | O(1) (Chậm hơn HashMap xíu)          | O(logn) (Chậm  nhất)        |
| Null key                      | Cho phép, và chỉ 1 null key                                                                                              | Cho phép, và chỉ 1 null key          | Không                       |
| Ràng buộc đối với class Key   | Phải override equals() và hashcode()                                                                                     | Phải override equals() và hashcode() | Phải implements Comparable  |
| Iteration order               | Random order                                                                                                             | Theo thứ tự insert                   | Sắp xếp theo thứ tự của key |
| Thread-safe                   | Không                                                                                                                    | Không                                | Không                       |

</div>
