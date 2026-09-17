package woowacourse.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.data.AppContainer
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

inline fun <reified VM : ViewModel> viewModelFactory(): ViewModelProvider.Factory =
    viewModelFactory {
        initializer<VM> {
            val appContainer = (this[APPLICATION_KEY] as DiApplication).appContainer
            val constructor =
                VM::class.primaryConstructor
                    ?: error("[에러] 주 생성자를 찾을 수 없습니다.")

            val dependencies =
                constructor.parameters.map { parameter ->
                    val property =
                        AppContainer::class
                            .declaredMemberProperties
                            .firstOrNull { it.returnType == parameter.type }
                            ?: error("[에러] ${parameter.type} 타입의 의존성을 찾을 수 없습니다.")
                    property.isAccessible = true
                    property.call(appContainer)
                }
            constructor.call(*dependencies.toTypedArray())
        }
    }
