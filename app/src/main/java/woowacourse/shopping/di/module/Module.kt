package woowacourse.shopping.di.module

abstract class Module {
    protected val cache = mutableMapOf<String, Any>()

    protected inline fun <reified T : Any> getInstance(crossinline create: () -> T): T {
        val name = T::class.qualifiedName ?: throw RuntimeException("클래스 이름 없음")
        val instance = cache[name] as T?
        return instance ?: kotlin.run {
            create().also { cache[name] = it }
        }
    }
}
