package woowacourse.bibi.di

import androidx.lifecycle.ViewModel
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import woowacourse.bibi.di.core.ActivityScope
import woowacourse.bibi.di.core.AppContainer
import woowacourse.bibi.di.core.AppScope
import woowacourse.bibi.di.core.ContainerBuilder
import woowacourse.bibi.di.core.ViewModelScope
import kotlin.reflect.typeOf

class ScopeBehaviorTest {
    interface CartRepository

    class CartRepositoryImpl : CartRepository

    interface ProductRepository

    class ProductRepositoryImpl : ProductRepository

    class DateFormatter(
        val tag: String = "df",
    )

    class NeedDateFormatterViewModel(
        val df: DateFormatter,
    ) : ViewModel()

    private lateinit var app: AppContainer
    private lateinit var activity1: AppContainer
    private lateinit var activity2: AppContainer
    private lateinit var vm1OfActivity1: AppContainer
    private lateinit var vm2OfActivity1: AppContainer
    private lateinit var vm1OfActivity2: AppContainer

    @Before
    fun setup() {
        val builder =
            ContainerBuilder().apply {
                register(CartRepository::class, AppScope::class) { CartRepositoryImpl() }
                register(ProductRepository::class, ViewModelScope::class) { ProductRepositoryImpl() }
                register(DateFormatter::class, ActivityScope::class) { DateFormatter() }
            }
        app = builder.build()

        activity1 = app.child(ActivityScope::class)
        activity2 = app.child(ActivityScope::class)

        vm1OfActivity1 = activity1.child(ViewModelScope::class)
        vm2OfActivity1 = activity1.child(ViewModelScope::class)
        vm1OfActivity2 = activity2.child(ViewModelScope::class)
    }

    @Test
    fun `AppScope는 모든 컨테이너에서 동일 인스턴스이다`() {
        val appCartRepository = app.resolve(typeOf<CartRepository>())
        val activity1CartRepository = activity1.resolve(typeOf<CartRepository>())
        val vm1OfActivity1CartRepository = vm1OfActivity1.resolve(typeOf<CartRepository>())
        val activity2CartRepository = activity2.resolve(typeOf<CartRepository>())
        val vm1OfActivity2CartRepository = vm1OfActivity2.resolve(typeOf<CartRepository>())

        assertSame(appCartRepository, activity1CartRepository)
        assertSame(activity1CartRepository, vm1OfActivity1CartRepository)
        assertSame(vm1OfActivity1CartRepository, activity2CartRepository)
        assertSame(activity2CartRepository, vm1OfActivity2CartRepository)
    }

    @Test
    fun `ActivityScope는 같은 Activity 컨테이너에서는 동일하고, 다른 Activity에서는 다르다`() {
        val activity1DateFormatter1 = activity1.resolve(typeOf<DateFormatter>())
        val activity1DateFormatter2 = activity1.resolve(typeOf<DateFormatter>())
        val activity2DateFormatter = activity2.resolve(typeOf<DateFormatter>())

        assertSame(activity1DateFormatter1, activity1DateFormatter2)
        assertNotSame(activity1DateFormatter1, activity2DateFormatter)

        val vm1OfActivity1DateFormatter = vm1OfActivity1.resolve(typeOf<DateFormatter>())
        assertSame(activity1DateFormatter1, vm1OfActivity1DateFormatter)
    }

    @Test
    fun `ViewModelScope는 같은 ViewModel 컨테이너에서는 동일하고, 다른 ViewModel에서는 다르다`() {
        val vm1OfActivity1ProductRepo1 = vm1OfActivity1.resolve(typeOf<ProductRepository>())
        val vm1OfActivity1ProductRepo2 = vm1OfActivity1.resolve(typeOf<ProductRepository>())
        val vm2OfActivity1ProductRepo = vm2OfActivity1.resolve(typeOf<ProductRepository>())
        val vm1OfActivity2ProductRepo = vm1OfActivity2.resolve(typeOf<ProductRepository>())

        assertSame(vm1OfActivity1ProductRepo1, vm1OfActivity1ProductRepo2)
        assertNotSame(vm1OfActivity1ProductRepo1, vm2OfActivity1ProductRepo)
        assertNotSame(vm1OfActivity1ProductRepo1, vm1OfActivity2ProductRepo)
    }

    @Test
    fun `ViewModel 생성자 주입에서 Activity 스코프 의존성을 nearest로 해석한다`() {
        val constructor = NeedDateFormatterViewModel::class.constructors.first()
        val dateFormatterParam = constructor.parameters.single()

        val resolvedDateFormatterFromVm = vm1OfActivity1.resolve(dateFormatterParam.type)
        val createdViewModel = constructor.call(resolvedDateFormatterFromVm)

        val activity1DateFormatter = activity1.resolve(typeOf<DateFormatter>())
        assertSame(activity1DateFormatter, createdViewModel.df)
    }
}
