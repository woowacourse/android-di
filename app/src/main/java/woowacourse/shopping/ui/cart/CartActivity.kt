package woowacourse.shopping.ui.cart

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.created.customdi.annotation.Field
import woowacourse.shopping.R
import woowacourse.shopping.databinding.ActivityCartBinding
import woowacourse.shopping.di.inject
import woowacourse.shopping.ui.cart.adapter.CartProductAdapter

class CartActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        ViewModelProvider(this, inject<CartViewModel>())[CartViewModel::class.java]
    }

    @Field
    private lateinit var dateFormatter: DateFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupBinding()
        setupDateFormatter()
        setupToolbar()
        setupView()
    }

    private fun setupDateFormatter() {
        dateFormatter = DateFormatter(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupBinding() {
        binding.lifecycleOwner = this
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
                onClickDelete = viewModel::deleteCartProduct,
            )
            binding.rvCartProducts.adapter = adapter
        }
        viewModel.onCartProductDeleted.observe(this) {
            if (!it) return@observe
            Toast.makeText(this, getString(R.string.cart_deleted), Toast.LENGTH_SHORT).show()
        }
    }
}
