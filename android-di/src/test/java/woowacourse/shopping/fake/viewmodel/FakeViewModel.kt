package woowacourse.shopping.fake.viewmodel

import androidx.lifecycle.ViewModel
import woowacourse.shopping.annotation.ApplicationLifecycle
import woowacourse.shopping.annotation.Inject
import woowacourse.shopping.annotation.Qualifier
import woowacourse.shopping.annotation.RetainedActivityLifecycle
import woowacourse.shopping.fake.activity.FAKE_ACTIVITY_NAME
import woowacourse.shopping.fake.repository.FAKE_CART_REPOSITORY
import woowacourse.shopping.fake.repository.FAKE_PRODUCT_REPOSITORY
import woowacourse.shopping.fake.repository.FakeRepository

class FakeMainViewModel(
    @RetainedActivityLifecycle(FAKE_ACTIVITY_NAME)
    @Qualifier(FAKE_PRODUCT_REPOSITORY)
    val productRepository: FakeRepository,
) : ViewModel() {
    @Inject
    @Qualifier(FAKE_CART_REPOSITORY)
    @ApplicationLifecycle
    lateinit var cartRepository: FakeRepository
}

class FakeCartViewModel(
    @Qualifier(FAKE_CART_REPOSITORY)
    @ApplicationLifecycle
    val cartRepository: FakeRepository,
) : ViewModel()
