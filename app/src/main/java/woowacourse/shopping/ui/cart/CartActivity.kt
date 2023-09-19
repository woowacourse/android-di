package woowacourse.shopping.ui.cart

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.Toast
import com.angrypig.autodi.Injector
import com.angrypig.autodi.injectViewModel
import com.angrypig.autodi.lifeCycleScopeHandler.AutoDIScopedActivity
import com.angrypig.autodi.lifeCycleScopeHandler.ScopedProperty
import com.angrypig.autodi.lifeCycleScopeHandler.annotation.activity.ActivityLifeCycleScope.*
import com.angrypig.autodi.lifeCycleScopeHandler.annotation.activity.ActivityScope
import woowacourse.shopping.R
import woowacourse.shopping.databinding.ActivityCartBinding

@ActivityScope(ACTIVITY_RETAINED)
class CartActivity() : AutoDIScopedActivity<CartActivity>(), Parcelable {

    override val registerScope: CartActivity = this

    private val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }

    @ScopedProperty
    private val viewModel: CartViewModel by injectViewModel()

    @ScopedProperty
    private val dateFormatter: DateFormatter by Injector("disposable")

    constructor(parcel: Parcel) : this() {
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartActivity> {
        override fun createFromParcel(parcel: Parcel): CartActivity {
            return CartActivity(parcel)
        }

        override fun newArray(size: Int): Array<CartActivity?> {
            return arrayOfNulls(size)
        }
    }
}
