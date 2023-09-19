package woowacourse.shopping.ui.cart

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.ActivityInjectManager.ActivityLifecycle
import woowacourse.shopping.Inject
import woowacourse.shopping.R
import woowacourse.shopping.databinding.ActivityCartBinding
import woowacourse.shopping.ui.base.BaseActivity

class CartActivity : BaseActivity() {
    private lateinit var adapter: CartProductAdapter
    private val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }

    private val viewModel by lazy {
        ViewModelProvider(this, Factory)[CartViewModel::class.java]
    }

    @Inject
    @ActivityLifecycle
    private lateinit var dateFormatter: DateFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerActivity(this)
        addProvider(Provider())
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
        setUpAdapter()
        setupCartProductData()
        setupCartProductList()
    }

    private fun setUpAdapter() {
        adapter = CartProductAdapter(
            dateFormatter = dateFormatter,
            onClickDelete = viewModel::deleteCartProduct,
        )
        binding.rvCartProducts.adapter = adapter
    }

    private fun setupCartProductData() {
        viewModel.getAllCartProducts()
    }

    private fun setupCartProductList() {
        viewModel.cartProducts.observe(this) {
            adapter.submitList(it)
        }
        viewModel.onCartProductDeleted.observe(this) {
            if (!it) return@observe
            viewModel.getAllCartProducts()
            Toast.makeText(this, getString(R.string.cart_deleted), Toast.LENGTH_SHORT).show()
        }
    }

    class Provider() {
        fun provideDateFormatter(context: Context): DateFormatter {
            return DateFormatter(context)
        }
    }
}
