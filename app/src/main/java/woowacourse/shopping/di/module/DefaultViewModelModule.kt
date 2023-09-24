package woowacourse.shopping.di.module

import com.example.di.module.ActivityRetainedModule
import com.example.di.module.ViewModelModule
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository

class DefaultViewModelModule(activityRetainedModule: ActivityRetainedModule) :
    ViewModelModule(activityRetainedModule) {
    fun getProductRepository(): ProductRepository {
        return DefaultProductRepository()
    }
}
