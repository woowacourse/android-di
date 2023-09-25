## 기능 목록
### Module
- [x] Injector가 AppContainer에서 inject할 인스턴스를 가져오는 구조에서 사용자가 정의한 Module에서 정의된 방식으로 인스턴스를 가져오는 방식으로 변경
  - [x] Module에 정의된 생성 방식을 가져온다.
  - [x] 정의된 생성 방식이 있다면 해당 방식을 토대로 인스턴스를 생성한다.
    - [x] 구현체를 정의하는 경우에는 Binds 어노테이션을 붙여 정의한다.
    - [x] 생성 함수를 정의하는 경우에는 Provides 어노테이션을 붙여 정의한다.
  - [x] 정의된 생성 방식이 없다면 주생성자로 인스턴스를 생성한다.
  - [x] 자식 모듈에 생성 방식이 정의되지 않은 경우 부모 모듈에서 탐색하여 생성한다.
  - [x] Singleton 어노테이션을 가진 경우에는 모듈에 정의된 단일 인스턴스를 가져올 수 있다.
  - [x] Application과 Activity context를 구분하여 주입받을 수 있다.

### Android DI
- [x] DIApplication
  - [x] 사용자가 정의한 DIModule 클래스를 관리한다.
    - ApplicationModule, ActivityRetainedModule, ActivityModule, ViewModelModule
- [ ] DIActivity
  - [x] ActivityRetainedModule, ActivityModule 객체를 관리한다.
  - [x] ActivityContext를 ActivityModule에 저장한다.
  - [x] ActivityModule을 이용하여 Activity 필드 주입을 진행한다.
  - [ ] Activity가 isFinishing이 아니라면 ActivityRetainedModule을 savedInstanceState에 저장한다.
- [ ] DIViewModelLazy
  - [ ] ViewModelModule 객체를 생성하여 해당 객체로 ViewModel을 주입한다.
