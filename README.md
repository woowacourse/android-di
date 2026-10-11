# android-di

## 구현할 기능 목록

### 0.5단계

- [x] `ProductsScreen`에 `ProductsViewModel` 생성자 수동 주입

### 1단계

- [x] 생성자 정보를 활용하는 범용 `ViewModelProvider.Factory` 구현
- [x] `ProductsViewModel`, `CartViewModel`에 동일한 자동 주입 로직 적용
- [x] `CartRepository`를 애플리케이션 생명주기에서 한 번만 생성
- [x] 사전 제공 테스트 및 앱 동작 검증


### 2단계

#### 기능 요구 사항

- [x] ViewModel 내 필드 주입 구현
- [x] Annotation으로 의존성 주입이 필요한 필드 명시
- [x] Annotation이 붙은 필드에만 의존성 주입
- [x] `CartRepository`가 `CartProductDao`를 참조하도록 변경하고 재귀적으로 의존성 주입
- [x] `CartProduct` 도메인 모델과 `toDomain()` 매퍼 추가
- [x] 장바구니 목록을 `CartProduct`로 관리하고 실제 상품 `Long id`로 삭제
- [x] 장바구니 Repository의 `suspend` 함수 호출을 ViewModel의 `viewModelScope`에서 처리
- [x] 장바구니 목록에 상품명과 담은 시각 표시

#### 선택 요구 사항

- [x] `LazyColumn`의 `items`에 `key` 지정
- [x] UI 계층에서 `CartProductEntity`를 직접 참조하지 않도록 변경

#### 검증

- [x] 필드 주입과 재귀적 DI를 검증하는 테스트 작성
- [x] 사전에 제공된 테스트 코드 모두 통과

### 3단계

#### Qualifier

- [x] 하나의 인터페이스에 여러 구현체가 등록된 경우 `Qualifier` 어노테이션으로 의존성을 구분

#### 모듈 분리

- [x] 직접 구현한 DI 라이브러리를 별도의 Gradle 모듈로 분리

#### 설계 선택

- `Qualifier`는 문자열이 아니라 런타임 애노테이션 타입으로 표현했습니다. 등록과 주입 지점에서 같은 애노테이션 타입을 사용해 문자열 오타를 줄이고, 코드에서 선택 의도를 드러낼 수 있습니다.
- `:di`는 Android Library 모듈로 만들었습니다. `ViewModelProvider.Factory`와 AndroidX ViewModel API를 사용하기 때문이며, 앱의 Repository나 화면 타입은 알지 못하도록 분리했습니다.
- 이 단계에서는 `DependencyContainer`의 앱 스코프에 타입·Qualifier별 인스턴스를 등록해 앱 프로세스 동안 공유했습니다. 이 구조만으로 화면별 스코프가 생기는 것은 아니므로, 4단계에서 `DependencyScope`의 부모·자식 관계와 ViewModel 생명주기를 연결합니다.

### 4단계: Lifecycle

#### 기능 요구 사항

- [x] `CartRepository`를 앱 전체 생명주기 동안 유지
- [x] `ProductRepository`를 ViewModel 생명주기 동안 유지
- [x] `DateFormatter`를 화면 생명주기 동안 유지. 화면은 내비게이션 목적지 하나를 의미하며, 장바구니 화면이 백 스택에서 제거되면 소멸
- [x] DI 라이브러리가 스코프별 인스턴스 생성과 소멸을 수행하는지 테스트

#### 선택 요구 사항

- [x] `DateFormatter`가 화면 회전 등의 구성 변경에도 유지
- [x] 컨테이너가 스코프 종류를 하드코딩하지 않고, 코어 코드 수정 없이 새로운 스코프 종류를 등록할 수 있도록 일반화

#### 스코프 동작과 한계

`DependencyScope` 인스턴스 자체가 스코프를 나타냅니다. `createScope(parent)`로 부모를 지정해 임의의 자식 스코프를 만들 수 있고, 컨테이너는 스코프의 종류를 `when`으로 분기하지 않습니다. 조회는 현재 스코프부터 부모 방향으로 진행하며, 찾지 못한 의존성은 요청한 스코프에 생성해 저장합니다.

따라서 타입만 보고 수명을 자동으로 결정하는 구조는 아닙니다. 앱에서 명시적으로 등록한 `CartRepository`는 앱 스코프에서 공유하고, 자동 생성되는 `ProductRepository`는 요청한 `ProductsViewModel` 스코프에 저장합니다. `DateFormatter`는 Cart 목적지의 `CartScreenScopeViewModel`에서 요청하므로 해당 목적지의 `NavBackStackEntry`에 연결된 ViewModel 스코프에 저장됩니다. 화면의 다른 코드도 같은 `CartScreenScopeViewModel`을 사용해 이 인스턴스를 공유할 수 있습니다. 각 화면 ViewModel에 `DateFormatter`를 직접 주입하면 별도 인스턴스가 생길 수 있으므로, 목적지 단위 의존성은 전용 스코프 소유자를 통해 요청합니다.

#### 프로그래밍 요구 사항

- [x] 스코프 소멸 시 해당 스코프가 보유한 인스턴스 참조를 버려 메모리 누수가 발생하지 않도록 한다.
- [x] 새로운 스코프 종류가 추가되어도 컨테이너 내부의 `when` 분기가 늘어나지 않도록 설계한다.

#### 리뷰 체크리스트

- [x] 장바구니 화면에서 나갔다가 다시 진입하면 새로운 `DateFormatter`가 생성되고, 화면 안에서는 같은 인스턴스를 재사용
- [x] `CartRepository`는 앱이 살아 있는 동안 같은 인스턴스이며, `ProductRepository`는 ViewModel과 함께 소멸
- [x] 화면을 반복해서 드나들어도 인스턴스가 누적되지 않음
- [x] 스코프별 인스턴스 생성과 소멸을 검증하는 테스트가 있음
- [x] 스코프 종류를 추가할 때 컨테이너 내부 분기가 늘어나지 않는 구조
- [x] README에 내가 만든 것이 DI인지 서비스 로케이터인지 코드의 구체적인 지점을 근거로 설명
- [x] README에 컴파일 타임 방식(KSP)으로 바꿀 때 재설계할 지점 또는 현재 구조를 유지할 근거를 설명

#### 설계 근거

##### DI와 서비스 로케이터

실제 앱의 사용 흐름은 `ShoppingApplication.onCreate()`에서 앱 범위 의존성을 등록하고, 화면이 `viewModel(factory = DependencyContainer)`로 ViewModel 생성을 요청하는 방식입니다. `DependencyContainer`가 ViewModel을 만들고 `@Inject` 필드를 채우므로, 현재 앱 코드에서는 의존성을 외부에서 전달하는 DI로 사용합니다. 화면이나 ViewModel이 `DependencyContainer.get()`을 직접 호출한다면 서비스 로케이터 방식이 됩니다. 컨테이너가 `get()` API를 제공하더라도 실제 호출부가 이를 사용하지 않는다는 점을 기준으로 구분했습니다.

이는 DIP, IoC와는 다른 구분입니다. DIP는 상위 정책이 구체 구현이 아니라 추상화에 의존하도록 하는 설계 원칙이고, IoC는 객체 생성·연결의 제어권이 사용하는 객체에서 외부 조립자에게 넘어가는 넓은 개념입니다. DI와 서비스 로케이터는 의존성을 외부에서 전달받는지, 객체가 중앙 저장소에서 직접 찾아오는지에 따른 사용 방식의 차이입니다. 이 프로젝트에서는 `CartViewModel`이 `CartRepository` 추상화에 의존하는 점이 DIP와 관련되고, 생성·연결을 컨테이너가 맡는 점이 IoC이며, `viewModel(factory = DependencyContainer)`로 주입받는 흐름이 DI입니다.

##### KSP를 적용한다면

현재 구현은 `KClass.primaryConstructor`, Java Reflection의 필드 탐색과 `Field.set()`으로 런타임에 객체 생성 및 주입 대상을 찾습니다. KSP를 적용한다면 이 탐색·호출 경로를 컴파일 타임에 생성하는 ViewModel Factory와 주입 코드로 대체할 수 있습니다. 다만 Room의 DAO나 `Context`처럼 직접 생성할 수 없는 의존성을 제공하는 등록 정보는 여전히 필요합니다. 이 미션에서는 런타임 Reflection을 통한 DI 동작을 직접 구현하는 것이 학습 목표이므로, 코드 생성 설정과 생성 코드 관리가 추가되는 KSP 대신 현재 구조를 유지합니다.

KSP로 바꾼다면 바인딩 목록, Qualifier, 생성자 파라미터, 스코프 선언처럼 객체 그래프를 구성하는 정보를 컴파일 타임에 수집해 생성 코드로 연결할 수 있습니다. 그러면 누락된 바인딩이나 지원하지 않는 주입 선언을 빌드 중 발견하고 생성된 직접 호출 경로를 사용할 수 있지만, KSP 설정과 생성 코드를 관리해야 하며 런타임에만 정해지는 정보를 다루기 어려워집니다.

Reflection은 수동 Factory 코드를 줄이고 런타임에 객체 그래프를 조립할 수 있는 대신 다음 비용이 있습니다.

- 생성자와 필드를 탐색하고 동적으로 호출하는 런타임 비용
- 잘못된 타입이나 누락된 바인딩을 컴파일 시점이 아니라 실행 중에 발견하는 안정성 비용
- 의존성이 생성자에 드러나지 않아 객체 생성 흐름을 따라가기 어려운 가독성 비용
- private 필드에 접근해 캡슐화를 약화하는 비용

이번 미션에서는 Reflection의 유연함을 얻는 대신 이 비용들을 감수하고, 런타임 객체 탐색과 주입 과정을 직접 구현하는 쪽을 선택했습니다.
