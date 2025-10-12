# android-di
## 0.5단계 기능 요구 사항
- [x] `MainActivity` 테스트 통과
- [x] `MainViewModel`의 Repository에 대한 의존성 주입

## 1단계 기능 요구 사항
- [x] ViewModel의 Repository에 대한 의존성 자동 주입 로직 구현
- [x] Repository 인스턴스는 싱글톤으로 유지

## 2단계 기능 요구 사항
### 필드 주입
- [] ViewModel 내 필드 주입을 구현한다.

### Annotation
- [] 의존성 주입이 필요한 필드와 그렇지 않은 필드를 구분할 수 없다.
  - [] Annotation을 붙여서 필요한 요소에만 의존성을 주입한다.
  - [] 내가 만든 의존성 라이브러리가 제대로 작동하는지 테스트 코드를 작성한다.

### Recursive DI
- [] `CartRepository`가 다음과 같이 DAO 객체를 참조하도록 변경한다.
- [] `CartProductViewHolder`의 `bind` 함수에 다음 구문을 추가하여 뷰에서도 날짜 정보를 확인할 수 있도록 한다.
```kotlin
fun bind(product: ...) {
    binding.item = product
    binding.tvCartProductCreatedAt.text = dateFormatter.formatDate(product.createdAt) // 추가됨
}
```

### 선택 요구 사항
- [] 상품의 position과 `CartRepository::deleteCartProduct`의 id가 동일한 값임을 보장할 수 없다는 문제를 해결한다.
- [] 뷰에서 `CartProductEntity`를 직접 참조하지 않는다.

## 3단계 기능 요구 사항
### Qualifier
다음 문제점을 해결한다.
- [] 하나의 인터페이스의 여러 구현체가 DI 컨테이너에 등록된 경우, 어떤 의존성을 가져와야 할지 알 수 없다.
- [] 상황에 따라 개발자가 Room DB 의존성을 주입받을지, In-Memory 의존성을 주입받을지 선택할 수 있다.

### 모듈 분리
- [] 내가 만든 DI 라이브러리를 모듈로 분리한다.

### 선택 요구 사항
- [] DSL을 활용한다.
- [] 내가 만든 DI 라이브러리를 배포하고 적용한다.