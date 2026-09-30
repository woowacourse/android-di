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
- 현재 `DependencyContainer`는 `object`와 타입·Qualifier별 맵을 사용해 등록된 인스턴스를 앱 프로세스 동안 공유합니다. 이 구조만으로 화면별 스코프가 생기는 것은 아니므로, 4단계에서는 의존성의 수명과 화면 스코프를 별도로 설계해야 합니다.
