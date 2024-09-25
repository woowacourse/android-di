package woowacourse.shopping

import android.os.Looper
import com.google.common.truth.Truth.assertThat
import com.woowacourse.di.ActivityScope
import com.woowacourse.di.DependencyContainer
import com.woowacourse.di.InMemory
import com.woowacourse.di.RoomDB
import com.woowacourse.di.Singleton
import com.woowacourse.di.ViewModelScope
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.android.controller.ActivityController
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.cart.DateFormatter

@RunWith(RobolectricTestRunner::class)
class DependencyContainerTest {
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
        DependencyContainer.addInstance(
            FakeProductRepository::class,
            fakeProductRepository,
            qualifier = InMemory::class,
            scope = Singleton::class,
        )

        // then
        val viewModel = activity.firstSuccessCaseViewModel
        assertNotNull(viewModel)
        assertEquals(viewModel.fakeProductRepository, fakeProductRepository)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `인터페이스 1개, 구현체 2개일 때 Qualifier를 명시하지 않으면 ViewModel 의존성 주입에 실패한다`() {
        // given
        DependencyContainer.addInstance(
            FakeCartRepository::class,
            fakeCartRepository,
            qualifier = RoomDB::class,
            scope = ViewModelScope::class,
        )

        // then
        val viewModel = activity.firstFailureCaseViewModel
        assertNotNull(viewModel)
        assertEquals(viewModel.fakeDefaultCartRepository, fakeCartRepository)
    }

    @Test
    fun `인터페이스 1개, 구현체 2개일 때 Qualifier를 명시하면 적절한 객체 인스턴스를 찾아 ViewModel 의존성을 주입한다`() {
        // given
        DependencyContainer.addInstance(
            FakeCartRepository::class,
            fakeCartRepository,
            qualifier = RoomDB::class,
            scope = ViewModelScope::class,
        )

        // then
        val viewModel = activity.secondSuccessCaseViewModel
        assertNotNull(viewModel)
        assertEquals(viewModel.fakeDefaultCartRepository, fakeCartRepository)
    }

    @Test
    fun `인터페이스 1개, 구현체 2개인 프로퍼티가 두 개이고, Qualifier를 명시하면 적절한 객체 인스턴스를 찾아 ViewModel 의존성을 주입한다 `() {
        // given
        DependencyContainer.addInstance(
            FakeProductRepository::class,
            fakeProductRepository,
            InMemory::class,
            Singleton::class,
        )
        DependencyContainer.addInstance(
            FakeCartRepository::class,
            fakeCartRepository,
            RoomDB::class,
            ViewModelScope::class,
        )

        // then
        val viewModel = activity.thirdSuccessCaseViewModel
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

        DependencyContainer.clearViewModelInstances()
        activity.firstSuccessCaseViewModel
    }

    // 작성 중인 테스트 - 정상 동작 하지 않음
    @Test
    fun `DateFormatter의 인스턴스는 Activity LifeCycle 동안 유지되어야 한다`() {
        val cartActivity =
            Robolectric
                .buildActivity(CartActivity::class.java)
                .create()
                .get()

        DependencyContainer.addInstance(
            FakeCartRepository::class,
            fakeCartRepository,
            qualifier = RoomDB::class,
            scope = ViewModelScope::class,
        )

        DependencyContainer.addInstance(
            DateFormatter::class,
            DIModule.provideDateFormatter(cartActivity.applicationContext),
            scope = ActivityScope::class,
        )

        val firstController: ActivityController<CartActivity> =
            Robolectric.buildActivity(CartActivity::class.java).setup()

        shadowOf(Looper.getMainLooper()).idle()

        val firstDateFormatter = firstController.get().dateFormatter

        firstController.recreate() // TODO: 예외발생 해결
        shadowOf(Looper.getMainLooper()).idle()

        val secondDateFormatter = firstController.get().dateFormatter

        firstController.pause().stop()
        firstController.destroy()

        val secondController: ActivityController<CartActivity> =
            Robolectric.buildActivity(CartActivity::class.java).setup()

        val thirdDateFormatter = secondController.get().dateFormatter
        shadowOf(Looper.getMainLooper()).idle()

        assertAll(
            { assertThat(firstDateFormatter).isNotNull() },
            { assertThat(secondDateFormatter).isEqualTo(firstDateFormatter) },
            { assertThat(thirdDateFormatter).isNotEqualTo(firstDateFormatter) },
        )
    }
}
