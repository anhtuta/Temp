## LinkedHashMap
Extend từ class HashMap, LinkedHashMap dùng thêm 1 DSLK đôi để duy trì thứ tự các phần tử được put vào map:
```java
// [Java8] Override lại Node của HashMap để thêm 2 con trỏ before, after cho việc tạo DSLK đôi
static class Entry<K,V> extends HashMap.Node<K,V> {
  Entry<K,V> before, after;
  Entry(int hash, K key, V value, Node<K,V> next) {
    super(hash, key, value, next);
  }
}
/**
 * The head (eldest) of the doubly linked list.
 * Đây chính là phần đầu của DSLK đôi
 */
transient LinkedHashMap.Entry<K,V> head;

/**
 * The tail (youngest) of the doubly linked list.
 * Đây chính là phần đuôi của DSLK đôi
 */
transient LinkedHashMap.Entry<K,V> tail;
```
Do cần thêm 1 DSLK đôi nên việc thêm, xóa trên LinkedHashMap sẽ chậm hơn 1 chút xíu so với HashMap

How does it work: trong method putVal của HashMap:
```java
final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict)  // [Java8]
```
Method này sẽ gọi tới hàm newNode khi 1 bucket có nhiều hơn 1 phần tử (collision):
```java
p.next = newNode(hash, key, value, null);  // [Java8]
```
LinkedHashMap đã override lại hàm này để insert phần tử mới trên vào DSLK đôi như sau:
```java
// [Java8]
Node<K,V> newNode(int hash, K key, V value, Node<K,V> e) {
  LinkedHashMap.Entry<K,V> p = new LinkedHashMap.Entry<K,V>(hash, key, value, e);
  linkNodeLast(p);
  return p;
}
private void linkNodeLast(LinkedHashMap.Entry<K,V> p) {
  LinkedHashMap.Entry<K,V> last = tail;
  tail = p;
  if (last == null)
    head = p;
  else {
    p.before = last;
    last.after = p;
  }
}
```

## TreeMap
HashMap và LinkedHashMap khá giống nhau, đều dùng mảng Buckets và hàm hash để lưu data, nhưng TreeMap thì khác hoàn toàn: nó KHÔNG dùng mảng Buckets và hash function, thay vào đó nó dùng Red-Black tree (1 loại cây nhị phân cân bằng - *self-balancing binary search tree*) để lưu data:
```java
// [Java8]
private transient Entry<K,V> root;  // Đây chính là root của cây BST

// Mỗi node của cây BST được định nghĩa như sau:
static final class Entry<K,V> implements Map.Entry<K,V> {
  K key;
  V value;
  Entry<K,V> left;
  Entry<K,V> right;
  Entry<K,V> parent;
  boolean color = BLACK;
}
```
Do đó, TreeMap chả có Buckets size, loadFactor hay threshold, và cũng chả có chức năng resize luôn!

Note: class Key phải implements interface Comparable, chẳng hạn:
```java
public class Key implements Comparable<Key> {
  private String key;

  @Override
  public int compareTo(Key anotherKey) {
    return this.key.compareTo(anotherKey.key);
  }
}
```
Nếu Key là kiểu Integer hay String thì đã implements sẵn Comparable rồi!

Khá ghét Intellij IDEA ở chỗ sau:
- giả sử có class Key như sau: ```public class Key {}```. Giờ muốn implements Comparable, thì khi gõ, Intellij nó gợi ý thành code sau (ko gợi ý kiểu Generic):
```java
public class Key implements Comparable {
  @Override
  public int compareTo(Object o) {
    // TODO Auto-generated method stub
    return 0;
  }
}
```
Trong khi dùng Eclipse thì nó gợi ý như sau chuẩn hơn:
```java
public class Key implements Comparable<Key> {
  @Override
  public int compareTo(Key o) {
    // TODO Auto-generated method stub
    return 0;
  }
}
```
- Intellij gợi ý KHÔNG hiển thị hết các method với các loại params. Giả sử class TreeMap có nhiều hơn 1 constructor là:
```java
public TreeMap() {
  comparator = null;
}
public TreeMap(Comparator<? super K> comparator) {
  this.comparator = comparator;
}
public TreeMap(Map<? extends K, ? extends V> m) {
  comparator = null;
  putAll(m);
}
...
```
Thì khi dùng Intellij, Ctr+space chỉ gợi ý duy nhất 1 constructor!

## So sánh HashMap, LinkedHashMap, TreeMap
| Thuộc tính                    | HashMap                                                                                                                  | LinkedHashMap                        | TreeMap                     |
|-------------------------------|--------------------------------------------------------------------------------------------------------------------------|--------------------------------------|-----------------------------|
| Implementation                | Buckets (mỗi bucket là 1 linkedlist, từ Java8, nếu bucket có >=8 phần tử thì linkedlist được chuyển thành balanced tree) | Buckets, double linked list          | Red-Black Tree              |
| Get, put, remove, containsKey | O(1) (Nhanh nhất)                                                                                                        | O(1) (Chậm hơn HashMap xíu)          | O(logn) (Chậm  nhất)        |
| Null key                      | Cho phép, và chỉ 1 null key                                                                                              | Cho phép, và chỉ 1 null key          | Không                       |
| Ràng buộc đối với class Key   | Phải override equals() và hashcode()                                                                                     | Phải override equals() và hashcode() | Phải implements Comparable  |
| Iteration order               | Random order                                                                                                             | Theo thứ tự insert                   | Sắp xếp theo thứ tự của key |
| Thread-safe                   | Không                                                                                                                    | Không                                | Không                       |

## HashSet
Sử dụng 1 HashMap để lưu data, với mọi value của map đều như nhau, ko đổi, có giá trị = PRESENT, và ko dùng luôn :)
```java
// [Java8]
public class HashSet<E> extends AbstractSet<E> implements Set<E>, Cloneable, java.io.Serializable {
  // Đây, biến HashMap này lưu data cho Set đó!
  private transient HashMap<E,Object> map;

  // Dummy value to associate with an Object in the backing Map
  private static final Object PRESENT = new Object();

  /**
   * Constructs a new, empty set; the backing <tt>HashMap</tt> instance has
   * default initial capacity (16) and load factor (0.75).
   */
  public HashSet() {
    map = new HashMap<>();
  }
}
```
Thế thôi, còn lại cách hoạt động giống như HashMap

## LinkedHashSet
Extend từ HashSet, chỉ khác ở chỗ LinkedHashSet dùng LinkedHashMap để lưu data. Mọi constructor của LinkedHashSet sẽ gọi hàm super constructor với 3 tham số như này:
```java
// [Java8]
public LinkedHashSet(int initialCapacity, float loadFactor) {
  super(initialCapacity, loadFactor, true);
}

// Hàm super constructor của class HashSet như này:
HashSet(int initialCapacity, float loadFactor, boolean dummy) {
  map = new LinkedHashMap<>(initialCapacity, loadFactor);
}
```
Thế thôi, còn lại class LinkedHashSet chả có method đặc biệt nào khác. Hoạt động giống HashSet

## TreeSet
Dùng TreeMap để lưu data, vậy thôi!

Tóm lại là cả 3 class Set trên đều dùng 3 map tương ứng để lưu data, với value là PRESENT!
