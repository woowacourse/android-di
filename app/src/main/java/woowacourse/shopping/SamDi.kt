package woowacourse.shopping

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.util.annotations.InjectField
import woowacourse.shopping.util.annotations.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

object SamDi {
    fun resolve(
        modelClass: KClass<*>,
        context: Context,
        annotation: Annotation? = null,
        ): Any {
        val app = context.applicationContext as MyApplication
        val container = app.appContainer // AppContainer 객체 들고오기

        val property = container::class.memberProperties.find {
            it.returnType.classifier == modelClass
        }
        if(property != null) {
            return property.call(container)!!
        }

        lateinit var implType: KClass<*>
        if(annotation != null) {
            implType = container.bindings[Pair(modelClass, annotation.annotationClass)]!!
        } else {
            val candidates = container.bindings.filterKeys { (type, _) -> type == modelClass }
            when (candidates.size) {
                0 -> implType = modelClass
                1 -> implType = candidates.values.single()
                else -> throw IllegalArgumentException(
                    "$modelClass 에 대해 여러 후보지가 있습니다. qualifier를 명확히 하세요",
                )
            }
        }

        val constructor = implType.primaryConstructor ?: throw IllegalArgumentException("요청 타입: $modelClass 가 없음") // 생성자 확인
        val inst = constructor.parameters.map { parameter ->
            val type = parameter.type.classifier as KClass<*>
            val qualifier = parameter.annotations.find { annotation ->
                annotation.annotationClass.hasAnnotation<Qualifier>()
            }
            resolve(type, context, qualifier)
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
