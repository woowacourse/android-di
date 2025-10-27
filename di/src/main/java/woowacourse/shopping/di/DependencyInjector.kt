package woowacourse.shopping.di

import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.di.annotation.Qualifier
import woowacourse.shopping.di.annotation.Scoped
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
    val container: Container,
) {
    // 클래스의 인스턴스를 생성하고 의존성 주입
    fun <T : Any> create(
        kClass: KClass<T>,
        scopeName: String,
    ): T {
        val instance = createInstance(kClass, scopeName)
        injectFields(instance, kClass, scopeName)
        return instance
    }

    // @Inject 어노테이션이 붙은 필드에 의존성 주입
    fun <T : Any> injectFields(
        instance: T,
        kClass: KClass<out T>,
        scopeName: String,
    ) {
        kClass.memberProperties.forEach { property: KProperty1<out T, *> ->
            property.findAnnotation<Inject>() ?: return@forEach
            if (property !is KMutableProperty<*>) return@forEach

            val propertyType = property.returnType.classifier as? KClass<*> ?: return@forEach
            val qualifier: String? = property.findAnnotation<Qualifier>()?.value

            if (container.canResolve(propertyType, qualifier)) {
                val scope: Scope = property.findAnnotation<Scoped>()?.scope ?: Scope.FACTORY
                val dependency = container.get(propertyType, qualifier, scope, scopeName)
                property.isAccessible = true
                property.setter.call(instance, dependency)
            }
        }
    }

    // 생성자 주입으로 인스턴스 생성
    private fun <T : Any> createInstance(
        kClass: KClass<T>,
        scopeName: String,
    ): T {
        val constructor: KFunction<T>? = findInjectableConstructor(kClass)
        return if (constructor != null) {
            val args: Array<Any> =
                constructor.parameters
                    .map { param: KParameter ->
                        val paramClass: KClass<*> = param.type.classifier as KClass<*>
                        val qualifier: String? = param.findAnnotation<Qualifier>()?.value
                        val scope: Scope = param.findAnnotation<Scoped>()?.scope ?: Scope.FACTORY
                        container.get(paramClass, qualifier, scope, scopeName)
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
                paramClass != null && container.canResolve(paramClass, qualifier)
            }
        }
}
