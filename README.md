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
- [ ] 앱·ViewModel 스코프 연결
  - CartRepository와 Room은 앱 컨테이너에서 공유한다.
  - ProductRepository를 일반 클래스로 바꾸고 ViewModel마다 별도 스코프에서 생성한다.
  - ViewModel의 closeable에 스코프를 연결하고 생성 실패 시에도 닫는다.
- [ ] 내비게이션 목적지의 화면 스코프와 DateFormatter 주입
  - NavBackStackEntry의 ViewModelStore에 화면 스코프 소유자를 둔다.
  - DateFormatter는 화면 스코프에서 재사용하며 CartScreen의 파라미터로 전달한다.
  - 화면 회전 시 유지, 백스택 제거 시 종료, 재진입 시 새 인스턴스 생성을 검증한다.
  - 반복 진입·이탈과 각 스코프의 참조 해제를 검증한다.
- [ ] 설계 설명과 전체 검증
  - DI·서비스 로케이터, DIP·IoC의 구분을 실제 코드 지점으로 설명한다.
  - KSP 전환 시 필요한 등록·생성·검증 설계와 reflection 비용의 차이를 기록한다.
  - 전체 테스트, ktlint, 디버그 빌드 및 lint를 실행한다.

### 4단계 설계 방향

스코프의 종류는 코어의 enum이나 when으로 고정하지 않고 앱이 `ScopeType` 값으로 정의한다.
등록 정보는 공유하되 인스턴스 캐시는 각 스코프가 가진다. 의존성 생성은 그 의존성의
소유 스코프에서 수행해 앱 객체가 화면·ViewModel 객체를 붙잡는 것을 막는다.
화면 스코프의 종료는 composition의 onDispose가 아니라 목적지에 속한 ViewModel의
정리 시점에 연결한다. 구성 변경은 스코프를 유지하고 백스택 제거는 스코프를 닫는다.
