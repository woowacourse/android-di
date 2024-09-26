package woowa.shopping.di.libs.android.sample

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import woowa.shopping.di.libs.android.R
import woowa.shopping.di.libs.android.scopeViewModel

internal class SampleActivity : AppCompatActivity() {
    val viewModel by scopeViewModel<SampleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        if (savedInstanceState == null) {
            println("SampleActivity - onCreate(): savedInstanceState is null")
            viewModel.init()
        }
        println("SampleActivity - onCreate() ")
        findViewById<Button>(R.id.sample_button).setOnClickListener {
            viewModel.updateSample()
        }
    }
}
