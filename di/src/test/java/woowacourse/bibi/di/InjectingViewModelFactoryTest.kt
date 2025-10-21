package woowacourse.bibi.di

import androidx.lifecycle.ViewModel
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import woowacourse.bibi.di.androidx.InjectingViewModelFactory
import woowacourse.bibi.di.core.ActivityScope
import woowacourse.bibi.di.core.AppScope
import woowacourse.bibi.di.core.Container
import woowacourse.bibi.di.core.ContainerBuilder
import woowacourse.bibi.di.core.Inject
import woowacourse.bibi.di.core.Local
import woowacourse.bibi.di.core.Remote
import kotlin.reflect.KClass
import kotlin.reflect.KType

class InjectingViewModelFactoryTest {
    interface ProductRepository

    class FakeProductRepository : ProductRepository

    interface CartRepository

    class FakeCartRepository : CartRepository

    class TestFormatter

    class ConstructorTestViewModel(
        @Remote val product: ProductRepository,
        @Local val cart: CartRepository,
    ) : ViewModel()

    class FieldTestViewModel : ViewModel() {
        @Inject
        @Remote
        lateinit var product: ProductRepository
        var notInjected: CartRepository? = null
    }

    class ScopedViewModel(
        @Local val cart: CartRepository,
        @Local val product: ProductRepository,
        val formatter: TestFormatter,
    ) : ViewModel()

    private lateinit var factory: InjectingViewModelFactory
    private lateinit var productRepo: ProductRepository
    private lateinit var cartRepo: CartRepository

    @Before
    fun setup() {
        productRepo = FakeProductRepository()
        cartRepo = FakeCartRepository()

        val container =
            MapBackedContainer(
                mapOf(
                    Key(ProductRepository::class, Remote::class) to productRepo,
                    Key(CartRepository::class, Local::class) to cartRepo,
                ),
            )

        factory = InjectingViewModelFactory(container)
    }

    @Test
    fun `생성자 주입 ViewModel 생성 테스트`() {
        // when
        val viewModel = factory.create(ConstructorTestViewModel::class.java)

        // then
        assertNotNull(viewModel)
        assertSame(productRepo, viewModel.product)
        assertSame(cartRepo, viewModel.cart)
    }

    @Test
    fun `@Inject가 붙은 필드만 주입된다`() {
        // when
        val viewModel = factory.create(FieldTestViewModel::class.java)

        // then
        assertNotNull(viewModel)
        assertSame(productRepo, viewModel.product)
        assertNull(viewModel.notInjected)
    }

    @Test(expected = IllegalStateException::class)
    fun `바인딩 누락 시 예외를 던진다`() {
        // given
        val brokenContainer =
            MapBackedContainer(
                mapOf(Key(ProductRepository::class, Remote::class) to productRepo),
            )
        val brokenFactory = InjectingViewModelFactory(brokenContainer)

        // when
        brokenFactory.create(ConstructorTestViewModel::class.java)
    }

    @Test
    fun `AppScope CartRepository는 ViewModel과 Activity를 넘어 동일 인스턴스가 재사용된다`() {
        // given
        val builder =
            ContainerBuilder().apply {
                register(CartRepository::class, Local::class, AppScope::class) {
                    FakeCartRepository()
                }
                register(
                    ProductRepository::class,
                    Local::class,
                    woowacourse.bibi.di.core.ViewModelScope::class,
                ) {
                    FakeProductRepository()
                }
                register(TestFormatter::class, woowacourse.bibi.di.core.ActivityScope::class) {
                    TestFormatter()
                }
            }
        val appContainer = builder.build()

        // when
        val activity1 = appContainer.child(ActivityScope::class)
        val factory1 = InjectingViewModelFactory(activity1)
        val viewModel1OfActivity1 = factory1.create(ScopedViewModel::class.java)

        val activity2 = appContainer.child(ActivityScope::class)
        val factory2 = InjectingViewModelFactory(activity2)
        val viewModel2OfActivity1 = factory2.create(ScopedViewModel::class.java)

        // then
        assertSame(viewModel1OfActivity1.cart, viewModel2OfActivity1.cart)
    }

    @Test
    fun `ViewModelScope ProductRepository는 같은 Activity 내에서도 ViewModel마다 서로 다르다`() {
        val builder =
            ContainerBuilder().apply {
                register(
                    CartRepository::class,
                    Local::class,
                    woowacourse.bibi.di.core.AppScope::class,
                ) {
                    FakeCartRepository()
                }
                register(
                    ProductRepository::class,
                    Local::class,
                    woowacourse.bibi.di.core.ViewModelScope::class,
                ) {
                    FakeProductRepository()
                }
                register(TestFormatter::class, woowacourse.bibi.di.core.ActivityScope::class) {
                    TestFormatter()
                }
            }
        val appContainer = builder.build()
        val activity = appContainer.child(ActivityScope::class)
        val factory = InjectingViewModelFactory(activity)

        // when
        val viewModel1 = factory.create(ScopedViewModel::class.java)
        val viewModel2 = factory.create(ScopedViewModel::class.java)

        // then
        assertNotNull(viewModel1.product)
        assertNotNull(viewModel2.product)
        assertSame(viewModel1.cart, viewModel2.cart)
        assertNotSame(viewModel1.product, viewModel2.product)
    }

    @Test
    fun `ActivityScope 의존성은 같은 Activity에서는 동일하고 다른 Activity에서는 다르다`() {
        // given
        val builder =
            ContainerBuilder().apply {
                register(
                    CartRepository::class,
                    Local::class,
                    woowacourse.bibi.di.core.AppScope::class,
                ) {
                    FakeCartRepository()
                }
                register(
                    ProductRepository::class,
                    Local::class,
                    woowacourse.bibi.di.core.ViewModelScope::class,
                ) {
                    FakeProductRepository()
                }
                register(TestFormatter::class, woowacourse.bibi.di.core.ActivityScope::class) {
                    TestFormatter()
                }
            }
        val appContainer = builder.build()

        // when
        val activity1 = appContainer.child(ActivityScope::class)
        val factory1 = InjectingViewModelFactory(activity1)
        val viewModel1OfActivity1 = factory1.create(ScopedViewModel::class.java)
        val viewModel2OfActivity1 = factory1.create(ScopedViewModel::class.java)

        val activity2 = appContainer.child(ActivityScope::class)
        val factory2 = InjectingViewModelFactory(activity2)
        val viewModel1OfActivity2 = factory2.create(ScopedViewModel::class.java)

        // then
        assertSame(viewModel1OfActivity1.formatter, viewModel2OfActivity1.formatter)
        assertNotSame(viewModel1OfActivity1.formatter, viewModel1OfActivity2.formatter)
    }

    private class MapBackedContainer(
        private val map: Map<Key, Any>,
    ) : Container {
        override fun resolve(
            type: KType,
            qualifier: KClass<out Annotation>?,
        ): Any {
            val kClass =
                type.classifier as? KClass<*>
                    ?: error("Unsupported type: $type")
            return map[Key(kClass, qualifier)]
                ?: error("No binding for $kClass (qualifier=${qualifier?.simpleName})")
        }

        override fun child(scope: KClass<out Annotation>): Container = this

        override fun clear() = Unit
    }

    private data class Key(
        val type: KClass<*>,
        val qualifier: KClass<out Annotation>?,
    )
}
