package woowacourse.shopping.hashdi

import woowacourse.shopping.hashdi.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class Injector(private val appContainer: AppContainer) {

    private val activityContainerMap: MutableMap<String, AppContainer?> = mutableMapOf()

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

    fun <T : Any> fieldInjection(clazz: KClass<out T>, target: T) {
        clazz.declaredMemberProperties.forEach { property ->
            if (property.hasAnnotation<Inject>()) {
                val activityContainer = activityContainerMap[clazz.simpleName.toString()]
                val injectValue =
                    appContainer.getInstance(property) ?: activityContainer?.getInstance(property)
                property.isAccessible = true
                (property as KMutableProperty<*>).setter.call(target, injectValue)
            }
        }
    }

    fun setActivityContainer(tag: String, container: AppContainer) {
        activityContainerMap[tag] = container
    }

    fun removeActivityContainer(tag: String) {
        activityContainerMap[tag] = null
    }

    fun hasContainer(tag: String): Boolean = activityContainerMap[tag] != null

    private fun getArgumentsMapping(parameters: List<KParameter>): Map<KParameter, Any> {
        return parameters.associateWith { param ->
            appContainer.getInstance(param)
                ?: throw IllegalArgumentException("인스턴스 찾을 수 없음 $param")
        }
    }
}
