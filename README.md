# android-di

## 요구사항
- [x] Repository 객체를 교체해도, viewModel에는 변경사항이 없는 기능을 만든다.
  - [x] 이때, 특정 viewModel이 아닌, 범용적으로 쓰일 수 있는 자동 주입 로직을 만든다.
  - [x] viewModel 개수에 따라 로직이 늘어나거나 줄어들지 않는다.
  - [x] 매번 새로운 인스턴스가 생성되는 것이 아니라, 하나의 인스턴스를 재활용한다.

## 선택 요구사항
- [ ] TDD로 DI 구현
- [ ] Robolectric으로 기능 테스트
- [ ] ViewModel 테스트
- [ ] 모든 도메인 로직, Repository 단위 테스트
