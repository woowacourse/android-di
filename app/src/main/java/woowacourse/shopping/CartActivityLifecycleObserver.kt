package woowacourse.shopping

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import woowacourse.shopping.ui.cart.DateFormatter
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

object CartActivityLifecycleObserver : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        val activity = owner as Context
        val dateFormatter = DateFormatter(activity)
        injectDateFormatter(activity, dateFormatter)
    }

    private fun injectDateFormatter(
        activity: Context,
        dateFormatter: DateFormatter,
    ) {
        val property =
            activity::class.declaredMemberProperties
                .filterIsInstance<KMutableProperty<*>>()
                .first { it.returnType.jvmErasure == DateFormatter::class }

        property.isAccessible = true
        property.setter.call(activity, dateFormatter)
    }
}
