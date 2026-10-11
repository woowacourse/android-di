package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.di.Container
import woowacourse.di.Scope
import woowacourse.di.ScopeType
import kotlin.reflect.KClass

internal const val VIEW_MODEL_SCOPE_CLOSEABLE_KEY = "woowacourse.shopping.di.viewModelScope"

object HunnitFactory : ViewModelProvider.Factory {
    private val container = Container()

    fun <T : Any> register(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) = container.register(type, instance, qualifier)

    fun <T : Any> bind(
        type: KClass<T>,
        implementation: KClass<out T>,
        qualifier: KClass<out Annotation>? = null,
        scope: ScopeType = ScopeType.ROOT,
    ) = container.bind(type, implementation, qualifier, scope)

    override fun <T : ViewModel> create(modelClass: Class<T>): T = createViewModel(modelClass, container.rootScope)

    fun viewModelFactory(parentScope: Scope): ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T = createViewModel(modelClass, parentScope)
        }

    fun screenScopeFactory(): ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                require(modelClass == ScreenScopeOwner::class.java) {
                    "${modelClass.simpleName}: ScreenScopeOwner만 생성할 수 있습니다."
                }
                return modelClass.cast(ScreenScopeOwner(container.openScope(ShoppingScopes.SCREEN)))
            }
        }

    fun <T : Any> createInstance(type: KClass<T>): T = container.create(type)

    fun getInstance(type: KClass<*>): Any = container.resolve(type)

    private fun <T : ViewModel> createViewModel(
        modelClass: Class<T>,
        parentScope: Scope,
    ): T {
        val viewModelScope = container.openScope(ShoppingScopes.VIEW_MODEL, parentScope)
        return try {
            viewModelScope.create(modelClass.kotlin).also {
                it.addCloseable(VIEW_MODEL_SCOPE_CLOSEABLE_KEY, viewModelScope)
            }
        } catch (exception: Throwable) {
            viewModelScope.close()
            throw exception
        }
    }
}
