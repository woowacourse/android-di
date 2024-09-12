package woowacourse.shopping.ui.injection

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class DIModule(val type: KClass<*>)

interface Module<T : Any, type : Any> {
    fun getDIInstance(type: KClass<out type>): type

    fun getInstance(): T

    fun getInstanceOrNull(): T?
}
