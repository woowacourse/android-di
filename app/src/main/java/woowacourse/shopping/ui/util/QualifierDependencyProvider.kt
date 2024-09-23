package woowacourse.shopping.ui.util

import com.example.alsonglibrary2.di.AlsongQualifier
import com.example.alsonglibrary2.di.QualifierDependencyProvider
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartRepository

@AlsongQualifier
annotation class QualifiedCartRepository

object QualifierDependencyProvider :
    QualifierDependencyProvider {
    @QualifiedCartRepository
    fun provideInMemoryCartRepository(): CartRepository {
        return ShoppingApplication.defaultCartRepository
    }
}
