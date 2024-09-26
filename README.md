# step1
## 기능 요구(해결) 사항
- [x] ViewModel에서 참조하는 Repository가 정상적으로 주입되지 않는다.
- [x] Repository를 참조하는 다른 객체가 생기면 주입 코드를 매번 만들어줘야 한다.
    - [x] ViewModel에 수동으로 주입되고 있는 의존성들을 자동으로 주입되도록 바꿔본다.
    - [x] 특정 ViewModel에서만이 아닌, 범용적으로 활용될 수 있는 자동 주입 로직을 작성한다. (MainViewModel, CartViewModel 모두 하나의 로직만 참조한다)
    - [x] 100개의 ViewModel이 생긴다고 가정했을 때, 자동 주입 로직 100개가 생기는 것이 아니다. 하나의 자동 주입 로직을 재사용할 수 있어야 한다.
- [x] 장바구니에 접근할 때마다 매번 CartRepository 인스턴스를 새로 만들고 있다.
    - [x] 여러 번 인스턴스화할 필요 없는 객체는 최초 한 번만 인스턴스화한다. (이 단계에서는 너무 깊게 생각하지 말고 싱글 오브젝트로 구현해도 된다.)

## 프로그래밍 요구 사항
- [x] 사전에 주어진 테스트 코드가 모두 성공해야 한다.
- [x] Annotation은 이 단계에서 활용하지 않는다.


# step2
## 기능 요구 사항
- [x] ViewModel 내 필드 주입을 구현한다. 
다음 문제점을 해결한다.
   - [x] 의존성 주입이 필요한 필드와 그렇지 않은 필드를 구분할 수 없다.  
   - [x] Annotation을 붙여서 필요한 요소에만 의존성을 주입한다.
   - [x] 내가 만든 의존성 라이브러리가 제대로 작동하는지 테스트 코드를 작성한다.
- [x] CartRepository가 DAO 객체를 참조하도록 변경한다.
- [x] CartProductViewHolder의 bind 함수를 수정하여 뷰에서도 날짜 정보를 확인할 수 있도록 한다.
## 프로그래밍 요구 사항
- [x] 사전에 주어진 테스트 코드가 모두 성공해야 한다.

## 선택 요구 사항
- [x] 상품의 position과 CartRepository::deleteCartProduct의 id가 동일한 값임을 보장할 수 없다는 문제를 해결한다.
- [x] 뷰에서 CartProductEntity를 직접 참조하지 않는다.


# step3
## 기능 요구 사항
- [x] 상황에 따라 개발자가 Room DB 의존성을 주입받을지, In-Memory 의존성을 주입받을지 선택할 수 있다.
- [x] 내가 만든 DI 라이브러리를 모듈로 분리한다.


# step4
## 기능 요구 사항
다음 문제점을 해결한다.
 - [x] CartActivity에서 사용하는 DateFormatter의 인스턴스를 매번 개발자가 관리해야 한다.
모든 의존성이 싱글 오브젝트로 만들어질 필요 없다.
 - [x] CartRepository는 앱 전체 LifeCycle 동안 유지되도록 구현한다.
 - [x] ProductRepository는 ViewModel LifeCycle 동안 유지되도록 구현한다.
 - [x] DateFormatter는 Activity LifeCycle 동안 유지되도록 구현한다.
   
- [x] 내가 만든 DI 라이브러리가 잘 작동하는지 테스트를 작성한다.