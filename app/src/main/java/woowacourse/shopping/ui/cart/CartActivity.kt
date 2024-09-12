package woowacourse.shopping.ui.cart

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import woowa.shopping.di.libs.android.injectViewModel
import woowacourse.shopping.R
import woowacourse.shopping.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }
    private val viewModel by injectViewModel<CartViewModel>()

    private lateinit var dateFormatter: DateFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupDateFormatter()
        setupBinding()
        setupToolbar()
        setupView()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
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
        setContentView(binding.root)
    }

    private fun setupView() {
        setupCartProductData()
        setupCartProductList()
    }

    private fun setupCartProductData() {
        viewModel.loadAllCartProducts()
    }

    private fun setupCartProductList() {
        val adapter = CartProductAdapter(
            onClickDelete = viewModel::deleteCartProduct,
            dateFormatter = dateFormatter,
        )
        binding.rvCartProducts.adapter = adapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartProducts.collect {
                    adapter.submitList(it)
                }
            }
        }
        viewModel.onCartProductDeleted.observe(this) {
            if (!it) return@observe
            Toast.makeText(this, getString(R.string.cart_deleted), Toast.LENGTH_SHORT).show()
        }
    }
}
