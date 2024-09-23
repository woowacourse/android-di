
# Step 2 까지
- [x] Proto : 호출 할때마다 새로운 인스턴스를 생성한다.
- [x] Single : 한번 생성된 인스턴스를 계속 사용한다.

# Step4 기능 사항

- proto, single 말고 다른 Scope를 사용할 수 있도록 한다.
- Scope 는 클래스나 타입을 기반으로 하는 스코프이다.
- Scope 의 경우 Android 종속성이 없다.

- [ ] Android LifeCycle 동안 유지 될 수 있는 Scope 를 만든다.
  - [ ] Android onDestory() 가 호출 될 때, ScopeInstant 를 제거한다.


- AAC ViewModel 이 아닌 ViewModel 을 만든다
  - [ ] LifeCycleOwner 를 사용한다
  - [ ] Lifecycle State 가 onDestroy && isConfigurationChange 일때 ScopeInstant 를 제거한다.