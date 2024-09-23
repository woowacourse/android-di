package com.woowa.di.test

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import org.junit.rules.ExternalResource
import org.robolectric.Robolectric
import org.robolectric.android.controller.ActivityController

class DIActivityTestRule<T : ComponentActivity>(private val activityClass: Class<T>) :
    ExternalResource() {
    private lateinit var activity: T
    private lateinit var controller: ActivityController<T>

    override fun before() {
        super.before()
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
    }

    fun getActivity(): T = activity

    fun getController(): ActivityController<T> = controller
}
