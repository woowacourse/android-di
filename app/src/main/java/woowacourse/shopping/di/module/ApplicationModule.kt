package woowacourse.shopping.di.module

import woowacourse.shopping.annotation.Binds
import woowacourse.shopping.data.DatabaseCartRepository
import woowacourse.shopping.di.annotation.UnDisposableCart
import woowacourse.shopping.module.DIApplicationModule

class ApplicationModule : DIApplicationModule() {
    @Binds
    @UnDisposableCart
    private lateinit var bindCartRepository: DatabaseCartRepository
}
