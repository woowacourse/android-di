package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

class ViewModelFactoryInjector(
    private val dependencyInjector: DependencyInjector,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val kClass: KClass<T> = modelClass.kotlin
        val constructor = getValidPrimaryConstructor(kClass, kClass.primaryConstructor)
        val params = resolveConstructorParameters(constructor)
        return createInstance(constructor, params)
    }

    private fun <T> getValidPrimaryConstructor(
        kClass: KClass<*>,
        primaryConstructor: KFunction<T>?,
    ): KFunction<T> =
        requireNotNull(primaryConstructor) {
            ERROR_NO_CONSTRUCTOR.format(kClass.simpleName)
        }

    private fun resolveConstructorParameters(constructor: KFunction<*>): Array<Any> =
        constructor.parameters.map { param ->
            dependencyInjector.get(param.type)
        }.toTypedArray()

    private fun <T> createInstance(
        constructor: KFunction<T>,
        params: Array<Any>,
    ): T = constructor.call(*params)

    companion object {
        private const val ERROR_NO_CONSTRUCTOR = "%s 클래스에 기본 생성자가 존재하지 않습니다."
    }
}
