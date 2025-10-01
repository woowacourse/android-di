package woowacourse.shopping.di.module

import woowacourse.shopping.di.DIContainer

interface Module {
    fun register(container: DIContainer)
}
