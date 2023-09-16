package woowacourse.shopping.di

import com.boogiwoogi.di.WoogiProperty
import com.boogiwoogi.di.WoogiQualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class Instantiator(
    private val module: ActivityModule
) {

    private fun List<KParameter>.instantiateParameters(): Array<Any?> =
        map { it.instantiate() }.toTypedArray()

    private fun KParameter.instantiate() = when {
        hasAnnotation<WoogiQualifier>() -> findAnnotation<WoogiQualifier>()?.run {
            module.provideInstanceOf(clazz)
        } ?: throw NoSuchElementException()

        hasAnnotation<WoogiProperty>() -> module.provideInstanceOf(this.type.jvmErasure)
            ?: type.jvmErasure.instantiateRecursively()

        else -> {}
    }

    private fun <T> KProperty<T>.instantiate(): Any = when {
        hasAnnotation<WoogiQualifier>() -> findAnnotation<WoogiQualifier>()?.run {
            module.provideInstanceOf(clazz)
        } ?: throw NoSuchElementException()

        hasAnnotation<WoogiProperty>() -> module.provideInstanceOf(this.returnType.jvmErasure)
            ?: returnType.jvmErasure.instantiateRecursively()

        else -> {}
    }

    private fun KClass<*>.instantiateRecursively(): Any {
        val constructor = primaryConstructor ?: throw Throwable(NO_SUCH_CONSTRUCTOR)
        if (constructor.parameters.isEmpty()) return constructor.call()

        val arguments = constructor.parameters.instantiateParameters()

        return constructor.call(*arguments)
    }

    fun instantiateProperty(property: KMutableProperty<*>): Any {
        return property.instantiate()
    }

    companion object {

        private const val NO_SUCH_CONSTRUCTOR = "생성자 없음"
    }
}
