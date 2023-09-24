# android-di

## 2단계

기능 요구 사항
필드 주입

- [x] 어노테이션을 필요한 요소에만 붙여서, 필드 주입을 구현한다.
- [x] 내가 만든 의존성 라이브러리가 제대로 작동하는지 테스트 코드를 작성한다.

재귀 DI

- [x] CartRepository가 DAO 객체를 참조하도록 변경한다.
- [x] CartProductViewHolder의 bind 함수를 수정해서, 장바구니에 상품이 담긴 날짜 정보를 확인 가능하도록 한다.

선택 요구 사항

- [x] 장바구니에서 항목을 클릭해서 삭제할 때, 리사이클러뷰의 포지션이 아닌, id를 기준으로 삭제한다.
- [x] 뷰에서 CartProductEntity를 직접 참조하지 않는다.

## 3단계

기능 요구 사항
Qualifier

- [x] 인터페이스 구현체가 DI 컨테이너 안에 여럿 등록된 경우, 어떤 의존성을 가져올지 알 수 없는 문제점을 해결한다
    - [x] 사용자가 Room DB를 주입 받을지, 메모리휘발성데이터를 주입받을지 결정할 수 있다.

모듈 분리

- [x] 내가 만든 DI 라이브러리를 모듈로 분리한다.

## 4단계

기능 요구 사항
- [x] CartRepository는 앱 전체 LifeCycle 동안 유지되도록 구현한다.
  - [x] ApplicationModule를 정의한다
- [x] ProductRepository는 ViewModel LifeCycle 동안 유지되도록 구현한다.
  - [ x ViewModelModule를 정의한다
- [x] DateFormatter는 Activity LifeCycle 동안 유지되도록 구현한다.
  - [x] ActivityModule를 정의한다

선택 요구 사항
- [x] DateFormatter가 Configuration Changes에도 살아남을 수 있도록 구현한다.
- [ ] Activity, ViewModel 외에도 다양한 컴포넌트(Fragment, Service 등)별 유지될 의존성을 관리한다.
