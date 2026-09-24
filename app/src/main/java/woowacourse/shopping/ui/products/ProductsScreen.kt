package woowacourse.shopping.ui.products

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
fun ProductsScreen(
    onNavigateToCart: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductsViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val addedMessage = stringResource(R.string.cart_added)

    LaunchedEffect(Unit) {
        viewModel.getAllProducts()
    }
    LaunchedEffect(Unit) {
        viewModel.onProductAdded.collect {
            Toast.makeText(context, addedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    ProductsContent(
        uiState = uiState,
        onProductClick = viewModel::addCartProduct,
        onCartClick = onNavigateToCart,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsContent(
    uiState: ProductsUiState,
    onProductClick: (Product) -> Unit,
    onCartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = onCartClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_cart),
                            contentDescription = stringResource(R.string.menu_cart_title),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(innerPadding),
        ) {
            items(uiState.products) { product ->
                ProductItem(
                    product = product,
                    onClick = { onProductClick(product) },
                )
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        AsyncImage(
            model = product.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().aspectRatio(1f),
        )
        Text(
            text = product.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 6.dp),
        )
        Text(
            text = stringResource(R.string.product_price, product.price),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductsContentPreview() {
    ShoppingTheme {
        ProductsContent(
            uiState =
                ProductsUiState(
                    products =
                        listOf(
                            Product(name = "우테코 과자", price = 10_000, imageUrl = ""),
                            Product(name = "우테코 쥬스", price = 8_000, imageUrl = ""),
                        ),
                ),
            onProductClick = {},
            onCartClick = {},
        )
    }
}
