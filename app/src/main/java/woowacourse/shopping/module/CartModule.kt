package woowacourse.shopping.module

import android.content.Context
import com.example.di.DIModule
import com.example.di.annotation.LifeCycle
import com.example.di.annotation.LifeCycleScope
import com.example.di.annotation.Qualifier
import com.example.di.annotation.QualifierType
import woowacourse.shopping.ui.cart.DateFormatter

class CartModule(private val context: Context) : DIModule {
    @LifeCycle(LifeCycleScope.ACTIVITY)
    @Qualifier(QualifierType.ActivityContext)
    fun provideActivityContext(): Context {
        return context
    }

    @LifeCycle(LifeCycleScope.ACTIVITY)
    fun provideDateFormatter(): DateFormatter {
        return DateFormatter(context)
    }
}
