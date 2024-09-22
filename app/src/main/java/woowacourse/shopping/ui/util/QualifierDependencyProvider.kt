package woowacourse.shopping.ui.util

import com.example.alsonglibrary2.di.AlsongQualifier
import com.example.alsonglibrary2.di.QualifierDependencyProvider
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartRepository

@AlsongQualifier
annotation class DefaultCartRepository

@AlsongQualifier
annotation class InMemoryCartRepository

object QualifierDependencyProvider :
    QualifierDependencyProvider {
    @DefaultCartRepository
    fun provideDefaultCartRepository(): CartRepository {
        return ShoppingApplication.defaultCartRepository
    }

    @InMemoryCartRepository
    fun provideInMemoryCartRepository(): CartRepository {
        return ShoppingApplication.inMemoryCartRepository
    }
}
