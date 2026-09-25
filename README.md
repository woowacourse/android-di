# android-di

## 2단계 구현 목록

1. Annotation 도입
   - [x] 의존성 주입이 필요한 필드와 그렇지 않은 필드 구분
   - [x] 어노테이션이 명시된 요소만 의존성 주입
   - [x] 어노테이션 동작 테스트 작성
2. Recursive 의존성 주입 및 구조 개선
   - [x] CartRepository가 CartProductDao를 주입받아 참조하도록 변경
   - [x] CartProduct 도메인 모델 추가 및 toDomain() 매퍼 함수 추가
   - [x] CartProduct id 필드 기반 상품 삭제 로직 구현
   - [x] viewModelScope를 통한 비동기 로직 구현
   - [x] 장바구니에 상품 추가 시 상품을 추가한 시각 기록 및 장바구니 화면 표시
3. 선택 요구사향
   - [x] LazyColumn의 items에 key 지정
   - [x] UI 계층에서 CartProductEntity를 직접 참조하지 않도록 구현

## 3단계 구현 목록

1. Qualifier
    - [x] 하나의 인터페이스에 여러 구현체를 `Qualifier` 어노테이션으로 구분

2. 모듈 분리
   - [x] DI 관련 코드를 별도의 모듈로 분리

## 설계 선택 근거

### 1. Qualifier를 애노테이션으로 표현

1. `@field:RoomBacked`처럼 의존성을 받는 위치에서 주입 의도가 잘 보임
2. 선언과 사용이 어노테이션 타입으로 연결되 오타와 이름 충돌을 방지할 수 있음

### 2. `:di`를 순수 JVM 모듈로 구성

DI 관련 파일을 DependencyContainer로 변경하고 별도의 모듈로 분리했음
DependencyContainer는 도메인 객체와 Android Room에 대한 의존성을 갖지 않기 때문에 따로 분리함
Room 데이터 베이스와 Dao를 제공하는 `DataContainer`와 DI 객체를 조립하는 `ShoppingApplication`은 앱에 남겼음
