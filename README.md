# android-di

## Step 3 - Qualifier

### 구현할 기능 목록

#### Qualifier

- [ ] 같은 인터페이스의 여러 구현체를 구분할 Qualifier를 정의한다.
- [ ] Room DB 구현체와 In-Memory 구현체를 추가한다.
- [ ] Qualifier에 따라 원하는 구현체를 주입한다.
- [ ] Qualifier가 없거나 잘못된 경우 명확한 예외를 발생시킨다.

#### DI 모듈 분리

- [x] DI 라이브러리를 `:di` 모듈로 분리한다.
- [x] `DependencyContainer`와 Annotation 등 공통 DI 코드를 `:di`로 이동한다.
- [x] `:di` 모듈이 `CartRepository`, `Product` 등 앱 도메인을 참조하지 않도록 한다.
- [x] 앱에서 구현체와 의존성을 등록할 수 있도록 구성한다.

### 테스트 목록

- [ ] Room DB 구현체가 올바르게 주입되는지 검증한다.
- [ ] In-Memory 구현체가 올바르게 주입되는지 검증한다.
- [ ] Qualifier가 없을 때 예외가 발생하는지 검증한다.
- [ ] 잘못된 Qualifier를 사용했을 때 예외가 발생하는지 검증한다.
- [x] `:di` 모듈이 앱 도메인에 의존하지 않는지 확인한다.
- [ ] 기존 테스트가 모두 통과하는지 확인한다.

### 설계 결정

- [x] Qualifier를 Annotation 방식으로 표현한다.
- [x] `:di` 모듈을 순수 JVM 모듈로 구성한다.

- Qualifier는 문자열 키 대신 Annotation으로 표현하기로 결정했다. 그 이유는 다음과 같다.
    - 컴파일 시점에 타입을 확인할 수 있다.
    - 문자열 오타를 줄일 수 있다.
    - `@Room`, `@InMemory`처럼 주입받는 구현체를 코드에서 명확하게 확인할 수 있다.

- `:di` 모듈은 순수 JVM 모듈로 구성한다.
    - DI 컨테이너가 Android와 앱의 도메인 타입에 의존하지 않도록 하기 위해서다.
    - 의존성 등록은 `:app`에서 담당하고, `:di`는 등록된 타입과 Qualifier를 기반으로 객체를 생성하고 주입한다.
    - Android 환경 없이 DI 로직을 빠르게 테스트할 수 있다.

### 선택 구현 목록

- [ ] 의존성 등록을 위한 DSL을 구현한다.
- [ ] DI 라이브러리를 배포하고 앱에 적용한다.

## Step 2 - Annotation

### 구현할 기능 목록

#### 필드 주입

- [x] 런타임에 조회 가능한 주입용 Annotation을 정의한다.
- [x] DI 컨테이너가 ViewModel을 생성한 뒤 Annotation이 붙은 필드에만 의존성을 주입하도록 구현한다.

#### 장바구니 데이터 처리

- [x] `id`, 상품 정보, `createdAt`을 가진 `CartProduct` 도메인 모델을 생성한다.
- [x] `CartProductEntity`를 `CartProduct`로 변환하는 `toDomain()` 매퍼를 추가한다.
- [x] `CartRepository` 인터페이스를 정의한다.
    - [x] 장바구니 상품 추가, 조회, 삭제 함수를 선언한다.
- [x] `DefaultCartRepository`가 `CartRepository`를 구현하도록 변경한다.
- [x] `DefaultCartRepository`가 `CartProductDao`를 생성자로 주입받도록 변경한다.
- [x] `DefaultCartRepository`에서 DAO 결과를 `CartProduct`로 변환한다.
- [x] DI 컨테이너가 `CartRepository` 요청 시 `DefaultCartRepository`를 생성하도록 연결한다.
- [x] Room Database에서 `CartProductDao`를 제공하고 DI 컨테이너에 등록한다.
- [x] `ProductsViewModel`과 `CartViewModel`에서 Repository의 `suspend` 함수를 `viewModelScope` 안에서 호출한다.
- [x] 장바구니 상태를 `List<Product>`에서 `List<CartProduct>`로 변경한다.
- [x] 상품 삭제 기준을 리스트 인덱스에서 실제 `id`로 변경한다.

#### 장바구니 화면

- [x] `DateFormatter`로 담은 시각을 포맷하여 상품명과 함께 표시한다.
- [x] 삭제 이벤트가 실제 상품의 `id`를 전달하도록 변경한다.
- [x] 변경된 모델에 맞게 `CartContentPreview`와 기존 테스트를 수정한다.

### 테스트 목록

- [x] Annotation이 붙은 필드에만 의존성이 주입되는지 검증한다.
- [x] 매핑 과정에서 `id`와 `createdAt`이 유지되는지 검증한다.
- [x] `CartRepository` 인터페이스 요청 시 `DefaultCartRepository`가 주입되는지 검증한다.
- [x] `CartViewModel`에서 `CartRepository`와 `CartProductDao`가 재귀적으로 주입되는지 검증한다.
- [x] 목록 중간 항목을 삭제해도 선택한 상품이 삭제되는지 검증한다.
- [x] 상품명과 담은 시각이 화면에 표시되는지 검증한다.
- [x] 사전 제공 테스트가 모두 통과하는지 확인한다.

### 선택 구현 목록

- [x] `LazyColumn`의 항목에 `id`를 key로 지정한다.
- [x] UI 계층에서 `CartProductEntity`를 직접 참조하지 않도록 한다.

## Step 1

### 리팩토링 목록

- [x] `ProductRepository`와 `CartRepository`를 `class`로 변경하고,
  `DependencyContainer`에서 인스턴스를 생성·캐싱하도록 수정한다.
- [x] `DependencyContainer`가 생성자 파라미터를 분석하여
  필요한 의존성을 재귀적으로 생성하도록 수정한다.
- [x] `AutoViewModelFactory`가 의존성 생성 로직을 직접 처리하지 않고
  `DependencyContainer`에 위임하도록 단순화한다.
- [x] 의존성을 생성할 수 없는 경우 원인을 알 수 있는 예외 메시지를 제공한다.

### 테스트 보완 목록

- [x] `ProductsViewModel`과 `CartViewModel`이 자동으로 생성되는지 검증한다.
- [x] Repository를 여러 번 요청해도 동일한 인스턴스가 반환되는지 검증한다.
- [x] 생성자 파라미터가 있는 클래스와 중첩된 의존성이 재귀적으로 생성되는지 검증한다.

### 구현할 기능 목록

- [x] `ProductRepository`와 `CartRepository`를 `object`로 변경하여 애플리케이션에서 단일 인스턴스를 공유한다.
- [x] 수동으로 주입하고 있는 ViewModel 의존성을 자동으로 주입하도록 변경한다.
    - [x] 생성자의 파라미터 정보를 바탕으로 필요한 의존성을 찾아 주입한다.
    - [x] 특정 ViewModel에 종속되지 않고 여러 ViewModel에서 재사용할 수 있는 자동 주입 로직을 구현한다.

## step 0.5
### 구현할 기능 목록

- [x] `ProductsViewModelFactory`를 생성하여 `ProductsViewModel`에 필요한 의존성을 수동으로 주입한다.
- [x] `CartViewModelFactory`를 생성하여 `CartViewModel`에 필요한 의존성을 수동으로 주입한다.
