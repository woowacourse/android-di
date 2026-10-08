# android-di

## 0.5단계
- [x] 생성자 주입 - 수동

## 1단계
- [x] ViewModel에 수동으로 주입되고 있는 의존성을 자동 주입으로 바꾸기
  - ProductsViewModel, CartViewModel 동일한 자동 주입 로직 적용
- [x] 여러 번 인스턴스화할 필요 없는 객체는 최초 한번만 인스턴스화 하기 
  - ProductsViewModel, CartViewModel이 동일한 CartRepository 사용

## 2단계 기능 구현 목록

- [x] 런타임 애노테이션을 사용한 ViewModel 필드 주입
  - `@Inject`가 붙은 필드만 주입하고, 표시하지 않은 필드는 유지한다.
  - ProductsViewModel과 CartViewModel에 동일한 필드 주입을 적용한다.
  - 주입 대상 구분, 상속받은 필드, 생성자·필드의 재귀 주입을 테스트한다.
- [x] 등록한 의존성의 재귀 해결과 싱글톤 관리
  - 외부에서 생성해야 하는 객체와 인터페이스의 구현체를 생성 함수로 등록한다.
  - 등록된 객체는 컨테이너 안에서 한 번만 만들고 공유한다.
  - 순환 의존성은 의존 경로가 포함된 오류로 알리고 테스트한다.
- [x] 장바구니 도메인 모델과 엔티티 매퍼
  - `CartProduct`에 실제 상품 식별자와 담은 시각, 화면에 필요한 상품 정보를 담는다.
  - `CartProductEntity.toDomain()`을 추가하고 ID와 담은 시각 보존을 테스트한다.
- [x] Room 기반 장바구니 저장·조회·삭제와 재귀 주입 연결
  - CartRepository 인터페이스와 CartProductDao를 주입받는 DefaultCartRepository를 만든다.
  - 애플리케이션에서 Room Database와 DAO 생성 방법을 등록한다.
  - ViewModel은 Repository의 suspend 함수를 viewModelScope 안에서 호출한다.
  - UI는 CartProduct를 사용하고 리스트 인덱스 대신 Long ID로 삭제한다.
  - 실제 Room 저장·재조회, 중간 항목 삭제, ViewModel 비동기 동작 및 DI 연결을 테스트한다.
- [x] 장바구니의 담은 시각 표시와 항목 식별
  - DateFormatter로 담은 시각을 표시하고 LazyColumn의 key로 상품 ID를 사용한다.
  - 사전 제공 화면 테스트와 Preview를 새 타입에 맞추고 날짜·삭제 동작을 검증한다.
- [x] 기존 코드 스타일 오류 정리 및 전체 검증
  - 프로젝트의 ktlint 규칙을 적용한다.
  - 사전 제공 테스트와 추가 테스트, 디버그 APK 빌드 및 lint 검사를 실행한다.

### 의존성 생성 경계

일반 클래스는 주 생성자와 `@Inject` 필드를 통해 자동으로 생성한다. 인터페이스의 구현체는
등록한 생성 함수로 결정한다. Room Database는 추상 클래스이며 Android Context와 Room의
빌더가 필요하므로 애플리케이션에서 생성 방법을 등록한다. DAO도 Room Database를 통해
얻도록 등록한다. Database, DAO, CartRepository는 애플리케이션의 컨테이너 안에서 공유하고,
ViewModel은 ViewModelProvider가 관리한다. DateFormatter의 화면 스코프 주입은 기존 다음
단계 과제로 남겨 두고 현재 화면에서 생성한 인스턴스를 파라미터로 전달한다.


### 검증 결과

JDK 21 환경에서 다음 명령으로 검증했다.

```shell
./gradlew :app:testDebugUnitTest ktlintCheck :app:assembleDebug :app:lintDebug
```

- 사전 제공 테스트를 포함한 전체 테스트 39개 통과
- ktlint 검사 및 디버그 APK 빌드 성공
- Android lint 오류 0개, 기존 빌드 도구·의존성의 새 버전 알림 12개
- Room 재연결 후 데이터 유지, 중복 상품의 중간 항목 및 연속 삭제 검증
- 애노테이션 필드 주입, 재귀 생성, 싱글톤 공유, 순환 의존성 오류 검증

## 3단계 기능 구현 목록

- [x] DI 코어를 순수 JVM `:di` 모듈로 분리
  - Injector와 Inject를 `woowacourse.di` 패키지로 옮긴다.
  - 코어 테스트는 `:di`에서, ViewModel·Room 연동 테스트는 `:app`에서 실행한다.
  - `:app -> :di` 방향으로만 의존하고 코어에 Android 및 쇼핑 도메인 의존성을 두지 않는다.
- [x] 애노테이션 타입을 사용하는 Qualifier 지원
  - `@Qualifier`로 표시한 애노테이션 타입과 의존성 타입을 함께 등록 키로 사용한다.
  - 생성자 파라미터와 `@Inject` 필드의 Qualifier를 해석한다.
  - 같은 타입이 둘 이상 등록되어 있는데 Qualifier가 없으면 후보를 포함한 오류를 낸다.
  - Qualifier별 싱글톤, 미등록 Qualifier, 중복 등록, 복수 Qualifier 및 순환 의존성을 테스트한다.
- [x] 의존성 등록 및 조회 DSL 제공
  - `injector { singleton<T> { ... } }`와 `get<T>()`를 제공한다.
  - DSL에서도 Qualifier 선택과 모호한 의존성 오류가 동일하게 동작하는지 테스트한다.
- [x] Room·In-Memory 장바구니 구현체 선택 및 앱 적용
  - ID와 담은 시각을 관리하는 InMemoryCartRepository를 추가하고 CRUD를 테스트한다.
  - `@RoomCart`와 `@InMemoryCart`를 정의하고 두 구현체를 앱에서 등록한다.
  - 두 ViewModel에 기본 저장소로 `@RoomCart`를 명시하고 메모리 구현체 선택 방법을 문서화한다.
  - 실제 앱 컨테이너의 두 구현체 선택, 데이터 분리, 모호한 요청의 실패를 검증한다.
- [x] 모듈 독립성과 전체 동작 검증
  - DI 모듈의 테스트와 의존성 목록으로 Android·앱 의존성이 없는지 확인한다.
  - 앱 전체 테스트, ktlint, 디버그 APK 빌드와 Android lint 검사를 실행한다.

### 3단계 설계 선택

**Qualifier는 애노테이션 타입으로 표현한다.** 코어의 `@Qualifier`를 붙인 `@RoomCart`,
`@InMemoryCart`를 앱에서 정의한다. 등록 키는 `(의존성 타입, Qualifier 애노테이션 타입)`이다.
문자열 대신 타입을 참조하므로 사용처에서 이름을 잘못 쓰면 컴파일 단계에서 확인할 수 있다.
리플렉션으로 읽기 위해 사용자 Qualifier는 `RUNTIME` 보존 정책과 `FIELD`, `VALUE_PARAMETER`
대상을 사용한다. 값을 가진 애노테이션으로 인스턴스를 구별하는 기능은 이번 단계의 범위가 아니다.

Qualifier를 지정한 요청은 해당 키만 조회한다. 한 타입에 여러 등록이 있을 때 Qualifier 없는
요청은 기본값을 임의로 선택하지 않는다. 하나만 등록되어 있어도 Qualifier를 붙여 등록한
의존성은 주입받는 쪽에서도 같은 Qualifier를 명시한다.

**DI 코어는 순수 JVM 모듈로 만든다.** 코어의 책임은 타입·생성 함수 등록, 리플렉션과 객체
수명 관리이므로 Android API가 필요하지 않다. `:di`의 빌드에는 Kotlin/JVM과 리플렉션,
테스트 라이브러리만 포함하며 `:app`, Room, AndroidX 의존성을 넣지 않는다. 앱에서 생성 방법을
등록하므로 코어는 CartRepository나 Context라는 구체적인 이름을 몰라도 객체를 해결할 수 있다.

등록은 ShoppingApplication이 소유한 컨테이너를 처음 사용할 때 한 번 수행하고 각 싱글톤은
처음 요청할 때 생성한다. ViewModelFactory와 Room 생성은 앱의 연동 계층에 남긴다.
4단계에서 화면 스코프를 여닫는 Android·Compose 생명주기 신호도 앱의 연동 계층에서 받아
코어에 전달한다. 이번 단계에서는 외부 배포 없이 `implementation(project(":di"))`로 적용한다.


### 저장소 선택 방법

앱의 `ShoppingModule.kt`에서 다음과 같이 두 구현체를 같은 인터페이스로 등록한다.
생성 함수를 등록한 시점에는 Database나 Repository를 생성하지 않는다.

```kotlin
singleton<CartRepository>(RoomCart::class) { get<DefaultCartRepository>() }
singleton<CartRepository>(InMemoryCart::class) { get<InMemoryCartRepository>() }
```

기본값은 두 ViewModel의 `cartRepository` 필드에 명시한 `@RoomCart`다. 메모리 저장소로
실행하려면 ProductsViewModel과 CartViewModel의 해당 필드를 **모두** 다음과 같이 바꾼다.
두 화면이 서로 다른 저장소를 선택하면 데이터도 공유되지 않는다.

```kotlin
@Inject
@InMemoryCart
lateinit var cartRepository: CartRepository
```

생성자 주입에서는 `@InMemoryCart repository: CartRepository`로 지정한다.
직접 조회할 때는 `injector.get<CartRepository>(InMemoryCart::class)`를 사용한다.
`injector.get<CartRepository>()`는 두 구현체 중 임의의 하나를 선택하지 않고 후보를 포함한
오류를 낸다. InMemoryCartRepository는 컨테이너 안에서 공유되지만 새 컨테이너를 만들거나
프로세스를 다시 시작하면 내용이 사라진다. Room 구현체는 기존처럼 영구 저장한다.


### 3단계 검증 결과

JDK 21 환경에서 검증했다.

```shell
./gradlew :di:test :app:testDebugUnitTest ktlintCheck :app:assembleDebug :app:lintDebug
./gradlew :di:dependencies --configuration runtimeClasspath
```

- `:di` 테스트 35개, `:app` 테스트 33개: 총 68개 통과, 실패·누락 없음
- ktlint 통과, 디버그 APK 빌드 성공
- Android lint 오류 0개, 빌드 도구·의존성 새 버전 알림 13개
- `:di` 런타임 의존성은 Kotlin 표준 라이브러리·리플렉션과 JetBrains annotations뿐이며,
  Android·AndroidX·`:app` 의존성 없음
- 코어 소스에도 Android·쇼핑 앱 import 없음
- 생성자·필드 Qualifier 선택, 누락 시 명확한 오류, Qualifier별 싱글톤·순환 의존성 검증
- 실제 앱에서 Room·메모리 저장소 선택 및 데이터 분리, 메모리 저장소의 ID 삭제·동시 저장 검증

## 4단계 기능 구현 목록

- [x] 수명별 저장 공간을 갖는 범용 스코프 코어
  - 등록 정보와 스코프별 인스턴스 저장소를 분리하고 임의의 스코프 타입·키를 지원한다.
  - 같은 스코프의 재사용, 서로 다른 스코프의 분리, 부모 의존성 탐색을 구현한다.
  - 종료 시 자식 스코프·캐시·부모 참조를 제거하고 정리 콜백을 한 번 실행한다.
  - 종료 후 접근, 수명이 짧은 의존성의 잘못된 참조, Qualifier·순환 의존성 회귀를 테스트한다.
- [x] 앱·ViewModel 스코프 연결
  - CartRepository와 Room은 앱 컨테이너에서 공유한다.
  - ProductRepository를 일반 클래스로 바꾸고 ViewModel마다 별도 스코프에서 생성한다.
  - ViewModel의 closeable에 스코프를 연결하고 생성 실패 시에도 닫는다.
- [x] 내비게이션 목적지의 화면 스코프와 DateFormatter 주입
  - NavBackStackEntry의 ViewModelStore에 화면 스코프 소유자를 둔다.
  - DateFormatter는 화면 스코프에서 재사용하며 CartScreen의 파라미터로 전달한다.
  - 화면 회전 시 유지, 백스택 제거 시 종료, 재진입 시 새 인스턴스 생성을 검증한다.
  - 반복 진입·이탈과 각 스코프의 참조 해제를 검증한다.
- [x] 설계 설명과 전체 검증
  - DI·서비스 로케이터, DIP·IoC의 구분을 실제 코드 지점으로 설명한다.
  - KSP 전환 시 필요한 등록·생성·검증 설계와 reflection 비용의 차이를 기록한다.
  - 전체 테스트, ktlint, 디버그 빌드 및 lint를 실행한다.

### 4단계 설계 방향

스코프의 종류는 코어의 enum이나 when으로 고정하지 않고 앱이 `ScopeType` 값으로 정의한다.
등록 정보는 공유하되 인스턴스 캐시는 각 스코프가 가진다. 의존성 생성은 그 의존성의
소유 스코프에서 수행해 앱 객체가 화면·ViewModel 객체를 붙잡는 것을 막는다.
화면 스코프의 종료는 composition의 onDispose가 아니라 목적지에 속한 ViewModel의
정리 시점에 연결한다. 구성 변경은 스코프를 유지하고 백스택 제거는 스코프를 닫는다.

### 스코프의 소유권과 종료

| 의존성 | 소유자 | 생성·재사용 | 종료 |
| --- | --- | --- | --- |
| CartRepository, Room, DAO, application Context | ShoppingApplication의 루트 Injector | 처음 요청할 때 생성하고 앱 전체에서 공유 | 프로세스 종료. 명시적인 `root.close()`는 전체 캐시를 비우고 Room을 닫음 |
| ProductRepository | 개별 ViewModel의 Injector | 같은 ViewModel에서 재사용, 다른 ViewModel과 분리 | ViewModelStore가 ViewModel을 정리할 때 |
| DateFormatter | NavBackStackEntry의 ScreenScopeViewModel | 같은 목적지에서 재사용, 구성 변경에도 유지 | 해당 목적지가 백스택에서 제거되어 ViewModelStore가 정리될 때 |

`ProductRepository`의 `object`를 `class`로 바꿨다. Kotlin 싱글톤 자체가 프로세스에 남으면
컨테이너 캐시를 비워도 ViewModel 단위의 생성·소멸을 구현할 수 없기 때문이다.
앱 컨테이너는 `ShoppingApplication.injector`가 소유한다. Android의 실제 프로세스 종료는
`Application.onTerminate()` 호출을 보장하지 않으므로 그 콜백에 의존하지 않는다.
테스트 등 명시적 컨테이너 종료에는 `close()`를 사용한다.

코어의 `Registry.definitions`는 생성 방법을, 각 `Injector.instances`는 그 스코프가 소유한
객체를 저장한다. `create()`는 등록된 수명의 소유자를 찾고 **소유자의 컨테이너에서**
재귀 생성한다. 화면에서 앱 싱글톤을 요청해도 앱 객체의 의존성은 루트에서 해결하므로,
앱 객체가 화면 객체를 붙잡으려 하면 활성 스코프가 없다는 오류가 발생한다.
등록하지 않은 일반 클래스는 요청마다 생성하며 캐시하지 않는다. ViewModel도 여기 해당한다.

`Injector.close()`는 닫힘을 먼저 표시한 뒤 부모의 자식 목록에서 자신을 제거하고,
자식 목록·인스턴스 캐시·부모 참조를 비운다. 자식을 닫은 뒤 자신이 소유한 객체의
`onClose` 콜백을 생성 역순으로 실행한다. 한 콜백이 실패해도 나머지 정리를 진행한다.
종료는 멱등적이며 닫힌 스코프의 조회와 새 자식 생성은 오류다. 등록 함수는 앱 수명의
정보만 캡처해야 한다. `shoppingInjector()`도 등록 전에 application Context를 추출해서
Activity를 생성 함수에 보관하지 않는다.

`ViewModelFactory.createViewModel()`은 ViewModel마다 새 스코프를 열고 `addCloseable()`로
연결한다. 생성 도중 예외가 나면 그 스코프를 즉시 닫는다.
`rememberScreenScope()`는 목적지의 ViewModelStore에 `ScreenScopeViewModel`을 보관하고,
이 소유자의 closeable로 화면 스코프를 연결한다. 화면이 composition에서 잠시 사라지는
시점이나 Activity의 `ON_DESTROY`만으로 닫지 않는다. 구성 변경과 최종 종료의 구분은
AndroidX ViewModelStore의 정리 시점에 맡긴다.
[ViewModel의 유지·closeable 계약](https://developer.android.com/reference/androidx/lifecycle/ViewModel.html)을 사용한 방식이다.
프로세스가 종료된 뒤에는 같은 인스턴스를 복원하지 않는다.

현재 ViewModel 스코프와 화면 스코프는 앱 루트의 형제다. ViewModel에는 화면 전용 포매터가
필요하지 않기 때문이다. 코어는 부모·자식 스코프도 지원하지만, 현재 앱에 불필요한 연결은
만들지 않았다. 필요하면 화면의 자식으로 다른 스코프를 열어 부모 의존성을 재사용할 수 있다.

스코프 종류는 `ShoppingScopes`처럼 앱에서 정의한다. 코어의 분기를 늘리지 않고 추가할 수 있다.

```kotlin
val checkout = ScopeType("checkout-session")
val container = injector {
    scoped<CheckoutSession>(checkout, onClose = { it.close() }) { CheckoutSession() }
}
val session = container.openScope("order-42", checkout)
val dependency = session.get<CheckoutSession>()
session.close()
container.close()
```

### 내가 만든 것은 DI인가 서비스 로케이터인가

**컨테이너는 조회 API를 제공하고, 앱의 소비자에게 연결하는 방식은 DI다.** 도구의 이름보다
누가 의존성을 찾아오는지로 구분했다.

- `Injector.construct()`는 생성자 인자를 해결해서 전달하고, `injectFields()`는 `@Inject`
  필드를 채운다. `DefaultCartRepository(dao)`는 DAO를 외부에서 받는 생성자 DI다.
- `ProductsViewModel`과 `CartViewModel`은 컨테이너를 조회하지 않고 필드 주입을 받는다.
  다만 생성자 시그니처에는 의존성이 드러나지 않고, 주입 전 `lateinit` 접근이 가능하다.
  따라서 DI이지만 생성자 주입보다 의존성 계약과 초기화 안전성이 약하다.
- `CartScreen(dateFormatter, ...)`와 `CartContent(dateFormatter, ...)`는 포매터를 인자로
  받으므로 의존성이 함수 시그니처에 드러난다. Preview와 화면 테스트도 직접 전달할 수 있다.
- `ShoppingNavHost`의 `scope.get()`과 `ViewModelFactory`의 `createViewModel()`은 조회하는
  지점이다. 여기서는 객체를 조립하는 연동 계층의 책임으로 제한했다. 같은 `get()`을
  ViewModel의 업무 함수나 UI 하위 컴포넌트 내부에서 직접 호출하면 그 소비자는 서비스
  로케이터에 의존하게 된다. 이 API를 쓴다는 이유만으로 모든 사용 방식이 DI가 되지는 않는다.

**DIP**는 소스 코드의 의존 방향에 관한 원칙이다. ViewModel이 `CartRepository`라는 추상화에
의존하고 앱의 조립 코드가 Room·In-Memory 구현체를 선택하는 것이 해당 사례다. DI는 객체를
전달하는 방법이라 구체 클래스인 ProductRepository를 주입한다고 해서 DIP까지 자동으로
만족하는 것은 아니다. 또한 `:app -> :di` 모듈 방향만으로 앱 전체의 DIP 준수가 증명되지 않는다.

**IoC**는 생성·호출·수명 관리의 제어를 외부에 맡기는 더 넓은 개념이다. 소비자가 직접
객체를 생성하지 않고 Injector가 생성하는 것, ViewModelProvider가 ViewModel을 보관하고
정리하는 것이 각각 사례다. DI는 이러한 제어 역전을 구현하는 한 방법이며 DIP와 동의어가 아니다.

### KSP로 바꾼다면 다시 설계할 부분

현재는 `primaryConstructor`, 파라미터와 필드 애노테이션, `constructor.call()`과 `field.set()`으로
실행 중에 그래프를 해석한다. `resolveKey()`의 Qualifier 누락·모호성, `ownerOf()`의 수명,
`resolve()`의 순환 의존성 판정도 실제 요청까지 미룬다.

[KSP](https://kotlinlang.org/docs/ksp-overview.html)는 소스의 심볼을 읽고 코드를 생성한다.
일반적인 런타임 등록 람다를 실행해서 모든 의존성을 알아내는 도구로 취급할 수는 없다.
컴파일 시점 검증으로 바꾸려면 다음 계약을 먼저 정해야 한다.

1. 주입 생성자·필드, 인터페이스 바인딩, Qualifier, 제공 함수의 인자, 스코프 및 허용된
   부모 관계를 정적으로 선언한다. 현재 자유로운 `singleton { ... }` 람다 안의 `get()`은
   명시적인 provider 인자로 바꾸거나 런타임 전용 경계로 남긴다. Room·Context는 생성 방법을
   provider로 선언하되 실제 Context 값과 화면 ID는 실행 중 전달한다.
2. `ProductsViewModel_Factory` 같은 생성 코드와 멤버 주입 코드를 만든다. 생성자 호출과
   프로퍼티 대입을 직접 생성하고 누락 바인딩, 중복 Qualifier, 순환 관계, 부적절한 수명을
   빌드 중 오류로 보고한다. 단, 이 검증은 정적으로 선언된 그래프에 한정된다.
3. 현재 reflection으로 허용하는 private 필드 주입을 그대로 생성 코드에서 할 수는 없다.
   생성자 주입을 우선하거나 생성 코드가 접근 가능한 멤버만 주입하도록 계약을 바꾼다.
   제네릭·nullable·기본 인자 지원 범위도 생성 규칙과 오류 메시지로 명시한다.
4. 순수 JVM 런타임 코어와 애노테이션, KSP processor를 분리한다. 앱의 타입을 참조하는
   생성 코드는 앱에서 컴파일하고 `:di -> :app` 의존성은 만들지 않는다. 프로세서의 증분
   처리·오류 진단·생성 결과 테스트를 추가한다. 스코프 캐시와 `close()` 및 Android 생명주기
   연동은 여전히 런타임 책임으로 유지한다.

현재 코드의 비용을 네 관점에서 비교하면 다음과 같다. 성능 차이는 측정하지 않았으므로
구체적인 실행 시간이나 APK 절감 수치는 주장하지 않는다.

| 비용 | 현재 reflection 구현 | 코드 생성으로 전환할 때 |
| --- | --- | --- |
| 실행 시간 | 최초 생성 및 캐시 없는 객체 생성마다 메타데이터 탐색·동적 호출을 수행 | 직접 호출로 해당 탐색을 줄임. 스코프 조회·수명 관리는 여전히 필요 |
| 배포 크기·유지 정보 | kotlin-reflect와 주입에 필요한 런타임 메타데이터에 의존 | DI의 reflection 의존성을 제거할 여지가 있으나 생성 코드 크기가 추가됨 |
| 오류 발견·추적 | 잘못된 바인딩·접근·순환을 해당 경로 실행 시 발견 | 선언된 그래프의 오류를 컴파일 진단으로 이동. 동적 입력과 실행 중 실패는 남음 |
| 캡슐화·도구와의 결합 | 접근 제한을 우회하고 reflection 대상의 최적화·이름 보존을 고려해야 함 | 접근 가능한 코드 계약이 필요하고 대신 프로세서·빌드 및 생성 코드 관리 비용이 생김 |

이번 단계는 외부에서 임의의 스코프와 생성 함수를 등록하는 런타임 컨테이너를 검증하는
범위라 reflection 구조를 유지한다. KSP로 옮기려면 reflection 호출만 교체할 것이 아니라
등록 언어와 검증 가능한 그래프의 경계부터 바꾸어야 한다.

### 수명 검증 방법

- `di/ScopeTest`: 캐시·자식 참조 제거, 종료 콜백, 부모 수명, 100회 생성·종료,
  사용자 정의 스코프, 종료 실패 중 나머지 정리, 닫힌 스코프의 사용 거부를 확인한다.
- `app/ViewModelScopeTest`: 같은 ViewModel의 상품 저장소 재사용, 서로 다른 ViewModel의
  분리, 앱 장바구니 공유, 20회 ViewModelStore 정리와 생성 실패 시 의존성 해제를 확인한다.
- `app/ScreenScopeLifecycleTest`: 실제 ShoppingNavHost에서 장바구니 진입·이탈 5회,
  재진입 시 새 포매터, Activity 재생성 시 동일 포매터·ViewModel 유지, 최종 Activity 종료 시
  화면·ViewModel 스코프 닫힘과 앱 스코프 유지를 확인한다.

여기서 소멸은 **컨테이너가 인스턴스를 더 이상 보관하지 않으며 정리 콜백을 실행했다**는
뜻이다. 테스트가 비교를 위해 보관한 지역 변수까지 무효화하거나 GC가 특정 시점에 실행된다고
가정하지 않는다. 캐시 개수 검사는 코어 테스트에만 보이는 internal 속성으로 제한했고,
앱 연동은 공개된 종료 상태와 실제 ViewModel 정리로 검증한다.


### 4단계 전체 검증 결과

JDK 21 환경에서 다음을 실행했다.

```shell
./gradlew :di:test :app:testDebugUnitTest ktlintCheck :app:assembleDebug :app:lintDebug
```

- 코어 45개, 앱 38개: 총 83개 테스트 통과, 실패·오류·누락 없음
- ktlint 검사 및 디버그 APK 빌드 성공
- Android lint 오류 0개, 기존 빌드 도구·의존성 버전 안내 경고 13개
- 기존 Qualifier·필드 주입·재귀 주입·순환 의존성·장바구니 CRUD 테스트 통과
- Robolectric에서 화면 반복 진입·이탈과 Activity 재생성·최종 종료 검증
