package olive.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class DIActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(DIActivityLifecycleTracker())
    }
}
