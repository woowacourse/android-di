package woowacourse.shopping.study

import java.lang.reflect.Constructor
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaGetter
import org.junit.jupiter.api.Test

class DiTest {
    @Test
    fun `ViewModel 주입 테스트`() {
        // given

        // when
        val viewModel = DiContainer.inject(MyViewModel::class.java)

        // then
        println(viewModel.get())
    }

    @Test
    fun `DiContainer 테스트`() {
        // given

        val diRepository: DiRepository = DiContainer.get(DiRepository::class.java)

        println(diRepository.get())
    }

    @Test
    fun `리플렉션 내부 정보를 출력한다`() {
        DiContainer.list()
    }
}

object DiContainer {
    private val fields = this.javaClass.declaredFields

    private val properties = this::class.declaredMemberProperties

    val diDataSource: DiDataSource by lazy { this.inject(DiSingletonDataSource::class.java) }

    val diDataSource2: DiDataSource2 by lazy { this.inject(DiPrototypeDataSource::class.java) }

    val diRepository: DiRepository get() = this.inject(DiDefaultRepository::class.java)

    private fun <T> getFromGetter(clazz: Class<T>): T? {
        return properties.firstOrNull { property ->
            property.javaGetter?.returnType?.simpleName == clazz.simpleName
        }?.getter?.call(this) as T
    }

    private fun <T> getFromField(clazz: Class<T>): T? {
        return fields.firstOrNull { field -> field.type.simpleName == clazz.simpleName }
            ?.get(this) as T
    }

    fun <T> get(clazz: Class<T>): T {
        return getFromField(clazz)
            ?: getFromGetter(clazz)
            ?: throw IllegalArgumentException()
    }

    fun <T> inject(clazz: Class<T>): T {
        val constructor = clazz.declaredConstructors.filterIsInstance<Constructor<T>>().first()

        return constructor.newInstance(
            *constructor.parameterTypes.map { get(it) }.toTypedArray()
        )
    }

    fun list() {
        println("constructor ------------------")
        this.javaClass.declaredConstructors.forEach { constructor ->
            constructor.parameterTypes.forEach { parameterType ->
                println(parameterType.simpleName)
            }
        }
        println("method ------------------")
        this.javaClass.declaredMethods.forEach { method ->
            method.parameterTypes.forEach { parameterType ->
                println(parameterType)
            }
        }
        println("field ------------------")
        this.javaClass.declaredFields.forEach { field ->
            println(field.type.simpleName)
        }
        println("inner class ------------------")
        this.javaClass.declaredClasses.forEach { clazz ->
            println(clazz.simpleName)
        }
        println("property ------------------")
        this::class.declaredMemberProperties.forEach { property ->
            println(property.javaGetter?.returnType?.simpleName)
            println(property.javaField?.type?.simpleName)
            if (property.name == "diDataSource2") {
                property.getter.call(this)
            }
        }

        println("member ------------------")
        this::class.declaredMembers.forEach { member ->
            println()
            println(member)
            println(member.name)
        }
    }
}

private class MyViewModel(
    private val repository: DiRepository
) {
    fun get(): String {
        return repository.get()
    }
}

private class DiDefaultRepository(
    private val diDataSource: DiDataSource
) : DiRepository {
    override fun get(): String {
        return diDataSource.get()
    }
}

interface DiRepository {
    fun get(): String
}

interface DiDataSource {
    fun get(): String
}

interface DiDataSource2 {
    fun get(): String
}

private class DiSingletonDataSource : DiDataSource {
    override fun get(): String {
        return "DiSingletonDataSource"
    }
}

private class DiPrototypeDataSource : DiDataSource2 {
    override fun get(): String {
        return "DiPrototypeDataSource"
    }
}
