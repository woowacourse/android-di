# android-di


## 0.5단계 기능 목록

- [x] `MainActivityTest`를 통과시킨다.
- [x] `ViewModelProvider.Factory`를 구현한다.
- [x] Factory에서 `Repository`를 생성한다.
- [x] 생성한 `Repository`를 `ProductsViewModel`에 생성자 주입한다.
- [x] Composable의 `viewModel()`에 Factory를 직접 전달한다.

## 1단계 기능 목록

- [x] 생성자와 생성자 파라미터 타입을 분석해 의존성을 자동으로 주입한다.
- [x] 여러 `ViewModel`에서 재사용할 수 있는 범용 `ViewModelProvider.Factory`를 구현한다.
- [x] `ProductsViewModel`과 `CartViewModel`이 동일한 자동 주입 로직을 사용하도록 한다.
- [x] 여러 번 생성할 필요가 없는 객체는 최초 한 번만 생성해 재사용한다.
- [x] 상품 목록 화면과 장바구니 화면에서 동일한 `CartRepository` 인스턴스를 사용한다.
- [x] Composable의 `viewModel()`에 자동 주입 Factory를 전달한다.
- [x] Annotation을 사용하지 않는다.
- [x] 사전에 제공된 테스트를 모두 통과시킨다.

### 선택 기능 목록

- [ ] TDD로 DI를 구현한다.
- [ ] Robolectric으로 기능 테스트를 작성한다.
- [ ] `ViewModel` 테스트를 작성한다.
- [ ] 도메인 로직과 `Repository` 단위 테스트를 작성한다.

## 2단계 기능 목록 - Annotation

### 필드 주입

- [x] 필드 주입 대상을 표시하는 애노테이션을 정의한다.
- [x] DI 컨테이너가 생성한 객체에서 애노테이션이 붙은 필드만 찾아 의존성을 주입하도록 구현한다.
- [x] 애노테이션이 붙지 않은 필드는 주입하지 않도록 구현한다.
- [x] `ViewModel`의 필드 주입이 정상적으로 동작하는지 테스트한다.
- [x] 애노테이션이 없는 필드에는 의존성이 주입되지 않는지 테스트한다.

### 재귀 의존성 주입

- [x] `CartRepository`를 인터페이스로 변경하고 `CartProductDao`를 생성자 주입받는 `DefaultCartRepository`를 구현한다.
- [x] DI 컨테이너가 객체 생성에 필요한 의존성을 재귀적으로 해결하도록 구현한다.
- [x] `CartRepository`에 `DefaultCartRepository`를 연결하고, `CartProductDao`까지 재귀적으로 주입되는지 테스트한다.

### 장바구니 도메인 및 데이터 계층

- [x] 식별자, 상품명, 가격, 담은 시각을 갖는 `CartProduct` 도메인 모델을 추가한다.
- [x] `CartProductEntity`를 `CartProduct`로 변환하는 `toDomain()` 매퍼를 추가한다.
- [x] `CartRepository`의 조회 결과를 `List<CartProduct>`로 변경한다.
- [x] `CartRepository`의 추가·조회·삭제 함수를 `suspend` 함수로 변경한다.
- [x] 장바구니 삭제가 목록 인덱스가 아닌 엔티티의 `Long` 타입 식별자를 사용하도록 변경한다.

### ViewModel 및 장바구니 UI

- [x] `ProductsViewModel`이 `viewModelScope`에서 장바구니 상품을 추가하도록 변경한다.
- [x] `CartViewModel`이 `viewModelScope`에서 장바구니 상품을 조회하고 상태를 갱신하도록 변경한다.
- [x] `CartViewModel`이 `viewModelScope`에서 식별자로 장바구니 상품을 삭제하도록 변경한다.
- [x] 장바구니 UI 상태의 상품 타입을 `List<CartProduct>`로 변경한다.
- [x] 장바구니 화면과 미리보기, 테스트가 `CartProduct`를 사용하도록 변경한다.
- [x] `DateFormatter`를 적절한 UI 계층에 주입하고 장바구니 상품을 담은 시각을 표시한다.
- [x] 삭제 버튼이 `ic_delete` 드로어블을 사용하고 선택한 상품의 식별자를 전달하도록 변경한다.
- [x] 기존 테스트와 새로 추가한 DI 테스트를 모두 통과시킨다.

### 선택 기능 목록

- [x] `LazyColumn`의 `items`에 상품 식별자를 `key`로 지정한다.
- [x] UI 계층에서 `CartProductEntity`를 직접 참조하지 않도록 한다.

## 3단계 기능 목록 - Qualifier

### Qualifier

- [x] 동일한 타입의 여러 구현체를 구분할 Qualifier 표현 방식을 결정하고 구현한다.
- [x] DI 컨테이너가 타입과 Qualifier를 함께 사용해 의존성을 등록하고 조회하도록 변경한다.
- [x] Room 기반 `CartRepository`와 In-Memory 기반 `CartRepository`를 각각 등록한다.
- [x] 주입 지점에 지정된 Qualifier에 따라 원하는 `CartRepository` 구현체를 주입한다.
- [x] 동일한 타입의 구현체가 여러 개일 때 Qualifier가 없으면 임의로 선택하지 않고 명확한 예외를 발생시킨다.
- [x] 서로 다른 Qualifier로 각 구현체가 올바르게 주입되는지 테스트한다.
- [x] 모호한 의존성을 Qualifier 없이 요청하면 명확한 예외가 발생하는지 테스트한다.

### DI 모듈 분리

- [x] DI 라이브러리를 별도의 `:di` 모듈로 분리한다.
- [x] `:app` 모듈이 `:di` 모듈을 단방향으로 의존하도록 Gradle 구성을 변경한다.
- [x] 컨테이너, 주입 애노테이션, Qualifier 관련 코드를 `:di` 모듈로 이동한다.
- [x] 쇼핑 앱의 도메인 의존성 등록은 `:app` 모듈의 애플리케이션 초기화 지점에서 수행한다.
- [x] `:di` 모듈이 `CartRepository`, `Product` 등 쇼핑 앱의 도메인 타입에 의존하지 않는지 확인한다.
- [x] 모듈 분리 후 모든 기존 테스트와 Qualifier 테스트를 통과시킨다.
- [x] Qualifier 표현 방식과 선택 근거를 README에 기록한다.
- [x] `:di` 모듈을 Android 모듈 또는 순수 JVM 모듈 중 하나로 결정하고 선택 근거 및 4단계 화면 스코프에 미치는 영향을 README에 기록한다.

### 선택 기능 목록

- [x] DSL로 의존성 등록 API를 구성한다.
- [ ] DI 라이브러리를 JitPack에 배포하고 앱에 외부 의존성으로 적용한다.

### 설계 선택

- Qualifier는 문자열 키 대신 `@Qualifier` 메타 애노테이션이 붙은 애노테이션 타입으로 표현한다. 문자열 키의 오타는 실행 중에야 발견되지만, 애노테이션 타입은 컴파일러가 참조 오류를 확인할 수 있고 IDE의 이름 변경 기능도 안전하게 사용할 수 있기 때문이다.
- `:di`는 순수 JVM 모듈로 구성한다. 컨테이너의 역할을 타입 분석, 객체 생성, 의존성 등록과 조회로 제한하면 Android API가 필요하지 않고, 다른 JVM 프로젝트에서도 재사용할 수 있으며 Gradle 의존성만으로 앱 도메인과의 경계를 검증할 수 있기 때문이다.
- Android `ViewModel` 생성과 앱 의존성 등록은 `:app`의 연동 계층인 `DIViewModelFactory`와 `AppContainer`가 담당한다. 이를 위해 코어는 매번 새 객체를 만드는 `create()`와 인스턴스를 재사용하는 `get()`을 구분한다.
- 4단계 화면 스코프에서는 화면 생명주기의 시작과 종료 신호를 Android/Compose를 아는 `:app` 연동 계층이 받아 코어의 스코프를 열고 닫도록 구현한다. 따라서 `:di`는 Android 생명주기에 의존하지 않고 스코프와 인스턴스 관리 규칙만 담당한다.

### JitPack 적용 방법

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}

// app/build.gradle.kts
dependencies {
    implementation("com.github.nadajinny.android-di:di:3.0.0")
}
```
