package woowacourse.shopping.presentation.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import woowacourse.shopping.presentation.databinding.FragmentTodayBinding
import javax.inject.Inject

@AndroidEntryPoint
class TodayFragment : Fragment() {
    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    init {
        Log.d(TAG, "init")
    }

    @Inject
    lateinit var dateFormatter: DateFormatter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvToday.text = dateFormatter.formatDate(System.currentTimeMillis())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private const val TAG = "TodayFragment"