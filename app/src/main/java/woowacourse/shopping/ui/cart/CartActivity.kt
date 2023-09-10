package woowacourse.shopping.ui.cart

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.re4rk.arkdi.ArkInject
import com.re4rk.arkdi.DiContainer
import com.re4rk.arkdi.HasDiContainer
import woowacourse.shopping.R
import woowacourse.shopping.databinding.ActivityCartBinding
import woowacourse.shopping.di.DiActivityModule
import woowacourse.shopping.ui.util.HasViewModelFactory
import woowacourse.shopping.ui.util.viewModels

class CartActivity : AppCompatActivity(), HasDiContainer, HasViewModelFactory {

    private val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }

    override lateinit var diContainer: DiContainer

    @ArkInject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CartViewModel by viewModels()

    @ArkInject
    private lateinit var dateFormatter: DateFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        diContainer = DiActivityModule(
            (application as? HasDiContainer)?.diContainer
                ?: throw IllegalStateException(""),
            this,
        )
        (diContainer as DiActivityModule).inject(this)

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
