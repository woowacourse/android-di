package woowacourse.shopping.di.annotationdi

import com.di.berdi.Container
import com.di.berdi.DIApplication
import com.di.berdi.Injector

class FakeAnnotationDIApplication : DIApplication() {
    override fun inject() {
        val container = Container()
        injector = Injector(container, applicationContext).apply { inject(FakeAnnotationDIModule) }
    }
}
