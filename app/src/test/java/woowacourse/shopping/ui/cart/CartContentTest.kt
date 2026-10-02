package woowacourse.shopping.ui.cart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
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
        CartProduct(
            id = 10L,
            name = "우테코 과자",
            price = 10_000,
            imageUrl = "",
            createdAt = 0L,
        )

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
    fun `중간 상품을 삭제하면 선택한 상품을 제외한 상품들이 남는다`() {
        val products =
            listOf(
                product.copy(id = 1L, name = "첫 번째 상품"),
                product.copy(id = 2L, name = "두 번째 상품"),
                product.copy(id = 3L, name = "세 번째 상품"),
            )

        composeRule.setContent {
            var cartProducts by remember { mutableStateOf(products) }

            CartContent(
                uiState = CartUiState(cartProducts = cartProducts),
                dateFormatter = DateFormatter(LocalContext.current),
                onDelete = { id ->
                    cartProducts = cartProducts.filterNot { cartProduct -> cartProduct.id == id }
                },
                onNavigateUp = {},
            )
        }

        composeRule.onAllNodesWithContentDescription("삭제")[1].performClick()

        composeRule.onNodeWithText("첫 번째 상품").assertIsDisplayed()
        composeRule.onNodeWithText("두 번째 상품").assertDoesNotExist()
        composeRule.onNodeWithText("세 번째 상품").assertIsDisplayed()
    }

    @Test
    fun `장바구니에 상품을 담은 시각이 화면에 보인다`() {
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

    @Test
    fun `삭제 버튼을 누르면 그 상품의 식별자가 전달된다`() {
        var deleted: Long? = null

        composeRule.setContent {
            CartContent(
                uiState = CartUiState(cartProducts = listOf(product)),
                dateFormatter = DateFormatter(LocalContext.current),
                onDelete = { deleted = it },
                onNavigateUp = {},
            )
        }
        composeRule.onNodeWithContentDescription("삭제").performClick()

        assertThat(deleted).isEqualTo(product.id)
    }
}
