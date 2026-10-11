# android-di

## Step 4 - Lifecycle

### 구현할 기능 목록

- [x] 의존성을 처음 요청할 때 생성하고, 같은 스코프 안에서 재사용한다.
- [x] `CartRepository`를 앱 스코프에서 공유 한다.
- [x] `ProductRepository`를 ViewModel 스코프에서 재사용하고, ViewModel 종료 때 정리한다.
- [x] `DateFormatter`를 화면 스코프에서 재사용하고, 화면을 나가면 정리한다.
- [x] 스코프가 종료되면 보유한 인스턴스 참조를 제거한다.
- [x] 새 스코프 종류를 추가해도 컨테이너 내부에 `when` 분기를 늘리지 않는다.

### 설계 정리

#### 내가 만든 컨테이너는 DI인가, 서비스 로케이터인가?

이 컨테이너는 **ViewModel에는 생성자 주입을 하고, 화면의 `DateFormatter`는 서비스 로케이터 방식으로 가져오는 혼합 구조**다.

- `AutoViewModelFactory.create()`는 `DependencyContainer.create()`에 ViewModel 타입을 넘기고, 컨테이너가 생성자를 읽어 의존성을 만든다. `ProductsViewModel`과 `CartViewModel`의 생성자에 필요한 타입이 드러나 있으므로 이 경로는 생성자 주입이다.
- 반면 `CartScreen`은 `DependencyContainer.getInstance(DateFormatter::class, scopeId)`를 직접 호출한다. 화면 함수의 매개변수에는 `DateFormatter`가 없고, 함수 내부에서 전역 컨테이너를 조회하므로 이 경계는 서비스 로케이터다.

따라서 컨테이너 전체를 순수한 DI라고 부르기는 어렵다. 특히 서비스 로케이터 경로에서는 화면이 컨테이너에 직접 의존하고 의존성이 함수 시그니처에 드러나지 않는다.

이 판단은 DIP나 IoC와는 다른 축이다. DIP는 상위 정책과 하위 구현이 추상화에 의존하도록 하는 원칙이다. 예를 들어 `CartViewModel`은 `CartRepository` 인터페이스를 받고, 앱은 `DefaultCartRepository`를 등록한다. IoC는 객체 생성과 흐름의 제어를 외부에 맡기는 개념이다. 컨테이너가 ViewModel 생성 책임을 맡는 것은 IoC에 해당하지만, 화면에서 컨테이너를 직접 조회하는 방식은 의존성을 숨기는 서비스 로케이터다. DIP를 적용했는지, IoC를 적용했는지와 주입 방식을 DI 또는 서비스 로케이터로 분류하는 것은 서로 같은 질문이 아니다.

#### KSP 코드 생성 방식으로 바꾼다면

현재 `DependencyContainer.create()`는 Kotlin Reflection으로 primary constructor, 파라미터 타입과 Qualifier를 런타임에 읽고 호출한다. `getInstance()`는 그 정보로 구현체를 찾거나 생성한다. KSP로 바꾸려면 이 동적 탐색을 빌드 시점에 생성되는 Factory와 의존성 그래프로 옮겨야 한다.

빌드 시점에 알려야 할 정보는 주입 대상 생성자, 각 파라미터 타입과 Qualifier, 바인딩할 구현체, 인스턴스의 스코프다. 지금처럼 `ShoppingApplication`에서 이미 만든 객체를 `register()`하는 API도 코드 생성 방식에 맞게 바꿔야 한다. KSP가 생성할 수 있도록 바인딩과 Provider를 정적으로 선언하고, 생성된 Factory가 런타임 스코프 ID에 해당하는 캐시를 조회하도록 설계할 수 있다. 화면·ViewModel이 실제로 열리고 닫히는 시점과 동적 스코프 ID는 여전히 런타임에 다룬다.

| Reflection의 비용 | KSP 코드 생성으로 얻는 점 |
| --- | --- |
| R8이 Reflection 대상 멤버를 지우거나 이름을 바꿀 수 있어 keep 규칙이 필요하고, 축소와 난독화 효과가 줄어든다. | 생성된 직접 호출 코드는 정적 참조가 되므로 Reflection용 keep 규칙을 줄이고 R8이 코드를 분석하기 쉬워진다. |
| `kotlin-reflect`가 앱에 포함되고 메타데이터와 의존성 그래프를 런타임에 읽어 시작 비용이 든다. | Factory와 그래프를 빌드 때 생성해 런타임 탐색과 메타데이터 처리 비용을 줄인다. |
| 빠진 의존성이나 잘못된 그래프가 해당 화면에 진입할 때 발견된다. | 생성 단계에서 그래프를 검사해 잘못된 바인딩을 빌드 오류로 앞당길 수 있다. |
| `kotlin-reflect`는 JVM 전용이라 공용 KMP 코드에서 사용할 수 없다. | Reflection에 기대지 않는 코드를 생성하면 공용 코드와 플랫폼별 구현을 분리하기 쉬워진다. |

대신 KSP에서는 빌드 시점에 알 수 없는 타입을 임의로 요청하는 런타임 유연성이 줄어든다. 바인딩과 주입 대상 선언, 프로세서 설정 및 생성 코드 관리도 필요하다. 이 미션은 DI가 의존성을 탐색하고 스코프에 보관하는 원리를 익히는 것이 목적이므로 Reflection 구조를 유지한다. 배포할 라이브러리라면 시작 비용, R8, 늦은 오류 발견, KMP 제약을 줄이기 위해 KSP 방식을 검토할 것이다.

## Step 3 - Qualifier

### 구현할 기능 목록

#### Qualifier

- [x] 같은 인터페이스의 여러 구현체를 구분할 Qualifier를 정의한다.
- [x] Room DB 구현체와 In-Memory 구현체를 추가한다.
- [x] Qualifier에 따라 원하는 구현체를 주입한다.
- [x] Qualifier가 없거나 잘못된 경우 명확한 예외를 발생시킨다.

#### DI 모듈 분리

- [x] DI 라이브러리를 `:di` 모듈로 분리한다.
- [x] `DependencyContainer`와 Annotation 등 공통 DI 코드를 `:di`로 이동한다.
- [x] `:di` 모듈이 `CartRepository`, `Product` 등 앱 도메인을 참조하지 않도록 한다.
- [x] 앱에서 구현체와 의존성을 등록할 수 있도록 구성한다.

### 테스트 목록

- [x] Room DB 구현체가 올바르게 주입되는지 검증한다.
- [x] In-Memory 구현체가 올바르게 주입되는지 검증한다.
- [x] Qualifier가 없을 때 예외가 발생하는지 검증한다.
- [x] 잘못된 Qualifier를 사용했을 때 예외가 발생하는지 검증한다.
- [x] `:di` 모듈이 앱 도메인에 의존하지 않는지 확인한다.
- [x] 기존 테스트가 모두 통과하는지 확인한다.

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
