# android-di

## Step 1

### 리팩토링 목록

- [x] `ProductRepository`와 `CartRepository`를 `class`로 변경하고,
  `DependencyContainer`에서 인스턴스를 생성·캐싱하도록 수정한다.
- [ ] `DependencyContainer`가 생성자 파라미터를 분석하여
  필요한 의존성을 재귀적으로 생성하도록 수정한다.
- [ ] `AutoViewModelFactory`가 의존성 생성 로직을 직접 처리하지 않고
  `DependencyContainer`에 위임하도록 단순화한다.
- [ ] 의존성을 생성할 수 없는 경우 원인을 알 수 있는 예외 메시지를 제공한다.

### 테스트 보완 목록

- [ ] `ProductsViewModel`과 `CartViewModel`이 자동으로 생성되는지 검증한다.
- [ ] Repository를 여러 번 요청해도 동일한 인스턴스가 반환되는지 검증한다.
- [ ] 생성자 파라미터가 있는 클래스와 중첩된 의존성이 재귀적으로 생성되는지 검증한다.
- [ ] `object` 클래스와 생성에 실패하는 경우를 검증한다.

### 구현할 기능 목록

- [x] `ProductRepository`와 `CartRepository`를 `object`로 변경하여 애플리케이션에서 단일 인스턴스를 공유한다.
- [x] 수동으로 주입하고 있는 ViewModel 의존성을 자동으로 주입하도록 변경한다.
    - [x] 생성자의 파라미터 정보를 바탕으로 필요한 의존성을 찾아 주입한다.
    - [x] 특정 ViewModel에 종속되지 않고 여러 ViewModel에서 재사용할 수 있는 자동 주입 로직을 구현한다.

## step 0.5
### 구현할 기능 목록

- [x] `ProductsViewModelFactory`를 생성하여 `ProductsViewModel`에 필요한 의존성을 수동으로 주입한다.
- [x] `CartViewModelFactory`를 생성하여 `CartViewModel`에 필요한 의존성을 수동으로 주입한다.
