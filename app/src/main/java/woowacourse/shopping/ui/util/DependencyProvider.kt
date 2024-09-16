package woowacourse.shopping.ui.util

import com.example.alsonglibrary2.di.LibraryDependencyProvider
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartRepository

annotation class SharedCartRepository

object DependencyProvider : LibraryDependencyProvider {
    @SharedCartRepository
    fun provideCartRepository(): CartRepository {
        return ShoppingApplication.inMemoryCartRepository
    }
}
