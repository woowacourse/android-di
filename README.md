# 🚀 0.5단계 - 수동 DI

`ProductsViewModel`을 생성할 때 필요한 객체를 직접 만들어 주입한다.

## 기능 목록

- [x] `ProductsScreen`의 `viewModel()`에 `ViewModelProvider.Factory`를 전달한다.
- [x] Factory에서 `ProductRepository`와 `CartRepository`를 직접 생성한다.
- [x] 생성한 Repository를 `ProductsViewModel`의 생성자에 전달한다.

# 🚀 1단계 - 자동 DI

ViewModel이 필요로 하는 객체를 자동으로 생성하고, 여러 화면에서 공유할 객체를 재사용한다.

## 기능 목록

- [x] 요청받은 ViewModel 타입의 생성자와 매개변수 타입을 확인해 인스턴스를 생성한다.
- [x] 생성자에 필요한 의존성을 재귀적으로 찾아 주입한다.
- [x] 이미 생성한 의존성을 다시 요청하면 같은 인스턴스를 반환한다.
- [x] `ProductsViewModel`과 `CartViewModel`이 같은 자동 주입 로직을 사용한다.
- [x] ViewModel을 추가해도 해당 ViewModel만을 위한 주입 로직을 새로 작성하지 않는다.
- [x] 순환 의존성을 발견하면 의존성 경로를 포함한 오류를 낸다.

## 프로그래밍 요구 사항

- [x] 사전에 제공된 테스트가 모두 통과한다.
- [x] 의존성 주입에 어노테이션을 사용하지 않는다.

# 🚀 2단계 - Annotation

애노테이션으로 주입 대상을 구분하고, 장바구니 데이터까지 이어지는 의존성을 재귀적으로 해결한다.

## 기능 목록

- [x] ViewModel을 생성한 뒤 애노테이션이 붙은 필드에만 의존성을 주입한다.
- [x] 필드 주입과 재귀적인 의존성 주입을 테스트한다.
- [x] `CartRepository`가 `CartProductDao`를 주입받아 장바구니 데이터를 저장하고 조회하고 삭제한다.
- [x] `CartProduct` 도메인 모델과 엔티티를 도메인 모델로 변환하는 `toDomain()` 매퍼를 추가한다.
- [x] ViewModel에서 Repository의 `suspend` 함수를 `viewModelScope` 안에서 호출한다.
- [x] 장바구니 항목을 인덱스 대신 식별자로 삭제한다.
- [x] 장바구니 화면에 상품명과 `DateFormatter`로 변환한 담은 시각을 표시한다.

## 선택 요구 사항

- [x] 장바구니 목록의 `LazyColumn` 항목에 `key`를 지정하고 차이를 확인한다.
- [x] UI 계층에서 `CartProductEntity`를 직접 참조하지 않는다.

## 프로그래밍 요구 사항

- [x] 사전에 제공된 테스트가 모두 통과한다.
- [x] 애노테이션이 붙지 않은 필드에는 의존성을 주입하지 않는다.

# 🚀 3단계 - Qualifier

같은 인터페이스의 여러 구현체 중 주입할 대상을 선택하고, DI 라이브러리를 별도 모듈로 분리한다.

## 기능 목록

- [x] `CartRepository`의 Room DB 구현체와 In-Memory 구현체를 DI 컨테이너에 등록한다.
- [x] Qualifier를 지정해 필요한 `CartRepository` 구현체를 선택하여 주입한다.
- [x] 같은 타입의 구현체가 여러 개일 때 Qualifier가 없으면 명확한 예외를 발생시킨다.
- [x] Qualifier에 따른 구현체 선택과 Qualifier가 없는 경우의 예외를 테스트한다.
- [x] DI 라이브러리를 `:di` 모듈로 분리하고 쇼핑 앱에서 사용한다.

## 프로그래밍 요구 사항

- [x] `:di` 모듈이 `CartRepository`, `Product` 등 쇼핑 앱의 도메인 타입에 의존하지 않는다.
- [x] Qualifier 표현 방식과 `:di` 모듈 형태의 선택 근거를 README에 기록한다.

## 설계 선택

- Qualifier는 애노테이션 타입으로 표현한다. `@RoomCart`, `@InMemoryCart`를 등록 키와 주입 지점에 함께 사용하므로 문자열 키의 오타를 컴파일 단계에서 발견할 수 있다. 두 구현체를 등록한 상태에서 Qualifier를 생략하면 컨테이너가 예외를 낸다.
- `:di`는 순수 JVM 모듈이다. 컨테이너는 리플렉션과 등록된 타입의 관계만 다루고, Android `Context`, ViewModel, 쇼핑 앱의 도메인 타입은 알지 않는다. Android의 `ViewModelProvider.Factory` 연동과 Room DAO 등록은 `:app`에 둔다. 이후 화면 스코프의 생명주기 신호도 앱 연동 계층에서 전달한다.
- 현재 상품 목록과 장바구니 화면에는 `@RoomCart`를 지정해 데이터를 공유한다. In-Memory 구현체가 필요한 주입 지점에는 `@InMemoryCart`를 지정한다.

# 🚀 4단계 - Lifecycle

의존성을 생명주기에 맞는 스코프에서 생성하고 재사용하며, 스코프가 끝나면 컨테이너가 인스턴스 참조를 해제한다.

## 기능 목록

- [x] 컨테이너에서 서로 다른 생명주기의 스코프를 열고 닫을 수 있다.
- [x] 스코프 안에서 같은 의존성을 다시 요청하면 같은 인스턴스를 반환하고, 닫힌 스코프는 인스턴스를 더 이상 참조하지 않는다.
- [x] `CartRepository`와 그 데이터 저장에 필요한 의존성을 앱 스코프에서 재사용한다.
- [x] `ProductRepository`를 `ProductsViewModel`의 생명주기 동안 재사용하고, ViewModel이 소멸하면 해당 스코프를 정리한다.
- [x] `DateFormatter`를 장바구니 내비게이션 목적지의 화면 스코프에서 주입한다. 같은 화면 안에서는 재사용하고, 백스택에서 나갔다 다시 들어오면 새로 만든다.
- [x] 기존 생성자 주입, `@Inject` 필드 주입, Qualifier 선택, 순환 의존성 오류가 스코프에서도 동작하도록 유지한다.
- [x] 스코프별 생성·재사용·소멸과 반복 진입 시 참조 해제를 테스트한다.
- [x] 컨테이너가 DI인지 서비스 로케이터인지, DIP·IoC와 어떻게 다른지 코드 근거로 설명한다.
- [x] 컴파일 타임 방식(KSP)으로 바꿀 때 재설계할 부분과 현재 reflection 방식을 유지할지에 대한 판단을 기록한다.

## 선택 요구 사항

- [x] 화면 회전 등의 구성 변경에도 같은 `DateFormatter` 인스턴스를 유지한다.
- [x] 코어 코드를 수정하지 않고 새 스코프 종류를 앱 계층에서 등록·사용하는 예제를 테스트한다.

## 프로그래밍 요구 사항

- [x] 스코프가 소멸하면 해당 스코프의 인스턴스 참조를 해제해 메모리 누수를 막는다.
- [x] 스코프 종류가 늘어나도 컨테이너 내부에 종류별 `when` 분기를 추가하지 않는다.

## 설계 설명

### DI와 서비스 로케이터

이 앱에서는 **DI 컨테이너를 사용한다**. `HunnitFactory`가 `ViewModelProvider.Factory`로 ViewModel을 만들며, `ProductsViewModel`의 생성자와 `CartViewModel`의 `@Inject` 필드에 의존성을 전달한다. `CartScreen`은 `DateFormatter`를 매개변수로 받으므로 화면의 의존성이 함수 시그니처에 드러난다. `ShoppingNavHost`가 화면 스코프에서 formatter를 조회해 전달하는 부분은 조립 지점의 조회이며, 각 화면이 전역 컨테이너를 직접 호출해 의존성을 숨기는 방식과 구분한다. `Container.resolve()`를 기능 코드에서 직접 호출하기 시작하면 서비스 로케이터에 가까워진다.

### DIP와 IoC

DIP는 고수준 정책과 저수준 구현이 구체 클래스가 아니라 추상화에 의존하도록 하는 설계 원칙이다. `ProductsViewModel`과 `CartViewModel`은 구체 저장소 대신 `CartRepository` 인터페이스를 사용하고, `DefaultCartRepository`는 `CartProductDao` 인터페이스를 받는다. IoC는 객체 생성과 흐름을 누가 제어하는지에 관한 개념이다. 이 코드에서는 `HunnitFactory`와 `Container`가 ViewModel 및 의존성을 생성하고, Navigation의 엔트리 소유 ViewModel이 화면 스코프를 닫는다. DI는 객체를 외부에서 제공하는 방법이며, DIP나 IoC와 같은 뜻은 아니다.

### KSP 전환과 reflection 비용

현재 `Container.create()`는 `primaryConstructor`, 생성자 매개변수의 타입과 Qualifier, `declaredFields`의 `@Inject`를 런타임에 읽어 객체를 만든다. KSP로 바꾸면 이 정보를 빌드 중 수집해 직접 생성·주입하는 코드를 만들 수 있다. 이를 위해 생성자, 필드 주입 대상, Qualifier, 인터페이스별 구현체, 스코프 종류와 앱의 바인딩 선언을 컴파일 시점에 알 수 있는 선언 구조로 옮겨야 한다. `Context`와 Room DAO처럼 실제 객체는 런타임에만 존재하므로, 생성된 팩토리에 런타임 인스턴스를 전달하는 연결 지점은 남는다.

- **실행 비용**: 현재는 객체 생성 때마다 Kotlin/Java reflection으로 생성자와 필드를 찾아 접근한다. 생성 코드에서는 이 탐색을 일반 함수 호출로 바꿀 수 있다.
- **오류 발견 시점**: 현재는 누락된 구현체, 중복 구현체의 Qualifier 누락, 생성자 문제와 순환 의존성이 해당 객체를 요청할 때 드러난다. 그래프를 KSP가 모두 볼 수 있게 선언하면 일부 오류를 빌드에서 발견할 수 있다.
- **런타임·축소 호환성**: `field.isAccessible = true`와 런타임 애노테이션 탐색은 reflection에 의존한다. 앱은 현재 minify를 끄고 있으며, 이를 켤 때는 reflection 대상과 런타임 애노테이션이 유지되는지 확인해야 한다. 생성 코드는 이 접근 제약을 줄인다.
- **유연성과 빌드 복잡성**: 현재 컨테이너는 `KClass`와 Qualifier를 런타임에 등록할 수 있다. KSP는 선언되지 않은 바인딩을 자동으로 알 수 없으므로 정적 모듈 선언과 생성 코드가 필요하고, 빌드 설정과 생성 단계가 추가된다. 이 앱의 작은 고정 그래프에서는 빌드 복잡성과 런타임 등록 유연성의 교환을 고려해 현재 reflection 방식을 유지한다.
