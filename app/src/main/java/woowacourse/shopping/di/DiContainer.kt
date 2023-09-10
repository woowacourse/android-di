package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

typealias Declaration<T> = () -> T
object DiContainer {
    val singletons: MutableMap<KClass<*>, Any> = HashMap()
    val declarations: MutableMap<KClass<*>, Declaration<Any>> = HashMap()
    fun loadModule(module: Module) {
        val functions = module::class.declaredMemberFunctions
        val singletonFunctions = functions.filter { it.hasAnnotation<Singleton>() }

        functions.forEach {
            declarations[it.returnType.jvmErasure] =
                { it.call(module) ?: throw IllegalStateException("함수 형태가 잘못됐어요") }
        }

        singletonFunctions.forEach {
            val kClass = it.returnType.jvmErasure
            singletons[kClass] = declarations[kClass]?.invoke() as Any
        }
    }
}

fun startDI(block: DiContainer.() -> Unit) = DiContainer.apply(block)
inline fun <reified T : Any> get(): T {
    val kClazz = T::class
    return when {
        DiContainer.singletons.keys.contains(kClazz) -> DiContainer.singletons[kClazz] as T
        DiContainer.declarations.keys.contains(kClazz) -> DiContainer.declarations[kClazz]?.invoke() as T
        else -> throw IllegalArgumentException("$kClazz 의존성 주입할 객체가 없습니다.")
    }
}

fun get(kClazz: KClass<*>): Any {
    return when {
        DiContainer.singletons.keys.contains(kClazz) -> DiContainer.singletons[kClazz]!!
        DiContainer.declarations.keys.contains(kClazz) -> DiContainer.declarations[kClazz]?.invoke()!!
        else -> throw IllegalArgumentException("$kClazz 의존성 주입할 객체가 없습니다.")
    }
}

inline fun <reified T : Any> inject(): T {
    val instance = initWithDependencies<T>().apply {
        injectFields(this)
    }
    return instance
}

inline fun <reified T : Any> initWithDependencies(): T {
    val primaryConstructor =
        T::class.primaryConstructor ?: throw IllegalStateException("")

    val constructorParameterKClasses =
        primaryConstructor.parameters.map { it.type.jvmErasure }

    val arguments = constructorParameterKClasses.map { kClazz ->
        get(kClazz)
    }.toTypedArray()
    return primaryConstructor.call(*arguments)
}

inline fun <reified T : Any> injectFields(instance: T) {
    val properties = instance::class
        .declaredMemberProperties.filter { it.hasAnnotation<Inject>() }

    properties.forEach { property ->
        property.isAccessible = true
        val value = get(property.returnType.jvmErasure)
        property.javaField?.set(instance, value)
    }
}
