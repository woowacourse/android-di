package woowacourse.shopping

import android.content.Context
import com.android.di.annotation.Qualifier
import com.android.di.component.DiSingletonComponent
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import woowacourse.shopping.data.ImMemoryShoppingDatabase
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.createInMemoryDatabase
import woowacourse.shopping.data.createRoomDatabase
import woowacourse.shopping.data.di.annotation.InMemoryDatabase
import woowacourse.shopping.data.di.annotation.RoomDatabase

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class QualifierTest {
    private lateinit var context: Context

    @Before
    fun setup() {
        context = RuntimeEnvironment.getApplication()
    }

    @Test
    fun `InMemoryDatabase 어노테이션 테스트`() {
        val isQualifier = InMemoryDatabase::class.annotations.any { it is Qualifier }
        assertTrue(isQualifier)
    }

    @Test
    fun `RoomDatabase 어노테이션 테스트`() {
        val isQualifier = RoomDatabase::class.annotations.any { it is Qualifier }
        assertTrue(isQualifier)
    }

    @Test
    fun `RoomDatabase Qualifier를 주입하였을 때 정상적인 Qualifier 적용을 테스트한다`() {
        val realDatabase = createRoomDatabase(context)
        DiSingletonComponent.provide(
            RoomDatabase::class,
            realDatabase,
        )
        val exceptDatabase: ShoppingDatabase = DiSingletonComponent.matchByQualifier(RoomDatabase::class)
        assertTrue(realDatabase == exceptDatabase)
    }

    @Test
    fun `InMemoryDatabase Qualifier를 주입하였을 때 정상적인 Qualifier 적용을 테스트한다`() {
        val realDatabase = createInMemoryDatabase(context)
        DiSingletonComponent.provide(
            InMemoryDatabase::class,
            realDatabase,
        )
        val exceptDatabase: ImMemoryShoppingDatabase = DiSingletonComponent.matchByQualifier(InMemoryDatabase::class)
        assertTrue(realDatabase == exceptDatabase)
    }
}
