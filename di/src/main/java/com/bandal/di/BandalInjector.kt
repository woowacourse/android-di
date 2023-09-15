package com.bandal.di

import com.bandal.di.DIError.NotFoundPrimaryConstructor
import com.bandal.di.DIError.NotFoundQualifierOrInstance
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

object BandalInjector {

    fun <T : Any> inject(kClass: KClass<T>): T {
        return (BandalInjectorAppContainer.getInstance(kClass) ?: createInstance(kClass)) as T
    }

    /**
     * 인스턴스를 만듭니다.
     * 주생성자의 의존성이 필요한 부분 또는 의존성 주입이 필요한 필드도 주입시켜 반환합니다.
     *
     * @param kClass 인스턴스를 만들 클래스를 뜻합니다.
     * @return 인자로 넘겨진 KClass<T>의 인스턴스를 반환합니다.
     * @throws NotFoundPrimaryConstructor 주생성자를 찾지 못하면 예외처리합니다.
     * */
    private fun <T : Any> createInstance(kClass: KClass<T>): T {
        val constructor = kClass.primaryConstructor
            ?: throw NotFoundPrimaryConstructor()

        val parameterValues = constructor.parameters.associateWith { kParameter ->
            findInstance(
                kParameter.type.jvmErasure,
                kParameter.annotations,
            )
        }

        return constructor.callBy(parameterValues).apply { injectFields(this) }
    }

    /**
     * 클래스의 멤버 프로퍼티들중 @BandalInject 어노테이션이 붙은 프로퍼티들만 필터링 합니다.
     * 그리고 해당하는 타입에 맞는 인스턴스를 찾아 주입시켜줍니다.
     *
     * @param classInstance 필드주입이 필요한 클래스 인스턴스를 받습니다.
     */
    private fun <T : Any> injectFields(classInstance: T) {
        classInstance::class.declaredMemberProperties
            .filter { it.hasAnnotation<BandalInject>() }
            .forEach { property ->
                property.isAccessible = true
                val propertyType = property.returnType.jvmErasure
                val value = findInstance(propertyType, property.annotations)
                (property as KMutableProperty<*>).setter.call(classInstance, value)
            }
    }

    /**
     * AppContainer로부터 원하는 타입의 인스턴스를 찾아오는 메서드입니다.
     *
     * 이 때 찾는 인스턴스가 없을 땐 예외 처리합니다.
     *
     * @param kClass 찾을 인스턴스의 타입을 명시해줍니다.
     * @param annotations 찾을 인스턴스가 가질 수 있는 어노테이션들입니다.
     * @throws NotFoundQualifierOrInstance 인스턴스를 찾을 때 필요한 Qualifier Annotation이 없거나 알맞는 인스턴스가 없을 때 발생합니다.
     */
    private fun <T : Any> findInstance(
        kClass: KClass<T>,
        annotations: List<Annotation>,
    ): Any {
        val annotationWithQualifier =
            annotations.filter { it.annotationClass.java.isAnnotationPresent(Qualifier::class.java) }
        return BandalInjectorAppContainer.getInstance(kClass, annotationWithQualifier)
            ?: throw NotFoundQualifierOrInstance(kClass)
    }
}
