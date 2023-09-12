package woowacourse.shopping

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DatabaseCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.di.annotation.Qualifier
import woowacourse.shopping.fake.FakeCartProductDao
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import kotlin.reflect.full.createInstance

class TargetClass(@Qualifier("databaseCartRepository") val cartRepository: CartRepository) {
    @Inject
    @Qualifier("defaultProductRepository")
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

class AppContainerTest {
    private lateinit var appContainer: AppContainer

    @Before
    fun setup() {
        appContainer = AppContainer()
        appContainer.addProvider(CartProductDao::class, FakeCartProductDao::class::createInstance)
        appContainer.addProvider(TestProvider::class, TestProvider::createInstance)
        appContainer.addProvider(NameTag::class, NameTag::createInstance)
        appContainer.addQualifier(
            Qualifier("defaultProductRepository"),
            DefaultProductRepository::class,
        )
        appContainer.addQualifier(
            Qualifier("databaseCartRepository"),
            DatabaseCartRepository::class,
        )
    }

    @Test
    fun `클래스를 넘기면 해당 클래스의 생성자 프로퍼티를 주입한 인스턴스를 반환해준다`() {
        // given

        // when
        val actual = appContainer.inject(TargetClass::class.java)

        // then
        assertThat(actual).isInstanceOf(TargetClass::class.java)
    }

    @Test
    fun `서로 다른 TargetClass가 주입받은 cartRepository는 같은 객체이다`() {
        // given
        val targetClass1 = appContainer.inject(TargetClass::class.java)
        val targetClass2 = appContainer.inject(TargetClass::class.java)

        // when
        assertThat(targetClass1).isNotEqualTo(targetClass2)

        // then
        assertThat(targetClass1.cartRepository).isEqualTo(targetClass2.cartRepository)
    }

    @Test
    fun `TargetClass의 프로퍼티 중 productRepository는 필드 주입 대상이다`() {
        // given
        val targetClass = appContainer.inject(TargetClass::class.java)

        // when

        // then
        assertThat(targetClass.isProductRepositoryInitialized()).isTrue
    }

    @Test
    fun `TargetClass의 프로퍼티 중 name은 필드 주입 대상이 아니다`() {
        // given
        val targetClass = appContainer.inject(TargetClass::class.java)

        // when

        // then
        assertThat(targetClass.isNameInitialized()).isFalse
    }

    @Test
    fun `TestProvider의 nameTag 프로퍼티의 name의 값은 글로이다`() {
        // given
        val provider = appContainer.inject(TestProvider::class.java)

        // when

        // then
        assertThat(provider.nameTag.name).isEqualTo("글로")
    }
}
