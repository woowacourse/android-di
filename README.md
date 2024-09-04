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
