package woowacourse.shopping.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.model.Product

@RunWith(RobolectricTestRunner::class)
class DefaultCartRepositoryTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var database: ShoppingDatabase
    private lateinit var repository: CartRepository
    private val product = Product(name = "우테코 과자", price = 10_000, imageUrl = "snack.jpg")

    @Before
    fun setUp() {
        context.deleteDatabase(DATABASE_NAME)
        openDatabase()
    }

    @After
    fun tearDown() {
        database.close()
        context.deleteDatabase(DATABASE_NAME)
    }

    @Test
    fun `데이터베이스를 다시 열어도 상품과 담은 시각이 유지된다`() =
        runTest {
            val beforeInsert = System.currentTimeMillis()
            repository.addCartProduct(product)
            val saved = repository.getAllCartProducts().single()

            assertThat(saved.id).isPositive()
            assertThat(saved.name).isEqualTo(product.name)
            assertThat(saved.price).isEqualTo(product.price)
            assertThat(saved.imageUrl).isEqualTo(product.imageUrl)
            assertThat(saved.createdAt).isBetween(beforeInsert, System.currentTimeMillis())
            database.close()
            openDatabase()

            assertThat(repository.getAllCartProducts()).containsExactly(saved)
        }

    @Test
    fun `같은 상품의 중간 항목을 삭제한 뒤에도 실제 식별자로 삭제한다`() =
        runTest {
            repeat(3) { repository.addCartProduct(product) }
            val saved = repository.getAllCartProducts()
            assertThat(saved.map { it.id }).doesNotHaveDuplicates()

            repository.deleteCartProduct(saved[1].id)
            assertThat(repository.getAllCartProducts()).containsExactly(saved[0], saved[2])

            repository.deleteCartProduct(saved[2].id)
            assertThat(repository.getAllCartProducts()).containsExactly(saved[0])
        }

    @Test
    fun `없는 식별자를 삭제해도 다른 상품은 유지된다`() =
        runTest {
            repository.addCartProduct(product)
            val saved = repository.getAllCartProducts()

            repository.deleteCartProduct(Long.MAX_VALUE)

            assertThat(repository.getAllCartProducts()).containsExactlyElementsOf(saved)
        }

    private fun openDatabase() {
        database = Room.databaseBuilder(context, ShoppingDatabase::class.java, DATABASE_NAME).build()
        repository = DefaultCartRepository(database.cartProductDao())
    }

    companion object {
        private const val DATABASE_NAME = "cart-repository-test.db"
    }
}
