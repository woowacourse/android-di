# android-di

## 구현 목록
### Step 1
- [x] 수동 의존성 주입
  - [x] ViewModel 생성 과정에서의 런타임 에러를 해결
    - [x] ViewModel 별 ViewModelFactory
    - [x] 화면 별 ViewModelFactory에 Repository 주입

- [x] 자동 의존성 주입
  - [x] Repository 추상화
  - [x] 각 화면의 ViewModel 생성 시 범용적으로 사용될 수 있는 ViewModelFactory 생성 함수
  - [x] 자동 의존성 주입을 위한 Injector 클래스
  - [x] 의존성 주입을 위한 BaseActivity 추상 클래스
  - [x] 한 번만 생성되어야 할 객체들을 모아놓은 Module 객체
    - [x] Application 객체 내에 Module을 두어 앱이 실행되는 동안 필요한 객체들을 전역적으로 한 번만 생성

- 선택 요구 사항
  - ~~[ ] TDD로 DI 구현~~
  - [x] Robolectric으로 기능 테스트
  - [x] ViewModel 테스트
  - [ ] 모든 도메인 로직, Repository 단위 테스트 

### Step 2
- [x] ViewModel 내 필드 주입을 구현한다.
- [x] 의존성 주입이 필요한 필드와 그렇지 않은 필드를 구분한다.
  - [x] Annotation을 붙여서 필요한 요소에만 의존성을 주입한다.
- [x] Recursive DI
  - [x] CartRepository가 DAO 객체를 참조하도록 변경한다.
- [x] CartProduct 아이템에서 날짜 정보를 확인할 수 있도록 한다.
- [x] 테스트 코드를 작성한다.

- 선택 요구 사항
  - [x] 상품의 position과 CartRepository::deleteCartProduct의 id가 동일한 값임을 보장할 수 없다는 문제를 해결한다.
  - [x] 뷰에서 CartProductEntity를 직접 참조하지 않는다.

### Step 3
- [x] 하나의 인터페이스의 여러 구현체가 DI 컨테이너에 등록된 경우, 어떤 의존성을 가져와야 할지 판단한다.
  - [x] 상황에 따라 개발자가 Room DB 의존성을 주입받을지, In-Memory 의존성을 주입받을지 선택할 수 있다.
- [x] DI 라이브러리를 모듈로 분리한다.

- 선택 요구 사항
  - [x] DSL을 활용한다.
  - [x] DI 라이브러리를 배포하고 적용한다.
