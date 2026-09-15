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
