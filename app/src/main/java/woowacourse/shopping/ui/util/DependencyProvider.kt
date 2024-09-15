package woowacourse.shopping.ui.util

import com.example.alsonglibrary2.di.AutoDIManager.createAutoDIInstance
import com.example.alsonglibrary2.di.LibraryDependencyProvider
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository

annotation class SharedCartRepository

object DependencyProvider : LibraryDependencyProvider {
    @SharedCartRepository
    fun provideCartRepository(): CartRepository {
        return createAutoDIInstance<DefaultCartRepository>()
    }
}
