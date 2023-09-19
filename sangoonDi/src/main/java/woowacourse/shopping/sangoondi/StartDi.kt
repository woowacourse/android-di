package woowacourse.shopping.sangoondi

import javax.naming.Context

object StartDi {

    operator fun invoke(block: StartDi.() -> Unit) {
        this.block()
    }

    fun registerContext(applicationContext: Context) {
        DiContainer.setContext(applicationContext)
    }

    fun registerModule(module: Any) {

    }
}
