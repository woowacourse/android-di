package woowacourse.shopping

import com.di.berdi.Container
import com.di.berdi.DIApplication
import com.di.berdi.Injector
import woowacourse.shopping.di.NormalModule

class ShoppingApplication : DIApplication() {

    override fun inject() {
        val container = Container()
        injector = Injector(container, applicationContext).apply { inject(NormalModule) }
    }
}
