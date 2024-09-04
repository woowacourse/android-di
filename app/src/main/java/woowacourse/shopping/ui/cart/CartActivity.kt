package woowacourse.shopping.ui.cart

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import woowacourse.shopping.R
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.databinding.ActivityCartBinding
import woowacourse.shopping.ui.BaseActivity
import woowacourse.shopping.ui.util.Injector
import woowacourse.shopping.ui.util.viewModelFactory

class CartActivity : BaseActivity<ActivityCartBinding>(R.layout.activity_cart) {
    private lateinit var cartRepository: CartRepository

    private val viewModel: CartViewModel by viewModels {
        viewModelFactory { CartViewModel(cartRepository = cartRepository) }
    }

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
        binding.vm = viewModel
    }

    private fun setupView() {
        setupCartProductData()
        setupCartProductList()
    }

    private fun setupCartProductData() {
        viewModel.getAllCartProducts()
    }

    private fun setupCartProductList() {
        viewModel.cartProducts.observe(this) {
            val adapter = CartProductAdapter(
                items = it,
                dateFormatter = dateFormatter,
                onClickDelete = viewModel::deleteCartProduct
            )
            binding.rvCartProducts.adapter = adapter
        }
        viewModel.onCartProductDeleted.observe(this) {
            if (!it) return@observe
            Toast.makeText(this, getString(R.string.cart_deleted), Toast.LENGTH_SHORT).show()
        }
    }
}
