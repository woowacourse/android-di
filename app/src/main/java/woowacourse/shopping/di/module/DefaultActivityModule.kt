package woowacourse.shopping.di.module

import android.content.Context
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository

class DefaultActivityModule(activityContext: Context, applicationModule: ApplicationModule) :
    ActivityModule(activityContext, applicationModule) {
    // 메소드의 매개변수로, 이 객체의 종속 항목을 모두 나열해야 한다.
    fun getProductRepository(): ProductRepository {
        return getOrCreateInstance { DefaultProductRepository() }
    }
}
