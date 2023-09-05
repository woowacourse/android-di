package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import woowacourse.shopping.di.service.DefaultService
import woowacourse.shopping.di.service.Service
import woowacourse.shopping.ui.util.ViewModelFactoryUtil
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

object Injector {
    private val services: MutableMap<KClass<*>, Service> = ConcurrentHashMap()
    lateinit var declarations: Map<KClass<*>, Declaration<Any>>

    fun <T : Any> getService(kClazz: KClass<out T>): Service {
        return services[kClazz] ?: error(SERVICE_NOT_FOUNT_ERROR_MESSAGE + "$kClazz")
    }

    fun loadModules(modules: List<Module>) {
        modules.forEach(::loadModule)
    }

    fun loadModule(module: Module) {
        declarations = module.declarationRegistry
        declarations.forEach { declaration ->
            val service = getServiceFromDeclaration(declaration.key, declaration.value)
            addService(service)
        }
    }

    private fun addService(service: Service) {
        services[service.type] = service
    }

    private fun <T : Any> getServiceFromDeclaration(
        type: KClass<*>,
        declaration: Declaration<T>,
    ): Service {
        val instance = declaration.invoke()
        return DefaultService.create(type, instance)
    }

    private const val SERVICE_NOT_FOUNT_ERROR_MESSAGE = "Unable to find definition of"
}

fun startDI(block: Injector.() -> Unit) = Injector.apply(block)

inline fun <reified T : Any> get(): T {
    val service = Injector.getService(T::class)
    return service.instance as T
}

inline fun <reified T : ViewModel> ViewModelStoreOwner.viewModel(): Lazy<T> = lazy {
    ViewModelProvider(this, ViewModelFactoryUtil.viewModelFactory<T>())[T::class.java]
}
