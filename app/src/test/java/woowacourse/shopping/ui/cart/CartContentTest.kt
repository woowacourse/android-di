package woowacourse.shopping.ui.cart

import androidx.compose.ui.platform.LocalContext
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
import woowacourse.shopping.model.Product

@RunWith(RobolectricTestRunner::class)
class CartContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val product = Product(name = "우테코 과자", price = 10_000, imageUrl = "")

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
    fun `삭제 버튼을 누르면 그 상품의 위치가 전달된다`() {
        var deleted: Int? = null

        composeRule.setContent {
            CartContent(
                uiState = CartUiState(cartProducts = listOf(product)),
                dateFormatter = DateFormatter(LocalContext.current),
                onDelete = { deleted = it },
                onNavigateUp = {},
            )
        }
        composeRule.onNodeWithContentDescription("삭제").performClick()

        assertThat(deleted).isEqualTo(0)
    }
}
