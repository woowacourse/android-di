package com.android.di.component

import com.android.di.annotation.Inject
import com.android.di.annotation.Qualifier

import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

fun List<Annotation>.hasQualifier(): Boolean =
    any { it.isQualifier() }

fun List<Annotation>.qualifierAnnotation(): KClass<out Annotation>? {
    return firstOrNull { it.isQualifier() }?.annotationClass
}

fun Annotation.isQualifier(): Boolean {
    return annotationClass.annotations.any { it.annotationClass == Qualifier::class }
}

object DiSingletonComponent {
    private val binds: MutableMap<KClass<*>, Any> = mutableMapOf()
    private val qualifierBinds: MutableMap<KClass<out Annotation>, Any> = mutableMapOf()

    private const val ERROR_DI_MATCH = "No binding Singleton Component %s"
    private const val ERROR_INSTANCE_MATCH = "No matching Instance %s"
    private const val ERROR_CONSTRUCTOR_MATCH = "No find Constructor %s"
    private const val ERROR_PARAM_MATCH = "No find parameter %s"
    private const val ERROR_QUALIFIER_MATCH = "No find Qualifier %s"

    fun <T : Any, I : T> bind(
        bindClassType: KClass<T>,
        implClassType: KClass<I>,
        logging: (String) -> Unit = {}
    ) {
        binds[bindClassType] = createInstance(implClassType, logging)
    }

    fun <T : Any> provide(
        bindClassType: KClass<out Annotation>,
        instance: T? = null
    ) {
        qualifierBinds[bindClassType] = instance ?: throw IllegalArgumentException(ERROR_QUALIFIER_MATCH.format(bindClassType))
    }

    fun <T : Any> match(bindClassType: KClass<T>): T {
        val instance = binds[bindClassType]
            ?: throw IllegalArgumentException(ERROR_DI_MATCH.format(bindClassType))
        return instance as? T
            ?: throw IllegalArgumentException(ERROR_INSTANCE_MATCH.format(bindClassType))
    }

    private fun <T : Any> matchByQualifier(bindClassType: KClass<out Annotation>): T {
        val instance = qualifierBinds[bindClassType]
            ?: throw IllegalArgumentException(ERROR_DI_MATCH.format(bindClassType))
        return instance as? T
            ?: throw IllegalArgumentException(ERROR_INSTANCE_MATCH.format(bindClassType))
    }

    private fun <T : Any> createInstance(
        clazz: KClass<T>,
        logging: (String) -> Unit
    ): T {
        val constructor = clazz.constructors.firstOrNull { it.hasAnnotation<Inject>() }
            ?: clazz.primaryConstructor
            ?: throw IllegalArgumentException(ERROR_CONSTRUCTOR_MATCH.format(clazz))

        val parameters = constructor.parameters.map { parameter ->
            val parameterType = parameter.type.classifier as? KClass<*>
                ?: throw IllegalArgumentException(ERROR_PARAM_MATCH.format(parameter))

            if (parameter.annotations.hasQualifier()) {
                val qualifier = parameter.annotations.qualifierAnnotation()
                    ?: throw IllegalArgumentException(ERROR_QUALIFIER_MATCH.format(parameter))
                logging("Qualifier Found: $qualifier")
                matchByQualifier(qualifier)
            } else {
                match(parameterType)
            }
        }

        return constructor.call(*parameters.toTypedArray())
    }
}
