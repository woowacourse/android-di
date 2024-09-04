# android-di

### 🚀 0.5단계 - 수동 DI
> **문제점 1**  
테스트하기 어렵다.
  - **원인**
    - Repository가 추상화 되어있지 않음
  - **해결방안**
    - [x] Repository 추상화  

>**문제점 2**  
Repository 객체를 교체하기 위해 또다른 객체를 만들어 바꿔줘야 한다. 즉, ViewModel에 직접적인 변경사항이 발생한다.
  - **원인**
    - CartRepository 가 메모리가 아닌 Room DB 의 데이터에 접근하게 되면 ViewModel에 직접적인 변경사항이 발생
  - **해결방안**
    - [x] CartProductDao를 수동 주입  

<br>

### 🚀 1단계 - 자동 DI
>**문제점1**  
ViewModel에서 참조하는 Repository가 정상적으로 주입되지 않는다. 

  - **원인**
    - ViewModel 생성 시 ViewModel에서 참조하는 Repository를 주입하지 않음
  - **해결방안**
    - [x] ViewModelFactory 구현

>**문제점2**  
Repository를 참조하는 다른 객체가 생기면 주입 코드를 매번 만들어줘야 한다.

  - **해결방안**
    - [ ] ViewModel에 수동으로 주입되고 있는 의존성들을 자동으로 주입되도록 바꿔본다.  
    - [ ] 특정 ViewModel에서만이 아닌, 범용적으로 활용될 수 있는 자동 주입 로직을 작성한다. (MainViewModel, CartViewModel 모두 하나의 로직만 참조한다)
    - [ ] 100개의 ViewModel이 생긴다고 가정했을 때, 자동 주입 로직 100개가 생기는 것이 아니다. 하나의 자동 주입 로직을 재사용할 수 있어야 한다.

>**문제점3**  
장바구니에 접근할 때마다 매번 CartRepository 인스턴스를 새로 만들고 있다.

  - **해결방안**
    - [x] 여러 번 인스턴스화할 필요 없는 객체는 최초 한 번만 인스턴스화한다. (이 단계에서는 너무 깊게 생각하지 말고 싱글 오브젝트로 구현해도 된다.)
