package woowacourse.shopping.di

import androidx.activity.ComponentActivity
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class DIViewModelFactory(
    private val container: AppContainer,
) : ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(
        modelClass: Class<VM>,
        extras: CreationExtras,
    ): VM {
        val kClass = modelClass.kotlin
        val constructor =
            requireNotNull(kClass.primaryConstructor) {
                "${kClass.qualifiedName} 클래스에 생성자가 존재하지 않습니다."
            }

        val args =
            constructor.parameters
                .map { param ->
                    when (param.type.classifier) {
                        SavedStateHandle::class -> extras.createSavedStateHandle()
                        else -> container.getInstance(param.type.classifier as KClass<*>)
                    }
                }.toTypedArray()

        return constructor.call(*args)
    }
}

inline fun <reified VM : ViewModel> ComponentActivity.injectViewModel(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null,
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: { DIViewModelFactory(DefaultAppContainer) }

    return ViewModelLazy(
        viewModelClass = VM::class,
        storeProducer = { viewModelStore },
        factoryProducer = factoryPromise,
        extrasProducer = { extrasProducer?.invoke() ?: defaultViewModelCreationExtras },
    )
}
