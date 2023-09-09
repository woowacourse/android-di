package woowacourse.shopping.di.module

import woowacourse.shopping.di.annotation.FieldInject
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible

abstract class Module(private val parentModule: Module? = null) {
    protected val cache =
        mutableMapOf<String, Any>() // 모듈은 자신의 생명이 살아있는 한, 한 번 만들었던 동일한 객체를 뱉어내야 한다.

    fun <T : Any> provideInstance(clazz: Class<T>): T {
        val injectableFunctionWithModuleMap = searchInjectableFunctions(clazz)

        // 해당 객체를 만들기 위한 필요 구성 요소들을 얻는 과정.
        return when (injectableFunctionWithModuleMap.size) {
            0 -> createWithPrimaryConstructor(clazz) // 모듈에 정의된 메소드들 중 해당되는 것이 없다는 것은, 이 객체를 만드는데 주생성자면 충분하다는 의미.
            else -> { // 추후 1과 else로 분리 예정
                val injectFunction =
                    injectableFunctionWithModuleMap.keys.first() // 메소드들 중 어노테이션으로 판단해서 일치하는 메소드를 실행시켜서 반환시킬 것임.
                val injectModule = injectableFunctionWithModuleMap[injectFunction]
                    ?: throw NullPointerException("${injectFunction.name} 함수가 없습니다.")
                createWithModuleFunc(injectModule, injectFunction)
            }
        }
    }

    private fun <T : Any> searchInjectableFunctions(
        clazz: Class<T>,
    ): Map<KFunction<*>, Module> {
        return getPublicMethodMap().filter { (func, _) ->
            val returnKClass = func.returnType.classifier as KClass<*>
            clazz.kotlin.isSubclassOf(returnKClass)
        }.takeUnless { it.isEmpty() }
            ?: parentModule?.searchInjectableFunctions(clazz) ?: mapOf()
    }

    private fun <T : Any> createWithModuleFunc(module: Module, func: KFunction<*>): T {
        @Suppress("UNCHECKED_CAST")
        return func.call(module, *getArguments(module, func).toTypedArray()) as T
    }

    private fun <T : Any> createWithPrimaryConstructor(clazz: Class<T>): T {
        val primaryConstructor =
            clazz.kotlin.primaryConstructor
                ?: throw NullPointerException("모듈에 특정 클래스를 주 생성자로 인스턴스화 하는데 필요한 인자를 제공하는 함수를 정의하지 않았습니다")
        val args = getArguments(this, primaryConstructor)
        return setFiledInjectElements(primaryConstructor.call(*args.toTypedArray()))
    }

    private fun getArguments(baseModule: Module, func: KFunction<*>): List<Any> {
        return func.valueParameters.map { param ->
            val paramKClass = param.type.classifier as KClass<*>
            baseModule.provideInstance(paramKClass.java) // 인자들을 얻어오는 과정은, 그 func라는 함수 이상 수준의 모듈에서부터 탐색을 시작해야 한다.\
        }
    }

    private fun <T : Any> setFiledInjectElements(instance: T): T {
        instance::class.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>()
            .filter { it.hasAnnotation<FieldInject>() }.forEach { field ->
                val fieldKClass = field.returnType.classifier as KClass<*>
                field.isAccessible = true
                field.setter.call(instance, this.provideInstance(fieldKClass.java))
            }
        return instance
    }

    protected inline fun <reified T : Any> getOrCreateInstance(crossinline create: () -> T): T {
        val name = T::class.qualifiedName ?: throw NullPointerException("클래스 이름 없음")
        if (cache[name] == null) cache[name] = create()
        return cache[name] as T
    }

    private fun getPublicMethodMap(): Map<KFunction<*>, Module> {
        return this@Module::class.declaredMemberFunctions.filter {
            it.visibility == KVisibility.PUBLIC
        }.associateWith { this@Module }
    }
}
