# android-di

---

# 기능 요구 사항
## 필드 주입
- [ ] ViewModel 내 필드 주입을 구현한다.

## Annotation
- [ ] 의존성 주입이 필요한 필드와 그렇지 않은 필드를 구분할 수 없다.
  - [ ] Annotation을 붙여서 필요한 요소에만 의존성을 주입한다.
  - [ ] 내가 만든 의존성 라이브러리가 제대로 작동하는지 테스트 코드를 작성한다.

## Recursive DI
- [ ] CartRepository가 다음과 같이 DAO 객체를 참조하도록 변경한다.
- [ ] CartProductViewHolder의 bind 함수에 다음 구문을 추가하여 뷰에서도 날짜 정보를 확인할 수 있도록 한다.


# 선택 요구 사항
- [ ] 현재는 장바구니 아이템 삭제 버튼을 누르면 RecyclerView의 position에 해당하는 상품이 지워진다.
  - [ ] 상품의 position과 CartRepository::deleteCartProduct의 id가 동일한 값임을 보장할 수 없다는 문제를 해결한다.
- [ ] 뷰에서 CartProductEntity를 직접 참조하지 않는다.

# 프로그래밍 요구 사항
- [ ] 사전에 주어진 테스트 코드가 모두 성공해야 한다.
