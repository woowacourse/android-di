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
- [x] 필드 주입
  - [x] viewModel 내 필드 주입을 구현한다.

- [x] Annotation을 이용해 다음 문제를 해결한다.
  - [x] 필요한 요소에만 의존성 주입
  - [x] 내가 만든 의존성 라이브러리가 제대로 작동하는지 테스트 코드 작성

- [x] Recursive DI
  - [x] 장바구니 저장을 mutableList가 아닌, dao 객체에 맡긴다
  - [x] `CartProductEntity`의 `createdAt`프로퍼티를 이용해, 뷰에 날짜 정보를 추가한다.

## 선택 요구사항
- [ ] 장바구니의 삭제 버튼을 누르면, cartRepository::deleteCartProduct의 id에 해당하는 상품이 지워진다.
- [x] 뷰에서 CartProductEntity를 직접 참조하지 않는다.

## 프로그래밍 요구사항
- [x] 사전에 주어진 테스트 코드가 모두 성공해야 한다.

# 3단계
## 기능 요구사항

- [x] 하나의 인터페이스에 대해 여러 구현체가 있을 경우, Qualifier annotation을 통해 어떤 객체를 가져올지 구분한다.
  - [x] In-Memory 의존성을 받을지, RoomDB 의존성을 받을지 선택할 수 있다.

- [x] DI 라이브러리를 모듈로 분리한다

## 선택 요구사항
- [x] DSL을 활용한다
- [ ] 내가 만든 DI 라이브러리를 배포하고 적용한다.

# 4단계
## 기능 요구사항
- [ ] 싱글 오브젝트가 아니라, 생명주기에 맞춰 객체를 유지시킨다
  - [x] CartRepository: 앱 전체 LifeCycle 동안 유지
  - [x] ProductRepository: ViewModel LifeCycle 동안 유지
  - [ ] DateFormatter: Activity LifeCycle 동안 유지
- [ ] 내가 만든 라이브러리에 대한 테스트코드 작성

## 선택 요구사항
- [ ] DateFormatter가 Configuration Changes에도 살아남을 수 있도록 구현한다.
- [ ] Activity, ViewModel 외에도 다양한 컴포넌트(Fragment,Service 등)별 유지될 의존성을 관리한다.