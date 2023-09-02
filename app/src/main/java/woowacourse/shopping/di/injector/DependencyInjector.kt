package woowacourse.shopping.di.injector

import woowacourse.shopping.di.util.validateHasPrimaryConstructor
import kotlin.reflect.KParameter
import kotlin.reflect.javaType

object ClassInjector {
    val dependencies = mutableMapOf<Class<*>, Any>()

    inline fun <reified T : Any> inject(instance: T) {
        dependencies[T::class.java] = instance
    }

    inline fun <reified T : Any> inject(): T {
        val primaryConstructor = validateHasPrimaryConstructor<T>()

//         TODO: 추후 요구사항에 따라 @Inject Annotation 포함 여부를 검증한다.
//         primaryConstructor.validateIncludingInjectAnnotation()

        val parameterValues = getParameterValues(primaryConstructor.parameters)
        return primaryConstructor.call(*parameterValues.toTypedArray())
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun getParameterValues(parameters: List<KParameter>): MutableList<Any> {
        val parameterTypes = parameters.map { it.type }
        val parameterValues = mutableListOf<Any>()

        parameterTypes.forEach { paramType ->
            val parameterType = paramType.javaType
            val parameterValue = dependencies[parameterType]

            requireNotNull(parameterValue) { "[ERROR] 주입할 의존성이 존재하지 않습니다." }
            parameterValues.add(parameterValue)
        }

        return parameterValues
    }
}

class ClassInjectorDsl {
    inline fun <reified T : Any> inject(instance: T) {
        ClassInjector.inject(instance)
    }
}

fun modules(block: ClassInjectorDsl.() -> Unit) {
    ClassInjectorDsl().apply(block)
}
