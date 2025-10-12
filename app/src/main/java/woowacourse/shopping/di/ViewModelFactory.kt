package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class ViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val kClass: KClass<T> = modelClass.kotlin
        val instance: T = createInstance(kClass)
        injectFields(instance, kClass)
        return instance
    }

    /**
     * ViewModel 인스턴스 생성
     *
     * @param kClass 생성할 클래스
     * @return 생성된 인스턴스
     * @throws IllegalArgumentException 적절한 생성자를 찾을 수 없는 경우
     */
    private fun <T : Any> createInstance(kClass: KClass<T>): T {
        val constructor: KFunction<T>? = findInjectableConstructor(kClass)
        return if (constructor != null) {
            val args: Array<Any> =
                constructor.parameters
                    .map { param: KParameter ->
                        val paramClass: KClass<*> = param.type.classifier as KClass<*>
                        appContainer.get(paramClass)
                    }.toTypedArray()
            constructor.call(*args)
        } else {
            kClass.createInstance()
        }
    }

    /**
     * 모든 파라미터를 주입 가능한 생성자를 찾음
     *
     * @param kClass 검색할 클래스
     * @return 주입 가능한 생성자, 없으면 null
     */
    private fun <T : Any> findInjectableConstructor(kClass: KClass<T>): KFunction<T>? =
        kClass.constructors.firstOrNull { constructor: KFunction<T> ->
            constructor.parameters.all { param: KParameter ->
                val paramClass: KClass<*>? = param.type.classifier as? KClass<*>
                paramClass != null && appContainer.canResolve(paramClass)
            }
        }

    /**
     * Inject 어노테이션이 붙은 필드에 의존성 주입
     *
     * 생성자 주입으로 이미 주입된 필드는 건너뛰고,
     * lateinit var로 선언된 필드만 추가로 주입
     *
     * @param instance 주입할 대상 인스턴스
     * @param kClass 대상 클래스
     */
    private fun <T : Any> injectFields(
        instance: T,
        kClass: KClass<T>,
    ) {
        kClass.memberProperties.forEach { property: KProperty1<T, *> ->
            property.findAnnotation<Inject>() ?: return@forEach
            if (property !is KMutableProperty<*>) return@forEach

            val propertyType =
                property.returnType.classifier as? KClass<*> ?: return@forEach

            if (appContainer.canResolve(propertyType)) {
                val dependency = appContainer.get(propertyType)
                property.isAccessible = true
                property.setter.call(instance, dependency)
            }
        }
    }
}
