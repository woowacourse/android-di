package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class ViewModelFactory(
    private val diContainer: DiContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val constructor =
            modelClass.kotlin.primaryConstructor
                ?: throw IllegalArgumentException(ERROR_INVALID_PRIMARY_CONSTRUCTOR)

        val params =
            constructor.parameters.map { param ->
                diContainer.getInstance(param.type.jvmErasure)
            }
        val instance = constructor.call(*params.toTypedArray())
        injectFields(modelClass, instance)

        return instance
    }

    private fun <T : ViewModel> injectFields(
        modelClass: Class<T>,
        instance: T,
    ) {
        val fields = modelClass.fields.filter { it.annotations.contains(FieldInject()) }
        fields.forEach { field ->
            field.isAccessible = true
            val value = diContainer.getInstance(field.type.kotlin)
            field.set(instance, value)
        }
    }

    companion object {
        private const val ERROR_INVALID_PRIMARY_CONSTRUCTOR = "No primary constructor"
    }
}
