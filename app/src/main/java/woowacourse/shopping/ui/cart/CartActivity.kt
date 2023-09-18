package woowacourse.shopping.ui.cart

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.R
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.annotation.Inject
import woowacourse.shopping.annotation.ReleasedActivityLifecycle
import woowacourse.shopping.annotation.RetainedActivityLifecycle
import woowacourse.shopping.databinding.ActivityCartBinding
import woowacourse.shopping.di.ActivityClassName
import woowacourse.shopping.lifecycleobserver.ActivityLifecycleObserver
import woowacourse.shopping.lifecycleobserver.DefaultActivityLifecycleObserver
import woowacourse.shopping.ui.viewModelFactory

fun createDateFormatter(@ReleasedActivityLifecycle(ActivityClassName.CART_ACTIVITY) context: Context): DateFormatter = DateFormatter(context)

class CartActivity :
    AppCompatActivity(),
    ActivityLifecycleObserver by DefaultActivityLifecycleObserver() {

    private val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CartViewModel::class.java]
    }

    @Inject
    @RetainedActivityLifecycle(ActivityClassName.CART_ACTIVITY)
    private lateinit var dateFormatter: DateFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupToolbar()
        setupView()

        setupLifecycleObserver()
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

    private fun setupLifecycleObserver() {
        val shoppingApplication = application as ShoppingApplication
        setupLifecycleObserver(
            lifecycle = lifecycle,
            activity = this,
            injector = shoppingApplication.injector,
        )
    }
}
