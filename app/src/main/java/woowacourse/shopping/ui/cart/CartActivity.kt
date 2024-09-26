package woowacourse.shopping.ui.cart

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alsonglibrary2.di.ActivityLifecycleObserver
import com.example.alsonglibrary2.di.anotations.ActivityScope
import com.example.alsonglibrary2.di.anotations.FieldInject
import com.example.alsonglibrary2.di.createAutoDIViewModel
import woowacourse.shopping.R
import woowacourse.shopping.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }

    private val viewModel by createAutoDIViewModel<CartViewModel>()

    @ActivityScope
    @FieldInject
    private lateinit var _dateFormatter: DateFormatter
    val dateFormatter: DateFormatter get() = _dateFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(ActivityLifecycleObserver())
    }

    override fun onStart() {
        super.onStart()
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
}
