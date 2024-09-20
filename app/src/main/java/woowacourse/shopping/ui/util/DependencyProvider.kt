package woowacourse.shopping.ui.util

import com.example.alsonglibrary2.di.AlsongQualifier
import com.example.alsonglibrary2.di.LibraryDependencyProvider
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartRepository

@AlsongQualifier
annotation class DefaultCartRepository

@AlsongQualifier
annotation class InMemoryCartRepository

object DependencyProvider : LibraryDependencyProvider {
    @DefaultCartRepository
    fun provideDefaultCartRepository(): CartRepository {
        return ShoppingApplication.defaultCartRepository
    }

    @InMemoryCartRepository
    fun provideInMemoryCartRepository(): CartRepository {
        return ShoppingApplication.inMemoryCartRepository
    }
}
