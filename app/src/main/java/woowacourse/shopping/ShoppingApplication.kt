package woowacourse.shopping

import android.app.Application
import com.woowacourse.bunadi.cache.Cache
import com.woowacourse.bunadi.cache.SingletonCache
import com.woowacourse.bunadi.dsl.modules
import woowacourse.shopping.ui.common.di.module.DaoModule

class ShoppingApplication : Application(), Cache by SingletonCache() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        modules {
            module(DaoModule(this@ShoppingApplication))
        }
    }

    companion object {
        lateinit var instance: Cache
    }
}
