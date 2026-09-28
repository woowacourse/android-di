package woowacourse.shopping.ui.cart

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertCountEquals
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
import org.robolectric.annotation.Config
import woowacourse.shopping.model.CartProduct

@RunWith(RobolectricTestRunner::class)
class CartContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val product =
        CartProduct(
            id = 42L,
            name = "우테코 과자",
            price = 10_000,
            imageUrl = "",
            createdAt = 1_700_000_000_000L,
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

    @Test
    fun `장바구니에 담은 시각을 날짜 형식으로 표시한다`() {
        val dateFormatter = DateFormatter(ApplicationProvider.getApplicationContext())
        composeRule.setContent {
            CartContent(
                uiState = CartUiState(cartProducts = listOf(product)),
                dateFormatter = dateFormatter,
                onDelete = {},
                onNavigateUp = {},
            )
        }

        composeRule.onNodeWithText(dateFormatter.formatDate(product.createdAt)).assertIsDisplayed()
    }

    @Test
    @Config(qualifiers = "h1200dp")
    fun `중간 항목 삭제 후 남은 항목을 눌러도 원래 식별자를 전달한다`() {
        val first = product.copy(id = 10L, name = "첫 상품")
        val middle = product.copy(id = 30L, name = "가운데 상품")
        val last = product.copy(id = 70L, name = "마지막 상품")
        val state = mutableStateOf(CartUiState(listOf(first, middle, last)))
        val deletedIds = mutableListOf<Long>()
        composeRule.setContent {
            CartContent(
                uiState = state.value,
                dateFormatter = DateFormatter(LocalContext.current),
                onDelete = { id ->
                    deletedIds.add(id)
                    state.value = CartUiState(state.value.cartProducts.filterNot { it.id == id })
                },
                onNavigateUp = {},
            )
        }

        composeRule.onAllNodesWithContentDescription("삭제").assertCountEquals(3)
        composeRule.onAllNodesWithContentDescription("삭제")[1].performClick()
        composeRule.onAllNodesWithContentDescription("삭제").assertCountEquals(2)
        composeRule.onAllNodesWithContentDescription("삭제")[1].performClick()

        composeRule.runOnIdle {
            assertThat(deletedIds).containsExactly(middle.id, last.id).inOrder()
            assertThat(state.value.cartProducts).containsExactly(first)
        }
    }
}
