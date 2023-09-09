package woowacourse.shopping.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class CommonViewModelFactory(private val appContainer: AppContainer) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val primaryConstructor: KFunction<T> = modelClass.kotlin.primaryConstructor
            ?: throw NullPointerException("주 생성자를 찾을 수 없습니다.")
        val propertiesForInject: List<Any> = getPropertiesForInject(primaryConstructor.parameters)

        return primaryConstructor.call(*propertiesForInject.toTypedArray())
    }

    private fun getPropertiesForInject(needParameters: List<KParameter>): List<Any> {
        return needParameters.map { needParameter ->
            val needType: KClass<*> = needParameter.type.jvmErasure
            appContainer.getInstance(needType)
                ?: throw NullPointerException("의존성 주입에 필요한 인스턴스를 찾을 수 없습니다.")
        }
    }
}
