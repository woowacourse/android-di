package woowacourse.shopping

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.common.AppContainer
import woowacourse.shopping.common.BandalInject
import woowacourse.shopping.common.CommonViewModelFactory
import woowacourse.shopping.common.DIError
import woowacourse.shopping.common.viewModelInject
import kotlin.reflect.KClass

interface FakeRepository

class DefaultFakeRepository : FakeRepository

class FakeViewModelWithConstructor @BandalInject constructor(
    val repository: FakeRepository,
) : ViewModel()

class FakeViewModelWithField : ViewModel() {
    @BandalInject
    lateinit var repository: FakeRepository
}

object FakeAppContainer : AppContainer {
    private val instances: HashMap<KClass<*>, Any> = HashMap()

    override fun getInstance(type: KClass<*>): Any? {
        return instances[type]
    }

    override fun addInstance(type: KClass<*>, instance: Any) {
        instances[type] = instance
    }

    override fun clear() {
        instances.clear()
    }
}

class FakeActivityWithConstructor : AppCompatActivity() {
    val viewModel: FakeViewModelWithConstructor by viewModelInject {
        CommonViewModelFactory(
            FakeAppContainer,
        )
    }
}

class FakeActivityWithField : AppCompatActivity() {
    val viewModel: FakeViewModelWithField by viewModelInject {
        CommonViewModelFactory(
            FakeAppContainer,
        )
    }
}

@RunWith(RobolectricTestRunner::class)
class ViewModelFactoryTest {

    @Before
    fun setUp() {
        FakeAppContainer.clear()
    }

    @Test
    fun `적절한 객체 인스턴스가 존재하면 ViewModel 의존성 주입이 성공한다`() {
        // given
        val defaultFakeRepository = DefaultFakeRepository()
        FakeAppContainer.addInstance(
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
    fun `다`() {
        // given
        val defaultFakeRepository = DefaultFakeRepository()
        FakeAppContainer.addInstance(
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

    @Test(expected = DIError.NotFoundInstanceForInject::class)
    fun `적절한 객체 인스턴스가 존재하지 않으면 ViewModel 의존성 주입에 실패한다`() {
        val activity = Robolectric
            .buildActivity(FakeActivityWithConstructor::class.java)
            .create()
            .get()
        activity.viewModel
    }
}
