package woowacourse.shopping

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.annotation.ApplicationLifecycle
import woowacourse.shopping.annotation.Inject
import woowacourse.shopping.annotation.Qualifier
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DatabaseCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.di.PackageName
import woowacourse.shopping.fake.FakeCartProductDao
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import kotlin.reflect.full.createInstance

class TargetClass(
    @ApplicationLifecycle
    @Qualifier(PackageName.DATABASE_CART)
    val cartRepository: CartRepository,
) {
    @Inject
    @Qualifier(PackageName.PRODUCT)
    lateinit var productRepository: ProductRepository
        private set

    lateinit var name: String
        private set

    fun isProductRepositoryInitialized() = this::productRepository.isInitialized

    fun isNameInitialized() = this::name.isInitialized
}

class TestProvider private constructor(val nameTag: NameTag) {
    companion object {
        fun createInstance(nameTag: NameTag): TestProvider {
            return TestProvider(nameTag)
        }
    }
}

class NameTag private constructor(val name: String) {
    companion object {
        fun createInstance(): NameTag {
            return NameTag("글로")
        }
    }
}

class DiContainerTest {
    private lateinit var diContainer: DiContainer
    private lateinit var injector: Injector

    @Before
    fun setup() {
        diContainer = DiContainer()
        diContainer.registerProviders {
            provider(CartProductDao::class to FakeCartProductDao::class::createInstance)
            provider(TestProvider::class to TestProvider::createInstance)
            provider(NameTag::class to NameTag::createInstance)
        }

        injector = Injector(diContainer)
    }

    @Test
    fun `클래스를 넘기면 해당 클래스의 생성자 프로퍼티를 주입한 인스턴스를 반환해준다`() {
        // given

        // when
        val actual = injector.inject(TargetClass::class)

        // then
        assertThat(actual).isInstanceOf(TargetClass::class.java)
    }

    @Test
    fun `서로 다른 TargetClass가 주입받은 cartRepository는 같은 객체이다`() {
        // given
        val targetClass1 = injector.inject(TargetClass::class)
        val targetClass2 = injector.inject(TargetClass::class)

        // when
        assertThat(targetClass1).isNotEqualTo(targetClass2)

        // then
        assertThat(targetClass1.cartRepository).isEqualTo(targetClass2.cartRepository)
    }

    @Test
    fun `TargetClass의 프로퍼티 중 productRepository는 필드 주입 대상이다`() {
        // given
        val targetClass = injector.inject(TargetClass::class)

        // when

        // then
        assertThat(targetClass.isProductRepositoryInitialized()).isTrue
    }

    @Test
    fun `TargetClass의 프로퍼티 중 name은 필드 주입 대상이 아니다`() {
        // given
        val targetClass = injector.inject(TargetClass::class)

        // when

        // then
        assertThat(targetClass.isNameInitialized()).isFalse
    }

    @Test
    fun `TestProvider의 nameTag 프로퍼티의 name의 값은 글로이다`() {
        // given
        val provider = injector.inject(TestProvider::class)

        // when

        // then
        assertThat(provider.nameTag.name).isEqualTo("글로")
    }

    @Test
    fun `TargetClass의 productRepository는 DefaultProductRepository이다`() {
        // given
        val targetClass = injector.inject(TargetClass::class)

        // when

        // then
        assertThat(targetClass.productRepository).isInstanceOf(DefaultProductRepository::class.java)
    }

    @Test
    fun `TargetClass의 cartRepository는 DatabaseCartRepository이다`() {
        // given
        val targetClass = injector.inject(TargetClass::class)

        // when

        // then
        assertThat(targetClass.cartRepository).isInstanceOf(DatabaseCartRepository::class.java)
    }

    @Test
    fun `TargetClass의 cartRepository는 InMemoryCartRepository가 아니다`() {
        // given
        val targetClass = injector.inject(TargetClass::class)

        // when

        // then
        assertThat(targetClass.cartRepository).isNotInstanceOf(InMemoryCartRepository::class.java)
    }
}
