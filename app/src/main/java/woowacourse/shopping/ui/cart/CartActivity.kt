package woowacourse.shopping.ui.cart

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.hyegyeong.di.DiActivity
import com.hyegyeong.di.DiModule
import com.hyegyeong.di.annotations.Inject
import woowacourse.shopping.R
import woowacourse.shopping.data.ViewModelFactory
import woowacourse.shopping.data.di.CartActivityDiModule
import woowacourse.shopping.databinding.ActivityCartBinding

class CartActivity : DiActivity() {
    override val module: DiModule = CartActivityDiModule(this)

    private val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }

    private val viewModel by lazy {
        ViewModelProvider(
            this, ViewModelFactory.provide<CartViewModel>()
        )[CartViewModel::class.java]
    }

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
