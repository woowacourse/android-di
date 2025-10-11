package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.Factory
import woowacourse.shopping.di.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation
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
            val qualifier = param.findAnnotation<Qualifier>()?.name
            qualifier?.let { dependencyInjector.get(param.type, it) }
                ?: dependencyInjector.get(param.type)
        }.toTypedArray()

    private fun <T> createInstance(
        constructor: KFunction<T>,
        params: Array<Any>,
    ): T {
        val instance = constructor.call(*params)
        dependencyInjector.inject(instance as Any)
        return instance
    }

    companion object {
        private const val ERROR_NO_CONSTRUCTOR = "%s 클래스에 기본 생성자가 존재하지 않습니다."
    }
}
//
// @MainThread
// inline fun <reified VM : ViewModel> ComponentActivity.petoViewModels(
//    noinline extrasProducer: (() -> CreationExtras)? = null,
//    noinline factoryProducer: (() -> Factory)? = null
// ): Lazy<VM> {
//    val factoryProducer = {
//        (application as Container).
//            ?: throw IllegalStateException(
//                "Application 클래스가 Container 인터페이스를 구현해야 합니다."
//            )
//    }
//
//    ViewModelLazy(
//        viewModelClass = VM::class,
//    )
// }
//
// @MainThread
// public inline fun <reified VM : ViewModel> androidx.activity.ComponentActivity.viewModels(
//    noinline extrasProducer: (() -> CreationExtras)? = null,
//    noinline factoryProducer: (() -> Factory)? = null
// ): Lazy<VM> {
//    val factoryPromise = factoryProducer ?: { defaultViewModelProviderFactory }
//
//    return ViewModelLazy(
//        VM::class,
//        { viewModelStore },
//        factoryPromise,
//        { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras }
//    )
// }
