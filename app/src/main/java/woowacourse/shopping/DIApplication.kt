package woowacourse.shopping

import android.app.Application
import com.woowa.di.component.injectDI
import woowacourse.shopping.di.dao.DaoBinder
import woowacourse.shopping.di.repository.RepositoryBinder
import woowacourse.shopping.di.repository.RepositoryBinder2

class DIApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        injectDI(this) {
            binder(RepositoryBinder::class)
            binder(RepositoryBinder2::class)
            binder(DaoBinder::class)
        }
    }
}
