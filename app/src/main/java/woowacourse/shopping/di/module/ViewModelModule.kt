package woowacourse.shopping.di.module

import woowacourse.shopping.annotation.Binds
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.di.annotation.DisposableCart
import woowacourse.shopping.module.DIActivityRetainedModule
import woowacourse.shopping.module.DIVIewModelModule

class ViewModelModule(parentModule: DIActivityRetainedModule) : DIVIewModelModule(parentModule) {
    @Binds
    @DisposableCart
    private lateinit var bindCartRepository: InMemoryCartRepository

    @Binds
    private lateinit var bindProductRepository: DefaultProductRepository
}
