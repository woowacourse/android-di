# android-di

# 1단계
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

# 2단계
## 기능 요구사항
- [ ] 필드 주입
  - [ ] viewModel 내 필드 주입을 구현한다.

- [ ] Annotation을 이용해 다음 문제를 해결한다.
  - [ ] 필요한 요소에만 의존성 주입
  - [ ] 내가 만든 의존성 라이브러리가 제대로 작동하는지 테스트 코드 작성

- [ ] Recursive DI
  - [ ] 장바구니 저장을 mutableList가 아닌, dao 객체에 맡긴다
  - [ ] `CartProductEntity`의 `createdAt`프로퍼티를 이용해, 뷰에 날짜 정보를 추가한다.

## 선택 요구사항
- [ ] 장바구니의 삭제 버튼을 누르면, artRepository::deleteCartProduct의 id에 해당하는 상품이 지워진다.
- [ ] 뷰에서 CartProductEntity를 직접 참조하지 않는다.

## 프로그래밍 요구사항
- [ ] 사전에 주어진 테스트 코드가 모두 성공해야 한다.
