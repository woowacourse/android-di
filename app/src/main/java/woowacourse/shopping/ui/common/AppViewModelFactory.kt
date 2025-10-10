package woowacourse.shopping.ui.common

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import woowacourse.shopping.di.AppInjector
import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.di.common.castKclassOrThrow
import woowacourse.shopping.di.definition.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class AppViewModelFactory(
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null,
    private val qualifiers: Map<KClass<*>, Qualifier> = emptyMap(),
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle,
    ): T {
        val constructor: KFunction<T>? = modelClass.kotlin.primaryConstructor

        var instance: T? = null
        if (constructor != null) {
            instance =
                constructorInjection(constructor, handle, constructor.hasAnnotation<Inject>())
        }
        return fieldInjection(modelClass.kotlin, instance)
    }

    private fun <T : ViewModel> constructorInjection(
        constructor: KFunction<T>,
        handle: SavedStateHandle,
        isConstructorInject: Boolean,
    ): T {
        val arguments: Map<KParameter, Any> =
            constructor.parameters
                .mapNotNull { parameter: KParameter ->
                    val kclass: KClass<*> = parameter.castKclassOrThrow()
                    val value: Any? =
                        resolveInjectValue(kclass, handle, parameter, isConstructorInject)
                    if (value != null) parameter to value else null
                }.toMap()
        return constructor.callBy(arguments)
    }

    private fun resolveInjectValue(
        kclass: KClass<*>,
        handle: SavedStateHandle,
        parameter: KParameter,
        isConstructorInject: Boolean,
    ) = when {
        kclass == SavedStateHandle::class -> handle
        isConstructorInject || parameter.hasAnnotation<Inject>() -> injectValue(kclass, parameter)
        else -> injectEnableDefaultValue(kclass, parameter)
    }

    private fun injectValue(
        kclass: KClass<*>,
        kParameter: KParameter,
    ): Any {
        val qualifier: Qualifier? = qualifiers[kclass]
        if (AppInjector.hasDefinition(kclass, qualifier)) return AppInjector.get(kclass, qualifier)

        throw IllegalArgumentException(ERROR_NO_DEFINITION_INFORMATION.format(kParameter.name))
    }

    private fun injectEnableDefaultValue(
        kclass: KClass<*>,
        parameter: KParameter,
    ): Any? =
        when {
            parameter.isOptional -> null
            parameter.type.isMarkedNullable -> null
            else -> throw IllegalArgumentException(
                ERROR_CANNOT_ASSIGN_PARAMETER.format(kclass.simpleName, parameter.name),
            )
        }

    private fun <T : ViewModel> fieldInjection(
        modelClass: KClass<T>,
        instance: T?,
    ): T {
        val newInstance: T = instance ?: modelClass.createInstance()

        modelClass.memberProperties
            .filterIsInstance<KMutableProperty1<T, Any?>>()
            .filter { it.isLateinit && it.hasAnnotation<Inject>() }
            .forEach { field ->
                val kclass: KClass<*> = field.returnType.classifier as? KClass<*> ?: return@forEach

                if (AppInjector.hasDefinition(kclass, qualifiers[kclass])) {
                    field.apply {
                        isAccessible = true
                        setter.call(newInstance, AppInjector.get(kclass, qualifiers[kclass]))
                    }
                }
            }

        return newInstance
    }

    companion object {
        private const val ERROR_NO_DEFINITION_INFORMATION = "주입된 정보가 존재하지 않습니다, parameterName = %s"
        private const val ERROR_CANNOT_ASSIGN_PARAMETER =
            "파라미터에 값을 할당할 수 없습니다. class = %s, parameterName = %s"
    }
}
