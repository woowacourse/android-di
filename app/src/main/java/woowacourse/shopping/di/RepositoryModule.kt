import com.yrsel.di.Module
import com.yrsel.di.annotation.Provides
import com.yrsel.di.annotation.Qualifier
import com.yrsel.di.annotation.SingletonScope
import com.yrsel.di.annotation.ViewModelScope
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Qualifier
annotation class InMemory

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Qualifier
annotation class LocalDatabase

class RepositoryModule : Module {
    @Provides
    @ViewModelScope
    fun provideProductRepository(): ProductRepository = ProductRepositoryImpl()

    @Provides
    @SingletonScope
    @LocalDatabase
    fun provideLocalCartRepository(
        @LocalDatabase dao: CartProductDao,
    ): CartRepository = CartRepositoryImpl(dao)

    @Provides
    @SingletonScope
    @InMemory
    fun provideInMemoryCartRepository(
        @InMemory dao: CartProductDao,
    ): CartRepository = CartRepositoryImpl(dao)
}
