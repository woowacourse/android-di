# 🚀 0.5단계 - 수동 DI

`ProductsViewModel`을 생성할 때 필요한 객체를 직접 만들어 주입한다.

## 기능 목록

- [x] `ProductsScreen`의 `viewModel()`에 `ViewModelProvider.Factory`를 전달한다.
- [x] Factory에서 `ProductRepository`와 `CartRepository`를 직접 생성한다.
- [x] 생성한 Repository를 `ProductsViewModel`의 생성자에 전달한다.

# 🚀 1단계 - 자동 DI

ViewModel이 필요로 하는 객체를 자동으로 생성하고, 여러 화면에서 공유할 객체를 재사용한다.

## 기능 목록

- [ ] 요청받은 ViewModel 타입의 생성자와 매개변수 타입을 확인해 인스턴스를 생성한다.
- [ ] 생성자에 필요한 의존성을 재귀적으로 찾아 주입한다.
- [ ] 이미 생성한 의존성을 다시 요청하면 같은 인스턴스를 반환한다.
- [ ] `ProductsViewModel`과 `CartViewModel`이 같은 자동 주입 로직을 사용한다.
- [ ] ViewModel을 추가해도 해당 ViewModel만을 위한 주입 로직을 새로 작성하지 않는다.

## 프로그래밍 요구 사항

- [ ] 사전에 제공된 테스트가 모두 통과한다.
- [ ] 의존성 주입에 어노테이션을 사용하지 않는다.
