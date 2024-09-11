# android-di

## 2단계 - Annotation

### 필드 주입

- [x] ViewModel 내 필드 주입을 구현한다.

### Annotation

다음 문제점을 해결한다.

- [x] 의존성 주입이 필요한 필드와 그렇지 않은 필드를 구분할 수 없다.
    - [x] Annotation을 붙여서 필요한 요소에만 의존성을 주입한다.
- [x] 내가 만든 의존성 라이브러리가 제대로 작동하는지 테스트 코드를 작성한다.

### Recursive DI

- [x] CartRepository가 다음과 같이 DAO 객체를 참조하도록 변경한다.

```kotlin
class DefaultCartRepository(
    private val dao: CartProductDao,
) : CartRepository {
    suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    suspend fun getAllCartProducts(): List<CartProductEntity> {
        return dao.getAll()
    }

    suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}

```

* CartProductEntity에는 createdAt 프로퍼티가 있어서 언제 장바구니에 상품이 담겼는지를 알 수 있다.

- [x] CartProductViewHolder의 bind 함수에 다음 구문을 추가하여 뷰에서도 날짜 정보를 확인할 수 있도록 한다.

```kotlin
fun bind(product: ...) {
    binding.item = product
    binding.tvCartProductCreatedAt.text = dateFormatter.formatDate(product.createdAt) // 추가됨
}

```

### 선택 요구 사항

- [x] 현재는 장바구니 아이템 삭제 버튼을 누르면 RecyclerView의 position에 해당하는 상품이 지워진다.
    - [x] 상품의 position과 CartRepository::deleteCartProduct의 id가 동일한 값임을 보장할 수 없다는 문제를 해결한다.
- [x] 뷰에서 CartProductEntity를 직접 참조하지 않는다.

## 3단계 - Qualifier

### Qualifier

다음 문제점을 해결한다.

- [ ] 하나의 인터페이스의 여러 구현체가 DI 컨테이너에 등록된 경우, 어떤 의존성을 가져와야 할지 알 수 없다.
- [ ] 상황에 따라 개발자가 Room DB 의존성을 주입받을지, In-Memory 의존성을 주입받을지 선택할 수 있다.

### 모듈 분리

- [ ] 내가 만든 DI 라이브러리를 모듈로 분리한다.

- ### 선택 요구 사항
- [ ] DSL을 활용한다.
- [ ] 내가 만든 DI 라이브러리를 배포하고 적용한다.

### Hilt 구현 예시

Hilt에 동일한 인터페이스의 다른 구현체를 제공하기 위해 @Qualifier를 사용한다.

```kotlin
@Qualifier
annotation class InMemoryLogger

@Qualifier
annotation class DatabaseLogger

@InstallIn(ApplicationComponent::class)
@Module
abstract class LoggingDatabaseModule {

    @DatabaseLogger
    @Singleton
    @Binds
    abstract fun bindDatabaseLogger(impl: LoggerLocalDataSource): LoggerDataSource
}

@InstallIn(ActivityComponent::class)
@Module
abstract class LoggingInMemoryModule {

    @InMemoryLogger
    @ActivityScoped
    @Binds
    abstract fun bindInMemoryLogger(impl: LoggerInMemoryDataSource): LoggerDataSource
}

```
