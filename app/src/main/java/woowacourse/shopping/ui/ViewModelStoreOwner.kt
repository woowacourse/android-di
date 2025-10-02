package woowacourse.shopping.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.di.AppContainer
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

inline fun <reified VM : ViewModel> ViewModelStoreOwner.viewModel(): Lazy<VM> =
    lazy {
        ViewModelProvider(
            owner = this,
            factory =
                object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(
                        modelClass: Class<T>,
                        extras: CreationExtras,
                    ): T {
                        val primaryConstructor: KFunction<VM> =
                            VM::class.primaryConstructor
                                ?: error("${VM::class.qualifiedName} doesn't have primary constructor")

                        val application: Application = checkNotNull(extras[APPLICATION_KEY])
                        val appContainer: AppContainer = application as AppContainer

                        val parameters: List<Any> =
                            primaryConstructor.parameters.map { parameter: KParameter ->
                                appContainer.dependency(parameter.type)
                            }

                        return primaryConstructor.call(*parameters.toTypedArray()) as T
                    }
                },
        )[VM::class.java]
    }
