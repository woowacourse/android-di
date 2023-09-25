package woowacourse.di

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import woowacourse.di.annotation.InjectField
import woowacourse.di.annotation.Qualifier

class InjectorTest {

    private lateinit var injector: Injector

    @Before
    fun setUp() {
        injector = Injector(Container(FakeModule()))
    }

    @Test
    fun `주어진 클래스 타입에 맞게 인스턴스를 생성해서 반환한다`() {
        // when
        val actual = injector.inject(FakeViewModel::class)

        // then
        assertNotNull(actual)
        assertEquals(FakeViewModel::class, actual::class)
    }

    class FakeViewModel(
        val productRepository: FakeProductRepository,
        val cartRepository: FakeCartRepository,
    )

    @Test(expected = IllegalStateException::class)
    fun `주어진 클래스 타입의 주생성자를 가져올 수 없다면 오류가 발생한다`() {
        injector.inject(NoPrimaryConstructor::class)
    }

    interface NoPrimaryConstructor

    @Test(expected = IllegalStateException::class)
    fun `container에 없는 인스턴스가 필요한 클래스를 요구할 경우 오류가 발생한다`() {
        injector.inject(FakeViewModel2::class)
    }

    object IsNotExist
    class FakeViewModel2(val repo: IsNotExist)

    @Test
    fun `요구하는 값의 생성자가 주입이 필요한 파라미터를 가진 경우 재귀를 통해 인스턴스를 생성하여 반환한다`() {
        // when
        val actual = injector.inject(RecursiveViewModel::class)

        // then
        assertNotNull(actual)
        assertEquals(InDiskFakeCartRepository::class, actual.cartRepository::class)
    }

    class RecursiveViewModel(@Qualifier(InDiskFakeCartRepository::class) val cartRepository: FakeCartRepository)

    @Test
    fun `Qualifier 어노테이션으로 각각 해당하는 인자를 넣어 만든 인스턴스를 반환한다`() {
        // when
        val actual = injector.inject(HaveAnnotationViewModel::class)

        // then
        assertNotNull(actual)
        assertEquals(HaveAnnotationViewModel::class, actual::class)
        assertEquals(InDiskFakeCartRepository::class, actual.inDiskCartRepository::class)
        assertEquals(InMemoryFakeCartRepository::class, actual.inMemoryCartRepository::class)
    }

    class HaveAnnotationViewModel(
        @Qualifier(InDiskFakeCartRepository::class) val inDiskCartRepository: FakeCartRepository,
        @Qualifier(InMemoryFakeCartRepository::class) val inMemoryCartRepository: FakeCartRepository,
    )

    @Test
    fun `의존성 주입이 필요한 필드를 가진 인스턴스를 생성해서 반환한다`() {
        // when
        val actual = injector.inject(InjectFieldViewModel::class)

        // then
        assertEquals(DefaultFakeProductRepository::class, actual.productRepository::class)
    }

    class InjectFieldViewModel {
        @InjectField
        lateinit var productRepository: FakeProductRepository
    }

    @Test
    fun `의존성 주입이 필요한 필드와 아닌 필드를 구분하여 인자를 넣어준다`() {
        // when
        val actual = injector.inject(InjectFieldViewModelWithNormalField::class)

        // then
        assertNotNull(actual.productRepository)
        assertNull(actual.pingu)
    }

    class InjectFieldViewModelWithNormalField {
        @InjectField
        var productRepository: FakeProductRepository? = null
        var pingu: FakeCartRepository? = null
    }

    @Test(expected = UninitializedPropertyAccessException::class)
    fun `의존성 주입이 필요한 필드에 InjectField 어노테이션을 붙이지 않는 경우 오류가 발생한다`() {
        // when
        val actual = injector.inject(NeedInjectFieldViewModel::class)
        assertNotNull(actual.productRepository)
    }

    class NeedInjectFieldViewModel {
        lateinit var productRepository: FakeProductRepository
    }

    @Test
    fun `Singleton 어노테이션이 붙은 경우 한 번만 생성해준다`() {
        // when
        val firstCall = injector.inject(SingletonViewModel::class)
        val secondCall = injector.inject(SingletonViewModel::class)

        // then
        assertEquals(SingleRepository::class, firstCall.repository::class)
        assertEquals(firstCall.repository, secondCall.repository)
    }

    class SingletonViewModel(@Qualifier(SingleRepository::class) val repository: FakeSingleRepository)

    @Test
    fun `Singleton 어노테이션이 붙지 않은 경우 매번 생성해준다`() {
        // when
        val firstCall = injector.inject(NotSingletonViewModel::class)
        val secondCall = injector.inject(NotSingletonViewModel::class)

        // then
        assertNotEquals(firstCall.repository, secondCall.repository)
    }

    class NotSingletonViewModel(@Qualifier(DefaultFakeProductRepository::class) val repository: FakeProductRepository)
}
