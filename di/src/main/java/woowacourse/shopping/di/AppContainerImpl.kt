package woowacourse.shopping.di

import android.util.Log
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation

private data class DependencyKey(
    val type: KType,
    val qualifier: KClass<out Annotation>?,
)

class AppContainerImpl : Container {
    private val registry = mutableMapOf<DependencyKey, (Container) -> Any>()
    private val singletonInstances = mutableMapOf<DependencyKey, Any>()

    fun <T : Any, Impl : T> register(
        type: KType,
        implementationClass: KClass<Impl>,
        qualifier: Annotation? = null,
        factory: (Container) -> Impl,
    ) {
        val key = DependencyKey(type, qualifier?.annotationClass)
        registry[key] = factory as (Container) -> Any

        if (implementationClass.findAnnotation<Singleton>() != null) {
            if (!singletonInstances.containsKey(key)) {
                singletonInstances[key] = factory(this)
            }
        }
    }

    override fun resolve(
        requestedType: KType,
        qualifier: Annotation?,
    ): Any? {
        val key = DependencyKey(requestedType, qualifier?.annotationClass)

        val singleton = singletonInstances[key]
        if (singleton != null) {
//            Log.d("DI_LIFECYCLE", "캐시 재활용: 요청 타입 $requestedType")
            return singleton
        }

        val factory = registry[key] ?: return null

//        Log.d("DI_LIFECYCLE", "팩토리 실행/인스턴스 생성: 요청 타입 $requestedType")

        return factory(this)
    }
}
