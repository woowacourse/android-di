package woowacourse.shopping.di

import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object Injector {
    fun <T : Any> create(type: KClass<T>): T {
        type.objectInstance?.let { return it }

        val constructor =
            requireNotNull(type.primaryConstructor) {
                "${type.simpleName}의 주 생성자를 찾을 수 없습니다."
            }
        val arguments =
            constructor.parameters.map { parameter ->
                val dependencyType =
                    parameter.type.classifier as? KClass<*>
                        ?: error("지원하지 않는 타입: ${parameter.type}")
                create(dependencyType)
            }

        return constructor.call(*arguments.toTypedArray()).also(::injectFields)
    }

    private fun injectFields(instance: Any) {
        generateSequence<Class<*>>(instance.javaClass) { it.superclass }
            .flatMap { it.declaredFields.asSequence() }
            .filter { it.isAnnotationPresent(Inject::class.java) }
            .forEach { field ->
                require(!Modifier.isFinal(field.modifiers) && !Modifier.isStatic(field.modifiers)) {
                    "@Inject는 변경 가능한 인스턴스 필드에만 사용할 수 있습니다: ${field.name}"
                }
                field.isAccessible = true
                field.set(instance, create(field.type.kotlin))
            }
    }
}
