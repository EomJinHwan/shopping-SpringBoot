# Spring Boot 쇼핑몰 REST API

## 프로젝트 소개

Java 콘솔 기반으로 구현했던 쇼핑몰 시스템을 Spring Boot REST API로 전환한 프로젝트입니다.  
콘솔에서 Scanner로 입력받던 방식을 HTTP 요청/응답 방식으로 바꾸고, 메모리(HashMap) 기반 데이터 관리를 JPA + H2 DB로 전환했습니다.  
Postman을 통해 API 동작을 확인했습니다.

## 주요 기능

### 회원 기능
- 회원가입 (아이디 길이 및 중복 검사, 비밀번호 길이 검사)
- 로그인 / 로그아웃
- 전체 회원 조회
- 회원 단건 조회
- 비밀번호 수정

### 상품 기능
- 상품 등록
- 전체 상품 조회
- 상품 단건 조회 (ID)
- 상품 검색 (이름)
- 상품 정보 수정 (이름 / 가격 / 재고 부분 수정 가능)
- 상품 삭제

### 장바구니 기능
- 상품 담기 (동일 상품 재담기 시 수량 합산)
- 장바구니 전체 조회
- 장바구니 단건 삭제
- 장바구니 전체 삭제

## 프로젝트 구조

```text
src/
├─ main/java/SpringBootShop/project/
│  ├─ domain/                # 도메인 객체
│  │  ├─ User.java
│  │  ├─ Product.java
│  │  └─ Cart.java
│  ├─ repository/            # JPA Repository 인터페이스
│  │  ├─ UserRepository.java
│  │  ├─ ProductRepository.java
│  │  └─ CartRepository.java
│  ├─ service/               # 비즈니스 로직
│  │  ├─ UserService.java
│  │  ├─ ProductService.java
│  │  └─ CartService.java
│  └─ controller/            # REST API 엔드포인트
│     ├─ UserController.java / UserForm.java
│     ├─ ProductController.java / ProductForm.java
│     └─ CartController.java / CartForm.java
└─ test/                     # 통합 테스트
   └─ service/
      ├─ UserServiceTest.java
      ├─ ProductServiceTest.java
      └─ CartServiceTest.java
```

## API 목록

### 회원
| Method | URL | 설명 |
|---|---|---|
| POST | /signUp | 회원가입 |
| POST | /login | 로그인 |
| GET | /logout | 로그아웃 |
| GET | /users | 전체 회원 조회 |
| GET | /users/{id} | 단건 회원 조회 |
| PUT | /users/{id} | 비밀번호 수정 |

### 상품
| Method | URL | 설명 |
|---|---|---|
| POST | /products | 상품 등록 |
| GET | /products | 전체 상품 조회 |
| GET | /products/{id} | 단건 상품 조회 |
| GET | /products/search?name= | 이름으로 상품 검색 |
| PUT | /products/{id} | 상품 정보 수정 |
| DELETE | /products/{id} | 상품 삭제 |

### 장바구니
| Method | URL | 설명 |
|---|---|---|
| POST | /cart/{userId} | 장바구니 담기 |
| GET | /cart/{userId} | 장바구니 전체 조회 |
| DELETE | /cart/{userId}/{productId} | 단건 삭제 |
| DELETE | /cart/{userId} | 전체 삭제 |

## 사용 기술

- Java
- Spring Boot
- Spring Data JPA
- H2 Database (인메모리)

## 구현하면서 고민한 내용

- Controller / Service / Repository 레이어 분리로 역할과 책임을 명확히 구분했습니다.
- JpaRepository 인터페이스를 상속해 기본 CRUD는 자동으로 제공받고, 필드 기반 조회/삭제는 메서드 네이밍으로 추가했습니다.
- 장바구니는 userId + productId 조합으로 식별하되, JPA 규칙에 맞게 surrogate key(cartId)를 별도로 두었습니다.
- User는 로그인 아이디(userId)와 PK(userNo)를 분리해 실무에서 PK가 변경되지 않아야 한다는 원칙을 적용했습니다.
- @SpringBootTest + @Transactional 조합으로 통합 테스트를 작성해 테스트 후 자동 롤백되도록 했습니다.

## 한계 및 개선 방향

- 비밀번호가 평문으로 저장되며, 실제 서비스에서는 BCrypt 암호화 처리가 필요합니다.
- H2 인메모리 DB를 사용해 서버 재시작 시 데이터가 초기화됩니다. 추후 MySQL 등 외부 DB로 전환할 수 있습니다.
