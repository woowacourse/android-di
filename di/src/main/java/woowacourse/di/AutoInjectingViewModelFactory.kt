package woowacourse.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import java.io.Closeable
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class AutoInjectingViewModelFactory(
    private val container: Container,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val kClass = modelClass.kotlin
        val constructor =
            kClass.primaryConstructor
                ?: error("No primary constructor for ${kClass.qualifiedName}")

        val viewModelKey =
            extras[ViewModelProvider.NewInstanceFactory.VIEW_MODEL_KEY]
                ?: modelClass.canonicalName
                ?: modelClass.simpleName
                ?: error("Unable to resolve key for ${kClass.qualifiedName}")
        val scopeContext = ScopeContext.viewModel(viewModelKey)

        val parameterValues =
            constructor.parameters
                .map { parameter ->
                    val kParameter =
                        parameter.type.classifier as? KClass<*>
                            ?: error("Unsupported parameter type: ${parameter.type}")

                    when (kParameter) {
                        SavedStateHandle::class -> extras.createSavedStateHandle()
                        else -> container.get(kParameter, scopeContext = scopeContext)
                    }
                }.toTypedArray()

        val viewModel = constructor.call(*parameterValues)
        FieldInjector.inject(viewModel, container, scopeContext)
        registerViewModelScope(viewModel, viewModelKey)

        return viewModel
    }

    private fun registerViewModelScope(
        viewModel: ViewModel,
        viewModelKey: Any,
    ) {
        val scopeCloseable: Closeable =
            ScopeCloseable {
                container.clearScope(ScopeType.VIEW_MODEL, viewModelKey)
            }
        try {
            when {
                setTagIfAbsentMethod != null ->
                    setTagIfAbsentMethod.invoke(viewModel, VIEW_MODEL_SCOPE_TAG, scopeCloseable)

                addCloseableMethod != null ->
                    addCloseableMethod.invoke(viewModel, scopeCloseable)

                else ->
                    throw IllegalStateException(
                        "ViewModel scope cleanup cannot be registered because no supported API is available.",
                    )
            }
        } catch (exception: ReflectiveOperationException) {
            throw IllegalStateException(
                "Failed to register ViewModel scope cleanup for ${viewModel::class.qualifiedName}",
                exception,
            )
        }
    }

    private class ScopeCloseable(
        private val onClose: () -> Unit,
    ) : Closeable {
        override fun close() {
            onClose()
        }
    }

    companion object {
        internal const val VIEW_MODEL_SCOPE_TAG: String = "woowacourse.di.viewModelScope"
        private val setTagIfAbsentMethod: Method? =
            resolveMethodOrNull("setTagIfAbsent", String::class.java, Any::class.java)
        private val addCloseableMethod: Method? =
            resolveMethodOrNull("addCloseable", Closeable::class.java)

        private fun resolveMethodOrNull(
            methodName: String,
            vararg parameterTypes: Class<*>,
        ): Method? =
            try {
                ViewModel::class.java
                    .getDeclaredMethod(methodName, *parameterTypes)
                    .apply { isAccessible = true }
            } catch (exception: NoSuchMethodException) {
                null
            }
    }
}
