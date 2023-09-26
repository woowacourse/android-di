package woowacourse.shopping.di.module

import android.content.Context
import com.bignerdranch.android.koala.DiModule
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.repository.ProductRepository

class ViewModelDiModule : DiModule {

    override var context: Context? = null

    fun getProductRepository(): ProductRepository {
        return DefaultProductRepository()
    }
}
