package woowacourse.shopping.ui.cart

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import woowacourse.shopping.R
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.theme.ShoppingTheme

@Composable
fun CartScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val deletedMessage = stringResource(R.string.cart_deleted)

    // TODO: Step4 - DateFormatter를 화면 스코프의 의존성으로 주입받도록 변경
    val dateFormatter = remember { DateFormatter(context) }

    LaunchedEffect(Unit) {
        viewModel.getAllCartProducts()
    }
    LaunchedEffect(Unit) {
        viewModel.onCartProductDeleted.collect {
            Toast.makeText(context, deletedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    CartContent(
        uiState = uiState,
        dateFormatter = dateFormatter,
        onDelete = viewModel::deleteCartProduct,
        onNavigateUp = onNavigateUp,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartContent(
    uiState: CartUiState,
    dateFormatter: DateFormatter,
    onDelete: (Int) -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.cart_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
        ) {
            itemsIndexed(uiState.cartProducts) { index, cartProduct ->
                CartProductItem(
                    cartProduct = cartProduct,
                    dateFormatter = dateFormatter,
                    onDelete = { onDelete(index) },
                )
            }
        }
    }
}

@Composable
fun CartProductItem(
    cartProduct: Product,
    dateFormatter: DateFormatter,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .padding(20.dp),
    ) {
        // TODO: Step2 - dateFormatter를 활용하여 상품이 담긴 날짜와 시간을 출력하도록 변경
        Text(text = "", style = MaterialTheme.typography.labelSmall)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = cartProduct.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = onDelete) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = stringResource(R.string.delete_cart_product),
                )
            }
        }
        AsyncImage(
            model = cartProduct.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .padding(top = 10.dp)
                    .width(136.dp)
                    .height(72.dp),
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.product_price, cartProduct.price),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.align(Alignment.CenterEnd),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CartContentPreview() {
    ShoppingTheme {
        CartContent(
            uiState =
                CartUiState(
                    cartProducts = listOf(Product(name = "우테코 과자", price = 10_000, imageUrl = "")),
                ),
            dateFormatter = DateFormatter(LocalContext.current),
            onDelete = {},
            onNavigateUp = {},
        )
    }
}
