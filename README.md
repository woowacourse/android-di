# android-di

---

# 기능 요구 사항
- [ ] `CartActivity`에서 사용하는 `DateFormatter`의 인스턴스를 매번 개발자가 관리해야 한다. 
- [ ] 모든 의존성이 싱글 오브젝트로 만들어질 필요 없다.
  - [ ] `CartRepository`는 앱 전체 LifeCycle 동안 유지되도록 구현한다.
  - [ ] `ProductRepository`는 ViewModel LifeCycle 동안 유지되도록 구현한다.
  - [ ] `DateFormatter`는 Activity LifeCycle 동안 유지되도록 구현한다.
- [ ] 내가 만든 DI 라이브러리가 잘 작동하는지 테스트를 작성한다.

# 선택 요구 사항
- [ ] `DateFormatter`가 Configuration Changes에도 살아남을 수 있도록 구현한다.
- [ ] Activity, ViewModel 외에도 다양한 컴포넌트(Fragment, Service 등)별 유지될 의존성을 관리한다.
