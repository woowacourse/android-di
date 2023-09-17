package woowacourse.shopping.ui.cart

import android.os.Bundle
import android.widget.Toast
import com.boogiwoogi.di.Inject
import com.boogiwoogi.di.version2.woogiViewModels
import woowacourse.shopping.R
import woowacourse.shopping.databinding.ActivityCartBinding
import woowacourse.shopping.di.activity.DiActivity

class CartActivity : DiActivity() {

    private val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }

    private val viewModel by woogiViewModels<CartViewModel>()

    @Inject
    private lateinit var dateFormatter: DateFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupToolbar()
        setupView()
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
        setContentView(binding.root)
    }

    private fun setupView() {
        setupCartProductData()
        setupCartProductList()
    }

    private fun setupCartProductData() {
        viewModel.getAllCartProducts()
    }

    private fun setupCartProductList() {
        viewModel.uiState.observe(this) {
            val adapter = CartProductAdapter(
                items = it.cartProducts,
                dateFormatter = dateFormatter,
                onClickDelete = it.onDelete
            )
            binding.rvCartProducts.adapter = adapter
        }
        viewModel.onCartProductDeleted.observe(this) {
            if (!it) return@observe
            Toast.makeText(this, getString(R.string.cart_deleted), Toast.LENGTH_SHORT).show()
        }
    }
}
