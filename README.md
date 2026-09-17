# android-di

## Step 1
### 구현할 기능 목록

- [ ] `ProductRepository`와 `CartRepository`를 `object`로 변경하여 애플리케이션에서 단일 인스턴스를 공유한다.
- [ ] 수동으로 주입하고 있는 ViewModel 의존성을 자동으로 주입하도록 변경한다.
    - [ ] 생성자의 파라미터 정보를 바탕으로 필요한 의존성을 찾아 주입한다.
    - [ ] 특정 ViewModel에 종속되지 않고 여러 ViewModel에서 재사용할 수 있는 자동 주입 로직을 구현한다.

## step 0.5
### 구현할 기능 목록

- [x] `ProductsViewModelFactory`를 생성하여 `ProductsViewModel`에 필요한 의존성을 수동으로 주입한다.
- [x] `CartViewModelFactory`를 생성하여 `CartViewModel`에 필요한 의존성을 수동으로 주입한다.
