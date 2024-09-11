# 2단계 요구 사항

## 기능 요구 사항

### 필드 주입
- [ ] `ViewModel` 내 필드 주입을 구현한다.

### Annotation
다음 문제점을 해결한다.

- [ ] 의존성 주입이 필요한 필드와 그렇지 않은 필드를 구분할 수 없다.
  - [ ] `Annotation`을 붙여서 필요한 요소에만 의존성을 주입한다.
  - [ ] 내가 만든 의존성 라이브러리가 제대로 작동하는지 테스트 코드를 작성한다.

### Recursive DI
- [ ] `CartRepository`가 다음과 같이 DAO 객체를 참조하도록 변경한다.  
  `CartProductEntity`에는 `createdAt` 프로퍼티가 있어서 언제 장바구니에 상품이 담겼는지를 알 수 있다.
- [ ] `CartProductViewHolder`의 bind 함수에 다음 구문을 추가하여 뷰에서도 날짜 정보를 확인할 수 있도록 한다.  
  ```kotlin
    fun bind(product: ...) { 
        binding.item = product
        binding.tvCartProductCreatedAt.text = dateFormatter.formatDate(product.createdAt) // 추가됨
    }
  ```

## 선택 요구 사항
- 현재는 장바구니 아이템 삭제 버튼을 누르면 `RecyclerView`의 `position`에 해당하는 상품이 지워진다.  
  - [ ] 상품의 `position`과 `CartRepository::deleteCartProduct`의 `id`가 동일한 값임을 보장할 수 없다는 문제를 해결한다.
- [ ] 뷰에서 `CartProductEntity`를 직접 참조하지 않는다.

## 프로그래밍 요구 사항
- 사전에 주어진 테스트 코드가 모두 성공해야 한다.
