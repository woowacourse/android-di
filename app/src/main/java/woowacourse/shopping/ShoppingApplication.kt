package woowacourse.shopping

import com.di.berdi.DIApplication
import com.di.berdi.Injector
import woowacourse.shopping.di.NormalModule

class ShoppingApplication : DIApplication() {

    override fun inject() {
        Injector(container, applicationContext).inject(NormalModule)
    }
}
