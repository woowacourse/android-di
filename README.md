# android-di

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

- [ ] `TestRepository`를 생성자로 받는 테스트용 Service 클래스를 작성한다.
- [ ] 컨테이너가 Service 생성자에 필요한 Repository를 자동으로 주입하는 테스트를 작성한다.
- [ ] 생성자 파라미터 타입마다 컨테이너의 `resolve()`를 재귀적으로 호출한다.
- [ ] 조회한 의존성들을 `constructor.call()`에 전달한다.

### 5. 범용 ViewModelProvider.Factory 구현

- [ ] 테스트용 Repository를 생성자로 받는 테스트용 ViewModel을 작성한다.
- [ ] Factory가 테스트용 ViewModel을 생성하는 테스트를 작성한다.
- [ ] `modelClass`의 주 생성자와 파라미터 타입을 Reflection으로 조회한다.
- [ ] 각 파라미터 타입의 인스턴스를 컨테이너에서 가져온다.
- [ ] 조회한 의존성으로 ViewModel을 생성해 반환한다.

### 6. 새로운 ViewModel에 Factory 재사용

- [ ] 다른 의존성을 받는 두 번째 테스트용 ViewModel을 작성한다.
- [ ] Factory 코드를 변경하지 않고 두 번째 ViewModel을 생성하는 테스트를 작성한다.
- [ ] 새로운 ViewModel이 추가되어도 Factory에 ViewModel별 로직이 추가되지 않는지 확인한다.

### 7. ViewModel과 의존성의 생명주기 분리

- [ ] Factory에 같은 ViewModel을 두 번 요청하면 서로 다른 ViewModel이 생성되는지 검증한다.
- [ ] 두 ViewModel에 주입된 동일한 타입의 Repository는 같은 인스턴스인지 검증한다.
- [ ] Repository는 컨테이너가 재사용하고 ViewModel은 컨테이너가 보관하지 않도록 한다.

### 8. 실제 화면에 공통 Factory 적용

- [ ] `ProductsScreen`과 `CartScreen`이 동일한 컨테이너를 사용하는 공통 Factory를 참조하도록 한다.
- [ ] 컨테이너가 Composable이 다시 그려질 때마다 생성되지 않도록 한다.
- [ ] `ProductsViewModel.Factory`와 `CartViewModel.Factory`를 제거한다.
- [ ] 상품을 담은 뒤 장바구니 화면에서 같은 상품이 표시되는지 확인한다.
- [ ] 장바구니 화면에 다시 진입해도 담은 상품이 유지되는지 확인한다.

### 9. 최종 검증

- [ ] 사전에 제공된 `MainActivityTest`를 포함한 전체 단위 테스트가 통과한다.
- [ ] ktlint 검사가 통과한다.
- [ ] Annotation을 사용하지 않았는지 확인한다.
- [ ] 새로운 ViewModel을 추가해도 컨테이너와 Factory를 수정하지 않는지 확인한다.
