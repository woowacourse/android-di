package woowacourse.shopping.ui.cart

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import woowacourse.shopping.R
import woowacourse.shopping.annotation.Inject
import woowacourse.shopping.annotation.ReleasedActivityLifecycle
import woowacourse.shopping.annotation.RetainedActivityLifecycle
import woowacourse.shopping.databinding.ActivityCartBinding
import woowacourse.shopping.di.ActivityClassName
import woowacourse.shopping.ui.DiAppCompatActivity
import woowacourse.shopping.ui.viewModels

fun createDateFormatter(@ReleasedActivityLifecycle(ActivityClassName.CART_ACTIVITY) context: Context): DateFormatter =
    DateFormatter(context)

class CartActivity : DiAppCompatActivity() {
    private val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<CartViewModel>()

    @Inject
    @RetainedActivityLifecycle(ActivityClassName.CART_ACTIVITY)
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
