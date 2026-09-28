package woowacourse.shopping.ui.cart

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.shopping.model.CartProduct

@RunWith(RobolectricTestRunner::class)
class CartContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val cartProduct =
        CartProduct(
            id = 7,
            name = "우테코 과자",
            price = 10_000,
            imageUrl = "",
            createdAt = 0,
        )

    @Test
    fun `장바구니에 담긴 상품의 이름과 담은 시각이 화면에 보인다`() {
        val dateFormatter = DateFormatter(RuntimeEnvironment.getApplication())

        composeRule.setContent {
            CartContent(
                uiState = CartUiState(cartProducts = listOf(cartProduct)),
                dateFormatter = dateFormatter,
                onDelete = {},
                onNavigateUp = {},
            )
        }

        composeRule.onNodeWithText(cartProduct.name).assertIsDisplayed()
        composeRule.onNodeWithText(dateFormatter.formatDate(cartProduct.createdAt)).assertIsDisplayed()
    }

    @Test
    fun `삭제 버튼을 누르면 그 상품의 식별자가 전달된다`() {
        var deleted: Long? = null
        val dateFormatter = DateFormatter(RuntimeEnvironment.getApplication())

        composeRule.setContent {
            CartContent(
                uiState = CartUiState(cartProducts = listOf(cartProduct)),
                dateFormatter = dateFormatter,
                onDelete = { deleted = it },
                onNavigateUp = {},
            )
        }
        composeRule.onNodeWithContentDescription("삭제").performClick()

        assertThat(deleted).isEqualTo(cartProduct.id)
    }
}
