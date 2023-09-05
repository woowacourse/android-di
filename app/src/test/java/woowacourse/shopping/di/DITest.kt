package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import junit.framework.Assert.assertEquals
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.FakeApplication

@Config(application = FakeApplication::class)
@RunWith(RobolectricTestRunner::class)
class DITest {
    @Test
    fun `의존성 주입해 뷰모델 인스턴스 생성에 성공한다`() {
        // given
        FakeApplication.injector = Injector(listOf(FakeRepositoryModule))

        // when
        val vm = FakeApplication.injector.inject<FakeViewModel>()

        // then
        assertThat(vm).isNotNull
        assertEquals(vm.productFakeRepository, ProductFakeRepository)
        assertEquals(vm.cartFakeRepository, CartFakeRepository)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `의존성 주입에 필요한 인스턴스가 없어 뷰모델 생성에 실패한다`() {
        // given
        FakeApplication.injector = Injector(listOf())

        // when
        val vm = FakeApplication.injector.inject<FakeViewModel>()
    }
}

class FakeViewModel(
    val productFakeRepository: ProductFakeRepository,
    val cartFakeRepository: CartFakeRepository,
) : ViewModel()
