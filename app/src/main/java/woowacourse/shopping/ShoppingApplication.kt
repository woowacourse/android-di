package woowacourse.shopping

import woowacourse.shopping.di.DIApplication
import woowacourse.shopping.di.Injector
import woowacourse.shopping.di.NormalModule

class ShoppingApplication : DIApplication() {

    override fun inject() {
        Injector(container, applicationContext).inject(NormalModule)
    }
}
