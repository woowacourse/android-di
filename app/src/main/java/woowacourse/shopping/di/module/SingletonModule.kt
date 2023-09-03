package woowacourse.shopping.di.module

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository

class SingletonModule : Module {
    private val cashe = mutableMapOf<String, Any>()

    // 메소드의 매개변수로, 이 객체가 종속 항목을 갖는 것들을 모두 나열해야 한다.
    fun getCartRepository(): CartRepository {
        return getInstance { DefaultCartRepository() }
    }

    private inline fun <reified T : Any> getInstance(crossinline create: () -> T): T {
        val name = T::class.qualifiedName ?: throw RuntimeException("클래스 이름 없음")
        val instance = cashe[name] as T?
        return instance ?: kotlin.run {
            create().also { cashe[name] = it }
        }
    }
}
