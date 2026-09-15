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

### 구현할 기능 목록

- 의도적으로 실패하는 `MainActivityTest`를 수동 주입으로 통과시킨다.
- DI 컨테이너를 만들지 않는다.
- 컴포저블에서 `viewModel()`을 호출할 때 `ViewModelProvider.Factory`를 직접 전달한다.
- 화면에 필요한 `Repository`를 직접 생성해 `ViewModel`의 생성자로 주입한다.
- 화면이 늘어날 때마다 Factory를 새로 작성해야 하는 문제를 확인한다.
- Repository 객체를 교체할 때 `ViewModel`을 직접 변경해야 하는 문제를 확인한다.
