package woowacourse.shopping.di

import com.re4rk.arkdi.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER

@Retention(RUNTIME)
@Target(VALUE_PARAMETER, FUNCTION)
@Qualifier
annotation class ContextType(val type: Type) {
    enum class Type {
        APPLICATION,
        ACTIVITY,
    }
}
