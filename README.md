# android-di
## 0.5단계
---
- [x] `MainActivityTest`를 수동으로 주입시켜 테스트를 통과시킨다.
- [x] DI 컨테이너를 만들지 않는다
- [x] `viewModel()`을 호출하는 자리에 `ViewModelProvider.Factory`를 직접 넘겨서 repository를 넘겨준다.

## 1단계
---
### 기능 요구 사항
- [x] 생성자 주입을 통해 자동으로 DI를 한다
  - [x] ViewModel에 수동으로 주입되고 있는 의존성을 자동으로 주입하도록 한다
  - [x] 특정 ViewModel에서만이 아닌, 범용적으로 활용될 수 있는 자동 주입 로직을 작성한다.
  - [x] `ProductsViewModel`, `CartViewModel` 모두 하나의 로직을 참조하는지 확인한다

## 체크리스트
---
- [x] 사전에 주어진 테스트 코드가 모두 성공해야 한다.
- [x] Annotation을 지금 활용하지 않는다.