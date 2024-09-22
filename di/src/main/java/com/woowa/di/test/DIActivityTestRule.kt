package com.woowa.di.test

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.testing.TestLifecycleOwner
import com.woowa.di.singleton.SingletonComponent
import com.woowa.di.singleton.SingletonComponentManager
import org.junit.rules.ExternalResource
import org.robolectric.Robolectric
import org.robolectric.android.controller.ActivityController

class DIActivityTestRule<T : ComponentActivity>(private val activityClass: Class<T>) :
    ExternalResource() {
    private lateinit var applicationLifecycleOwner: TestLifecycleOwner
    private lateinit var activity: T
    private lateinit var viewModelStore: ViewModelStore
    private lateinit var controller: ActivityController<T>

    override fun before() {
        super.before()
        applicationLifecycleOwner = TestLifecycleOwner(Lifecycle.State.CREATED)

        SingletonComponentManager.binderClazzs.forEach {
            val component = SingletonComponent.getInstance(it)
            applicationLifecycleOwner.lifecycle.addObserver(component)
        }
        applicationLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        controller = Robolectric.buildActivity(activityClass)
        activity =
            controller.create()
                .get()
        viewModelStore = activity.viewModelStore
    }

    override fun after() {
        super.after()
        applicationLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        viewModelStore.clear()
        controller.destroy()
    }

    fun getActivity(): T = activity
}
