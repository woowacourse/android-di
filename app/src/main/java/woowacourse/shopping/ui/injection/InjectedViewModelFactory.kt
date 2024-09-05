package woowacourse.shopping.ui.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

inline fun <reified T : ViewModel> getInjectedViewModelFactory(): ViewModelProvider.Factory {
    val primaryConstructor =
        T::class.primaryConstructor
            ?: throw IllegalArgumentException("ViewModel class는 주 생성자를 가져야 합니다.")

    val repositories =
        primaryConstructor.parameters.filter { it.type.jvmErasure.isSubclassOf(RepositoryDI::class) }
            .map { property ->
                RepositoryModule.getInstance()
                    .getRepository(property.type.jvmErasure as KClass<out RepositoryDI>)
            }

    return viewModelFactory {
        addInitializer(T::class) {
            primaryConstructor.call(*repositories.toTypedArray())
        }
    }
}
