package woowacourse.shopping.study

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.di.injector.Injector
import woowacourse.shopping.di.module.Module
import woowacourse.shopping.di.viewModels

interface FakeRepository1
class DefaultFakeRepository1 : FakeRepository1

interface FakeRepository2
class DefaultFakeRepository2 : FakeRepository2

object TestRepositoryModule : Module {
    fun provideFakeRepository(): FakeRepository1 = DefaultFakeRepository1()
}

class FakeActivity1 : AppCompatActivity() {
    val viewModel: FakeViewModel1 by viewModels(TestApplication.injector)
}

class FakeActivity2 : AppCompatActivity() {
    val viewModel: FakeViewModel2 by viewModels(TestApplication.injector)
}

class FakeViewModel1(@Inject val fakeRepository1: FakeRepository1) : ViewModel()
class FakeViewModel2(@Inject val fakeRepository2: FakeRepository2) : ViewModel()

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        injector = Injector(TestRepositoryModule)
    }

    companion object {
        lateinit var injector: Injector
    }
}

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class ViewModel1InjectorTest {

    @Test
    fun `적절한 객체 인스턴스를 찾아 ViewModel 의존성을 주입한다`() {
        // given
        val activity = Robolectric
            .buildActivity(FakeActivity1::class.java)
            .create()
            .get()

        // then
        val viewModel = activity.viewModel
        assertNotNull(viewModel)
    }
}

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class ViewModel2InjectorTest {

    @Test(expected = IllegalArgumentException::class)
    fun `적절한 객체 인스턴스가 존재하지 않으면 ViewModel 의존성 주입에 실패한다`() {
        val activity = Robolectric
            .buildActivity(FakeActivity2::class.java)
            .create()
            .get()
        activity.viewModel
    }
}
