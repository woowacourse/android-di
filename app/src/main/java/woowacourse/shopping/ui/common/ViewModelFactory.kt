package woowacourse.shopping.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor = modelClass::class.primaryConstructor
        requireNotNull(constructor) { "Unknown ViewModel Class $modelClass" }
        val params = constructor.parameters

        val instances = params.map {
            Container.getInstance(it.type.jvmErasure)
        }

        return constructor.call(*instances.toTypedArray()) as T
    }
}
