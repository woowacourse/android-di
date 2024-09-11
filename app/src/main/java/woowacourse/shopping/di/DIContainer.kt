package woowacourse.shopping.di

import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.di.annotation.Qualifier
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

class DIContainer(
    application: ShoppingApplication,
    diModules: List<KClass<out DIModule>>,
) {
    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()
    private val namedInstances: MutableMap<KClass<*>, MutableList<Pair<KClass<out Annotation>, Any>>> =
        mutableMapOf()

    init {
        instances[ShoppingApplication::class] = application
        diModules.filter { !it.isAbstract }.forEach { it.injectModule() }
        diModules.filter { it.isAbstract }.forEach { it.injectAbstractModule() }
    }

    private fun KClass<out DIModule>.injectAbstractModule() {
        val functions = this.declaredMemberFunctions
        functions.forEach { function ->
            val instanceType = function.parameters
                .map { it.type.classifier as KClass<*> }
                .first { it != this }
            val cacheType = function.returnType.classifier as KClass<*>

            if (function.annotations.isQualifierAnnotation()) {
                val nameAnnotation = function.annotations.qualifierNameAnnotation()
                namedInstances.getOrPut(cacheType) { mutableListOf() }
                    .add(nameAnnotation to create(instanceType))
                return@forEach
            }
            createSingleton(instanceType, cacheType)
        }
    }

    private fun List<Annotation>.isQualifierAnnotation(): Boolean {
        return any { annotation ->
            annotation.annotationClass.annotations.any { it.annotationClass == Qualifier::class }
        }
    }

    private fun List<Annotation>.qualifierNameAnnotation(): KClass<out Annotation> {
        val nameAnnotation = first { functionAnnotation ->
            functionAnnotation.annotationClass.annotations.any { it.annotationClass == Qualifier::class }
        }
        return nameAnnotation.annotationClass
    }

    private fun KClass<out DIModule>.injectModule() {
        val functions = this.declaredMemberFunctions
        functions.forEach { function ->
            val type = function.returnType.classifier as KClass<*>
            val parameters = function.parameters
                .map { it.type.classifier as KClass<*> }
                .filter { this != it }
            val objectInstance = createSingleton(this)
            val arguments = parameters.map { singletonInstance(it) }
            val instance = function.call(objectInstance, *arguments.toTypedArray())
            instances[type] = instance ?: throw IllegalArgumentException()
        }
    }

    fun <T : Any> instance(classType: KClass<T>): T {
        return create(classType)
    }

    fun <T : Any> singletonInstance(classType: KClass<T>): T {
        return instances[classType] as? T ?: createSingleton(classType)
    }

    private fun <T : Any> createSingleton(instanceType: KClass<T>, cacheType: KClass<*> = instanceType): T {
        return create(instanceType).apply {
            putSingletonInstance(cacheType)
        }
    }

    private fun <T : Any> create(classType: KClass<T>): T {
        if (instances[classType] != null) return instances[classType] as T
        val parameters = classType.constructors.first().parameters
        val arguments = parameters.map(::argumentInstance)
        val instance = classType.constructors.first().call(*arguments.toTypedArray())
        instance.injectFieldDependency()
        return instance
    }

    private fun argumentInstance(parameter: KParameter): Any {
        val parameterType = parameter.type.classifier as KClass<*>
        if (parameter.annotations.isQualifierAnnotation()) {
            val nameAnnotation: KClass<out Annotation> = parameter.annotations.qualifierNameAnnotation()
            val instance = namedInstances[parameterType]?.firstOrNull { it.first == nameAnnotation }
            return instance
                ?: throw IllegalArgumentException("${nameAnnotation.simpleName}로 지정된 인스턴스가 존재하지 않습니다.")
        }
        return instances[parameterType] ?: createSingleton(parameterType)
    }

    private fun Any.putSingletonInstance(type: KClass<*>) {
        if (instances.containsKey(type)) {
            throw IllegalArgumentException("이미 해당 클래스의 인스턴스가 존재합니다.")
        }
        instances[type] = this
    }

    private fun Any.injectFieldDependency() {
        val properties =
            this::class.declaredMemberProperties
                .filter { it.isLateinit }
                .filter { it.hasAnnotation<Inject>() }

        properties.forEach { property ->
            val p = property as KMutableProperty1
            p.isAccessible = true
            val type = p.returnType.classifier as KClass<*>
            if (p.annotations.isQualifierAnnotation()) {
                val nameAnnotation: KClass<out Annotation> = p.annotations.qualifierNameAnnotation()
                val instance = namedInstances[type]?.firstOrNull { it.first == nameAnnotation }?.second
                    ?: throw IllegalArgumentException("${nameAnnotation.simpleName}로 지정된 인스턴스가 존재하지 않습니다.")
                property.setter.call(this, instance)
                return@forEach
            }

            val instance = instances[type] ?: createSingleton(type)
            property.setter.call(this, instance)
        }
    }
}
