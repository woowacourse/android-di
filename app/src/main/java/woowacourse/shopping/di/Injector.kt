package woowacourse.shopping.di

import woowacourse.shopping.di.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class Injector(private val appContainer: AppContainer) {

    fun <T : Any> inject(clazz: KClass<T>): T {
        val primaryConstructor =
            clazz.primaryConstructor ?: throw IllegalArgumentException("주생성자 없음")

        val requiredParams: List<KParameter> =
            primaryConstructor.parameters.filter { it.hasAnnotation<Inject>() }

        val requiredArgs: Map<KParameter, Any> = getArgumentsMapping(requiredParams)

        val instance = primaryConstructor.callBy(requiredArgs)

        fieldInjection(clazz, instance)

        return instance
    }

    private fun <T : Any> fieldInjection(clazz: KClass<T>, target: T) {
        clazz.declaredMemberProperties.forEach { property ->
            if (property.hasAnnotation<Inject>()) {
                val injectValue = appContainer.getInstance(property.returnType)
                property.isAccessible = true
                (property as KMutableProperty<*>).setter.call(target, injectValue)
            }
        }
    }

    private fun getArgumentsMapping(parameters: List<KParameter>): Map<KParameter, Any> {
        return parameters.associateWith {
            appContainer.getInstance(it.type)
        }
    }
}
