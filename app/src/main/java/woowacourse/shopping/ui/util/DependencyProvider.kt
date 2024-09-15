package woowacourse.shopping.ui.util

import android.app.Application
import com.example.alsonglibrary2.di.AutoDIManager
import com.example.alsonglibrary2.di.AutoDIManager.createAutoDIInstance
import com.example.alsonglibrary2.di.LibraryDependencyProvider
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository

annotation class SharedCartRepository

object DependencyProvider : LibraryDependencyProvider {
    @SharedCartRepository
    fun provideCartRepository(): CartRepository {
        return ShoppingApplication().defaultCartRepository
    }
}
