package woowacourse.shopping.ui.util

import com.example.alsonglibrary2.di.AutoDIManager.createNoQualifierInstance
import com.example.alsonglibrary2.di.LibraryDependencyProvider
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import javax.inject.Qualifier

@Qualifier
annotation class SharedCartRepository

object DependencyProvider : LibraryDependencyProvider {
    @SharedCartRepository
    fun provideCartRepository(): CartRepository {
        return createNoQualifierInstance<CartRepositoryImpl>()
    }
}
