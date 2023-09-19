package woowacourse.shopping.ui.cart

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dygames.android_di.ViewModelDependencyInjector.injectViewModel
import com.dygames.android_di.lifecycle.LifecycleWatcherActivity
import woowacourse.shopping.R
import woowacourse.shopping.databinding.ActivityCartBinding
import kotlin.reflect.typeOf

class CartActivity : LifecycleWatcherActivity(typeOf<CartActivity>()) {

    private val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }

    private val viewModel by lazy {
        ViewModelProvider(this, injectViewModel<CartViewModel>())[CartViewModel::class.java]
    }

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
                dateFormatter = inject(),
                onClickDelete = { cartProduct ->
                    viewModel.deleteCartProduct(cartProduct)
                }
            )
            binding.rvCartProducts.adapter = adapter
        }
        viewModel.onCartProductDeleted.observe(this) {
            if (!it) return@observe
            Toast.makeText(this, getString(R.string.cart_deleted), Toast.LENGTH_SHORT).show()
        }
    }
}
