# 만들면서 배우는 DI 1단계

## 0.5단계

### 목적
실패하는 테스트(`MainActivityTest`)를 통과시키기 위해 `ProductsScreen`에 `ProductViewModel`을 수동으로 주입한다.

### 기능 목록

- [x] `ProductViewModel`에 ViewModel factory를 구현한다.
- [x] `ProductViewModel`의 팩토리를 생성하여 필요한 Repository를 주입하고 이를 `ProductsScreen`에 넘긴다.

### 문제점

1. 화면이 늘어날 때마다 팩토리를 하나씩 새로 쓴다.
2. Repository 객체를 교체하기 위해 또다른 객체를 만들어 바꿔줘야 한다. 즉, ViewModel에 직접적인 변경사항이 발생한다.

## 1단계

### 목적
하나의 자동 주입 로직을 통해 ViewModel에 의존성들을 자동으로 주입할 수 있도록 구현한다.

### 기능 목록

- [x] 특정 ViewModel에서만이 아닌, 범용적으로 활용될 수 있는 자동 주입 로직을 작성한다.
- [x] DIContainer에 인스턴스를 생성하고 보관하는 로직을 추가한다.

## 2단계

### 목적
Annotation을 붙여서 필요한 요소에만 의존성을 주입하는 방식으로 ViewModel 내 필드 주입을 구현한다. 

### 기능 목록 

- [x] `CartRepository`가 DAO 객체를 참조하도록 변경한다.
  - [x] `CartProduct`를 만든다.
  - [x] 엔티티에서 `CartProduct` 도메인으로 바꾸는 매퍼 함수를 추가한다.
  - [x] `CartContentTest`를 `CartProduct`를 사용하도록 수정한다. 
  - [x] `CartContentPreview`를 `CartProduct`를 사용하도록 수정한다.
  - [x] `deleteCartProduct`의 인자가 인덱스가 아닌 id를 받도록 수정한다. 
  - [x] `ProductsViewModel.addCartProduct`를 `viewModelScope.launch` 안으로 옮긴다. 
  - [x] `CartViewModel.getAllCartProducts` 결과를 조회한 후 `update { }` 람다에서 갱신하도록 구조를 변경한다.
  - [x] `CartViewModel.deleteCartProduct`를 `viewModelScope.launch` 안으로 옮긴다. 
  - [x] 장바구니 화면에 담은 시각을 표시한다.
- [x] ViewModel 내 필드 주입을 구현한다.
  - [x] DAO가 자동으로 생성되도록 변경한다. 
  - [x] ViewModel의 생성자 파라미터가 인터페이스를 받도록 변경한다. 
  - [x] Annotation 학습 테스트를 학습한다.
  - [x] 필드 주입 테스트를 작성한다. 
  - [x] 재귀 주입을 구현한다.
  - [x] 재귀 주입 테스트를 작성한다.

## 3단계

### 목적

하나의 인터페이스에 여러 구현체가 존재하더라도 개발자가 원하는 구현체를 명확하게 선택하여 주입받을 수 있도록 Qualifier를 구현한다.
또한 DIContainer가 쇼핑 앱의 구체적인 도메인 타입에 의존하지 않도록 DI 기능을 별도의 모듈로 분리하여, 재사용 가능한 DI 라이브러리 구조로 변경한다.

### 기능 목록

- [x] 동일한 인터페이스의 여러 구현체를 DIContainer에 등록할 수 있도록 변경한다.
    - [x] Room DB를 사용하는 구현체와 In-Memory 구현체를 각각 등록할 수 있도록 한다.
    - [x] 동일 타입의 구현체를 구분하기 위한 Qualifier 표현 방식을 결정한다.
    - [x] 주입받는 지점에서 Qualifier를 지정하여 원하는 구현체를 선택할 수 있도록 한다.
    - [x] Qualifier가 지정되지 않은 상태에서 동일 타입의 구현체가 여러 개 존재하면 명확한 예외를 발생시킨다.
    - [x] 임의의 구현체를 선택하지 않도록 한다.
    - [x] Qualifier에 따라 올바른 구현체가 주입되는지 테스트를 작성한다.
    - [x] Qualifier 없이 모호한 의존성을 요청했을 때 예외가 발생하는지 테스트를 작성한다.

- [x] DI 기능을 별도의 `:di` 모듈로 분리한다.
    - [x] DIContainer와 DI에 필요한 애노테이션 및 공통 기능을 `:di` 모듈로 이동한다.
    - [x] `:di` 모듈이 `CartRepository`, `Product` 등 쇼핑 앱의 도메인 타입을 직접 참조하지 않도록 한다.
    - [x] 쇼핑 앱에서 필요한 타입과 구현체의 관계를 DIContainer에 등록하도록 구조를 변경한다.
    - [x] 앱 시작 시 필요한 의존성 관계를 등록하는 위치를 결정한다.
    - [x] `:app → :di` 단방향 의존 관계를 유지한다.
    - [x] `:di` 모듈을 Android 모듈 또는 순수 JVM 모듈 중 어떤 형태로 구성할지 결정한다.

### 선택 근거 

### Qualifier를 어노테이션으로 표현한 이유 
문자열을 사용할 경우 오타가 발생해도 컴파일 단계에서는 알 수 없다. 잘못된 문자열은 실제 의존성을 조회하는 런타임에서 문제를 확인할 수 있다.
반면 어노테이션을 사용하면 존재하지 않는 Qualifier일 경우 컴파일 단계에서 바로 확인할 수 있다. 

또한 현재 DIContainer가 @Inject를 포함한 어노테이션을 런타임 reflection으로 해석하는 구조이기 때문에 Qualifier 역시 어노테이션으로 표현하면 기존 주입 방식과 일관된 형태로 처리할 수 있다.

### `:di` 모듈을 순수 JVM 모듈로 구성한 이유
Android 모듈로 구성하면 Context, ViewModel, Room 등의 Android API를 DI 모듈에서 직접 사용할 수 있어 구현은 편리하지만, DI 모듈을 Android 프레임워크 외의 환경에서 재사용하기 어려워진다.
따라서 DI 모듈의 책임을 특정 Android 프레임워크와 관계없이 객체의 등록, 생성, 탐색, 주입을 관리하는 것으로 한정하기 위해 순수 JVM 모듈로 구성하였다.

순수 JVM 모듈에서는 Android 타입을 직접 알 수 없으므로, Context나 Room에서 생성하는 DAO처럼 DIContainer가 직접 생성하기 어려운 객체는 `:app`에서 컨테이너에 등록해야 한다.
또한 화면 스코프처럼 Android 생명주기와 관련된 기능도 DI 모듈이 Android 생명주기를 알 수 없으므로 `:app`에서 처리해야 한다. 

## 4단계

### 목적
스코프 개념을 도입하여 각 객체를 생명주기에 따라 관리할 수 있도록 한다. 

### 기능 목록

- [x] 스코프에 따라 의존성을 생성할 수 있도록 구현한다.
  - [x] CartRepository를 앱 스코프로 관리한다.
  - [x] ProductRepository를 ViewModel 스코프로 관리한다. 
  - [x] DateFormtter를 화면 스코프로 관리한다. 
  - [x] Android 생명주기와 di 모듈의 책임을 분리한다. 
  - [x] 스코프 종료 시 인스턴스 참조를 제거한다.
  - [x] 화면 회전 등의 구성 변경에도 화면 스코프가 유지되도록 구현한다.
  - [x] 새로운 스코프 종류를 di 모듈을 수정하지 않고도 추가할 수 있도록 한다.

- [x] Lifecycle 시나리오를 테스트한다. 

### 내가 만든 컨테이너는 DI인가, 서비스 로케이터인가?

현재 앱은 컨테이너를 **DI 방식으로 사용한다**. `resolve()` 호출은 객체를 조립하는 `ViewModelFactory`와 `CartScreenScopeViewModelFactory`에 모여 있다. ViewModel은 컨테이너를 직접 조회하지 않고 의존성을 주입받으며, `CartScreen`의 `viewModel`과 `dateFormatter`는 함수 시그니처에 드러난다.

다만 `@Inject` 필드는 생성자 시그니처에 보이지 않고, 팩토리는 `CreationExtras`에서 앱의 컨테이너를 찾는다. 따라서 모든 의존성이 명시적인 생성자 DI인 것은 아니다. 소비자 클래스가 직접 `resolve()`를 호출한다면 그 사용 방식은 서비스 로케이터에 가까워진다.

**DIP**는 어떤 타입에 의존하느냐에 대한 원칙이다. `CartViewModel`은 `CartRepository` 인터페이스에 의존하지만 `ProductsViewModel`은 구체 클래스 `ProductRepository`에 의존하므로 적용 정도가 다르다.

**IoC**는 객체 생성과 정리의 주도권에 관한 이야기다. AndroidX가 ViewModel을 만들 때 팩토리를 호출하고, 팩토리는 컨테이너에 생성과 주입을 맡긴다. 백스택에서 화면이 제거되면 AndroidX가 ViewModel을 정리하고 `addCloseable`을 통해 스코프가 닫힌다. 이 과정에는 제어의 역전이 있지만, 앱이 컨테이너를 설정하고 필요할 때 `resolve()`를 호출하므로 컨테이너 자체는 앱의 흐름을 주도하는 프레임워크보다 라이브러리에 가깝다.

### Reflection을 KSP 코드 생성으로 바꾼다면

현재 `DIContainer.resolve()`는 객체를 처음 요청할 때 바인딩과 스코프를 찾고, reflection으로 생성자와 `@Inject` 필드를 확인해 주입한다. KSP로 바꾸려면 현재의 `bind()` 호출을 빌드 시점에 읽을 수 있는 선언으로 재설계해야 한다. 구현체, Qualifier, 스코프, 생성자 인자와 주입 필드, `Context`나 DAO 같은 외부 객체의 제공 방법을 미리 알아야 생성 코드를 만들고 누락이나 중복, 순환 의존성을 검사할 수 있다. 화면 ID, 스코프의 시작과 종료, 인스턴스 보관은 계속 런타임의 책임이다.

「Reflection의 대가」에서 정리한 네 가지 비용도 달라진다.

- **R8:** 생성 코드가 생성자와 필드를 직접 참조하므로 광범위한 keep 규칙의 필요성이 줄어든다.
- **성능:** 처음 객체를 요청할 때 수행하던 reflection 작업이 줄어드는 대신 빌드 과정이 늘어난다. 현재 컨테이너가 앱 시작에 그래프 전체를 훑는 것은 아니다.
- **오류 시점:** 바인딩 누락이나 Qualifier 불일치 등을 빌드 오류로 발견할 수 있다. 이미 닫힌 스코프 접근 같은 오류는 여전히 런타임에 확인해야 한다.
- **KMP:** 현재 `:di`의 `kotlin-reflect`와 `.java` 사용을 걷어내고 공용 런타임을 설계할 수 있다. Android의 `Context`, Room, Navigation 연동은 플랫폼 코드로 남는다.
