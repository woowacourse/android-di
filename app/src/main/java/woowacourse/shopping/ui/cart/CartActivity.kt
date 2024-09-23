package woowacourse.shopping.ui.cart

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.alsonglibrary2.di.createAutoDIViewModel
import woowacourse.shopping.R
import woowacourse.shopping.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }

    private val viewModel by createAutoDIViewModel<CartViewModel>()

    private var _dateFormatter: DateFormatter? = null
    private val dateFormatter: DateFormatter get() = _dateFormatter!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupToolbar()
        setupView()

        setUpLifecycle()
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
        viewModel.cartProducts.observe(this) { products ->
            val adapter =
                CartProductAdapter(
                    items = products,
                    dateFormatter = dateFormatter,
                    onClickDelete = { position ->
                        viewModel.deleteCartProduct(products[position].id)
                    },
                )
            binding.rvCartProducts.adapter = adapter
        }
        viewModel.onCartProductDeleted.observe(this) {
            if (!it) return@observe
            Toast.makeText(this, getString(R.string.cart_deleted), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpLifecycle() {
        lifecycle.addObserver(
            object : LifecycleEventObserver {
                override fun onStateChanged(
                    source: LifecycleOwner,
                    event: Lifecycle.Event,
                ) {
                    when (event) {
                        Lifecycle.Event.ON_CREATE -> _dateFormatter = DateFormatter(this@CartActivity)
                        Lifecycle.Event.ON_DESTROY -> _dateFormatter = null
                        else -> {}
                    }
                }
            },
        )
    }
}
