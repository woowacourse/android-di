package woowacourse.shopping.di.module

abstract class ActivityModule() : Module() {
    fun clear() {
        cache.clear()
    }
}
