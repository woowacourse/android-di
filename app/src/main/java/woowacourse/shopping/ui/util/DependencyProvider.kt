package woowacourse.shopping.ui.util

import com.example.alsonglibrary2.di.AutoDIManager.createNoQualifierInstance
import com.example.alsonglibrary2.di.LibraryDependencyProvider
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import javax.inject.Qualifier

@Qualifier
annotation class SharedCartRepository

/**
 * DIModule의 멤버 함수는 반드시 createNoQualifierInstance() 함수를 리턴해야 합니다.
 **/
object DependencyProvider : LibraryDependencyProvider {
    @SharedCartRepository
    fun provideCartRepository(): CartRepository {
        return createNoQualifierInstance<CartRepositoryImpl>()
    }
}
