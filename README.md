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

## 2단계 - Annotation 기능 목록

### 필드 주입

- [x] 주입 대상 필드를 표시하는 런타임 Annotation을 정의한다.
- [x] ViewModel 생성 후 Annotation이 붙은 필드에 의존성을 주입한다.
- [x] Annotation이 없는 필드는 주입 대상에서 제외한다.
- [x] Annotation이 붙은 필드만 주입되고, 붙지 않은 필드는 주입되지 않는지 테스트한다.
- [x] Robolectric에서 ViewModel 생성과 필드 주입 성공, 의존성 누락 시 실패를 시나리오로 검증한다.

### 재귀 의존성 주입

- [x] 생성자 의존성을 재귀적으로 탐색하고, 등록된 인스턴스와 직접 생성할 의존성의 경계를 정한다.
- [x] 앱에서 생성한 Room Database 인스턴스를 컨테이너에 등록하고 재귀 생성 경계를 정한다.
- [x] 순환 의존성이 생기면 무한 재귀 대신 의존성 경로를 포함한 오류를 낸다.
- [x] `CartRepository`가 `CartProductDao`를 사용하도록 변경한다.
- [x] 장바구니 상품 엔티티를 도메인 모델 `CartProduct`로 변환하는 `toDomain()` 매퍼를 추가한다.
- [x] `CartProduct`에 상품 식별자, 화면 표시 정보, 장바구니에 담은 시각을 제공한다.
- [x] 장바구니 조회와 삭제를 실제 상품 식별자(`Long`) 기준으로 처리한다.
- [x] DAO와 Repository의 비동기 함수를 `suspend`로 변경하고, ViewModel에서 `viewModelScope`로 호출한다.
- [x] 장바구니 화면에 `DateFormatter`로 포맷한 담은 시각을 표시한다.
- [x] UI 계층에서 `CartProductEntity`를 참조하지 않는다.
- [x] `LazyColumn` 항목에 상품 식별자를 `key`로 지정한다.
- [x] 재귀 주입과 장바구니 변경 사항을 검증하고 사전 제공 테스트를 통과한다.

## 3단계 - Qualifier 기능 목록

### Qualifier로 구현체 선택

- [x] 같은 인터페이스에 여러 구현체를 등록하고 Qualifier로 원하는 구현체를 선택한다.
- [x] Room 구현체와 In-Memory 구현체를 각각 선택해 주입할 수 있도록 구성한다.
- [x] 같은 타입의 구현체가 여러 개일 때 Qualifier가 없으면 명확한 예외를 발생시킨다.
- [x] Qualifier가 구현체를 올바르게 선택하는지, Qualifier가 없을 때 예외가 발생하는지 테스트한다.

### DI 모듈 분리

- [x] DI 라이브러리를 `:di` 모듈로 분리하고 앱에서 사용하도록 연결한다.
- [x] `:di` 모듈이 쇼핑 앱의 도메인 타입에 의존하지 않도록 구성한다.
- [x] `:di` 모듈을 순수 JVM 또는 Android 모듈 중 하나로 결정하고 선택 근거를 기록한다.
- [x] Qualifier를 Annotation 또는 문자열 키 중 하나로 표현하고 선택 근거를 기록한다.
- [x] Qualifier 동작과 모듈 경계를 검증하는 테스트를 작성한다.

### 선택 요구 사항

- [x] DI 설정에 사용할 DSL을 설계한다.
- [ ] DI 라이브러리를 배포하고 앱에 적용한다.

### 설계 결정

- `:di`는 순수 JVM 모듈로 둔다. DI 코어의 리플렉션과 런타임 Annotation 처리에는 Android API가 필요하지 않다. 앱은 Room Database와 DAO처럼 직접 만들 인스턴스를 등록하고 `:di`는 타입 그래프를 해결한다.
- 컨테이너 구현은 `DependencyResolver` 인터페이스 뒤에 두고, `AutoViewModelFactory`가 이 인터페이스를 사용한다. ViewModel은 컨테이너를 조회하지 않고 생성자 의존성을 받는다.
- `CartRepository` 계약은 안정적인 `domain.repository` 패키지에 두고, Room 구현은 `data` 계층에 둔다. 데이터 구현이 도메인 계약을 구현하도록 의존 방향을 정한다.
- 앱이 Factory를 호출해 객체 생성을 요청하므로 흐름은 앱이 소유한다. 컨테이너가 앱의 생명주기를 지휘하는 프레임워크로 동작하지 않으며, ViewModel이 컨테이너에서 의존성을 직접 꺼내는 서비스 로케이터 패턴도 사용하지 않는다.
- Qualifier는 `@Qualifier` 메타 Annotation을 붙인 런타임 Annotation으로 표현한다. 등록 키에 Annotation 타입을 사용해 문자열 오타를 줄이고, 등록과 주입 지점이 맞지 않으면 구체적인 누락 또는 모호성 오류를 낸다.

## 4단계 - Lifecycle 기능 목록

### 스코프와 의존성 생명주기

- [x] 스코프 종류를 `:di`가 알지 않도록, 외부에서 만든 스코프 키와 캐시를 사용해 의존성을 관리한다.
- [x] 앱 생명주기 스코프에서 `CartRepository`를 한 번 생성해 공유한다.
- [x] ViewModel 스코프에서 `ProductRepository`를 생성하고 ViewModel이 정리될 때 캐시를 비운다.
- [x] 내비게이션 목적지 스코프에서 `DateFormatter`를 생성하고 목적지가 백스택에서 제거될 때 캐시를 비운다.
- [x] 스코프 종료 시 내부 인스턴스 참조를 제거하고, 닫힌 스코프의 재사용을 막는다.
- [x] 등록한 스코프 종류마다 인스턴스 재사용, 스코프 분리, 종료 후 정리를 테스트한다.
- [x] 새로운 스코프 키를 추가할 때 `:di`의 분기문을 수정하지 않아도 되는지 테스트한다.

### Android 생명주기 연동

- [x] 앱 연동 계층에서 `NavBackStackEntry` ID를 화면 스코프 인스턴스 ID로 사용한다.
- [x] 화면 스코프 소유 ViewModel을 목적지의 `ViewModelStore`에 연결해 구성 변경 중에는 유지하고 실제 pop 때 정리한다.
- [x] 화면 진입·이탈과 반복 진입 시 새 `DateFormatter` 생성 및 이전 인스턴스 정리를 시나리오로 검증한다.
- [x] 장바구니 화면은 `DateFormatter`를 주입받고, 표시 컴포저블은 전달받은 인스턴스만 사용한다.

### 설계 기록과 검증

- [x] DI와 서비스 로케이터를 구분하고, DIP와 IoC 각각의 관계를 코드 위치와 함께 설명한다.
- [x] KSP 코드 생성으로 전환할 때 빌드 시점에 알아야 할 정보와 재설계 지점을 Reflection의 비용과 연결해 기록한다.
- [x] 전체 테스트와 ktlint 검사를 통과한다.

### 설계 결정

- `ScopeKey`는 스코프의 종류를 가리키고 `DependencyScope`는 한 소유자에 속한 캐시다. `:di`는 임의의 키를 비교하고 캐시를 제공할 뿐 Android 생명주기나 스코프 종류를 알지 않는다. 바깥 계층에서 새 키를 선언해 등록할 수 있고, 스코프 종료 시 캐시를 비운다. 구현은 [`DependencyScope.kt`](di/src/main/kotlin/woowacourse/di/DependencyScope.kt)와 [`DependencyContainer.kt`](di/src/main/kotlin/woowacourse/di/DependencyContainer.kt)에 있다.
- 앱은 Room 기반 `CartRepository`를 앱 컨테이너의 기본 캐시에 둔다. `ProductRepository`는 ViewModel 스코프에 등록하고 [`AutoViewModelFactory`](app/src/main/java/woowacourse/shopping/di/AutoViewModelFactory.kt)가 ViewModel에 스코프 정리를 연결한다.
- `DateFormatter`는 내비게이션 목적지 스코프에 등록한다. [`ShoppingNavHost`](app/src/main/java/woowacourse/shopping/ui/ShoppingNavHost.kt)는 `NavBackStackEntry.id`로 스코프 소유 ViewModel을 만들고, ViewModel의 `onCleared()`에서 캐시를 닫는다. Navigation은 화면을 실제로 pop할 때 목적지의 `ViewModelStore`를 정리하고 구성 변경 동안에는 보존하므로, 화면을 다시 그리는 경우 같은 포매터를 사용하고 재진입하는 경우 새 인스턴스를 만든다. [`CartScreen`](app/src/main/java/woowacourse/shopping/ui/cart/CartScreen.kt)은 포매터를 매개변수로 받고 콘텐츠 계층에 전달한다.
- 이 컨테이너를 DI로 판단하는 기준은 소비자가 의존성을 함수·생성자 시그니처로 드러내는지다. `ProductsViewModel`과 `CartViewModel`은 저장소를 생성자에서 받으며 `CartScreen`은 `DateFormatter`를 매개변수로 받는다. 소비자는 컨테이너를 직접 조회하지 않는다. 조회는 객체 그래프를 조립하는 [`ShoppingNavHost`](app/src/main/java/woowacourse/shopping/ui/ShoppingNavHost.kt)와 ViewModel Factory 경계에 모여 있다. 이 점이 의존성을 감춘 채 객체가 컨테이너에서 꺼내는 서비스 로케이터와 다르다.
- DI와 DIP는 다른 질문에 답한다. DI는 객체가 의존성을 받는 방식이고, DIP는 고수준 정책과 저수준 구현 사이의 소스 의존 방향에 관한 원칙이다. 앱의 `CartRepository` 계약은 도메인 패키지에 있고 Room 구현은 그 계약을 구현한다. `:di`는 쇼핑 도메인 타입을 참조하지 않는다. 이 구조는 DIP를 따르며 DI를 사용하지만, DI를 했다는 사실만으로 DIP가 성립하는 것은 아니다.
- IoC는 객체 생성과 실행 흐름의 제어권이 어디에 있는지에 관한 개념이다. Android의 `ViewModelProvider`가 등록된 Factory를 호출하고 Factory가 컨테이너에 그래프 생성을 맡긴다. 화면 스코프 정리는 컨테이너가 화면을 관찰하지 않고 Android가 소유한 목적지 ViewModel의 `onCleared()`를 연결 계층이 사용한다. 컨테이너는 생성 작업 일부를 맡지만 앱의 생명주기를 지휘하지 않는다. DI는 IoC 컨테이너 없이도 적용할 수 있다.
- KSP로 바꾸려면 지금 `primaryConstructor`와 파라미터 타입을 런타임에 읽는 `createFromPrimaryConstructor`, 런타임 등록을 고르는 `selectKey`, Annotation을 훑는 `qualifierOf`와 `injectMembers`의 역할을 다시 설계해야 한다. KSP는 컴파일 시점에 생성자, Qualifier, 주입 대상, 스코프 선언, 등록 그래프를 알아야 누락·모호성·순환 오류를 빌드 오류로 바꿀 수 있다. 이를 위해 런타임 `KClass` 맵 대신 컴파일 시점 바인딩 선언과 생성된 팩토리를 두고, Room Database나 Android `Context`처럼 런타임에 생기는 값은 앱이 제공하는 명시적 생성 함수로 연결해야 한다. 이 방식은 Reflection의 R8 keep 규칙과 릴리즈 크기 부담, 앱 시작 시 메타데이터 해석, 늦은 런타임 오류를 줄이고 KMP 선택지를 넓힌다. 대신 런타임에 자유롭게 등록하는 유연성을 잃고 KSP 프로세서 및 모듈 그래프 설계가 추가된다. 이 학습 미션에서는 바인딩 시점과 생명주기 원리를 드러내기 위해 Reflection을 유지한다.
