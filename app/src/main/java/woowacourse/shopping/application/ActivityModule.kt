package woowacourse.shopping.application

import android.content.Context
import com.example.di.DIModule
import com.example.di.annotation.Qualifier
import com.example.di.annotation.QualifierType
import woowacourse.shopping.ui.cart.DateFormatter

class ActivityModule(private val context: Context) : DIModule {
    @Qualifier(QualifierType.ActivityContext)
    fun provideActivityContext(): Context {
        return context
    }

    fun provideDateFormatter(): DateFormatter {
        return DateFormatter(context)
    }
}
