package woowacourse.shopping.ui.cart

import android.os.Bundle
import android.widget.Toast
import com.mission.androiddi.component.activity.InjectableActivity
import com.mission.androiddi.scope.ActivityScope
import com.mission.androiddi.util.viewModel.viewModel
import com.woowacourse.bunadi.annotation.Inject
import woowacourse.shopping.R
import woowacourse.shopping.databinding.ActivityCartBinding

class CartActivity : InjectableActivity() {
    override val activityClazz = CartActivity::class
    private val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }
    private val viewModel: CartViewModel by viewModel()

    @Inject
    @ActivityScope
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
        viewModel.fetchAllCartProducts()
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
