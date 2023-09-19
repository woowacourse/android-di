package woowacourse.shopping

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import woowacourse.shopping.ActivityInjectManager.ActivityLifecycle
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

class DefaultActivityInjectManager : ActivityInjectManager, DefaultLifecycleObserver {
    private var _activity: AppCompatActivity? = null
    override val activity: AppCompatActivity get() = requireNotNull(_activity)

    private var _injectorInActivity: Injector? = object : Injector() {
        override val factoryContainer: FactoryContainer =
            FactoryContainer(this, DependencyContainer())
    }
    private val injectorInActivity get() = requireNotNull(_injectorInActivity)

    override fun addFactory(vararg factory: Any) {
        factory.forEach { injectorInActivity.factoryContainer.addProvideFactory(it) }
        injectProperties()
    }

    override fun registerActivity(activity: AppCompatActivity) {
        this._activity = activity
        this.activity.lifecycle.addObserver(this)
        injectorInActivity.dependencyContainer.addInstance(
            Context::class,
            emptyList(),
            this.activity,
        )
    }

    private fun injectProperties() {
        activity::class.declaredMemberProperties.forEach { property ->
            if (!property.hasAnnotation<Inject>()) return@forEach
            val qualifierTag = property.findAnnotation<Qualifier>()?.className
            property.isAccessible = true
            property.javaField?.set(activity, getPropertyInstance(property, qualifierTag))
        }
    }

    private fun getPropertyInstance(
        property: KProperty1<out AppCompatActivity, *>,
        qualifierTag: String?,
    ): Any {
        if (property.hasAnnotation<ActivityLifecycle>()) {
            return injectorInActivity.inject(property.returnType.jvmErasure, qualifierTag)
        }
        return Injector.getSingletonInstance().inject(property.returnType.jvmErasure, qualifierTag)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        _activity = null
        _injectorInActivity = null
    }
}
