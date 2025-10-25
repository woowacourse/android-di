package woowacourse.peto.di

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.peto.di.annotation.Qualifier
import woowacourse.peto.di.annotation.Scope
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

class ViewModelFactoryInjector(
    private val dependencyContainer: DependencyContainer,
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

    private fun resolveConstructorParameters(constructor: KFunction<*>): Array<Any> {
        return constructor.parameters.map { param ->
            val qualifier = param.findAnnotation<Qualifier>()?.name
            qualifier?.let { dependencyContainer.get(param.type, it, Scope.VIEWMODEL) }
                ?: dependencyContainer.get(param.type, Scope.VIEWMODEL)
        }.toTypedArray()
    }

    private fun <T> createInstance(
        constructor: KFunction<T>,
        params: Array<Any>,
    ): T {
        val instance = constructor.call(*params)
        dependencyContainer.inject(instance as Any, Scope.VIEWMODEL)
        return instance
    }

    companion object {
        private const val ERROR_NO_CONSTRUCTOR = "%s 클래스에 기본 생성자가 존재하지 않습니다."
    }
}

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.specterViewModel(noinline extrasProducer: (() -> CreationExtras)? = null): Lazy<VM> {
    val factoryProducer: () -> ViewModelProvider.Factory = {
        ViewModelFactoryInjector(DependencyInjector.dependencyContainer)
    }

    return viewModels(
        extrasProducer = extrasProducer,
        factoryProducer = factoryProducer,
    )
}

@MainThread
inline fun <reified VM : ViewModel> Fragment.specterViewModel(noinline extrasProducer: (() -> CreationExtras)? = null): Lazy<VM> {
    val factoryProducer: () -> ViewModelProvider.Factory = {
        ViewModelFactoryInjector(DependencyInjector.dependencyContainer)
    }

    return viewModels(
        extrasProducer = extrasProducer,
        factoryProducer = factoryProducer,
    )
}
