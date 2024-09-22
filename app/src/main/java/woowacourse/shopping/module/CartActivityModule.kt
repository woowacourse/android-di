package woowacourse.shopping.module

import android.content.Context
import com.example.di.DIModule
import com.example.di.annotation.Qualifier
import com.example.di.annotation.QualifierType
import woowacourse.shopping.ui.cart.DateFormatter

class CartActivityModule(private val context: Context) : DIModule {
    @Qualifier(QualifierType.ActivityContext)
    fun provideActivityContext(): Context {
        return context
    }

    fun provideDateFormatter(): DateFormatter {
        return DateFormatter(context)
    }
}
