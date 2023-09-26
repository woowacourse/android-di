package woowacourse.shopping.fake.application

import woowacourse.shopping.fake.activity.FakeDateFormatter
import woowacourse.shopping.fake.activity.createFakeDateFormatter
import woowacourse.shopping.fake.repository.FakeCartProductDao
import woowacourse.shopping.fake.repository.createFakeCartProductDao
import woowacourse.shopping.ui.DiApplication

class FakeApplication : DiApplication() {
    override fun onCreate() {
        super.onCreate()
        setupProviders()
    }

    private fun setupProviders() {
        registerProviders {
            provider(FakeCartProductDao::class to ::createFakeCartProductDao)
            provider(FakeDateFormatter::class to ::createFakeDateFormatter)
        }
    }
}
