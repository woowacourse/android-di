# android-di

### 🚀 0.5단계 - 수동 DI
**문제점 1**  
테스트하기 어렵다.
  - **원인**
    - Repository가 추상화 되어있지 않음
  - **해결방안**
    - [x] Repository 추상화  

**문제점 2**  
Repository 객체를 교체하기 위해 또다른 객체를 만들어 바꿔줘야 한다. 즉, ViewModel에 직접적인 변경사항이 발생한다.
  - **원인**
    - CartRepository 가 메모리가 아닌 Room DB 의 데이터에 접근하게 되면 ViewModel에 직접적인 변경사항이 발생
  - **해결방안**
    - [ ] CartProductDao를 수동 주입
