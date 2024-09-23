package com.woowa.di.test

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.testing.TestLifecycleOwner
import com.woowa.di.singleton.SingletonComponent
import com.woowa.di.singleton.SingletonComponentManager
import org.junit.rules.ExternalResource
import org.robolectric.Robolectric
import org.robolectric.android.controller.ActivityController
import kotlin.reflect.KClass

class DIActivityTestRule<T : ComponentActivity>(private val activityClass: Class<T>) :
    ExternalResource() {
    private lateinit var activity: T
    private lateinit var controller: ActivityController<T>
    private lateinit var applicationLifecycleOwner: TestLifecycleOwner


    override fun before() {
        super.before()
        applicationLifecycleOwner = TestLifecycleOwner(Lifecycle.State.CREATED)
        val component = SingletonComponent.getInstance()
        applicationLifecycleOwner.lifecycle.addObserver(component)
        applicationLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        controller = Robolectric.buildActivity(activityClass)
        activity =
            controller.create()
                .get()
    }

    override fun after() {
        super.after()
        if (activity.lifecycle.currentState != Lifecycle.State.DESTROYED) {
            controller.destroy()
        }
        applicationLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)

    }

    fun getActivity(): T = activity

    fun getController(): ActivityController<T> = controller
}
