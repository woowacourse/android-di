package woowacourse.shopping

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import woowacourse.shopping.ActivityInjectManager.ActivityLifecycle
import woowacourse.shopping.dependency.DependencyContainer
import woowacourse.shopping.provider.ProviderContainer
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

    private var _injectorInActivity: Injector? = object : Injector(DependencyContainer()) {
        override val providerContainer: ProviderContainer =
            ProviderContainer(this, dependencyContainer)
    }
    private val injectorInActivity get() = requireNotNull(_injectorInActivity)

    override fun registerActivity(activity: AppCompatActivity) {
        this._activity = activity
        this.activity.lifecycle.addObserver(this)
        // activity context는 직접 저장해준다.
        injectorInActivity.dependencyContainer
            .addInstance(Context::class, emptyList(), this.activity)
        injectProperties()
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
        // ActivityLifeCycle 어노테이션이 붙어있는 경우엔 injectorInActivity를 통해 주입해준다.
        if (property.hasAnnotation<ActivityLifecycle>()) {
            return injectorInActivity.inject(property.returnType.jvmErasure, qualifierTag)
        }
        // 그렇지 않으면 싱글턴 인젝터를 통해 주입받는다.
        return Injector.getSingletonInstance().inject(property.returnType.jvmErasure, qualifierTag)

        // 두 경우의 차이점은 전자의 경우 activity context를 갖고 있고, 후자의 경우 application context를 갖고있다는 것이다.
    }

    // 혹시 모를 메모리릭을 방지하기 위해 참조를 해제해준다.
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        _activity = null
        _injectorInActivity = null
    }
}
