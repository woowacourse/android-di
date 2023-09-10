package woowacourse.shopping.di.annotationdi

import woowacourse.shopping.di.DIApplication
import woowacourse.shopping.di.Injector

class FakeAnnotationDIApplication : DIApplication() {
    override fun inject() {
        Injector(container, applicationContext).inject(FakeAnnotationDIModule)
    }
}
