# android-di

# 0.5단계 - 생성자 주입 - 수동

## 구현한 목록

### 1. viewModel을 수동으로 주입하여 테스트를 통과하도록 구현함
`MainActivityTest.kt` 코드는 MainActivity의 생명주기를 진행시키고 테스트가 잘 생성되는지를 검증하는 코드입니다.

`.setup()`은 onCreate -> onStart -> onResume의 단계를 진행시킵니다.

`.get()`은 Activity의 인스턴스를 반환하는 것입니다.

결과적으로 isNotNull()을 통해 MainActivity가 잘 생성되었는지 확인합니다.

테스트가 실패한 이유는 MainActivity안의 컴포저블 함수를 생성하는 과정에서 필요한 ViewModel의 의존성을 주입받지 못했기 때문입니다.

그래서 ViewModelProvider.Factory를 사용해 컴포저블 함수의 ViewModel()이 필요한 의존성을 전달하도한 목록 구현하였습니다.

이렇게 만든 ViewModel을 Screen컴포저블 함수에 주입함으로써 `MainActivityTest.kt`가 통과할 수 있었습니다.
