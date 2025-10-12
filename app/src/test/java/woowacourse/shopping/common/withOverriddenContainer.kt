package woowacourse.shopping.common

import woowacourse.bibi_di.AppContainer
import woowacourse.shopping.ShoppingApplication

inline fun <R> withOverriddenContainer(
    app: ShoppingApplication,
    newContainer: AppContainer,
    block: () -> R,
): R {
    val old = app.container
    app.overrideContainerForTest(newContainer)
    return try {
        block()
    } finally {
        app.overrideContainerForTest(old)
    }
}
