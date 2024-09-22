package woowacourse.shopping.ui.product

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import com.example.di.DIActivity
import com.example.di.DIModule
import com.example.di.viewmodel.provideViewModel
import woowacourse.shopping.R
import woowacourse.shopping.databinding.ActivityProductBinding
import woowacourse.shopping.module.ProductModule
import woowacourse.shopping.ui.cart.CartActivity

class ProductActivity : DIActivity() {
    private val binding by lazy { ActivityProductBinding.inflate(layoutInflater) }

    private val viewModel: ProductViewModel by provideViewModel(ProductModule::class)

    override val module: DIModule = ProductModule()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupBinding()
        setupToolbar()
        setupView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cart_menu, menu)
        menu?.findItem(R.id.cart)?.actionView?.let { view ->
            view.setOnClickListener { navigateToCart() }
        }
        return true
    }

    private fun setupBinding() {
        binding.lifecycleOwner = this
        binding.vm = viewModel
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupView() {
        setupProductData()
        setupProductList()
    }

    private fun setupProductData() {
        viewModel.getAllProducts()
    }

    private fun setupProductList() {
        viewModel.products.observe(this) {
            val adapter =
                ProductAdapter(
                    items = it,
                    onClickProduct = viewModel::addCartProduct,
                )
            binding.rvProducts.adapter = adapter
        }
        viewModel.onProductAdded.observe(this) {
            if (!it) return@observe
            Toast.makeText(this, getString(R.string.cart_added), Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToCart() {
        startActivity(Intent(this, CartActivity::class.java))
    }
}
