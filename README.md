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

- [ ] 같은 인터페이스에 여러 구현체를 등록하고 Qualifier로 원하는 구현체를 선택한다.
- [ ] Room 구현체와 In-Memory 구현체를 각각 선택해 주입할 수 있도록 구성한다.
- [ ] 같은 타입의 구현체가 여러 개일 때 Qualifier가 없으면 명확한 예외를 발생시킨다.
- [ ] Qualifier가 구현체를 올바르게 선택하는지, Qualifier가 없을 때 예외가 발생하는지 테스트한다.

### DI 모듈 분리

- [ ] DI 라이브러리를 `:di` 모듈로 분리하고 앱에서 사용하도록 연결한다.
- [ ] `:di` 모듈이 쇼핑 앱의 도메인 타입에 의존하지 않도록 구성한다.
- [ ] `:di` 모듈을 순수 JVM 또는 Android 모듈 중 하나로 결정하고 선택 근거를 기록한다.
- [ ] Qualifier를 Annotation 또는 문자열 키 중 하나로 표현하고 선택 근거를 기록한다.
- [ ] Qualifier 동작과 모듈 경계를 검증하는 테스트를 작성한다.

### 선택 요구 사항

- [ ] DI 설정에 사용할 DSL을 설계한다.
- [ ] DI 라이브러리를 배포하고 앱에 적용한다.
