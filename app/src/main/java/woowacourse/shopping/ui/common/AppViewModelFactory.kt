package woowacourse.shopping.ui.common

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import woowacourse.shopping.di.AppInjector
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
        val constructor: KFunction<T> =
            requireNotNull(modelClass.kotlin.primaryConstructor) {
                ERROR_NO_PRIMARY_CONSTRUCTOR.format(modelClass.name)
            }

        val arguments: Map<KParameter, Any> =
            constructor.parameters
                .mapNotNull { parameter: KParameter ->
                    val kclass: KClass<*> =
                        checkNotNull(parameter.type.classifier as? KClass<*>) {
                            ERROR_CAN_NOT_CONVERT_CLASS.format(parameter.name)
                        }

                    val value: Any? =
                        when {
                            kclass == SavedStateHandle::class -> handle
                            else -> getValueOrThrow(kclass, parameter)
                        }

                    if (value != null) parameter to value else null
                }.toMap()

        return constructor.callBy(arguments)
    }

    private fun getValueOrThrow(
        kclass: KClass<*>,
        kParameter: KParameter,
    ): Any? {
        if (kParameter.isOptional) return null

        val qualifier: Qualifier? = qualifiers[kclass]
        if (AppInjector.hasDefinition(kclass, qualifier)) return AppInjector.get(kclass, qualifier)

        throw IllegalArgumentException(ERROR_NO_DEFINITION_INFORMATION.format(kParameter.name))
    }

    companion object {
        private const val ERROR_NO_PRIMARY_CONSTRUCTOR = "ViewModel 주 생성자가 필요합니다, className = %s"
        private const val ERROR_CAN_NOT_CONVERT_CLASS =
            "parameter 클래스 타입으로 변환 실패, parameterName = %s"
        private const val ERROR_NO_DEFINITION_INFORMATION = "주입된 정보가 존재하지 않습니다, parameterName = %s"
    }
}
