package woowacourse.shopping

import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import shopping.di.DIContainer
import shopping.di.Scope
import woowacourse.shopping.data.fake.FakeCartRepository
import woowacourse.shopping.data.fake.ICartRepository
import woowacourse.shopping.ui.MainActivity
import woowacourse.shopping.ui.cart.DateFormatter

@RunWith(RobolectricTestRunner::class)
class ActivityScopeTest {
    @Before
    fun setUp() {
        // 테스트 간 DIContainer 상태 격리를 위해 클리어
        DIContainer.clearAll()

        // 필요한 의존성 등록
        DIContainer.register(
            clazz = ICartRepository::class.java,
            instance = FakeCartRepository(),
            scope = Scope.APP,
        )
    }

    @Test
    fun `Activity 스코프에서 의존성이 유지되고 제거되는지 테스트`() {
        // Given
        val controller = Robolectric.buildActivity(MainActivity::class.java)
        val activity = controller.create().get()

        DIContainer.register(
            clazz = DateFormatter::class.java,
            instance = DateFormatter(activity),
            scope = Scope.ACTIVITY,
            owner = activity,
        )

        // When
        val dateFormatter1 =
            DIContainer.resolve(
                DateFormatter::class.java,
                scope = Scope.ACTIVITY,
                owner = activity,
            )

        // Then
        val dateFormatter2 =
            DIContainer.resolve(
                DateFormatter::class.java,
                scope = Scope.ACTIVITY,
                owner = activity,
            )

        // 동일한 인스턴스인지 확인
        assertSame(dateFormatter1, dateFormatter2)

        // Activity를 destroy하여 스코프 제거
        controller.destroy()

        // 스코프 제거 후 의존성 요청 시 예외 발생 확인
        assertThrows(IllegalArgumentException::class.java) {
            DIContainer.resolve(
                DateFormatter::class.java,
                scope = Scope.ACTIVITY,
                owner = activity,
            )
        }
    }
}
