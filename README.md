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
    - [x] 의존성 라이브러리 테스트 코드를 작성
- [x] CartRepository가 DAO 객체를 참조하도록 변경
- [x] 뷰에서도 날짜 정보를 확인할 수 있도록 한다.
- [x] Qualifier 구현
- [x] DI 라이브러리를 모듈로 분리.
- [ ] DateFormatter 의존성 주입
- [ ] 생명주기에 맞게 주입
  - [ ] CartRepository는 앱 전체 LifeCycle 동안 유지
  - [ ] ProductRepository는 ViewModel LifeCycle 동안 유지
  - [ ] DateFormatter는 Activity LifeCycle 동안 유지
- [ ] DI 라이브러리가 작동하는지 테스트를 작성

### 선택 요구 사항
- [ ] TDD로 DI 구현
- [x] Robolectric으로 기능 테스트
- [ ] ViewModel 테스트
- [ ] 모든 도메인 로직, Repository 단위 테스트
- [x] Reflection 학습 테스트
- [x] 장바구니 아이템 삭제 기능
- [x] 뷰에서 CartProductEntity를 직접 참조하지 않게 변경
- [ ] DSL을 활용
- [ ] DI 라이브러리를 배포하고 적용