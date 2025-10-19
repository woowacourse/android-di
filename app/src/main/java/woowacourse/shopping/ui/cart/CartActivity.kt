package woowacourse.shopping.ui.cart

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.di.DependencyInjector
import com.example.di.RequireContext
import com.example.di.RequireInjection
import com.example.di.scope.ActivityScope
import woowacourse.shopping.R
import woowacourse.shopping.databinding.ActivityCartBinding
import woowacourse.shopping.ui.ProductClickListener
import woowacourse.shopping.ui.autoViewModel

class CartActivity :
    AppCompatActivity(),
    ProductClickListener {
    init {
        DependencyInjector.injectField(this, this)
    }

    private val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }

    private val viewModel: CartViewModel by autoViewModel()

    @RequireContext(RequireContext.ContextType.ACTIVITY)
    @RequireInjection(impl = DateFormatter::class, scope = ActivityScope::class)
    private lateinit var dateFormatter: DateFormatter

    private val cartProductAdapter by lazy {
        CartProductAdapter(this, dateFormatter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupContentView()
        setupBinding()
        setupToolbar()
        setupCartProductList()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupContentView() {
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupBinding() {
        binding.lifecycleOwner = this
        binding.vm = viewModel
    }

    private fun setupCartProductList() {
        binding.rvCartProducts.adapter = cartProductAdapter
        viewModel.getAllCartProducts()
        viewModel.cartProducts.observe(this) {
            cartProductAdapter.submitList(it)
        }
        viewModel.onCartProductDeleted.observe(this) {
            if (!it) return@observe
            Toast.makeText(this, getString(R.string.cart_deleted), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onProductDeleteClicked(id: Long) {
        viewModel.deleteCartProduct(id)
    }
}
