package woowacourse.shopping.ui.cart

import android.os.Bundle
import android.widget.Toast
import com.example.di.DIActivity
import com.example.di.DIModule
import com.example.di.annotation.Inject
import com.example.di.viewmodel.provideViewModel
import woowacourse.shopping.R
import woowacourse.shopping.application.ActivityModule
import woowacourse.shopping.databinding.ActivityCartBinding

class CartActivity : DIActivity() {
    private val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }

    private val viewModel: CartViewModel by provideViewModel()

    private lateinit var adapter: CartProductAdapter

    @Inject
    private lateinit var dateFormatter: DateFormatter

    override val module: DIModule by lazy { ActivityModule(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupToolbar()
        setupView()
        setUpAdapter()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupBinding() {
        binding.lifecycleOwner = this
        binding.vm = viewModel
        setContentView(binding.root)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupView() {
        setupCartProductData()
        setupCartProductList()
    }

    private fun setUpAdapter() {
        adapter =
            CartProductAdapter(
                onClickDelete = viewModel::deleteCartProduct,
                dateFormatter = dateFormatter,
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
            Toast.makeText(this, getString(R.string.cart_deleted), Toast.LENGTH_SHORT).show()
        }
    }
}
