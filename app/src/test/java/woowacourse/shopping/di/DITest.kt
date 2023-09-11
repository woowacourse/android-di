package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import junit.framework.Assert.assertEquals
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.FakeApplication
import woowacourse.shopping.di.annotation.Inject

@Config(application = FakeApplication::class)
@RunWith(RobolectricTestRunner::class)
class DITest {
    @Test
    fun `의존성 주입해 뷰모델 인스턴스 생성에 성공한다`() {
        // given
        FakeApplication.injector = Injector(AppContainer(listOf(FakeRepositoryModule)))

        // when
        val vm = FakeApplication.injector.inject(FakeViewModel::class)

        // then
        assertThat(vm).isNotNull
        assertEquals(vm.productFakeRepository, ProductFakeRepository)
        assertEquals(vm.cartFakeRepository, CartFakeRepository)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `의존성 주입에 필요한 인스턴스가 없어 뷰모델 생성에 실패한다`() {
        // given
        FakeApplication.injector = Injector(AppContainer(listOf()))

        // when
        val vm = FakeApplication.injector.inject(FakeViewModel::class)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Inject 어노테이션을 붙이지 않아 뷰모델 생성에 실패한다`() {
        // given
        FakeApplication.injector = Injector(AppContainer(listOf(FakeRepositoryModule)))

        // when
        val vm = FakeApplication.injector.inject(FakeViewModelMissingAnnotation::class)
    }

    @Test
    fun `의존성 주입이 필요없는 필드를 가진 뷰모델을 생성한다`() {
        // given
        FakeApplication.injector = Injector(AppContainer(listOf(FakeRepositoryModule)))

        // when
        val vm = FakeApplication.injector.inject(FakeViewModelWithDefaultValue::class)

        // then
        assertThat(vm).isNotNull
        assertEquals(vm.productFakeRepository, ProductFakeRepository)
    }

    @Test
    fun `필드 주입하여 뷰모델을 생성한다`() {
        // given
        FakeApplication.injector = Injector(AppContainer(listOf(FakeRepositoryModule)))

        // when
        val vm = FakeApplication.injector.inject(FakeViewModelWithFieldInjection::class)

        // then
        assertThat(vm).isNotNull
        assertEquals(vm.cartFakeRepository, CartFakeRepository)
    }

    @Test
    fun `재귀 의존성 주입 및 어노테이션으로 데이터소스 구분하여 주입한다`() {
        // given
        FakeApplication.injector = Injector(AppContainer(listOf(FakeRepositoryModule)))

        // when
        val vm = FakeApplication.injector.inject(FakeViewModelWithRecursiveDI::class)

        // then
        assertThat(vm).isNotNull
    }
}

class FakeViewModel(
    @Inject val productFakeRepository: ProductFakeRepository,
    @Inject val cartFakeRepository: CartFakeRepository,
) : ViewModel()

class FakeViewModelMissingAnnotation(
    @Inject val productFakeRepository: ProductFakeRepository,
    val cartFakeRepository: CartFakeRepository,
) : ViewModel()

class FakeViewModelWithDefaultValue(
    @Inject val productFakeRepository: ProductFakeRepository,
    val someString: String = "",
) : ViewModel()

class FakeViewModelWithFieldInjection() : ViewModel() {
    @Inject
    lateinit var cartFakeRepository: CartFakeRepository
}

class FakeViewModelWithRecursiveDI(
    @Inject val fakeRepositoryWithDataSource: FakeRepositoryWithDataSource,
)
