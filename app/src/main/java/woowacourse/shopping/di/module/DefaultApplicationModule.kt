package woowacourse.shopping.di.module

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository

class DefaultApplicationModule : ApplicationModule() {
    // 메소드의 매개변수로, 이 객체의 종속 항목을 모두 나열해야 한다.
    fun getCartRepository(): CartRepository {
        return getInstance { DefaultCartRepository() }
    }
}
