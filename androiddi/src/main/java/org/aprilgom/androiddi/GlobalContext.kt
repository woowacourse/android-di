package org.aprilgom.androiddi

import androidx.lifecycle.ViewModel
import kotlin.reflect.KClass

object GlobalContext {
    private var diContainer: DIContainer? = null

    fun register(diContainer: DIContainer) {
        this.diContainer = diContainer
    }

    fun inject(
        instance: Any?,
        targetClazz: KClass<*>,
    ) = diContainer?.inject(instance, targetClazz)

    fun provide(
        name: String,
        clazz: KClass<*>,
    ) = diContainer?.provide(name, clazz)

    fun provide(clazz: KClass<*>) = diContainer?.provide(clazz)

    fun <VM : ViewModel> provideViewModelFactory(clazz: KClass<VM>): ViewModelFactory<VM> =
        diContainer?.provideViewModelFactory(clazz)
            ?: throw IllegalStateException("DIContainer is not initialized")
}
