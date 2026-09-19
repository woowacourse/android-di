package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object KirbyDIFactory : ViewModelProvider.Factory {
    private val dependencyInstances = mutableMapOf<KClass<*>, Any>()

    override fun <T : ViewModel> create(modelClass: Class<T>): T = createInstance(modelClass.kotlin)

    private fun <T : Any> createInstance(type: KClass<T>): T {
        val constructor =
            type.primaryConstructor ?: throw IllegalArgumentException("주 생성자가 없습니다: $type")
        val arguments =
            constructor.parameters.map { parameter ->
                val dependencyType =
                    parameter.type.classifier as? KClass<*>
                        ?: throw IllegalArgumentException("의존성 타입을 확인할 수 없습니다: $parameter")
                resolveDependency(dependencyType)
            }
        return constructor.call(*arguments.toTypedArray())
    }

    private fun resolveDependency(type: KClass<*>): Any {
        dependencyInstances[type]?.let { return it }

        val instance = createInstance(type)
        dependencyInstances[type] = instance
        return instance
    }
}
