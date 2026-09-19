# android-di

## 1단계 - 자동 DI 기능 목록

### 의존성 자동 생성

- [x] 클래스의 주 생성자를 Reflection으로 탐색한다.
- [x] 생성자 파라미터의 타입을 확인한다.
- [x] 파라미터 타입에 필요한 의존성을 찾아 생성자에 전달한다.
- [x] ViewModel 종류와 관계없이 재사용할 수 있는 자동 생성 로직을 구현한다.

### 의존성 관리

- [x] `ProductRepository`와 `CartRepository`를 자동 주입 대상으로 제공한다.
- [x] 여러 화면에서 사용하는 `CartRepository`를 한 번만 생성하고 공유한다.

### ViewModel 생성 연결

- [x] 공통 자동 생성 로직을 사용하는 `ViewModelProvider.Factory`를 구현한다.
- [x] `ProductsViewModel`의 수동 생성 코드를 공통 Factory 사용으로 변경한다.
- [x] `CartViewModel`도 같은 공통 Factory를 사용하도록 연결한다.

### 동작 검증

- [x] 상품 목록 화면과 장바구니 화면이 정상적으로 실행되는지 확인한다.
- [x] 상품 목록에서 담은 상품이 장바구니 화면에서도 보이는지 확인한다.
- [x] 새로운 ViewModel을 추가해도 별도의 주입 로직이 필요하지 않은지 확인한다.
- [x] 사전 제공 테스트를 모두 통과한다.
- [x] 자동 DI 구현에 Annotation을 사용하지 않았는지 확인한다.
