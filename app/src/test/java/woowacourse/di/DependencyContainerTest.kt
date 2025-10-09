package woowacourse.di

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import woowacourse.fake.NotRegisteredRepository
import woowacourse.shopping.core.DependencyModule
import woowacourse.shopping.data.repository.RepositoryModule
import woowacourse.shopping.di.DependencyInjector
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import kotlin.reflect.typeOf

class DependencyContainerTest {
    private lateinit var repositoryModule: DependencyModule
    private lateinit var diContainer: DependencyInjector

    @Before
    fun setup() {
        repositoryModule = RepositoryModule()
        diContainer = DependencyInjector(listOf(repositoryModule))
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
                diContainer.get(typeOf<NotRegisteredRepository>())
            }

        // then
        val expected = "NotRegisteredRepository 타입의 의존성이 등록되어있지 않습니다."
        val actual = exception.message
        assertEquals(expected, actual)
    }

    @Test
    fun `providers에 람다만 존재해야 한다`() {
        // given
        val providersField = diContainer::class.java.getDeclaredField("providers")
        providersField.isAccessible = true
        val providers = providersField.get(diContainer) as Map<*, *>

        // then
        providers.values.forEach { provider ->
            assertTrue(provider is Function0<*>)
        }
    }

    @Test
    fun `get 호출 전에는 instances에 실제 인스턴스가 없어야 한다`() {
        // given
        val instancesField = diContainer::class.java.getDeclaredField("instances")
        instancesField.isAccessible = true
        val instances = instancesField.get(diContainer) as Map<*, *>

        // then
        assertTrue(instances.isEmpty())
    }

    @Test
    fun `get 호출 시 실제 인스턴스가 생성되고, 이후 호출에는 동일 인스턴스를 반환한다`() {
        // given
        val instancesField = diContainer::class.java.getDeclaredField("instances")
        instancesField.isAccessible = true
        val instances = instancesField.get(diContainer) as Map<*, *>

        // when
        val productRepository1 = diContainer.get(typeOf<ProductRepository>())
        val cart1 = diContainer.get(typeOf<CartRepository>())

        val productRepository2 = diContainer.get(typeOf<ProductRepository>())
        val cart2 = diContainer.get(typeOf<CartRepository>())

        // then
        assertEquals(productRepository1, instances[typeOf<ProductRepository>()])
        assertEquals(cart1, instances[typeOf<CartRepository>()])
        assertEquals(productRepository1, productRepository2)
        assertEquals(cart1, cart2)
    }
}
