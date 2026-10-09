package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.di.DiContainer
import woowacourse.di.ScopeContext
import woowacourse.di.ScopeType
import java.util.UUID
import kotlin.reflect.KClass

/**
 * 생성자 주입은 객체를 만들 때, 필드 주입은 이미 만든 ViewModel을 반환하기 전에 수행한다.
 * 인터페이스의 구현 규칙과 실제 객체의 보관은 별개다. 규칙만으로 DAO 같은 객체를 만들 수는 없다.
 */
object AoDi : ViewModelProvider.Factory {
    private val container = DiContainer()

    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras,
    ): T {
        val scopeId = "view-model:${modelClass.qualifiedName}:${UUID.randomUUID()}"
        val scopeContext = ShoppingScopes.viewModelContext(scopeId)
        container.openScope(scopeId)
        container.registerScopeRule(modelClass, ShoppingScopes.viewModel)

        return try {
            val viewModel = container.instantiate(modelClass, scopeContext = scopeContext)
            viewModel.addCloseable(
                VIEW_MODEL_SCOPE_CLOSEABLE_KEY,
                ScopeCloseable(scopeId, container::closeScope),
            )
            // lateinit 필드를 사용하기 전에 주입을 마쳐야 한다. 필드 주입에는 이 순서 제약이 있다.
            container.inject(viewModel, scopeContext)
            viewModel
        } catch (error: Throwable) {
            container.closeScope(scopeId)
            throw error
        }
    }

    internal fun openApplicationScope() {
        container.openScope(ShoppingScopes.APPLICATION_SCOPE_ID)
    }

    internal fun screenScopeOwnerFactory(scopeId: String): ViewModelProvider.Factory =
        viewModelFactory {
            initializer {
                container.openScope(scopeId)
                ScreenScopeOwnerViewModel(ScopeCloseable(scopeId, container::closeScope))
            }
        }

    internal fun <T : Any> instantiateInScreen(
        type: KClass<T>,
        scopeId: String,
    ): T = container.instantiate(type, scopeContext = ShoppingScopes.screenContext(scopeId))

    fun <T : Any> instantiate(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T = container.instantiate(type, qualifier)

    internal fun <T : Any> instantiate(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        scopeContext: ScopeContext,
    ): T = container.instantiate(type, qualifier, scopeContext)

    fun <T : Any> register(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) {
        container.register(type, instance, qualifier)
    }

    internal fun <T : Any> register(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
        scopeContext: ScopeContext,
    ) {
        container.register(type, instance, qualifier, scopeContext)
    }

    fun <T : Any, I : T> registerInterfaceRule(
        type: KClass<T>,
        implType: KClass<I>,
        qualifier: KClass<out Annotation>? = null,
    ) {
        container.registerInterfaceRule(type, implType, qualifier)
    }

    internal fun <T : Any> registerScopeRule(
        type: KClass<T>,
        scopeType: ScopeType,
        qualifier: KClass<out Annotation>? = null,
    ) {
        container.registerScopeRule(type, scopeType, qualifier)
    }

    fun inject(target: Any) = container.inject(target)

    private const val VIEW_MODEL_SCOPE_CLOSEABLE_KEY = "ao-di:view-model-scope"
}
