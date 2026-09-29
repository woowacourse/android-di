# 0.5단계 - 생성자 주입 - 수동

## 기능 요구 사항

의도적으로 실패하는 테스트(MainActivityTest)를 수동 주입으로 통과시킨다. DI 컨테이너를 만들지 않는다. 컴포저블이 viewModel()을 호출하는 자리에 ViewModelProvider.Factory를 직접 넘겨, 그 화면이 필요로 하는 Repository를 손으로 만들어 넣어주면 된다.

```text
viewModel: ProductsViewModel = viewModel(factory = /* 여기서 직접 만들어 넣는다 */)
```

이렇게 만들어 두고 다음 문제점을 확인한다. 1단계는 여기서 출발한다.

- 화면이 늘어날 때마다 팩토리를 하나씩 새로 쓴다.
- Repository 객체를 교체하기 위해 또다른 객체를 만들어 바꿔줘야 한다. 즉, ViewModel에 직접적인 변경사항이 발생한다.

0.5단계는 1단계의 출발점이다. OT에서는 수동 DI 구현을 먼저 PR로 리뷰받는다. 진행 순서와 오늘의 리뷰 기준은 OT 문서를 따른다.

## 구현한 목록

### 1. viewModel을 수동으로 주입하여 테스트를 통과하도록 구현함
`MainActivityTest.kt` 코드는 MainActivity의 생명주기를 진행시키고 테스트가 잘 생성되는지를 검증하는 코드입니다.

`.setup()`은 onCreate -> onStart -> onResume의 단계를 진행시킵니다.

`.get()`은 Activity의 인스턴스를 반환하는 것입니다.

결과적으로 isNotNull()을 통해 MainActivity가 잘 생성되었는지 확인합니다.

테스트가 실패한 이유는 MainActivity안의 컴포저블 함수를 생성하는 과정에서 필요한 ViewModel의 의존성을 주입받지 못했기 때문입니다.

그래서 ViewModelProvider.Factory를 사용해 컴포저블 함수의 ViewModel()이 필요한 의존성을 전달하도한 목록 구현하였습니다.

이렇게 만든 ViewModel을 Screen컴포저블 함수에 주입함으로써 `MainActivityTest.kt`가 통과할 수 있었습니다.
