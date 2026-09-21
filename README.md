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

- [ ] 하나의 인터페이스에 여러 구현체가 등록된 경우 `Qualifier` 어노테이션으로 의존성을 구분

#### 모듈 분리

- [ ] 직접 구현한 DI 라이브러리를 별도의 Gradle 모듈로 분리
