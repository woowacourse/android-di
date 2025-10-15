package woowacourse.shopping.ui.cart

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import woowacourse.bibi.di.androidx.injectedViewModel
import woowacourse.shopping.R
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.databinding.ActivityCartBinding

class CartActivity :
    AppCompatActivity(),
    CartProductClickListener {
    private val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }

    private val viewModel by lazy {
        injectedViewModel<CartViewModel> { (application as ShoppingApplication).container }
    }

    private lateinit var dateFormatter: DateFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupContentView()
        setupDateFormatter()
        setupBinding()
        setupToolbar()
        setupViewData()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupContentView() {
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupDateFormatter() {
        dateFormatter = DateFormatter(this)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupBinding() {
        binding.lifecycleOwner = this
        binding.vm = viewModel
    }

    private fun setupViewData() {
        setupCartProductData()
        setupCartProductList()
    }

    private fun setupCartProductData() {
        viewModel.getAllCartProducts()
    }

    private fun setupCartProductList() {
        val adapter = CartProductAdapter(dateFormatter, this)
        binding.rvCartProducts.adapter = adapter

        viewModel.cartProducts.observe(this) {
            adapter.submitList(it)
        }

        viewModel.onCartProductDeleted.observe(this) {
            if (!it) return@observe
            Toast.makeText(this, getString(R.string.cart_deleted), Toast.LENGTH_SHORT).show()
        }

        viewModel.errorMessage.observe(this) { message ->
            if (!message.isNullOrBlank()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                viewModel.consumeError()
            }
        }
    }

    override fun onDeleteClicked(id: Long) {
        viewModel.deleteCartProduct(id)
    }
}
