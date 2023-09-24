package woowacourse.shopping.hasydi

import android.content.Context
import woowacourse.shopping.hasydi.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class Injector(private val diContainer: DiContainer) {

    private val activityRetainedModuleMap: MutableMap<KClass<*>, Module> = mutableMapOf()

    private val activityRetainedContainerMap: MutableMap<KClass<*>, DiContainer?> = mutableMapOf()

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
                val activityRetainedContainer = activityRetainedContainerMap[clazz]
                val injectValue =
                    diContainer.getInstance(property) ?: activityRetainedContainer?.getInstance(
                        property,
                    )
                property.isAccessible = true
                (property as KMutableProperty<*>).setter.call(target, injectValue)
            }
        }
    }

    fun addActivityRetainedModule(clazz: KClass<*>, module: Module) {
        activityRetainedModuleMap[clazz] = module
    }

    fun setActivityRetainedContainer(clazz: KClass<*>, context: Context) {
        val module = activityRetainedModuleMap[clazz]
        module?.let {
            module.context = context
            val container = DiContainer(module)
            activityRetainedContainerMap[clazz] = container
        }
    }

    fun removeActivityRetainedContainer(clazz: KClass<*>) {
        activityRetainedContainerMap[clazz] = null
    }

    fun hasContainer(clazz: KClass<*>): Boolean = activityRetainedContainerMap[clazz] != null

    private fun getArgumentsMapping(parameters: List<KParameter>): Map<KParameter, Any> {
        return parameters.associateWith { param ->
            diContainer.getInstance(param)
                ?: throw IllegalArgumentException("인스턴스 찾을 수 없음 $param")
        }
    }
}
