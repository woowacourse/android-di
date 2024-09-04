# android-di

### 기능 목록표
- [x] 테스트하기 어려운 문제 해결
- [x] ViewModel에서 참조하는 Repository를 정상적으로 주입
- [ ] Repository를 참조하는 다른 객체가 생겨도 주입 코드를 매번 생성하지 않게 변경.
  - [ ] ViewModel에 수동 주입 의존성 -> 자동 주입
  - [ ] 특정 ViewModel에서만이 아닌, 범용적으로 활용될 수 있는 자동 주입
- [x] 여러 번 인스턴스화할 필요 없는 객체는 최초 한 번만 인스턴스화

### 선택 요구 사항
- [ ] TDD로 DI 구현
- [ ] Robolectric으로 기능 테스트
- [ ] ViewModel 테스트
- [ ] 모든 도메인 로직, Repository 단위 테스트
- [x] Reflection 학습 테스트
