# 🚀 0.5단계 - 수동 DI

`ProductsViewModel`을 생성할 때 필요한 객체를 직접 만들어 주입한다.

## 기능 목록

- [x] `ProductsScreen`의 `viewModel()`에 `ViewModelProvider.Factory`를 전달한다.
- [x] Factory에서 `ProductRepository`와 `CartRepository`를 직접 생성한다.
- [x] 생성한 Repository를 `ProductsViewModel`의 생성자에 전달한다.

# 🚀 1단계 - 자동 DI

ViewModel이 필요로 하는 객체를 자동으로 생성하고, 여러 화면에서 공유할 객체를 재사용한다.

## 기능 목록

- [x] 요청받은 ViewModel 타입의 생성자와 매개변수 타입을 확인해 인스턴스를 생성한다.
- [x] 생성자에 필요한 의존성을 재귀적으로 찾아 주입한다.
- [x] 이미 생성한 의존성을 다시 요청하면 같은 인스턴스를 반환한다.
- [x] `ProductsViewModel`과 `CartViewModel`이 같은 자동 주입 로직을 사용한다.
- [x] ViewModel을 추가해도 해당 ViewModel만을 위한 주입 로직을 새로 작성하지 않는다.

## 프로그래밍 요구 사항

- [x] 사전에 제공된 테스트가 모두 통과한다.
- [x] 의존성 주입에 어노테이션을 사용하지 않는다.

# 🚀 2단계 - Annotation

애노테이션으로 주입 대상을 구분하고, 장바구니 데이터까지 이어지는 의존성을 재귀적으로 해결한다.

## 기능 목록

- [x] ViewModel을 생성한 뒤 애노테이션이 붙은 필드에만 의존성을 주입한다.
- [x] 필드 주입과 재귀적인 의존성 주입을 테스트한다.
- [x] `CartRepository`가 `CartProductDao`를 주입받아 장바구니 데이터를 저장하고 조회하고 삭제한다.
- [x] `CartProduct` 도메인 모델과 엔티티를 도메인 모델로 변환하는 `toDomain()` 매퍼를 추가한다.
- [x] ViewModel에서 Repository의 `suspend` 함수를 `viewModelScope` 안에서 호출한다.
- [x] 장바구니 항목을 인덱스 대신 식별자로 삭제한다.
- [x] 장바구니 화면에 상품명과 `DateFormatter`로 변환한 담은 시각을 표시한다.

## 선택 요구 사항

- [x] 장바구니 목록의 `LazyColumn` 항목에 `key`를 지정하고 차이를 확인한다.
- [x] UI 계층에서 `CartProductEntity`를 직접 참조하지 않는다.

## 프로그래밍 요구 사항

- [x] 사전에 제공된 테스트가 모두 통과한다.
- [x] 애노테이션이 붙지 않은 필드에는 의존성을 주입하지 않는다.
