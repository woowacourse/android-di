package woowacourse.shopping.di.component

import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.di.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

object DiSingletonComponent {
    private val binds: MutableMap<KClass<*>, Any> = mutableMapOf()
    private const val ERROR_DI_MATCH = "No binding Singleton Component %s"
    private const val ERROR_INSTANCE_MATCH = "No matching Instance %s"
    private const val ERROR_CONSTRUCTOR_MATCH = "No find Constructor %s"
    private const val ERROR_PARAM_MATCH = "No find parameter %s"

    fun <T : Any, I : T> bind(bindClassType: KClass<T>, implClassType: KClass<I>) {
        binds[bindClassType] = createInstance(implClassType)
    }

    fun <T : Any> provide(
        bindClassType: KClass<T>,
        instance: T? = null,
    ) {
        binds[bindClassType] = instance ?: createInstance(bindClassType)
    }

    fun <T : Any> match(bindClassType: KClass<T>): T {
        val instance = binds[bindClassType]
            ?: throw IllegalArgumentException(ERROR_DI_MATCH.format(bindClassType))
        return instance as? T
            ?: throw IllegalArgumentException(ERROR_INSTANCE_MATCH.format(bindClassType))
    }

    private fun <T : Any> createInstance(clazz: KClass<T>): T {
        val constructor = clazz.constructors.firstOrNull { it.hasAnnotation<Inject>() }
            ?: clazz.primaryConstructor
            ?: throw IllegalArgumentException(ERROR_CONSTRUCTOR_MATCH.format(clazz))

        val parameters = constructor.parameters.map { parameter ->
            val parameterType = parameter.type.classifier as? KClass<*>
                ?: throw IllegalArgumentException(ERROR_PARAM_MATCH.format(parameter))
            val qualifier = parameter.findAnnotation<Qualifier>()
            if (qualifier == null) {
                match(parameterType)
            } else {
                match(qualifier.annotationClass)
            }
        }

        return constructor.call(*parameters.toTypedArray<Any>())
    }
}
