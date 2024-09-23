package woowacourse.shopping.ui.cart

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zzang.di.DIContainer.clearActivityScopedInstances
import com.zzang.di.DependencyInjector
import com.zzang.di.annotation.Inject
import com.zzang.di.provideViewModel
import woowacourse.shopping.R
import woowacourse.shopping.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }

    private val viewModel: CartViewModel by provideViewModel()

    @Inject
    private lateinit var dateFormatter: DateFormatter
    private lateinit var adapter: CartProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DependencyInjector.injectDependencies(this,this)
        setupBinding()
        setupToolbar()
        setupView()
        viewModel.getAllCartProducts()
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
        adapter =
            CartProductAdapter(
                dateFormatter = dateFormatter,
                onClickDelete = viewModel::deleteCartProduct,
            )
        binding.rvCartProducts.adapter = adapter

        viewModel.cartProducts.observe(this) { updatedList ->
            adapter.submitList(updatedList)
        }

        viewModel.onCartProductDeleted.observe(this) {
            if (!it) return@observe
            Toast.makeText(this, getString(R.string.cart_deleted), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearActivityScopedInstances(this)
    }
}
