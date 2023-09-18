package woowacourse.shopping.di.activity

import android.content.Context
import com.boogiwoogi.di.Modules
import com.boogiwoogi.di.Provides
import com.boogiwoogi.di.Qualifier
import woowacourse.shopping.di.ContextProvider
import woowacourse.shopping.ui.cart.DateFormatter
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure

class ActivityModules(override val context: Context) : ContextProvider, Modules {

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

    override fun provideInstanceOf(clazz: KClass<*>): Any? {
        val functions = this::class
            .functions
            .filter { it.hasAnnotation<Provides>() }
            .firstOrNull { it.returnType.jvmErasure == clazz }

        return functions?.call(this)
    }

    override fun provideInstanceOf(simpleName: String): Any? {
        val function = this::class
            .functions
            .filter { it.hasAnnotation<Qualifier>() }
            .firstOrNull { it.findAnnotation<Qualifier>()!!.simpleName == simpleName }

        return function?.call(this)
    }

    companion object {
        private const val CONTEXT_TYPE_ERROR = "inappropriate context"
    }
}
