## Hibernate N+1 problem on OneToOne relationship
Ref: https://vladmihalcea.com/the-best-way-to-map-a-onetoone-relationship-with-jpa-and-hibernate/

Giả sử có 2 bảng Post và PostDetail quan hệ 1-1, bảng con PostDetail có 1 field post_id tham chiếu tới bảng cha. Khai báo entiy như sau:
```java
@Entity(name = "Post")
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "title")
    private String title;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private PostDetails details;

    // Getters and setters...
}

@Entity(name = "PostDetails")
@Table(name = "post_details")
public class PostDetails {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "created_by")
    private String createdBy;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // Getters and setters...
}
```

Fetch Post (parent entity), Hibernate tự động fetch children entity:
```java
Post post = entityManager.find(Post.class, 1L);
```

2 câu query sinh ra:
```sql
SELECT p.id AS id1_0_0_, p.title AS title2_0_0_
FROM   post p
WHERE  p.id = 1
 
SELECT pd.post_id AS post_id3_1_0_, pd.created_by AS created_1_1_0_,
       pd.created_on AS created_2_1_0_
FROM   post_details pd
WHERE  pd.post_id = 1
```

Solution:
1. Dùng @LazyToOne(LazyToOneOption.NO_PROXY) ở entity cha, nhưng đã test thử và KHÔNG thành công:
```java
@OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
@LazyToOne(LazyToOneOption.NO_PROXY)
private PostDetails details;
```

2. Dùng @MapsId: Thay vì bảng PostDetail có field post_id là fk tham chiếu tới bảng Post, ta dùng luôn cột id (PK) của bảng này làm FK tham chiếu tới bảng Post:
```java
@Entity(name = "PostDetails")
@Table(name = "post_details")
public class PostDetails {
    @Id
    private Long id;

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "created_by")
    private String createdBy;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Post post;
}
```

3. Dùng @Query cho nhanh, viết JPQL hoặc native query 😄

4. Dùng @NamedEntityGraph, @EntityGraph??? (Chưa test thử)

## Gộp các câu query COUNT vào 1 câu query
Xét database sakila (database sau khi update sau khi download từ trang chủ của MySQL), xét 2 table `store` và `customer` với quan hệ 1-n. Giờ ta muốn lấy tên store, sau đó đếm số lượng khách hàng nam, nữ ở từng store thì làm như nào?

Solution:
1. Dùng 3 câu
```sql
-- Câu đầu tiên get store name:
SELECT s.name AS storeName FROM store s;

-- 2 câu tiếp, mỗi câu đếm số lượng khách hàng với giới tính tương ứng:
SELECT COUNT(c.customer_id) AS maleCount
FROM customer c, store s
WHERE c.store_id = s.store_id AND c.gender = 'male'
GROUP BY c.store_id;

SELECT COUNT(c.customer_id) AS femaleCount
FROM customer c, store s
WHERE c.store_id = s.store_id AND c.gender = 'female'
GROUP BY c.store_id;
```

2. Dùng 1 câu query duy nhất, ref: https://stackoverflow.com/a/12789493/7688028
```sql
-- Using SUM:
SELECT s.name AS storeName,
  SUM(CASE WHEN c.gender = 'male' THEN 1 ELSE 0 END) AS maleCount,
  SUM(CASE WHEN c.gender = 'female' THEN 1 ELSE 0 END) AS femaleCount
FROM customer c, store s
WHERE c.store_id = s.store_id
GROUP BY c.store_id

-- Using COUNT (COUNT only counts non null values and the DECODE will return non null value 1 only if your condition is satisfied):
SELECT 
  s.name AS storeName,
  COUNT(IF(c.gender = 'male', 1, NULL)) AS maleCount,
  COUNT(IF(c.gender = 'female', 1, NULL)) AS femaleCount
FROM customer c, store s
WHERE c.store_id = s.store_id
GROUP BY c.store_id;
```

