package com.example.di.di

import com.example.di.annotation.Inject
import com.example.di.annotation.Qualifier
import com.example.di.annotation.Scope
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

/**
 * 의존성 주입(Dependency Injection)을 처리하는 클래스.
 * 주어진 KClass<T>에 대한 인스턴스를 생성하고, `@Inject` 어노테이션이 붙은 생성자 파라미터와 프로퍼티에
 * 알맞은 의존성을 주입합니다.
 *
 * 의존성은 `AppContainer`를 통해 관리되며, `@Qualifier` 어노테이션을 사용하여
 * 특정 구현체를 명시적으로 지정할 수 있습니다.
 *
 * @property appContainer 의존성 인스턴스를 관리하고 제공하는 컨테이너.
 */
class DependencyInjector(
    private val appContainer: AppContainer,
) {
    fun <T : Any> inject(
        kClass: KClass<T>,
        scopeHolder: Any? = null,
    ): T {
        val constructor: KFunction<T> =
            kClass.primaryConstructor ?: throw IllegalArgumentException("주생성자가 없습니다.")
        val arguments = mutableMapOf<KParameter, Any?>()

        // 생성자 주입
        constructor.parameters.forEach { param: KParameter ->
            val injectAnnotation = param.findAnnotation<Inject>()
            if (injectAnnotation != null) {
                // Qualifier가 있으면 해당 타입으로, 아니면 파라미터 타입으로
                val qualifier = param.findAnnotation<Qualifier>()
                val dependencyClass: KClass<out Any> =
                    qualifier?.value ?: param.type.classifier as? KClass<out Any>
                        ?: throw IllegalStateException("의존성 타입을 확인할 수 없습니다: ${param.name}")

                val dependencyInstance = appContainer.getInstance(dependencyClass, scopeHolder)
                arguments[param] = dependencyInstance
            }
        }

        // 인스턴스 생성
        val instance: T = constructor.callBy(arguments)

        // @Inject 어노테이션이 붙은 필드 주입
        kClass.declaredMemberProperties.forEach { prop: KProperty1<T, *> ->
            val injectAnnotation = prop.findAnnotation<Inject>()
            if (injectAnnotation != null && prop is KMutableProperty1) {
                // Qualifier가 있으면 해당 타입으로, 아니면 파라미터 타입으로
                val qualifier = prop.findAnnotation<Qualifier>()
                val dependencyClass: KClass<out Any> =
                    qualifier?.value ?: prop.returnType.classifier as? KClass<out Any>
                        ?: throw IllegalStateException("의존성 타입을 확인할 수 없습니다: ${prop.name}")

                val scope = prop.findAnnotation<Scope>()
                val dependencyInstance =
                    appContainer.getInstance(dependencyClass, scopeHolder, scope?.value)
                // 프로퍼티 setter 호출
                prop.isAccessible = true
                prop.setter.call(instance, dependencyInstance)
            }
        }

        return instance
    }
}
