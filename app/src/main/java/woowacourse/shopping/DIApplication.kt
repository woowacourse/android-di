package woowacourse.shopping

import android.app.Application
import com.woowa.di.component.injectDI
import woowacourse.shopping.di.DaoBinder
import woowacourse.shopping.di.DatabaseBinder
import woowacourse.shopping.di.DateFormatBinder
import woowacourse.shopping.di.SingletonRepositoryBinder
import woowacourse.shopping.di.ViewModelRepositoryBinder

class DIApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        injectDI(this) {
            binder(ViewModelRepositoryBinder::class)
            binder(SingletonRepositoryBinder::class)
            binder(DaoBinder::class)
            binder(DateFormatBinder::class)
            binder(DatabaseBinder::class)
        }
    }
}
