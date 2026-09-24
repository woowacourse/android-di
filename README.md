# android-di

# 2단계 - Annotation

## 과제 진행 요구 사항
- [x] 기능을 구현하기 전 README.md에 구현할 기능 목록을 정리해 추가한다.
- [x] Git의 커밋 단위는 앞 단계에서 README.md에 정리한 기능 목록 단위로 추가한다.
  - AngularJS Git Commit Message Conventions을 참고해 커밋 메시지를 작성한다.

## 기능 요구 사항
### 필드 주입
- [x] ViewModel 내 필드 주입을 구현한다.

### Annotation
- [x] Annotation을 붙여서 필요한 요소에만 의존성을 주입한다.
- [x] 내가 만든 의존성 라이브러리가 제대로 작동하는지 테스트 코드를 작성한다.
  
### Recursive DI
- [x] CartRepository가 다음과 같이 DAO 객체를 참조하도록 변경한다.
    - [x] CartProduct 도메인 모델을 만든다.
    - [x] 장바구니 상태를 List<CartProduct>로 변경한다.
    - [x] data/mapper에 CartProductMapper를 만든다.
- [x] 장바구니 화면에 담은 시각을 표시한다. 날짜 포맷팅은 DateFormatter가 담당한다.

### 선택 요구 사항
- [x] LazyColumn의 items에 key를 지정한다. 지정 전후에 어떤 차이가 생기는지 관찰한다.
- [x] UI 계층에서 CartProductEntity를 직접 참조하지 않는다.

## 프로그래밍 요구 사항
- [x] 사전에 주어진 테스트 코드가 모두 성공해야 한다.
- [x] 필드 주입 대상은 애노테이션으로 명시된 필드만이어야 한다. 모든 필드를 훑어 주입하지 않는다.

## 리뷰 체크리스트
- [x] 장바구니 목록에 상품명과 담은 시각이 함께 표시된다.
- [x] 애노테이션이 붙은 필드만 주입되고, 붙지 않은 필드는 주입되지 않는다.
- [x] CartRepository가 CartProductDao를 주입받는다. 즉 의존성이 재귀적으로 해결된다.
- [x] CartProduct 도메인 모델과 toDomain() 매퍼가 추가되어 있다.
- [x] ViewModel이 CartRepository의 suspend 함수를 viewModelScope 안에서 호출한다.
- [x] 목록 중간 항목을 삭제해도 의도한 상품이 지워진다.
- [x] 필드 주입과 재귀 주입에 대한 테스트가 있다.
- [x] 사전 제공 테스트가 모두 통과한다.
