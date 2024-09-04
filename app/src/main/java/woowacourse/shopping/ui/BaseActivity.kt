package woowacourse.shopping.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import woowacourse.shopping.ui.util.Injector

abstract class BaseActivity<T : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int,
) : AppCompatActivity() {
    val binding: T by lazy {
        DataBindingUtil.setContentView(this, layoutResId)
    }

    private val injector: Injector by lazy { Injector() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        injector.inject(this)
        binding.lifecycleOwner = this
    }
}
