package woowacourse.shopping.di

import com.boogiwoogi.di.ClazzInfoExtractor
import com.boogiwoogi.di.Instance
import com.boogiwoogi.di.InstanceContainer
import com.boogiwoogi.di.Instantiator
import com.boogiwoogi.di.Module
import com.boogiwoogi.di.Singleton
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class AndroidDiInjector(
    val applicationInstanceContainer: InstanceContainer,
    val applicationModule: Module
) {

    val instantiator = Instantiator()

    inline fun <reified T : Any> inject(
        activityInstanceContainer: InstanceContainer,
        activityModule: Module
    ): T {
        val primaryConstructor = requireNotNull(T::class.primaryConstructor)
        val arguments = primaryConstructor.parameters.map { parameter ->
            val instanceOfParam = applicationInstanceContainer.find(parameter)
                ?: activityInstanceContainer.find(parameter)
                ?: instantiator.instantiate(applicationModule, parameter)
                ?: instantiator.instantiate(activityModule, parameter)
                ?: throw IllegalArgumentException("${parameter::class} 타입의 인스턴스를 생성할 수 없습니다.")

            if (parameter.hasAnnotation<Singleton>()) {
                applicationInstanceContainer.add(Instance(instanceOfParam))
            }
            instanceOfParam
        }
        return primaryConstructor.call(*arguments.toTypedArray())
    }

    inline fun <reified T : Any> injectMemberProperty(
        activityInstanceContainer: InstanceContainer,
        activityModule: Module,
        target: T
    ) {
        ClazzInfoExtractor.extractInjectMemberProperties(target::class).forEach { memberProperty ->
            val instanceOfProperty =
                applicationInstanceContainer.find(memberProperty.returnType.jvmErasure)
                    ?: activityInstanceContainer.find(memberProperty.returnType.jvmErasure)
                    ?: instantiator.instantiateProperty(applicationModule, memberProperty)
                    ?: instantiator.instantiateProperty(activityModule, memberProperty)
                    ?: throw IllegalArgumentException("${memberProperty.returnType.jvmErasure} 타입의 인스턴스를 생성할 수 없습니다.")

            if (memberProperty.hasAnnotation<Singleton>()) {
                applicationInstanceContainer.add(Instance(memberProperty))
            }
            memberProperty.isAccessible = true
            memberProperty.setter.call(target, instanceOfProperty)
        }
    }
}
