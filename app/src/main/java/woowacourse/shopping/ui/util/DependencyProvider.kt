package woowacourse.shopping.ui.util

import com.example.alsonglibrary2.di.AutoDIManager.createAutoDIInstance
import com.example.alsonglibrary2.di.LibraryDependencyProvider
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import javax.inject.Qualifier


annotation class SharedCartRepository

object DependencyProvider : LibraryDependencyProvider {
    @SharedCartRepository
    fun provideCartRepository(): CartRepository {
        return createAutoDIInstance<DefaultCartRepository>()
    }
}
