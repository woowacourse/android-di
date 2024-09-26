package woowacourse.shopping.ui.cart

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.woowacourse.di.Inject
import woowacourse.shopping.GlobalViewModelFactory
import woowacourse.shopping.LifecycleTracker
import woowacourse.shopping.R
import woowacourse.shopping.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<CartViewModel> { GlobalViewModelFactory() }

    private lateinit var adapter: CartProductAdapter

    @Inject
    lateinit var dateFormatter: DateFormatter

    private lateinit var lifecycleTracker: LifecycleTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLifecycleTracker()
        setupDateFormatter()
        setupBinding()
        setupToolbar()
        setupView()
        observeCartProducts()
        observeDeletedCartProductPosition()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupLifecycleTracker() {
        lifecycleTracker = LifecycleTracker()
        lifecycle.addObserver(lifecycleTracker)
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
        viewModel.getAllCartProducts()
    }

    private fun setupCartProductList() {
        adapter =
            CartProductAdapter(
                viewModel,
                dateFormatter = dateFormatter,
            )
    }

    private fun observeCartProducts() {
        viewModel.cartProducts.observe(this) {
            adapter.updateCartProducts(it)
            binding.rvCartProducts.adapter = adapter
        }
    }

    private fun observeDeletedCartProductPosition() {
        viewModel.deletedCartProductPosition.observe(this) { position ->
            if (position == null) return@observe
            adapter.removeItem(position)
            Toast.makeText(this, getString(R.string.cart_deleted), Toast.LENGTH_SHORT).show()
        }
    }
}
