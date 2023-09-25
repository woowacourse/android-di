package woowacourse.shopping

import com.dygames.android_di.lifecycle.LifecycleWatcherApplication
import kotlin.reflect.typeOf

class CartApplication : LifecycleWatcherApplication(typeOf<CartApplication>()) {
    override fun onCreate() {
        initDependencies()
        super.onCreate()
    }

    private fun initDependencies() {
        CartDependencies()
    }
}
