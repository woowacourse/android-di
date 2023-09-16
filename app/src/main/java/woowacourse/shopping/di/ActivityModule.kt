package woowacourse.shopping.di

import android.content.Context
import com.boogiwoogi.di.Module
import com.boogiwoogi.di.Provides
import com.boogiwoogi.di.UsableOn
import woowacourse.shopping.ui.cart.DateFormatter
import kotlin.reflect.KClass
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure

@UsableOn(ActivityDiInjector::class)
@Module
class ActivityModule(private val context: Context) {

    init {
        if (context == context.applicationContext) throw IllegalArgumentException(CONTEXT_TYPE_ERROR)
    }

    @Provides
    fun provideDateFormatter(): DateFormatter {
        return DateFormatter(context)
    }

    @Provides
    fun provideActivityContext(): Context {
        return context
    }

    fun provideInstanceOf(clazz: KClass<*>): Any? {
        val functions = this::class
            .functions
            .filter { it.hasAnnotation<Provides>() }
            .firstOrNull { it.returnType.jvmErasure == clazz }

        return functions?.call(this)
    }

    companion object {
        private const val CONTEXT_TYPE_ERROR = "inappropriate context"
    }
}
