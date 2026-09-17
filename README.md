# android-di

## 0.5단계 · 생성자 주입(수동)

### 목표

의도적으로 실패하는 `MainActivityTest`를 수동 주입으로 통과시킨다.

이번 단계에서는 **DI 컨테이너를 사용하지 않는다.** 컴포저블이 `viewModel()`을 호출하는 자리에 `ViewModelProvider.Factory`를 직접 전달하고, 화면에 필요한 `Repository`를 직접 생성해 주입한다.

### 구현 예시

```kotlin
viewModel: ProductsViewModel =
    viewModel(
        factory = /* 여기서 직접 만들어 넣는다 */,
    )
```

위와 같이 구현한 뒤 다음 문제점을 확인한다. 이 문제가 1단계의 출발점이다.

### 확인할 문제점

- 화면이 늘어날 때마다 Factory를 하나씩 새로 작성해야 한다.
- Repository 객체를 교체하려면 또 다른 객체로 바꿔줘야 한다.
- Repository 교체로 인해 `ViewModel`에 직접적인 변경사항이 발생한다.

> 💡 **0.5단계는 1단계의 출발점이다.**
>
> OT에서는 수동 DI 구현을 먼저 PR로 리뷰받는다. 진행 순서와 오늘의 리뷰 기준은 OT 문서를 따른다.

### 구현할(확인할) 기능 목록

- 의도적으로 실패하는 `MainActivityTest`를 수동 주입으로 통과시킨다.
- DI 컨테이너를 만들지 않는다.
- 컴포저블에서 `viewModel()`을 호출할 때 `ViewModelProvider.Factory`를 직접 전달한다.
- 화면에 필요한 `Repository`를 직접 생성해 `ViewModel`의 생성자로 주입한다.
- 화면이 늘어날 때마다 Factory를 새로 작성해야 하는 문제를 확인한다.
  - 다음 변경으로 확인함 (ed8053bc)
- Repository 객체를 교체할 때 `ViewModel`을 직접 변경해야 하는 문제를 확인한다.
  - `ProductRepository`를 다른 구현체로 교체하는 상황을 가정한다.
  - `ProductsViewModel`의 생성자가 구체 타입인 `ProductRepository`를 직접 의존하고 있어, Factory의 생성 코드만 변경해서는 교체할 수 없다.
  - 다른 Repository 구현체를 주입하려면 `ProductsViewModel`의 생성자 타입도 함께 변경해야 한다.

## 1단계 · 생성자 주입(자동)

다음 문제점을 해결한다.

- ViewModel에서 참조하는 Repository가 정상적으로 주입되지 않는다.
- Repository를 참조하는 다른 객체가 생기면 주입 코드를 매번 만들어줘야 한다.
  - ViewModel에 수동으로 주입되고 있는 의존성들을 자동으로 주입되도록 바꿔본다.
  - 특정 ViewModel에서만이 아닌, 범용적으로 활용될 수 있는 자동 주입 로직을 작성한다. (ProductsViewModel, CartViewModel 모두 하나의 로직만 참조한다)
  - 100개의 ViewModel이 생긴다고 가정했을 때, 자동 주입 로직 100개가 생기는 것이 아니다. 하나의 자동 주입 로직을 재사용할 수 있어야 한다.
- 0.5단계처럼 화면 진입 지점마다 CartRepository를 직접 만들면, 상품 목록 화면과 장바구니 화면이 서로 다른 인스턴스를 갖게 된다. CartRepository는 담은 상품을 메모리에만 들고 있으므로, 상품을 장바구니에 담고 장바구니 화면으로 이동하면 목록이 비어 있다. 앱을 실행해 직접 확인해 보자.
  - 여러 번 인스턴스화할 필요 없는 객체는 최초 한 번만 인스턴스화한다. (이 단계에서는 너무 깊게 생각하지 말고 싱글 오브젝트로 구현해도 된다.)

### 구현할 기능 목록

- 생성자를 탐색해 의존성을 자동 주입하는 공용 `ViewModelProvider.Factory`를 만들고, 화면 간 `CartRepository` 인스턴스를 재사용한다. ViewModel 종류별 분기 없이 새 ViewModel에도 적용한다.
- 상품 목록 화면과 장바구니 화면의 수동 Factory를 공용 Factory로 교체한다.

### 완료 기준

- 상품을 장바구니에 담고 화면을 이동해도 같은 상품이 보이며, 삭제와 재진입도 정상 동작한다.
- 사전에 제공된 테스트가 모두 통과한다.
- DI 구현에 Annotation을 사용하지 않는다.
