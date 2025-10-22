package woowacourse.shopping.common

import woowacourse.bibi.di.core.Container
import woowacourse.shopping.ShoppingApplication

inline fun <R> withOverriddenContainer(
    app: ShoppingApplication,
    container: Container,
    block: () -> R,
): R {
    val old = app.container
    app.overrideContainerForTest(container)
    return try {
        block()
    } finally {
        app.overrideContainerForTest(old)
    }
}
