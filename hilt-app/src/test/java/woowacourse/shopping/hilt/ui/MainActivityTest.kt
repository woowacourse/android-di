package woowacourse.shopping.hilt.ui

import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.types.shouldBeTypeOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import woowacourse.shopping.hilt.data.CartRepository
import woowacourse.shopping.hilt.data.ProductRepository
import woowacourse.shopping.hilt.di.qualifier.SingleCartQualifier
import woowacourse.shopping.hilt.fake.StubCartRepository
import woowacourse.shopping.hilt.fake.StubProductRepository
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class)
class MainActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val scenarioRule = activityScenarioRule<MainActivity>()
    private val scenario get() = scenarioRule.scenario

    // ProductRepository 및 CartRepository 주입
    @Inject
    lateinit var productRepository: ProductRepository

    @Inject
    @SingleCartQualifier
    lateinit var cartRepository: CartRepository

    @Before
    fun init() {
        // Hilt로 의존성 주입 실행
        hiltRule.inject()
    }

    @Test
    fun `Activity 실행 테스트`() {
        scenario.onActivity { activity ->
            activity.shouldNotBeNull()
        }
    }

    @Test
    fun `Stub Repository 주입 테스트`() {
        productRepository.shouldBeTypeOf<StubProductRepository>()
        cartRepository.shouldBeTypeOf<StubCartRepository>()
    }
}
