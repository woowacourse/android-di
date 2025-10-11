package woowacourse.shopping.di

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.core.DependencyModule
import woowacourse.shopping.di.annotation.Qualifier
import woowacourse.shopping.di.fake.FakeDependencyModule
import woowacourse.shopping.di.fake.FakeRepositoryModule
import woowacourse.shopping.di.fake.FakeViewModel
import woowacourse.shopping.di.fake.repository.FakeNotRegisteredProductRepository
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import java.util.Collections.synchronizedSet
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.typeOf

@RunWith(RobolectricTestRunner::class)
class DependencyContainerTest {
    private lateinit var databaseModule: DependencyModule
    private lateinit var repositoryModule: DependencyModule
    private lateinit var diContainer: DependencyInjector

    @Before
    fun setup() {
        databaseModule = FakeDependencyModule()
        repositoryModule = FakeRepositoryModule()
        diContainer = DependencyInjector(listOf(databaseModule, repositoryModule))
    }

    @Test
    fun `등록된 인터페이스 타입으로 의존성을 조회하면 정상적으로 반환된다`() {
        val productRepository = diContainer.get(typeOf<ProductRepository>())
        val cartRepository = diContainer.get(typeOf<CartRepository>())

        assertNotNull(productRepository)
        assertNotNull(cartRepository)
    }

    @Test
    fun `등록되지 않은 타입으로 조회 시 예외가 발생한다`() {
        // given
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                // when
                diContainer.get(typeOf<FakeNotRegisteredProductRepository>())
            }

        // then
        val expected = "FakeNotRegisteredProductRepository 타입의 의존성이 등록되어있지 않습니다."
        val actual = exception.message
        assertEquals(expected, actual)
    }

    @Test
    fun `get 호출 시 실제 인스턴스가 생성되고, 이후 호출에는 동일 인스턴스를 반환한다`() {
        // when
        val productRepository1 = diContainer.get(typeOf<ProductRepository>())
        val cart1 = diContainer.get(typeOf<CartRepository>())

        val productRepository2 = diContainer.get(typeOf<ProductRepository>())
        val cart2 = diContainer.get(typeOf<CartRepository>())

        // then
        assertNotNull(productRepository1)
        assertNotNull(cart1)
        assertEquals(productRepository1, productRepository2)
        assertEquals(cart1, cart2)
    }

    @Test
    fun `멀티스레드 환경에서도 동일 인스턴스를 반환한다`() =
        runBlocking {
            val iterations = 100
            val concurrentCalls = 100_000

            repeat(iterations) { round ->
                val results = synchronizedSet(mutableSetOf<ProductRepository>())

                coroutineScope {
                    repeat(concurrentCalls) {
                        launch(Dispatchers.Default) {
                            val repo = diContainer.get(typeOf<ProductRepository>()) as ProductRepository
                            results.add(repo)
                        }
                    }
                }

                assertTrue("동일 인스턴스가 아닙니다", results.size == 1)
            }
        }

    @Test
    fun `Qualifier가 붙은 동일 타입 의존성이 올바르게 주입된다`() {
        // given
        val viewModel = FakeViewModel()

        // when
        diContainer.inject(viewModel)
        val myCartProperties =
            viewModel::class.declaredMemberProperties
                .first { it.name == "myCartRepository" }
        val othersCartProperties =
            viewModel::class.declaredMemberProperties
                .first { it.name == "othersCartRepository" }

        val myCartQualifier = myCartProperties.findAnnotation<Qualifier>()?.name
        val othersCartQualifier = othersCartProperties.findAnnotation<Qualifier>()?.name

        val myCartInstance = myCartProperties.getter.call(viewModel)
        val othersCartInstance = othersCartProperties.getter.call(viewModel)

        // then
        assertNotEquals(myCartInstance, othersCartInstance)
        assertEquals("myCart", myCartQualifier)
        assertEquals("othersCart", othersCartQualifier)
    }
}
