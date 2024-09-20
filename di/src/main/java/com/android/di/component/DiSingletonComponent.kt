package com.android.di.component

import com.android.di.annotation.Inject
import com.android.di.annotation.hasQualifier
import com.android.di.annotation.qualifierAnnotation
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

object DiSingletonComponent {
    private val binds: MutableMap<KClass<*>, Any> = mutableMapOf()
    private val qualifierBinds: MutableMap<KClass<out Annotation>, Any> = mutableMapOf()

    private const val ERROR_DI_MATCH = "No binding Singleton Component %s"
    private const val ERROR_INSTANCE_MATCH = "No matching Instance %s"
    private const val ERROR_CONSTRUCTOR_MATCH = "No find Constructor %s"
    private const val ERROR_PARAM_MATCH = "No find parameter %s"
    private const val ERROR_QUALIFIER_MATCH = "No find Qualifier %s"
    private const val ERROR_INJECT_MATCH = "No find inject %s"

    fun <T : Any, I : T> bind(
        bindClassType: KClass<T>,
        implClassType: KClass<I>,
    ) {
        binds[bindClassType] = createInstance(implClassType)
    }

    fun <T : Any> provide(
        bindClassType: KClass<out Annotation>,
        instance: T? = null
    ) {
        qualifierBinds[bindClassType] =
            instance ?: throw IllegalArgumentException(ERROR_QUALIFIER_MATCH.format(bindClassType))
    }

    fun <T : Any> match(bindClassType: KClass<T>): T {
        val instance = binds[bindClassType]
            ?: throw IllegalArgumentException(ERROR_DI_MATCH.format(bindClassType))
        return instance as? T
            ?: throw IllegalArgumentException(ERROR_INSTANCE_MATCH.format(bindClassType))
    }

    fun <T : Any> matchByQualifier(bindClassType: KClass<out Annotation>): T {
        val instance = qualifierBinds[bindClassType]
            ?: throw IllegalArgumentException(ERROR_DI_MATCH.format(bindClassType))
        return instance as? T
            ?: throw IllegalArgumentException(ERROR_INSTANCE_MATCH.format(bindClassType))
    }

    private fun <T : Any> createInstance(clazz: KClass<T>): T {

        val constructor = clazz.constructors.firstOrNull { it.hasAnnotation<Inject>() }
            ?: clazz.primaryConstructor
            ?: throw IllegalArgumentException(ERROR_CONSTRUCTOR_MATCH.format(clazz))

        val parameters = constructor.parameters.map { parameter ->
            if (!parameter.hasAnnotation<Inject>()) {
                throw IllegalArgumentException(ERROR_INJECT_MATCH.format(parameter.name))
            }

            val parameterType = parameter.type.classifier as? KClass<*>
                ?: throw IllegalArgumentException(ERROR_PARAM_MATCH.format(parameter))

            if (parameter.annotations.hasQualifier()) {
                val qualifier = parameter.annotations.qualifierAnnotation()
                    ?: throw IllegalArgumentException(ERROR_QUALIFIER_MATCH.format(parameter))
                matchByQualifier(qualifier)
            } else {
                match(parameterType)
            }
        }

        return constructor.call(*parameters.toTypedArray())
    }
}
