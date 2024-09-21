package woowacourse.shopping

import com.woowacourse.di.DependencyInjector
import com.woowacourse.di.InMemory
import com.woowacourse.di.RoomDB
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DependencyInjectorTest {
    private lateinit var activity: FakeActivity
    private lateinit var fakeProductRepository: FakeProductRepository
    private lateinit var fakeCartRepository: FakeCartRepository

    @Before
    fun setUp() {
        fakeProductRepository = FakeDefaultProductRepository()
        fakeCartRepository = FakeDefaultCartRepository()

        activity =
            Robolectric
                .buildActivity(FakeActivity::class.java)
                .create()
                .get()
    }

    @Test
    fun `인터페이스 1개, 구현체 1개일 때 적절한 객체 인스턴스를 찾아 ViewModel 의존성을 주입한다`() {
        // given
        DependencyInjector.addInstance(FakeProductRepository::class, fakeProductRepository, InMemory::class)

        // then
        val viewModel = activity.firstViewModel
        assertNotNull(viewModel)
        assertEquals(viewModel.fakeProductRepository, fakeProductRepository)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `인터페이스 1개, 구현체 2개일 때 Qualifier를 명시하지 않으면 ViewModel 의존성 주입에 실패한다`() {
        // given
        DependencyInjector.addInstance(FakeCartRepository::class, fakeCartRepository, RoomDB::class)

        // then
        val viewModel = activity.secondViewModel
        assertNotNull(viewModel)
        assertEquals(viewModel.fakeDefaultCartRepository, fakeCartRepository)
    }

    @Test
    fun `인터페이스 1개, 구현체 2개일 때 Qualifier를 명시하면 적절한 객체 인스턴스를 찾아 ViewModel 의존성을 주입한다`() {
        // given
        DependencyInjector.addInstance(FakeCartRepository::class, fakeCartRepository, RoomDB::class)

        // then
        val viewModel = activity.thirdViewModel
        assertNotNull(viewModel)
        assertEquals(viewModel.fakeDefaultCartRepository, fakeCartRepository)
    }

    @Test
    fun `인터페이스 1개, 구현체 2개인 프로퍼티가 두 개이고, Qualifier를 명시하면 적절한 객체 인스턴스를 찾아 ViewModel 의존성을 주입한다 `() {
        // given
        DependencyInjector.addInstance(FakeProductRepository::class, fakeProductRepository, InMemory::class)
        DependencyInjector.addInstance(FakeCartRepository::class, fakeCartRepository, RoomDB::class)

        // then
        val viewModel = activity.fourthViewModel
        assertNotNull(viewModel)
        assertEquals(viewModel.fakeProductRepository, fakeProductRepository)
        assertEquals(viewModel.fakeDefaultCartRepository, fakeCartRepository)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `적절한 객체 인스턴스가 존재하지 않으면 ViewModel 의존성 주입에 실패한다`() {
        val activity =
            Robolectric
                .buildActivity(FakeActivity::class.java)
                .create()
                .get()

        activity.firstViewModel
    }
}
