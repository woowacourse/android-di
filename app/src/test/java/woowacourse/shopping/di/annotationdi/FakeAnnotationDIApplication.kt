package woowacourse.shopping.di.annotationdi

import com.di.berdi.DIApplication
import com.di.berdi.Injector

class FakeAnnotationDIApplication : DIApplication() {
    override fun inject() {
        Injector(container, applicationContext).inject(FakeAnnotationDIModule)
    }
}
