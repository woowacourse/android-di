# android-di

## 기능 목록

- 상품 목록을 조회하고 상품을 장바구니에 담는다.
- 장바구니에 담긴 상품을 조회하고 삭제한다.
- 두 ViewModel의 의존성을 하나의 Factory에서 주입한다.
- 두 화면이 같은 `CartRepository`를 사용해 장바구니 내용을 공유한다.
- `@Inject`가 붙은 ViewModel 필드에만 의존성을 주입한다.
- Room에 장바구니 상품을 저장하고 식별자로 삭제한다.
- 장바구니에 상품을 담은 시각을 표시한다.

### 1단계 점검

- [x] ViewModel 생성자 타입을 읽어 필요한 Repository를 자동 생성하고, 두 화면에서 같은 인스턴스를 사용한다.
- [x] 새 구체 Repository를 추가해도 DI 등록을 변경하지 않는다.

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

1단계의 `DIViewModelFactory`는
ViewModel의 주 생성자 파라미터 타입을 읽고 필요한 의존성을 자동으로 생성한다.
의존성도 다른 객체를 생성자로 받으면 같은 방식으로 재귀적으로 생성한다.
생성한 객체는 타입별 Map에 보관하므로 `ProductsViewModel`과 `CartViewModel`은
같은 `CartRepository` 인스턴스를 사용한다.

새 구체 Repository를 추가해도 Factory에 등록할 필요가 없다. 다만 인터페이스는
생성할 수 없으며, 같은 인터페이스의 여러 구현체를 구분하는 기능도 없다.
1단계에서는 DI 어노테이션을 사용하지 않는다.

1단계에서는 `ProductsViewModel`과 `CartViewModel`이 같은 저장소를 사용하는지
테스트로 확인했다.

## 2단계: 필드 주입과 재귀적 의존성 생성

ViewModel을 만든 뒤 `@Inject`가 붙은 필드만 찾아 의존성을 주입한다.
인터페이스인 `CartRepository`는 `DefaultCartRepository`로 연결하고,
그 생성자에 필요한 `CartProductDao`까지 재귀적으로 해결한다. Room이 생성하는
[`AppDI`](app/src/main/java/woowacourse/shopping/di/AppDI.kt)는 `CreationExtras`의
Application으로 Room DAO를 최초 한 번 생성하고, 화면에서는
`AppDI.viewModelFactory`를 사용한다. `ReflectionViewModelFactoryTest`는
애노테이션이 없는 필드를 건드리지 않는지와
생성자 의존성을 재귀적으로 주입하는지 확인한다.

장바구니 화면은 Room 엔티티 대신 `CartProduct` 도메인 모델을 사용한다.
각 항목은 데이터베이스 식별자를 key와 삭제 인자로 사용하고, `createdAt`은
`DateFormatter`를 통해 화면에 표시한다.

## 3단계: Qualifier와 DI 모듈 분리

### 기능 목록

- [ ] Qualifier 애노테이션으로 같은 타입의 여러 구현체를 구분한다.
- [ ] 장바구니 저장소로 Room 구현체와 In-Memory 구현체 중 하나를 선택할 수 있다.
- [ ] 같은 타입의 구현체가 여러 개일 때 Qualifier가 없으면 명확한 오류를 낸다.
- [ ] Qualifier 해석과 누락 오류를 DI 모듈 테스트로 검증한다.
- [ ] DI 코드를 순수 JVM `:di` 모듈로 분리한다.
- [ ] `:di` 모듈이 쇼핑 앱의 도메인 타입과 Android 타입을 참조하지 않도록 한다.

### 설계 선택

Qualifier는 문자열 키 대신 메타 애노테이션으로 표현한다. 앱에서 `@RoomCart`와
`@InMemoryCart`처럼 의미가 드러나는 애노테이션을 선언할 수 있고, 문자열 오타 없이
컴파일 시점에 참조를 확인할 수 있기 때문이다. DI 모듈은 각 애노테이션의 구체적인
이름을 알지 않고 `@Qualifier`가 붙었는지만 확인한다.

`:di`는 순수 JVM 모듈로 만든다. 의존성 탐색과 생성에는 Kotlin Reflection만 필요하고,
Android의 `ViewModelProvider.Factory`는 앱 모듈에 남길 수 있기 때문이다. 따라서 DI
모듈의 `build.gradle.kts`에는 Android 플러그인과 쇼핑 앱 의존성이 들어가지 않는다.
4단계의 화면 스코프도 Android 객체를 DI 모듈에 전달하는 대신 앱에서 컨테이너의
생명주기를 관리하는 방식으로 확장한다.
