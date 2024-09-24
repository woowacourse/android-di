package woowacourse.shopping.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import woowacourse.shopping.R

class ProductDetailsActivity : AppCompatActivity() {

    //    private lateinit var lifecycleTracker: LifecycleTracker
    private lateinit var lifecycleEventTracker: LifecycleEventTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        setupLifecycle()
    }

    private fun setupLifecycle() {
        lifecycleEventTracker = LifecycleEventTracker()
        lifecycle.addObserver(lifecycleEventTracker)
    }
}

class LifecycleEventTracker : LifecycleEventObserver {

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        Log.d("LifecycleEventTracker", "$event")

        when (event) {
            Lifecycle.Event.ON_CREATE, Lifecycle.Event.ON_START -> {
                // ...
            }

            else -> {
                // ...
            }
        }
    }
}
//
//class LifecycleTracker : DefaultLifecycleObserver {
//
//    override fun onCreate(owner: LifecycleOwner) {
//        super.onCreate(owner)
//        Log.d("LifecycleTracker", "onCreate: $owner")
//    }
//
//    override fun onDestroy(owner: LifecycleOwner) {
//        super.onDestroy(owner)
//        Log.d("LifecycleTracker", "onDestroy: $owner")
//    }
//
//    override fun onPause(owner: LifecycleOwner) {
//        super.onPause(owner)
//        Log.d("LifecycleTracker", "onPause: $owner")
//    }
//
//    override fun onResume(owner: LifecycleOwner) {
//        super.onResume(owner)
//        Log.d("LifecycleTracker", "onResume: $owner")
//    }
//
//    override fun onStart(owner: LifecycleOwner) {
//        super.onStart(owner)
//        Log.d("LifecycleTracker", "onStart: $owner")
//    }
//
//    override fun onStop(owner: LifecycleOwner) {
//        super.onStop(owner)
//        Log.d("LifecycleTracker", "onStop: $owner")
//    }
//}
