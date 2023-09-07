package woowacourse.shopping.util.autoDI

import androidx.lifecycle.ViewModel
import woowacourse.shopping.util.autoDI.dependencyContainer.DependencyContainer

object AutoDI {
    operator fun invoke(init: AutoDI.() -> Unit) {
        this.init()
    }

    fun <T : Any> singleton(qualifier: String? = null, registerBlock: () -> T) {
        DependencyContainer.singletonRegister.register(qualifier, registerBlock)
    }

    fun <T : Any> disposable(qualifier: String? = null, registerBlock: () -> T) {
        DependencyContainer.disposableRegister.register(qualifier, registerBlock)
    }

    fun <VM : ViewModel> viewModel(registerBlock: () -> VM) {
        DependencyContainer.viewModelRegister.register(registerBlock)
    }

    inline fun <reified T : Any> inject(qualifier: String? = null): T =
        DependencyContainer.search(qualifier)
}
