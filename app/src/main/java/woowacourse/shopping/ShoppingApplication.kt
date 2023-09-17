package woowacourse.shopping

import com.di.berdi.Container
import com.di.berdi.DIApplication
import com.di.berdi.Injector
import woowacourse.shopping.di.NormalModule

class ShoppingApplication : DIApplication() {

    override fun inject() {
        injector = Injector(Container(), applicationContext).apply { injectBy(NormalModule) }
    }
}
