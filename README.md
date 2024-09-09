# android-di

### 기능 목록표
- [x] 테스트하기 어려운 문제 해결
- [x] ViewModel에서 참조하는 Repository를 정상적으로 주입
- [x] Repository를 참조하는 다른 객체가 생겨도 주입 코드를 매번 생성하지 않게 변경.
- [x] ViewModel에 수동 주입 의존성 -> 자동 주입
- [x] 특정 ViewModel에서만이 아닌, 범용적으로 활용될 수 있는 자동 주입
- [x] 여러 번 인스턴스화할 필요 없는 객체는 최초 한 번만 인스턴스화
- [x] ViewModel 내 필드 주입을 구현한다.
- [x] 의존성 주입이 필요한 필드와 그렇지 않은 필드를 구분
  - [x] Annotation을 붙여서 필요한 요소에만 의존성을 주입
  - [ ] 의존성 라이브러리 테스트 코드를 작성
- [ ] CartRepository가 DAO 객체를 참조하도록 변경
- [ ] 뷰에서도 날짜 정보를 확인할 수 있도록 한다.

### 선택 요구 사항
- [ ] TDD로 DI 구현
- [ ] Robolectric으로 기능 테스트
- [ ] ViewModel 테스트
- [ ] 모든 도메인 로직, Repository 단위 테스트
- [x] Reflection 학습 테스트
