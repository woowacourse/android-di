package woowacourse.shopping

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.bandal.di.BandalInject
import com.bandal.di.BandalInjectorAppContainer
import com.bandal.di.DIError
import com.bandal.di.Database
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.common.injectViewModelByBandal

interface FakeRepository

@Database
class DefaultFakeRepository : FakeRepository

class FakeViewModelWithConstructor @BandalInject constructor(
    val repository: FakeRepository,
) : ViewModel()

class FakeViewModelWithField : ViewModel() {
    @Database
    @BandalInject
    lateinit var repository: FakeRepository
}

class FakeActivityWithConstructor : AppCompatActivity() {
    val viewModel: FakeViewModelWithConstructor by injectViewModelByBandal()
}

class FakeActivityWithField : AppCompatActivity() {
    val viewModel: FakeViewModelWithField by injectViewModelByBandal()
}

@RunWith(RobolectricTestRunner::class)
class ViewModelFactoryTest {

    @Before
    fun setUp() {
        BandalInjectorAppContainer.clear()
    }

    @Test
    fun `적절한 객체 인스턴스가 존재하면 ViewModel 의존성 주입이 성공한다`() {
        // given
        val defaultFakeRepository = DefaultFakeRepository()
        BandalInjectorAppContainer.addInstance(
            type = FakeRepository::class,
            instance = defaultFakeRepository,
        )

        // when
        val activity: FakeActivityWithConstructor? =
            Robolectric.buildActivity(FakeActivityWithConstructor::class.java)
                .create()
                .get()
        val viewModel: FakeViewModelWithConstructor? = activity?.viewModel

        // then
        assertNotNull(viewModel)
        assertEquals(viewModel?.repository, defaultFakeRepository)
    }

    @Test
    fun `ViewModel 필드에 의존성을 주입할 수 있다`() {
        // given
        val defaultFakeRepository = DefaultFakeRepository()
        BandalInjectorAppContainer.addInstance(
            type = FakeRepository::class,
            instance = defaultFakeRepository,
        )

        // when
        val activity = Robolectric
            .buildActivity(FakeActivityWithField::class.java)
            .create()
            .get()

        val viewModel: FakeViewModelWithField? = activity?.viewModel

        // then
        assertNotNull(viewModel)
        assertEquals(viewModel?.repository, defaultFakeRepository)
    }

    @Test(expected = DIError.NotFoundQualifierOrInstance::class)
    fun `적절한 객체 인스턴스가 존재하지 않으면 ViewModel 의존성 주입에 실패한다`() {
        val activity = Robolectric
            .buildActivity(FakeActivityWithConstructor::class.java)
            .create()
            .get()
        activity.viewModel
    }
}
