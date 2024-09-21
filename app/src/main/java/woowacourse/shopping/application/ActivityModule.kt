package woowacourse.shopping.application

import android.content.Context
import com.example.di.Module
import com.example.di.annotation.Qualifier
import com.example.di.annotation.QualifierType
import woowacourse.shopping.ui.cart.DateFormatter

class ActivityModule(private val context: Context) : Module {
    @Qualifier(QualifierType.ActivityContext)
    fun provideActivityContext(): Context {
        return context
    }

    fun provideDateFormatter(): DateFormatter {
        return DateFormatter(context)
    }
}
