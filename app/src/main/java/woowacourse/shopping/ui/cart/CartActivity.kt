package woowacourse.shopping.ui.cart

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import shopping.di.DIContainer
import shopping.di.Inject
import shopping.di.Scope
import shopping.di.ScopeAnnotation
import woowacourse.shopping.R
import woowacourse.shopping.databinding.ActivityCartBinding
import woowacourse.shopping.ui.base.BaseActivity

class CartActivity : BaseActivity() {
    private val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }

    private val viewModel: CartViewModel by viewModels()

    @Inject
    @ScopeAnnotation(Scope.ACTIVITY)
    private lateinit var dateFormatter: DateFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DIContainer.register(
            clazz = DateFormatter::class.java,
            instance = DateFormatter(this),
            scope = Scope.ACTIVITY,
            owner = this
        )

        DIContainer.injectFields(this)

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
            val adapter =
                CartProductAdapter(
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
