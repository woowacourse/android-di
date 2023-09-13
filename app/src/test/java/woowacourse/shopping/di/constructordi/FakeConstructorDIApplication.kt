package woowacourse.shopping.di.constructordi

import com.di.berdi.DIApplication
import com.di.berdi.Injector

class FakeConstructorDIApplication : DIApplication() {
    override fun inject() {
        Injector(container, applicationContext).inject(FakeConstructorDIModule)
    }
}
