package woowacourse.shopping.di.constructordi

import woowacourse.shopping.di.DIApplication
import woowacourse.shopping.di.Injector

class FakeConstructorDIApplication : DIApplication() {
    override fun inject() {
        Injector(container, applicationContext).inject(FakeConstructorDIModule)
    }
}
