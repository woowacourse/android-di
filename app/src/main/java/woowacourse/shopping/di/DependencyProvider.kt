package woowacourse.shopping.di

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.typeOf

object DependencyProvider {
    private val Dependencies: Map<KType, Lazy<Any>> =
        mapOf(
            typeOf<ProductRepository>() to lazy { DefaultProductRepository() },
            typeOf<CartRepository>() to lazy { DefaultCartRepository() },
        )

    fun dependency(key: KType): Any = Dependencies[key]?.value ?: error("${key}에 대한 의존성이 정의되지 않았습니다.")

    fun injectToActivity(activity: Activity) {
        activity::class.memberProperties.forEach { property: KProperty1<out Activity, *> ->
            if (property.findAnnotation<InjectableViewModel>() != null && property is KMutableProperty1) {
                Log.wtf("asdf", "${property.returnType}")
                if (!property.returnType.isSubtypeOf(typeOf<ViewModel>())) error("${property.returnType}은(는) ViewModel이 아닙니다.")
                val kClass: KClass<*> = property.returnType.classifier as KClass<*>
                val viewModel: Any = kClass.createInstance()
                inject(viewModel)
                property.setter.call(activity, viewModel)
            }
        }
    }

    fun inject(target: Any) {
        val kClass = target::class
        kClass.memberProperties.forEach { property: KProperty1<out Any, *> ->
            if (property.findAnnotation<Inject>() != null && property is KMutableProperty1) {
                property.setter.call(target, dependency(property.returnType))
            }
        }
    }
}
