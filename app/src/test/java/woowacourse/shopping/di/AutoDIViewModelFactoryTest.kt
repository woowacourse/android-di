package woowacourse.shopping.di

import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertSame
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.fake.FakeProductRepository
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

class AutoDIViewModelFactoryTest {
    private lateinit var autoDIViewModelFactory: AutoDIViewModelFactory
    private val cartRepository: CartRepository = FakeCartRepository()
    private val productRepository: ProductRepository = FakeProductRepository()

    @Before
    fun setUp() {
        val dependencies: Map<KClass<*>, Any> =
            mapOf(
                CartRepository::class to cartRepository,
                ProductRepository::class to productRepository,
            )
        autoDIViewModelFactory = AutoDIViewModelFactory(dependencies)
    }

    @Test
    fun `MainViewModel 생성 테스트`() {
        // given:
        // when:
        val viewModel =
            autoDIViewModelFactory.create(MainViewModel::class.java)

        // then:
        assertNotNull(viewModel)
    }

    @Test
    fun `CartViewModel 생성 테스트`() {
        // given:
        // when:
        val viewModel =
            autoDIViewModelFactory.create(CartViewModel::class.java)

        // then:
        assertNotNull(viewModel)
    }

    @Test
    fun `MainViewModel 생성 시 productRepository와 cartRepository 필드가 주입된다`() {
        // given:
        // when:
        val viewModel = autoDIViewModelFactory.create(MainViewModel::class.java)

        // then:
        val expectedDependencies =
            mapOf(
                CartRepository::class to cartRepository,
                ProductRepository::class to productRepository,
            )

        expectedDependencies.forEach { (type, expectedInstance) ->
            val injectedRepository =
                viewModel::class
                    .declaredMemberProperties
                    .firstOrNull { it.returnType.classifier == type }
                    ?.apply { isAccessible = true }
                    ?.getter
                    ?.call(viewModel)

            assertSame(expectedInstance, injectedRepository)
        }
    }

    @Test
    fun `CartViewModel 생성 시 cartRepository 필드가 주입된다`() {
        // given:
        // when:
        val viewModel = autoDIViewModelFactory.create(CartViewModel::class.java)

        // then:
        val injectedRepository =
            viewModel::class
                .declaredMemberProperties
                .first { it.name == "cartRepository" }
                .apply { isAccessible = true }
                .getter
                .call(viewModel)

        assertSoftly {
            assertNotNull(injectedRepository)
            assertTrue(injectedRepository is FakeCartRepository)
            assertSame(cartRepository, injectedRepository)
        }
    }
}
