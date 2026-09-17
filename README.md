# android-di

## 기능 목록

- 상품 목록을 조회하고 상품을 장바구니에 담는다.
- 장바구니에 담긴 상품을 조회하고 삭제한다.
- 두 ViewModel의 생성자 의존성을 하나의 Factory에서 주입한다.
- 두 화면이 같은 `CartRepository`를 사용해 장바구니 내용을 공유한다.

## 0.5단계: 생성자 주입 - 수동

0.5단계에서는 DI 컨테이너 없이 `ViewModel`이 필요한 의존성을 직접 전달했다.

`ProductsViewModel`은 다음 두 Repository를 생성자로 받는다.

```kotlin
class ProductsViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
) : ViewModel()
```

당시 `ProductsScreen`에서 기본 `viewModel()` 대신
`ViewModelProvider.Factory`를 직접 전달한 방식은 다음과 같다.
현재는 아래 1단계 방식으로 교체되었다.

```kotlin
viewModel(
    factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProductsViewModel(
                productRepository = ProductRepository(),
                cartRepository = CartRepository(),
            ) as T
        }
    },
)
```

이 방식으로 `MainActivityTest`가 `MainActivity`를 실행할 때
`ProductsViewModel`을 생성할 수 있었다.

### 확인할 문제

- 화면이 늘어날 때마다 Factory를 새로 작성해야 한다.
- Repository를 교체하려면 Factory의 생성 코드를 수정해야 한다.
- 화면마다 `CartRepository`를 새로 만들면 서로 다른 장바구니를 사용하게 된다.

## 1단계: 생성자 주입 - 자동

[`DIViewModelFactory`](app/src/main/java/woowacourse/shopping/DIViewModelFactory.kt)가
`ProductRepository`와 `CartRepository`를 한 번씩 생성해 타입별 Map에 보관한다.
ViewModel의 주 생성자 파라미터 타입으로 필요한 Repository를 찾아 주입하므로,
`ProductsViewModel`과 `CartViewModel`은 같은 Factory를 사용한다.

새 ViewModel이 기존 Repository를 사용하면 Factory를 새로 만들 필요가 없다.
새 Repository가 필요할 때만 Map에 등록한다. DI 어노테이션은 사용하지 않는다.

[`DIViewModelFactoryTest`](app/src/test/java/woowacourse/shopping/DIViewModelFactoryTest.kt)는
`ProductsViewModel`에서 담은 상품이 `CartViewModel`에서도 보이는지 확인한다.
