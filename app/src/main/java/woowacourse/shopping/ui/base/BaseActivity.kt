package woowacourse.shopping.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import shopping.di.DIContainer
import shopping.di.ScopeOwner

abstract class BaseActivity : AppCompatActivity(), ScopeOwner {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DIContainer.createActivityScope(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        DIContainer.clearActivityScope(this)
    }
}
