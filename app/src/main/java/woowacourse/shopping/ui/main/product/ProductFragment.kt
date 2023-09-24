package woowacourse.shopping.ui.main.product

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mission.androiddi.component.fragment.InjectableFragment
import com.mission.androiddi.util.viewModel.createViewModel
import com.woowacourse.bunadi.annotation.Inject
import woowacourse.shopping.R
import woowacourse.shopping.databinding.FragmentProductBinding
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.main.ChildShareResource

class ProductFragment : InjectableFragment() {
    override val fragmentClazz = ProductFragment::class
    private lateinit var binding: FragmentProductBinding
    private val viewModel: ProductViewModel by lazy {
        createViewModel(requireActivity())
    }

    @Inject
    private lateinit var childShareResource: ChildShareResource

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBinding()
        setupToolbar()
        setupView()

        // Fragment에서 Activity의 의존성을 주입받아 (공유하여) 사용하는 예시
        childShareResource.print("ProductFragment", requireContext())
    }

    private fun setupBinding() {
        binding.lifecycleOwner = this
        binding.vm = viewModel
    }

    private fun setupToolbar() {
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.cart -> navigateToCart()
            }
            true
        }
    }

    private fun setupView() {
        setupProductData()
        setupProductList()
    }

    private fun setupProductData() {
        viewModel.fetchAllProducts()
    }

    private fun setupProductList() {
        viewModel.products.observe(viewLifecycleOwner) {
            val adapter = ProductAdapter(
                items = it,
                onClickProduct = viewModel::addCartProduct,
            )
            binding.rvProducts.adapter = adapter
        }
        viewModel.onProductAdded.observe(viewLifecycleOwner) {
            if (!it) return@observe
            Toast.makeText(requireContext(), getString(R.string.cart_added), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun navigateToCart() {
        startActivity(Intent(requireContext(), CartActivity::class.java))
    }
}
