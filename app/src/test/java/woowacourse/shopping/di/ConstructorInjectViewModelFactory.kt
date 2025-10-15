package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.fixture.TestAppContainer
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.primaryConstructor

class ConstructorInjectViewModelFactory(
    private val container: TestAppContainer,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val kClass: KClass<T> = modelClass.kotlin
        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalArgumentException("${modelClass.simpleName}에는 기본 생성자가 없습니다.")

        val dependencies =
            constructor.parameters
                .map { param ->
                    val dependency = container.findDependency(param.type)
                    dependency
                        ?: if (param.isOptional) {
                            null
                        } else {
                            throw IllegalArgumentException("AppContainer에 ${param.name}(${param.type}) 타입의 의존성이 없습니다.")
                        }
                }.toTypedArray()

        return try {
            constructor.call(*dependencies)
        } catch (e: InvocationTargetException) {
            val cause = e.targetException ?: e
            if (cause is RuntimeException) throw cause
            throw IllegalArgumentException("ViewModel 생성 중 예외 발생: ${cause.message}", cause)
        }
    }

    private fun TestAppContainer.findDependency(type: KType): Any? =
        this::class
            .members
            .firstOrNull { it.returnType == type }
            ?.call(this)
}
