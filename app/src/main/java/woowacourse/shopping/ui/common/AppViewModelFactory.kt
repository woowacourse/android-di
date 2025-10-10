package woowacourse.shopping.ui.common

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import woowacourse.shopping.di.AppInjector
import woowacourse.shopping.di.common.castKclassOrThrow
import woowacourse.shopping.di.common.getPrimaryConstructorOrThrow
import woowacourse.shopping.di.definition.Qualifier

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
        val constructor: KFunction<T> = modelClass.getPrimaryConstructorOrThrow()

        val arguments: Map<KParameter, Any> =
            constructor.parameters
                .mapNotNull { parameter: KParameter ->
                    val kclass: KClass<*> = parameter.castKclassOrThrow()
                    val value: Any? = resolveInjectValue(kclass, handle, parameter)
                    if (value != null) parameter to value else null
                }.toMap()

        return constructor.callBy(arguments)
    }

    private fun resolveInjectValue(
        kclass: KClass<*>,
        handle: SavedStateHandle,
        parameter: KParameter,
    ) = when {
        kclass == SavedStateHandle::class -> handle
        else -> resolveInjectedValue(kclass, parameter)
    }

    private fun resolveInjectedValue(
        kclass: KClass<*>,
        kParameter: KParameter,
    ): Any? {
        val qualifier: Qualifier? = qualifiers[kclass]
        if (AppInjector.hasDefinition(kclass, qualifier)) return AppInjector.get(kclass, qualifier)

        if (hasDefaultValue(kParameter)) return null

        throw IllegalArgumentException(ERROR_NO_DEFINITION_INFORMATION.format(kParameter.name))
    }

    private fun hasDefaultValue(kParameter: KParameter) = kParameter.isOptional

    companion object {
        private const val ERROR_NO_DEFINITION_INFORMATION = "주입된 정보가 존재하지 않습니다, parameterName = %s"
    }
}
