package woowacourse.shopping.fixture

import android.app.Application
import com.woowa.di.component.injectDI
import woowacourse.shopping.di.DaoBinder
import woowacourse.shopping.di.SingletonRepositoryBinder
import woowacourse.shopping.di.ViewModelRepositoryBinder

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        injectDI(this) {
            binder(ViewModelRepositoryBinder::class)
            binder(SingletonRepositoryBinder::class)
            binder(DaoBinder::class)
        }
    }
}
