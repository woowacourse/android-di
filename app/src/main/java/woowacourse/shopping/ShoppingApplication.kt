package woowacourse.shopping

import android.app.Application
import com.woowacourse.bunadi.cache.Cache
import com.woowacourse.bunadi.cache.DefaultCache
import com.woowacourse.bunadi.dsl.modules
import com.woowacourse.bunadi.injector.SingletonDependencyInjector
import woowacourse.shopping.ui.common.di.module.DaoModule

class ShoppingApplication : Application(), Cache by DefaultCache() {
    override fun onCreate() {
        super.onCreate()
        initDi()
    }

    private fun initDi() {
        SingletonDependencyInjector.cache = this
        modules {
            module(DaoModule(this@ShoppingApplication))
        }
    }
}
