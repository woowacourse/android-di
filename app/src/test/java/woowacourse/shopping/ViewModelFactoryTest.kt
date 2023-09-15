package woowacourse.shopping

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bandal.di.AppContainer
import com.bandal.di.BandalInject
import com.bandal.di.Database
import com.bandal.di.Qualifier
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.common.CommonViewModelFactory
import kotlin.reflect.KClass

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

object FakeAppContainer : AppContainer {
    private val instances: HashMap<Pair<KClass<*>, List<Annotation>>, Any> = HashMap()

    override fun getInstance(type: KClass<*>, annotations: List<Annotation>): Any? {
        return instances.keys.firstOrNull { (clazz, annotationList) ->
            clazz == type && annotationList.containsAll(annotations)
        }?.let { instances[it] }
    }

    override fun addInstance(type: KClass<*>, instance: Any) {
        val annotationWithQualifier = instance::class.annotations.filter {
            it.annotationClass.java.isAnnotationPresent(
                Qualifier::class.java,
            )
        }
        val key = Pair(
            type,
            annotationWithQualifier,
        )
        instances[key] = instance
    }

    override fun clear() {
        instances.clear()
    }
}

class FakeActivityWithConstructor : AppCompatActivity() {
    val viewModel: FakeViewModelWithConstructor by lazy {
        ViewModelProvider(
            this,
            CommonViewModelFactory,
        )[FakeViewModelWithConstructor::class.java]
    }
}

class FakeActivityWithField : AppCompatActivity() {
    val viewModel: FakeViewModelWithField by lazy {
        ViewModelProvider(
            this,
            CommonViewModelFactory,
        )[FakeViewModelWithField::class.java]
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
    fun `ViewModel 필드에 의존성을 주입할 수 있다`() {
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

    @Test(expected = com.bandal.di.DIError.NotFoundInstanceForInject::class)
    fun `적절한 객체 인스턴스가 존재하지 않으면 ViewModel 의존성 주입에 실패한다`() {
        val activity = Robolectric
            .buildActivity(FakeActivityWithConstructor::class.java)
            .create()
            .get()
        activity.viewModel
    }
}
