package woowacourse.shopping.di

import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.di.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class DependencyInjector(
    private val appContainer: AppContainer,
) {
    // 클래스의 인스턴스를 생성하고 의존성 주입
    fun <T : Any> create(kClass: KClass<T>): T {
        val instance = createInstance(kClass)
        injectFields(instance, kClass)
        return instance
    }

    // 생성자 주입으로 인스턴스 생성
    private fun <T : Any> createInstance(kClass: KClass<T>): T {
        val constructor: KFunction<T>? = findInjectableConstructor(kClass)
        return if (constructor != null) {
            val args: Array<Any> =
                constructor.parameters
                    .map { param: KParameter ->
                        val paramClass: KClass<*> = param.type.classifier as KClass<*>
                        val qualifier: String? = param.findAnnotation<Qualifier>()?.value
                        appContainer.get(paramClass, qualifier)
                    }.toTypedArray()
            constructor.call(*args)
        } else {
            kClass.createInstance()
        }
    }

    // 모든 파라미터를 주입 가능한 생성자를 찾음
    private fun <T : Any> findInjectableConstructor(kClass: KClass<T>): KFunction<T>? =
        kClass.constructors.firstOrNull { constructor: KFunction<T> ->
            constructor.parameters.all { param: KParameter ->
                val paramClass: KClass<*>? = param.type.classifier as? KClass<*>
                val qualifier: String? = param.findAnnotation<Qualifier>()?.value
                paramClass != null && appContainer.canResolve(paramClass, qualifier)
            }
        }

    // @Inject 어노테이션이 붙은 필드에 의존성 주입
    private fun <T : Any> injectFields(
        instance: T,
        kClass: KClass<T>,
    ) {
        kClass.memberProperties.forEach { property: KProperty1<T, *> ->
            property.findAnnotation<Inject>() ?: return@forEach
            if (property !is KMutableProperty<*>) return@forEach

            val propertyType = property.returnType.classifier as? KClass<*> ?: return@forEach
            val qualifier: String? = property.findAnnotation<Qualifier>()?.value
            if (appContainer.canResolve(propertyType, qualifier)) {
                val dependency = appContainer.get(propertyType, qualifier)
                property.isAccessible = true
                property.setter.call(instance, dependency)
            }
        }
    }
}
