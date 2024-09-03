# android-di

## 구현 목록
### Step 1
- [ ] 수동 의존성 주입
  - [ ] ViewModel 생성 과정에서의 런타임 에러를 해결
    - [ ] ViewModel 별 ViewModelFactory
    - [ ] 화면 별 ViewModelFactory에 Repository 주입

- [ ] 자동 의존성 주입
  - [ ] Repository 추상화
  - [ ] 각 화면의 ViewModel 생성 시 범용적으로 사용될 수 있는 ViewModelFactory 생성 함수
  - [ ] 한 번만 생성되어야 할 객체들을 모아놓은 Module 객체
    - [ ] Application 객체 내에 Module을 두어 앱이 실행되는 동안 필요한 객체들을 전역적으로 한 번만 생성
