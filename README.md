# android-di

## 0.5단계: 생성자 주입 - 수동

DI 컨테이너 없이 `ViewModel`이 필요한 의존성을 직접 전달한다.

`ProductsViewModel`은 다음 두 Repository를 생성자로 받는다.

```kotlin
class ProductsViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
) : ViewModel()
```

따라서 `ProductsScreen`에서 기본 `viewModel()` 대신
`ViewModelProvider.Factory`를 직접 전달한다.

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

이렇게 하면 `MainActivityTest`가 `MainActivity`를 실행할 때
`ProductsViewModel`을 생성할 수 있다.

### 확인할 문제

- 화면이 늘어날 때마다 Factory를 새로 작성해야 한다.
- Repository를 교체하려면 Factory의 생성 코드를 수정해야 한다.
- 화면마다 `CartRepository`를 새로 만들면 서로 다른 장바구니를 사용하게 된다.

다음 단계에서는 반복되는 객체 생성과 Repository 공유 문제를 줄이기 위해
의존성 생성 위치를 화면 밖으로 이동한다.
