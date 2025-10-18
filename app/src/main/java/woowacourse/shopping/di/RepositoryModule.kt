import com.yrsel.di.Module
import com.yrsel.di.annotation.Provides
import com.yrsel.di.annotation.Qualifier
import com.yrsel.di.annotation.Singleton
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
    @Singleton
    fun provideProductRepository(): ProductRepository = ProductRepositoryImpl()

    @Provides
    @Singleton
    @LocalDatabase
    fun provideLocalCartRepository(
        @LocalDatabase dao: CartProductDao,
    ): CartRepository = CartRepositoryImpl(dao)

    @Provides
    @Singleton
    @InMemory
    fun provideInMemoryCartRepository(
        @InMemory dao: CartProductDao,
    ): CartRepository = CartRepositoryImpl(dao)
}
