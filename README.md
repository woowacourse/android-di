# android-di

## 3단계 기능 목록

### 1. DI 코어를 순수 JVM 모듈로 분리

- [ ] `:di` Kotlin/JVM 모듈을 추가하고 `:app`이 `:di`를 의존하도록 구성한다.
- [ ] `DependencyContainer`, `MyInject`와 관련 단위 테스트를 `:di`로 이동한다.
- [ ] Android의 `ViewModelProvider`를 사용하는 `ReflectionViewModelFactory`와 앱 의존성 구성은 `:app`에 유지한다.
- [ ] `:di`의 `build.gradle.kts`에 Android 및 쇼핑 앱 의존성이 없음을 확인한다.
- [ ] 모듈 이동 후 기존 컨테이너 테스트와 앱 테스트가 통과하는지 확인한다.

### 2. Qualifier를 포함한 의존성 등록 및 조회

- [ ] 테스트 전용 Qualifier 두 개로 같은 인터페이스의 서로 다른 구현체를 등록하는 실패 테스트를 작성한다.
- [ ] Qualifier 애노테이션을 식별하는 메타 애노테이션을 정의한다.
- [ ] 컨테이너의 의존성 키가 타입과 Qualifier 애노테이션 타입을 함께 사용하도록 변경한다.
- [ ] 요청한 Qualifier와 일치하는 구현체를 반환하는 최소 구현을 추가한다.
- [ ] Qualifier 등록 및 조회 API의 중복을 정리한다.

### 3. Qualifier가 없는 모호한 요청 검증

- [ ] 같은 타입의 구현체가 둘 이상 등록된 상태에서 Qualifier 없이 조회하면 실패하는 테스트를 작성한다.
- [ ] 요청 타입, 등록된 Qualifier, 해결 방법이 드러나는 명확한 예외 메시지를 정의한다.
- [ ] 컨테이너가 임의의 구현체를 선택하지 않고 모호한 의존성 예외를 던지도록 구현한다.
- [ ] 구현체가 하나만 등록된 경우에는 Qualifier 없이도 기존처럼 조회되는지 회귀 테스트를 작성한다.

### 4. 주입 지점의 Qualifier 해석

- [ ] Qualifier가 붙은 필드에 지정한 구현체가 주입되는 실패 테스트를 작성한다.
- [ ] 필드의 애노테이션 중 Qualifier 메타 애노테이션이 붙은 애노테이션을 찾아 컨테이너 조회에 전달한다.
- [ ] Qualifier가 없는 필드에서 후보가 여러 개면 모호한 의존성 예외가 전파되는지 테스트한다.
- [ ] Qualifier가 없고 후보가 하나인 기존 필드 주입 동작이 유지되는지 확인한다.

### 5. 등록 DSL 제공

- [ ] 타입과 Qualifier를 선언적으로 등록할 수 있는 DSL 사용 예시를 테스트로 먼저 작성한다.
- [ ] 인스턴스와 생성 함수를 등록할 수 있는 최소 DSL을 구현한다.
- [ ] DSL이 기존 등록 및 조회 규칙을 그대로 따르는지 테스트한다.
- [ ] 앱의 의존성 구성을 DSL로 변경한다.

### 6. Room 및 In-Memory 장바구니 구현체 구성

- [ ] 앱 모듈에 `RoomCart`, `InMemoryCart` Qualifier 애노테이션을 정의한다.
- [ ] 메모리에 장바구니 상품을 보관하는 `InMemoryCartRepository`를 테스트 주도로 구현한다.
- [ ] 앱 시작 시 Room 구현체와 In-Memory 구현체를 각각의 Qualifier로 등록한다.
- [ ] 앱의 등록 코드만 `CartRepository`, `CartProductDao` 등 쇼핑 도메인 타입을 아는지 확인한다.

### 7. 장바구니 화면에서 구현체 선택

- [ ] `CartViewModel`이 `RoomCart`를 지정했을 때 Room 구현체를 주입받는 테스트를 작성한다.
- [ ] 주입받는 필드에 `RoomCart` Qualifier를 지정한다.
- [ ] Qualifier를 `InMemoryCart`로 바꾸면 컨테이너나 ViewModel Factory 수정 없이 In-Memory 구현체가 주입되는지 테스트한다.
- [ ] 실제 앱에서 선택한 저장소를 사용해 장바구니 추가, 조회, 삭제가 동작하는지 확인한다.

### 8. 최종 검증

- [ ] `:di` 모듈 단위 테스트를 실행한다.
- [ ] 사전에 제공된 테스트를 포함한 `:app` 전체 테스트를 실행한다.
- [ ] ktlint 검사를 실행한다.
- [ ] 같은 타입의 두 구현체를 Qualifier로 구분해 선택할 수 있는지 확인한다.
- [ ] Qualifier 없는 모호한 요청이 구현체를 임의 선택하지 않고 명확한 예외를 내는지 확인한다.
- [ ] `:di`의 소스와 Gradle 의존성에 Android 및 쇼핑 앱 도메인 타입이 없는지 확인한다.

## 2단계 기능 목록

### 1. Kotlin Annotation 학습

- [x] 애노테이션의 적용 대상에 따라 Reflection으로 조회되는 위치가 달라지는지 테스트한다.
- [x] 애노테이션의 유지 정책에 따라 런타임 조회 가능 여부가 달라지는지 테스트한다.

### 2. 애노테이션 기반 필드 주입

- [x] 의존성 주입 대상을 구분할 애노테이션을 정의한다.
- [x] 컨테이너가 애노테이션이 붙은 필드에만 의존성을 주입하는지 테스트한다.
- [x] 애노테이션이 붙지 않은 필드는 주입하지 않는지 테스트한다.

### 3. ViewModel 필드 주입

- [x] ViewModel의 생성자 의존성을 애노테이션이 붙은 필드 의존성으로 변경한다.
- [x] Factory가 ViewModel을 생성한 뒤 필드 의존성을 주입하도록 변경한다.
- [x] ViewModel에 필요한 의존성이 필드 주입되는지 테스트한다.

### 4. CartRepository의 DAO 의존성 주입

- [x] `CartRepository`가 `CartProductDao`를 생성자로 전달받도록 변경한다.
- [x] 컨테이너가 `CartRepository`에 필요한 DAO까지 재귀적으로 해결하도록 구성한다.
- [x] 컨테이너가 직접 생성할 수 없는 의존성의 생성 경계를 정하고 등록한다.
- [x] `CartRepository`에 DAO가 정상적으로 주입되는지 테스트한다.

### 5. 장바구니 도메인 모델과 매퍼

- [x] UI에서 사용할 식별자와 담은 시각을 가진 `CartProduct` 도메인 모델을 추가한다.
- [x] `CartProductEntity`를 `CartProduct`로 변환하는 매퍼를 추가한다.
- [x] UI 계층이 `CartProductEntity`를 직접 참조하지 않도록 한다.

### 6. Room을 사용하는 CartRepository

- [x] 장바구니 추가, 조회, 삭제가 `CartProductDao`를 통해 동작하도록 변경한다.
- [x] DAO 호출에 맞춰 Repository의 함수를 `suspend` 함수로 변경한다.
- [x] 장바구니 조회 결과를 엔티티가 아닌 도메인 모델로 반환한다.

### 7. ViewModel의 비동기 장바구니 처리

- [x] 상품 추가를 `viewModelScope` 안에서 실행한다.
- [x] 장바구니 조회를 먼저 완료한 뒤 조회 결과로 UI 상태를 갱신한다.
- [x] 장바구니 삭제와 삭제 이후의 상태 갱신을 `viewModelScope` 안에서 실행한다.

### 8. 장바구니 화면 변경

- [x] 장바구니 UI 상태와 화면이 `CartProduct`를 사용하도록 변경한다.
- [x] 목록의 위치가 아닌 상품의 실제 식별자를 이용해 삭제한다.
- [x] 장바구니 상품에 상품명과 담은 시각을 함께 표시한다.
- [x] `DateFormatter`를 화면 스코프에서 생성하고 하위 UI 계층에 전달한다.
- [x] 미리보기와 UI 테스트 데이터를 변경된 도메인 모델에 맞게 수정한다.

### 9. LazyColumn 항목 식별자 적용

- [x] 목록 위치를 사용하지 않도록 `itemsIndexed`를 `items`로 변경한다.
- [x] `LazyColumn`의 각 항목에 고유한 key를 지정한다.
- [x] key 지정 전후에 목록 중간 항목을 삭제했을 때의 차이를 확인한다.

### 10. 최종 검증

- [x] 필드 주입과 재귀 주입에 대한 테스트가 통과한다.
- [x] 목록 중간 항목을 삭제해도 선택한 상품이 삭제되는지 확인한다.
- [x] 사전에 제공된 테스트를 포함한 전체 테스트가 통과한다.

## 0.5단계 기능 목록

- [x] ProductsViewModel에 필요한 Repository를 수동으로 주입한다.
- [x] CartViewModel에 필요한 Repository를 수동으로 주입한다.
- [x] 각 화면에서 ViewModelProvider.Factory를 직접 생성한다.
- [x] MainActivityTest가 통과하는지 확인한다.

## 1단계 TDD 진행 순서

### 1. 생성자 Reflection 학습

- [x] `ProductsViewModel`의 주 생성자를 조회하는 테스트를 작성한다.
- [x] `constructor.call()`에 Repository 인스턴스를 전달해 `ProductsViewModel`을 생성한다.

```text
.call()은 Reflection으로 가져온 생성자나 함수를 실행하는 역할
```

### 2. 요청한 타입의 인스턴스 생성

- [x] 컨테이너에 타입을 요청하면 해당 타입의 인스턴스를 생성하는 테스트를 작성한다.
- [x] 타입의 주 생성자를 조회하고 호출하는 최소한의 컨테이너를 구현한다.

### 3. 동일한 의존성 인스턴스 재사용

- [x] 동일한 타입을 두 번 요청하면 같은 인스턴스를 반환하는 테스트를 작성한다.
- [x] 생성한 인스턴스를 `KClass`를 키로 하는 Map에 저장한다.
- [x] 이미 생성된 타입이면 Map에 저장된 인스턴스를 반환한다.

```text
.cast()는 런타임에 지정한 클래스인지 확인하고, 맞으면 그 타입을 반환
```

### 4. 생성자 의존성 재귀 생성

- [x] `TestRepository`를 생성자로 받는 테스트용 Service 클래스를 작성한다.
- [x] 컨테이너가 Service 생성자에 필요한 Repository를 자동으로 주입하는 테스트를 작성한다.
- [x] 생성자 파라미터 타입마다 컨테이너의 `resolve()`를 재귀적으로 호출한다.
- [x] 조회한 의존성들을 `constructor.call()`에 전달한다.

```text
재귀적으로 instances에 객체들이 저장됨
이미 저장되었으면 꺼내서 사용
```

### 5. 범용 ViewModelProvider.Factory 구현

- [x] 테스트용 Repository를 생성자로 받는 테스트용 ViewModel을 작성한다.
- [x] Factory가 테스트용 ViewModel을 생성하는 테스트를 작성한다.
- [x] `modelClass`의 주 생성자와 파라미터 타입을 Reflection으로 조회한다.
- [x] 각 파라미터 타입의 인스턴스를 컨테이너에서 가져온다.
- [x] 조회한 의존성으로 ViewModel을 생성해 반환한다.

```text
ReflectionViewModelFactory vs DependencyContainer

- factory와 container의 구조는 비슷하지만 하는 일과 목적이 다름
- factory는 container에 저장되어있는 의존성을 사용해 viewModel을 만듬
- container는 앱이 시작되고 메모리에 객체가 살아있음.
- viewModelStore가 생명주기에 맞춰서 factory로 viewModel 생성
```

### 6. 새로운 ViewModel에 Factory 재사용

- [x] 다른 의존성을 받는 두 번째 테스트용 ViewModel을 작성한다.
- [x] Factory 코드를 변경하지 않고 두 번째 ViewModel을 생성하는 테스트를 작성한다.
- [x] 새로운 ViewModel이 추가되어도 Factory에 ViewModel별 로직이 추가되지 않는지 확인한다.

### 7. ViewModel과 의존성의 생명주기 분리

- [x] Factory에 같은 ViewModel을 두 번 요청하면 서로 다른 ViewModel이 생성되는지 검증한다.
- [x] 두 ViewModel에 주입된 동일한 타입의 Repository는 같은 인스턴스인지 검증한다.
- [x] Repository는 컨테이너가 재사용하고 ViewModel은 컨테이너가 보관하지 않도록 한다.

### 8. 실제 화면에 공통 Factory 적용

- [x] `ProductsScreen`과 `CartScreen`이 동일한 컨테이너를 사용하는 공통 Factory를 참조하도록 한다.
- [x] 컨테이너가 Composable이 다시 그려질 때마다 생성되지 않도록 한다.
- [x] `ProductsViewModel.Factory`와 `CartViewModel.Factory`를 제거한다.
- [x] 상품을 담은 뒤 장바구니 화면에서 같은 상품이 표시되는지 확인한다.
- [x] 장바구니 화면에 다시 진입해도 담은 상품이 유지되는지 확인한다.

### 9. 최종 검증

- [x] 사전에 제공된 `MainActivityTest`를 포함한 전체 단위 테스트가 통과한다.
- [x] ktlint 검사가 통과한다.
- [x] Annotation을 사용하지 않았는지 확인한다.
- [x] 새로운 ViewModel을 추가해도 컨테이너와 Factory를 수정하지 않는지 확인한다.
