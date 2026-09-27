# 만들면서 배우는 DI 1단계

## 0.5단계

### 목적
실패하는 테스트(`MainActivityTest`)를 통과시키기 위해 `ProductsScreen`에 `ProductViewModel`을 수동으로 주입한다.

### 기능 목록

- [x] `ProductViewModel`에 ViewModel factory를 구현한다.
- [x] `ProductViewModel`의 팩토리를 생성하여 필요한 Repository를 주입하고 이를 `ProductsScreen`에 넘긴다.

### 문제점

1. 화면이 늘어날 때마다 팩토리를 하나씩 새로 쓴다.
2. Repository 객체를 교체하기 위해 또다른 객체를 만들어 바꿔줘야 한다. 즉, ViewModel에 직접적인 변경사항이 발생한다.

## 1단계

### 목적
하나의 자동 주입 로직을 통해 ViewModel에 의존성들을 자동으로 주입할 수 있도록 구현한다.

### 기능 목록

- [x] 특정 ViewModel에서만이 아닌, 범용적으로 활용될 수 있는 자동 주입 로직을 작성한다.
- [x] DIContainer에 인스턴스를 생성하고 보관하는 로직을 추가한다.

## 2단계

### 목적
Annotation을 붙여서 필요한 요소에만 의존성을 주입하는 방식으로 ViewModel 내 필드 주입을 구현한다. 

### 기능 목록 

- [x] `CartRepository`가 DAO 객체를 참조하도록 변경한다.
  - [x] `CartProduct`를 만든다.
  - [x] 엔티티에서 `CartProduct` 도메인으로 바꾸는 매퍼 함수를 추가한다.
  - [x] `CartContentTest`를 `CartProduct`를 사용하도록 수정한다. 
  - [x] `CartContentPreview`를 `CartProduct`를 사용하도록 수정한다.
  - [x] `deleteCartProduct`의 인자가 인덱스가 아닌 id를 받도록 수정한다. 
  - [x] `ProductsViewModel.addCartProduct`를 `viewModelScope.launch` 안으로 옮긴다. 
  - [x] `CartViewModel.getAllCartProducts` 결과를 조회한 후 `update { }` 람다에서 갱신하도록 구조를 변경한다.
  - [x] `CartViewModel.deleteCartProduct`를 `viewModelScope.launch` 안으로 옮긴다. 
  - [ ] 장바구니 화면에 담은 시각을 표시한다.
- [ ] ViewModel 내 필드 주입을 구현한다.
  - [x] DAO가 자동으로 생성되도록 변경한다. 
  - [x] ViewModel의 생성자 파라미터가 인터페이스를 받도록 변경한다. 
  - [ ] Annotation 학습 테스트를 학습한다.
  - [ ] 필드 주입 테스트를 작성한다. 
  - [ ] 재귀 주입을 구현한다.
  - [ ] 재귀 주입 테스트를 작성한다.
