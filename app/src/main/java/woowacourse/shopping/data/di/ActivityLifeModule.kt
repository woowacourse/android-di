package woowacourse.shopping.data.di

import android.content.Context
import com.android.di.component.DiContainer
import com.android.di.component.Module
import woowacourse.shopping.ui.cart.DateFormatter

class ActivityLifeModule(private val context: Context) : Module {
    fun bindDateFormatter(diContainer: DiContainer){
        diContainer.bind(
            DateFormatter::class,
            DateFormatter(context)
        )
    }
}
