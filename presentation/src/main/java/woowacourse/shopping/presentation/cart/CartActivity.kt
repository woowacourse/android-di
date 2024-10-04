package woowacourse.shopping.presentation.cart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import woowacourse.shopping.presentation.R
import woowacourse.shopping.presentation.databinding.ActivityCartBinding

@AndroidEntryPoint
class CartActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setupToolbar()
        initFragments(savedInstanceState)

    }

    private fun initFragments(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, CartFragment::class.java, null)
            }
        }

        with(binding) {
            btnCart.setOnClickListener {
                supportFragmentManager.commit {
                    replace(R.id.fragment_container, woowacourse.shopping.presentation.cart.CartFragment::class.java, null)
                }
            }
            btnToday.setOnClickListener {
                supportFragmentManager.commit {
                    replace(R.id.fragment_container, woowacourse.shopping.presentation.cart.TodayFragment::class.java, null)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
