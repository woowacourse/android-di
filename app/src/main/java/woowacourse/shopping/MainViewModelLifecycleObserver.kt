package woowacourse.shopping

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.alsonglibrary2.di.AutoDIManager
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository

object MainViewModelLifecycleObserver : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        val defaultProductRepository =
            AutoDIManager.createAutoDIInstance<DefaultProductRepository>()
        AutoDIManager.registerDependency<ProductRepository>(defaultProductRepository)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        AutoDIManager.removeDependency<ProductRepository>()
    }
}
