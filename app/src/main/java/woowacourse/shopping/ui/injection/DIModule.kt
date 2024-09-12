package woowacourse.shopping.ui.injection

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class DIModule

interface Module<T : Any, type : Any> {
    fun getDIInstance(type: KClass<out type>): type
}
