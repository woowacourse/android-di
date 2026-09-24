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

---

# 3단계 - Qualifier

## 기능 요구 사항

### Qualifier
- [x] 상황에 따라 개발자가 Room DB 의존성을 주입받을지, In-Memory 의존성을 주입받을지 선택할 수 있다.
- [x] 내가 만든 DI 라이브러리가 Qualifier를 제대로 해석하는지 테스트를 작성한다. Qualifier가 없을 때 예외가 나는 것까지 검증한다.

### 모듈 분리
- [x] 내가 만든 DI 라이브러리를 모듈로 분리한다.

## 선택 요구 사항
- [ ] DSL을 활용한다.
- [ ] 내가 만든 DI 라이브러리를 배포하고 적용한다.

## 프로그래밍 요구 사항
- [ ] 같은 타입의 구현체가 둘 등록되어 있고 Qualifier가 없으면 명확한 오류를 낸다. 임의로 하나를 고르지 않는다.
- [ ] 분리한 DI 모듈은 쇼핑 앱의 도메인 타입(CartRepository, Product 등)을 알지 못해야 한다.
- [ ] Qualifier를 무엇으로 표현했는지(애노테이션 / 문자열 키)와 :di 모듈을 안드로이드 모듈·순수 JVM 모듈 중 무엇으로 만들었는지, 그 선택의 근거를 페어와 정해 README.md에 남긴다. 모듈 형태의 선택은 4단계의 화면 스코프 구현에 영향을 준다.

## 리뷰 체크리스트
- [ ] Room DB 구현체와 In-Memory 구현체를 개발자가 선택해서 주입받을 수 있다.
- [ ] Qualifier 없이 모호한 상황이 되면 명확한 예외 메시지가 나온다.
- [ ] DI 라이브러리가 별도 모듈로 분리되어 있다.
- [ ] :di 모듈이 앱의 도메인 타입에 의존하지 않는다.
- [ ] Qualifier 동작에 대한 테스트가 있다.
- [ ] Qualifier 표현 방식과 :di 모듈 형태(안드로이드 / 순수 JVM)의 선택 근거가 README.md에 있다.
