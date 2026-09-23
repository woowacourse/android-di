@file:Suppress("NonAsciiCharacters")

package woowacourse.shopping.ui.cart

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
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

    private val product1 =
        CartProduct(id = 1L, name = "우테코 과자", price = 10_000, imageUrl = "", createdAt = 12345678L)
    private val product2 =
        CartProduct(id = 2L, name = "우테코 까까", price = 10_000, imageUrl = "", createdAt = 12345679L)
    private val product3 =
        CartProduct(id = 3L, name = "우테코 군것질거리", price = 10_000, imageUrl = "", createdAt = 12345680L)

    @Test
    fun `장바구니에 담긴 상품의 이름이 화면에 보인다`() {
        composeRule.setContent {
            CartContent(
                uiState = CartUiState(cartProducts = listOf(product1)),
                dateFormatter = DateFormatter(LocalContext.current),
                onDelete = {},
                onNavigateUp = {},
            )
        }

        composeRule.onNodeWithText(product1.name).assertIsDisplayed()
    }

    @Test
    fun `삭제 버튼을 누르면 그 상품의 id가 전달된다`() {
        var deleted: Long? = null

        composeRule.setContent {
            CartContent(
                uiState = CartUiState(cartProducts = listOf(product1)),
                dateFormatter = DateFormatter(LocalContext.current),
                onDelete = { deleted = it },
                onNavigateUp = {},
            )
        }
        composeRule.onNodeWithContentDescription("삭제").performClick()

        assertThat(deleted).isEqualTo(1L)
    }

    @Test
    fun `목록 중간 상품을 삭제하면 해당 상품의 id가 전달된다`() {
        var deletedId: Long? = null

        composeRule.setContent {
            CartContent(
                uiState = CartUiState(cartProducts = listOf(product1, product2, product3)),
                dateFormatter = DateFormatter(LocalContext.current),
                onDelete = { deletedId = it },
                onNavigateUp = {},
            )
        }
        composeRule
            .onAllNodesWithContentDescription("삭제")[1]
            .performClick()

        assertThat(deletedId).isEqualTo(2L)
    }

    @Test
    fun `장바구니 상품을 담은 시각이 화면에 보인다`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dateFormatter = DateFormatter(context)
        val formattedDate = dateFormatter.formatDate(product1.createdAt)

        composeRule.setContent {
            CartContent(
                uiState =
                    CartUiState(
                        cartProducts = listOf(product1),
                    ),
                dateFormatter = dateFormatter,
                onDelete = {},
                onNavigateUp = {},
            )
        }

        composeRule
            .onNodeWithText(formattedDate)
            .assertIsDisplayed()
    }
}
