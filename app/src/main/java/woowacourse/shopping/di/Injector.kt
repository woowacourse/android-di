package woowacourse.shopping.di

import android.util.Log
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

object Injector {
    // klass의 인스턴스를 생성하여 반환한다
    fun <T : Any> inject(klass: KClass<*>): T {
        // 1. Container에 인자로 넘겨준 클래스의 인스턴스가 존재하는지 확인한다
        val instance = Container.getInstance(klass)

        // 2. 존재하면 바로 반환한다
        if (instance != null) {
            return instance as T
        }

        // 3. 존재하지 않으면 인스턴스를 생성한다
        return createInstance(klass)
    }

    private fun <T> createInstance(klass: KClass<*>): T {
        //  주생성자를 가져온다
        val primaryConstructor = klass.primaryConstructor ?: throw NullPointerException("주 생성자가 없습니다.")

        // 주생성자의 인자들을 인스턴스화 시킨다
        // Container에 있는 경우 바로 가져오고 없다면 인스턴스를 생성한다
        val insertedParameters = primaryConstructor.parameters.map {
            val type = it.type.jvmErasure
            Log.d("krrong", type.toString())
            Container.getInstance(type) ?: inject(type)
        }

        return primaryConstructor.call(*insertedParameters.toTypedArray()) as T
    }
}
