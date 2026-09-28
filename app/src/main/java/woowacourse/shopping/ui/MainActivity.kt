package woowacourse.shopping.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.harodi.Inject
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.ui.theme.ShoppingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val diManager = (application as ShoppingApplication).diManager

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingTheme {
                val a = diManager.fieldInject(A::class.java)
                a.b
                a.b.a
            }
        }
    }
}

class A {
    @Inject
    lateinit var b: B
}

class B {
    @Inject
    lateinit var a: A
}
