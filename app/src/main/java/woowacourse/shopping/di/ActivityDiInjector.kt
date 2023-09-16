package woowacourse.shopping.di

import android.content.Context
import kotlin.reflect.jvm.isAccessible

class ActivityDiInjector(
    context: Context
) {

    val lifeCycleInstances: ActivityLifeCycleInstances = ActivityLifeCycleInstances()

    val instantiator = Instantiator(ActivityModule(context))

    inline fun <reified T : Any> inject(
        target: T,
    ) {
        ClazzInfoExtractor.extractInjectMemberProperties(target::class).forEach { memberProperty ->
            val instance = lifeCycleInstances.find(memberProperty::class)
                ?: instantiator.instantiateProperty(memberProperty)
                    .also { lifeCycleInstances.add(Instance(it)) }

            memberProperty.isAccessible = true
            memberProperty.setter.call(target, instance)
        }
    }
}
