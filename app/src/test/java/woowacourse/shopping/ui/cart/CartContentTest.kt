package woowacourse.shopping.ui.cart

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.model.CartProduct

@RunWith(RobolectricTestRunner::class)
class CartContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val product =
        CartProduct(id = 42L, name = "우테코 과자", price = 10_000, imageUrl = "", createdAt = 0L)

    @Test
    fun `장바구니에 담긴 상품의 이름이 화면에 보인다`() {
        composeRule.setContent {
            CartContent(
                uiState = CartUiState(cartProducts = listOf(product)),
                dateFormatter = DateFormatter(LocalContext.current),
                onDelete = {},
                onNavigateUp = {},
            )
        }

        composeRule.onNodeWithText(product.name).assertIsDisplayed()
    }

    @Test
    fun `중간 상품의 삭제 버튼을 누르면 그 상품의 식별자가 전달된다`() {
        var deleted: Long? = null
        val first = product.copy(id = 7L, name = "첫 번째 상품")
        val last = product.copy(id = 99L, name = "마지막 상품")

        composeRule.setContent {
            CartContent(
                uiState = CartUiState(cartProducts = listOf(first, product, last)),
                dateFormatter = DateFormatter(LocalContext.current),
                onDelete = { deleted = it },
                onNavigateUp = {},
            )
        }
        composeRule.onAllNodesWithContentDescription("삭제")[1].performClick()

        assertThat(deleted).isEqualTo(product.id)
    }

    @Test
    fun `장바구니에 담은 시각이 화면에 보인다`() {
        lateinit var formattedDate: String
        composeRule.setContent {
            val dateFormatter = DateFormatter(LocalContext.current)
            formattedDate = dateFormatter.formatDate(product.createdAt)
            CartContent(
                uiState = CartUiState(cartProducts = listOf(product)),
                dateFormatter = dateFormatter,
                onDelete = {},
                onNavigateUp = {},
            )
        }

        composeRule.onNodeWithText(formattedDate).assertIsDisplayed()
    }
}
