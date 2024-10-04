package woowacourse.shopping.presentation.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import woowacourse.shopping.presentation.R
import woowacourse.shopping.presentation.databinding.FragmentCartBinding

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val dateFormatter by inject<DateFormatter>()

    val viewModel: CartViewModel by viewModel<CartViewModel>()

    init {
        Log.d(TAG, "init: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCartProductData()
        setupCartProductList()
    }

    private fun binding(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.vm = viewModel
        return binding.root
    }

    private fun setupCartProductList() {
        viewModel.cartProducts.observe(viewLifecycleOwner) {
            val adapter = CartProductAdapter(
                items = it,
                dateFormatter = dateFormatter,
                onClickDelete = viewModel::deleteCartProduct
            )
            binding.rvCartProducts.adapter = adapter
        }
        viewModel.onCartProductDeleted.observe(viewLifecycleOwner) {
            if (!it) return@observe
            Toast.makeText(requireContext(), getString(R.string.cart_deleted), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupCartProductData() {
        viewModel.getAllCartProducts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

private const val TAG = "CartFragment"