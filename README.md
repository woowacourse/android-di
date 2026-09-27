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

## 2단계
---
- [ ] 재귀적으로 다른 객체들을 참조하도록 변경
  - [x] `CartRepository` 인터페이스화 
  - [x] `CartRepository`가 Dao를 참조하도록 변경
  - [x] `Product`에 식별자 추가
  - [x] Repository 에 suspend 추가
  - [x] 담은 시각을 표시한다. (`createAt`이 언제 담겼는지 들고 있다. 날짜 포맷팅은 `DateFormatter`가 담당한다.)
- [ ] 의존성 주입이 필요한 필드와 그렇지 않은 필드 구분
  - [ ] Annotation을 붙여서 필요한 요소에만 의존성 주입
  - [ ] 내가 만든 의존성 라이브러리가 제대로 작동하는지 테스트 코드 작성
- [ ] LazyColumn의 items에 key 지정하기


### 프로그래밍 요구사항
- [ ] 테스트 코드가 모두 통과한다
- [ ] 필드 주입 대상은 어노테이션으로 명시된 필드만 한다. 모든 필드를 훑어 주입하지 않는다
