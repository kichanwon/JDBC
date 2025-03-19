# **1️⃣ 트랜잭션 (Transaction)**

트랜잭션은 **데이터베이스에서 하나의 논리적 작업 단위를 의미하며, All-or-Nothing 방식으로 실행**돼.  
즉, **모든 작업이 성공하면 커밋(commit)**, **하나라도 실패하면 롤백(rollback)** 되는 원칙이야.

## **💡 트랜잭션의 ACID 특성**

1. **Atomicity (원자성)**
    - 트랜잭션 내의 작업은 **모두 수행되거나, 모두 롤백되어야 함**
    - 예: 계좌 이체(출금 + 입금) → 출금만 되고 입금이 안 되면 안 됨
2. **Consistency (일관성)**
    - 트랜잭션 수행 전후에 **데이터의 무결성이 보장되어야 함**
    - 예: 돈을 이체했는데, 총 자산 금액이 변하면 안 됨
3. **Isolation (고립성)**
    - 동시에 여러 트랜잭션이 실행되더라도 **서로 영향을 주지 않아야 함**
    - 예: 계좌 잔액이 10만 원인데 동시에 2명이 10만 원을 인출하면 안 됨
4. **Durability (지속성)**
    - 트랜잭션이 완료되면 **데이터가 영구적으로 반영**되어야 함
    - 예: 전원이 꺼져도 데이터는 유지

---

# **2️⃣ 트랜잭션 적용 방식**

트랜잭션을 적용하는 방법은 여러 가지가 있어.

### **1. JDBC 트랜잭션 (수동 관리)**

JDBC에서는 기본적으로 **자동 커밋(auto-commit)이 활성화**되어 있어.  
이를 비활성화하고 **수동으로 `commit()` / `rollback()`을 실행하면 트랜잭션을 관리**할 수 있어.

java

복사편집

`Connection conn = null; PreparedStatement pstmt = null;  try {     conn = DriverManager.getConnection(DB_URL, USER, PASS);     conn.setAutoCommit(false); // 자동 커밋 해제      String sql = "INSERT INTO users (id, pw, name) VALUES (?, ?, ?)";     pstmt = conn.prepareStatement(sql);          pstmt.setString(1, "user100");     pstmt.setString(2, "pass100");     pstmt.setString(3, "유저백");     pstmt.executeUpdate();      conn.commit(); // 모든 작업이 성공하면 커밋 } catch (Exception e) {     if (conn != null) {         conn.rollback(); // 하나라도 실패하면 롤백     }     e.printStackTrace(); } finally {     if (pstmt != null) pstmt.close();     if (conn != null) conn.close(); }`

✅ **장점:** 명시적으로 `commit()` / `rollback()`을 수행하므로 직접 제어 가능  
❌ **단점:** 코드가 길어지고 복잡해짐, 예외 처리 부담 증가

---

### **2. Spring @Transactional (자동 관리)**

Spring에서는 **`@Transactional`을 사용하면 간편하게 트랜잭션을 관리**할 수 있어.

java

복사편집

`@Service public class UserService {      @Autowired     private UserRepository userRepository;      @Transactional // 트랜잭션 자동 적용     public void registerUsers(List<User> users) {         for (User user : users) {             userRepository.save(user); // insert 수행         }     } }`

✅ **장점:** 코드가 간결해지고, 예외 발생 시 자동으로 롤백됨  
❌ **단점:** 세밀한 제어가 어렵고, 특정 상황에서 예상치 못한 동작이 발생할 수 있음

---

# **3️⃣ 동시성 제어 (Concurrency Control)**

트랜잭션이 동시에 실행될 때 **데이터 무결성을 보장하기 위한 기법**들이 있어.

## **1. 동시 실행 시 발생하는 문제**

1. **Dirty Read (더티 리드)**
    
    - 트랜잭션 A가 아직 `commit()`하지 않은 데이터를 트랜잭션 B가 읽는 경우
    - 해결 방법: **READ COMMITTED 이상으로 격리 수준 설정**
2. **Non-Repeatable Read (반복 불가능한 읽기)**
    
    - 트랜잭션 A가 데이터를 조회한 후, 트랜잭션 B가 데이터를 수정하면, A가 다시 조회할 때 값이 달라지는 현상
    - 해결 방법: **REPEATABLE READ 이상으로 격리 수준 설정**
3. **Phantom Read (팬텀 리드)**
    
    - 트랜잭션 A가 특정 조건의 데이터를 조회한 후, 트랜잭션 B가 데이터를 삽입하면, A가 다시 조회할 때 새로운 데이터가 나타남
    - 해결 방법: **SERIALIZABLE 격리 수준 설정**

---

## **2. 데이터베이스 격리 수준 (Isolation Level)**

**격리 수준이 높아질수록 데이터 무결성은 보장되지만, 성능 저하가 발생**해.

|격리 수준|Dirty Read|Non-Repeatable Read|Phantom Read|성능|
|---|---|---|---|---|
|**READ UNCOMMITTED**|발생 가능|발생 가능|발생 가능|빠름|
|**READ COMMITTED**|방지|발생 가능|발생 가능|보통|
|**REPEATABLE READ**|방지|방지|발생 가능|느림|
|**SERIALIZABLE**|방지|방지|방지|가장 느림|

✅ **MySQL 기본 설정:** `REPEATABLE READ`  
✅ **Oracle 기본 설정:** `READ COMMITTED`

### **Java에서 트랜잭션 격리 수준 설정**

Spring에서 `@Transactional(isolation = Isolation.XXX)`을 설정 가능

java

복사편집

`@Transactional(isolation = Isolation.REPEATABLE_READ)`

---

## **3. 동시성 제어 방식**

### **1) 비관적 락 (Pessimistic Lock)**

- 트랜잭션이 시작될 때 **다른 트랜잭션이 접근하지 못하도록 락(lock)을 거는 방식**
- 충돌이 빈번한 환경에서 사용 (ex. 은행 계좌 이체)
- **`SELECT ... FOR UPDATE`** 사용

java

복사편집

`@Lock(LockModeType.PESSIMISTIC_WRITE) @Query("SELECT u FROM User u WHERE u.userId = :userId") User findUserForUpdate(@Param("userId") String userId);`

✅ **장점:** 충돌이 발생할 가능성을 최소화  
❌ **단점:** 성능 저하 (다른 트랜잭션이 기다려야 함)

---

### **2) 낙관적 락 (Optimistic Lock)**

- 트랜잭션이 데이터를 수정할 때 **버전 정보를 확인하고 충돌을 감지하는 방식**
- `@Version`을 사용하여 충돌 발생 시 예외 발생 (`OptimisticLockException`)

java

복사편집

`@Entity public class User {     @Id     private String userId;          @Version // 버전 정보     private int version; }`

✅ **장점:** 락을 사용하지 않아 성능이 좋음  
❌ **단점:** 충돌이 발생하면 트랜잭션을 다시 실행해야 함

---
## **`READ ONLY` vs `FOR UPDATE` 비교 요약**

|비교 항목|`READ ONLY` 트랜잭션|`FOR UPDATE` 트랜잭션|
|---|---|---|
|**목적**|읽기 전용 성능 최적화|데이터 무결성 보장|
|**락(Lock) 사용**|없음 (동시성 높음)|배타적 락 (X-Lock, 동시성 낮음)|
|**데드락 발생 가능성**|없음|있음 (여러 락이 걸릴 경우)|
|**트랜잭션 성능**|빠름 (락 없음)|느릴 수 있음 (락으로 인해 대기 발생)|
|**동시 실행 가능성**|높음 (여러 트랜잭션이 동시에 읽기 가능)|낮음 (다른 트랜잭션이 대기해야 할 수 있음)|
|**사용 사례**|리포트 조회, 대시보드, 분석|은행 계좌 이체, 재고 감소, 예약 시스템|

---

# **4️⃣ 정리**

|개념|설명|
|---|---|
|**트랜잭션**|데이터 작업을 하나의 논리적 단위로 묶어 All-or-Nothing 실행|
|**JDBC 트랜잭션**|`commit() / rollback()`을 수동 호출|
|**Spring @Transactional**|트랜잭션을 자동 관리|
|**동시성 문제**|Dirty Read, Non-Repeatable Read, Phantom Read|
|**격리 수준**|READ UNCOMMITTED → SERIALIZABLE (높을수록 안전하지만 느림)|
|**비관적 락**|충돌 방지를 위해 락을 걸어 동시 접근 차단|
|**낙관적 락**|버전 정보를 확인해 충돌을 감지|

| 방법                      | 특징                      | 사용 사례             |
| ----------------------- | ----------------------- | ----------------- |
| `SELECT ... FOR UPDATE` | **비관적 락** (수정 차단)       | 계좌 잔액 차감, 인벤토리 관리 |
| `FOR SHARE`             | **공유 락** (읽기 가능, 수정 불가) | 다중 트랜잭션이 읽을 때     |
| **낙관적 락 (`@Version`)**  | 충돌 발생 시 예외 처리           | 충돌 가능성이 낮을 때      |
