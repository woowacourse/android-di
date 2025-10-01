package woowacourse.di

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.data.repository.RepositoryModule
import woowacourse.shopping.di.RepositoryProvider
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class RepositoryProviderTest {
    @Test
    fun `읜터페이스 타입으로 객체를 가져온다`() {
        val productRepository = RepositoryProvider.get<ProductRepository>()
        val cartRepository = RepositoryProvider.get<CartRepository>()

        assertNotNull(productRepository)
        assertNotNull(cartRepository)
    }

    @Test
    fun `구현체 타입으로 의존성을 가져오면 예외가 발생한다`() {
        assertThrows(IllegalStateException::class.java) {
            RepositoryProvider.get<DefaultProductRepository>()
        }

        assertThrows(IllegalStateException::class.java) {
            RepositoryProvider.get<DefaultCartRepository>()
        }
    }

    @Test
    fun `RepositoryModule의 프로퍼티가 실제로 RepositoryProvider에 등록된다`() {
        val instancesField = RepositoryProvider::class.java.getDeclaredField("instances")
        instancesField.isAccessible = true
        val instances = instancesField.get(RepositoryProvider) as Map<*, *>

        RepositoryModule::class.memberProperties.forEach { property ->
            property.isAccessible = true
            val instance = property.getter(RepositoryModule) ?: return@forEach
            val propertyType = property.returnType.classifier

            assertTrue(instances.containsKey(propertyType))
            assertEquals(instance, instances[propertyType])
        }
    }
}
