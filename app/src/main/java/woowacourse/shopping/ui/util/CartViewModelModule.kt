package woowacourse.shopping.ui.util

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.FakeCartRepository
import com.example.alsonglibrary2.di.AutoDIManager.createNoQualifierInstance
import javax.inject.Qualifier

@Qualifier
annotation class SharedCartRepository

/**
 * DIModule의 멤버 함수는 반드시 createNoQualifierInstance() 함수를 리턴해야 합니다.
 **/
object DependencyProvider {
    @SharedCartRepository
    fun provideCartRepository(): CartRepository {
        return createNoQualifierInstance<FakeCartRepository>()
    }
}
