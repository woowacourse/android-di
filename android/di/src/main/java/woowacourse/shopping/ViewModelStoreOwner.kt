package woowacourse.shopping

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

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
                        val application: Application = checkNotNull(extras[APPLICATION_KEY])
                        val container: DependencyContainer = application as DependencyContainer
                        val viewModel = container.instance<VM>() as T
                        val fieldsToBeInjected: List<KProperty1<out T, *>> =
                            viewModel::class.memberProperties.filter { it.hasAnnotation<InjectedProperty>() }

                        fieldsToBeInjected.forEach { property: KProperty1<out T, *> ->
                            property.apply {
                                isAccessible = true
                                javaField?.set(
                                    viewModel,
                                    container.dependency(returnType, property.annotations),
                                )
                            }
                        }

                        return viewModel
                    }
                },
        )[VM::class.java]
    }
