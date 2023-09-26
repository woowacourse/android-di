package com.di.berdi

import androidx.lifecycle.ViewModel
import com.di.berdi.annotation.Inject
import com.di.berdi.annotation.Qualifier
import com.di.berdi.fake.FakeModule
import com.di.berdi.fake.FakeObj
import com.di.berdi.fake.FakeRepository
import com.di.berdi.fake.InMemoryFakeRepository
import com.di.berdi.fake.OnDiskFakeRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

class FakeDIApplication : DIApplication() {
    override fun inject() {
        injector = Injector(Container(), FakeModule)
    }
}

class FakeViewModel(
    @Qualifier(qualifiedName = "InMemory") val inMemoryFakeRepository: FakeRepository,
    @Qualifier(qualifiedName = "OnDisk") val onDiskFakeRepository: FakeRepository,
) : ViewModel() {
    @Inject
    lateinit var fakeObj: FakeObj
}

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeDIApplication::class)
class DIActivityTest {

    @Test
    fun `맞는 타입의 객체 인스턴스가 존재하면 ViewModel 의존성 주입이 성공한다`() {
        class FakeDIActivity : DIActivity() {
            lateinit var viewModel: FakeViewModel
        }

        val activity = Robolectric
            .buildActivity(FakeDIActivity::class.java)
            .create()
            .get()

        val viewModel = activity?.viewModel

        assertThat(viewModel).isNotNull

        // and: Qualifier Inject
        assertThat(viewModel?.inMemoryFakeRepository).isInstanceOf(InMemoryFakeRepository::class.java)
        assertThat(viewModel?.onDiskFakeRepository).isInstanceOf(OnDiskFakeRepository::class.java)

        // and: Field Inject
        assertThat(viewModel?.fakeObj).isSameAs(FakeObj)
    }
}
