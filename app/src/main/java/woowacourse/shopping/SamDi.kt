package woowacourse.shopping

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.util.annotations.InjectField
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

object SamDi {
    fun resolve(
        modelClass: KClass<*>,
        context: Context
    ): Any {
        val app = context.applicationContext as MyApplication
        val container = app.appContainer // AppContainer 객체 들고오기

        val property = container::class.memberProperties.find {
            it.returnType.classifier == modelClass
        }
        if(property != null) {
            return property.call(container)!!
        }

        val implType = container.bindings[modelClass] ?: modelClass
        val constructor = implType.primaryConstructor!! // 생성자 확인
        val types = constructor.parameters.map { it.type.classifier as KClass<*> }

        val inst =
            types.map { type ->
                resolve(type, context)
            }

        return constructor.call(*inst.toTypedArray())
    }

    private fun injectFields(
        instance: ViewModel,
        context: Context,
    ) {
        instance::class.memberProperties
            .filter { it.findAnnotation<InjectField>() != null }
            .filterIsInstance<KMutableProperty1<*, *>>()
            .forEach { property ->
                val type = property.returnType.classifier as KClass<*>
                val value = resolve(type, context)
                property.setter.call(instance, value)
            }
    }

    fun viewModelFactory(
        context: Context
    ): ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val viewModel = resolve(modelClass.kotlin, context) as T
                injectFields(viewModel, context)
                return viewModel
            }
        }
}
