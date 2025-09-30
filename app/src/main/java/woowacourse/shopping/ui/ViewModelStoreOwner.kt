package woowacourse.shopping.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.di.AppContainer
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

fun <VM : ViewModel> ViewModelStoreOwner.viewModel(clazz: KClass<VM>): Lazy<VM> =
    lazy { ViewModelProvider(this, viewModelFactory(clazz))[clazz.java] }

private fun <VM : ViewModel> viewModelFactory(viewModelKClass: KClass<VM>): ViewModelProvider.Factory =
    object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras,
        ): T {
            val application: Application = checkNotNull(extras[APPLICATION_KEY])

            val primaryConstructor: KFunction<VM> =
                viewModelKClass.primaryConstructor
                    ?: error("${viewModelKClass.qualifiedName} doesn't have primary constructor")

            val appContainer: AppContainer = application as AppContainer

            val parameters: List<Any> =
                primaryConstructor.parameters.map { parameter: KParameter ->
                    appContainer.dependency(parameter.type)
                }

            return primaryConstructor.call(*parameters.toTypedArray()) as T
        }
    }
