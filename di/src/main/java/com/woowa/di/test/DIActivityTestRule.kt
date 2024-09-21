package com.woowa.di.test

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.testing.TestLifecycleOwner
import com.woowa.di.singleton.SingletonComponent
import com.woowa.di.singleton.SingletonComponentManager
import org.junit.rules.ExternalResource
import org.robolectric.Robolectric

class DIActivityTestRule<T : ComponentActivity>(private val activityClass: Class<T>) : ExternalResource() {
    private lateinit var applicationLifecycleOwner: TestLifecycleOwner
    private lateinit var activity: T
    private lateinit var viewModelStore: ViewModelStore

    override fun before() {
        super.before()
        applicationLifecycleOwner = TestLifecycleOwner(Lifecycle.State.CREATED)

        SingletonComponentManager.binderClazzs.forEach {
            val component = SingletonComponent.getInstance(it)
            applicationLifecycleOwner.lifecycle.addObserver(component)
        }
        applicationLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        activity =
            Robolectric.buildActivity(activityClass)
                .create()
                .start()
                .resume()
                .get()
        viewModelStore = activity.viewModelStore
    }

    override fun after() {
        super.after()
        applicationLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        viewModelStore.clear()
    }

    fun getActivity(): T = activity
}
