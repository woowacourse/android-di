package woowacourse.shopping.di.constructordi

import com.di.berdi.Container
import com.di.berdi.DIApplication
import com.di.berdi.Injector

class FakeConstructorDIApplication : DIApplication() {
    override fun inject() {
        val container = Container()
        injector = Injector(container, applicationContext).apply { inject(FakeConstructorDIModule) }
    }
}
