package woowacourse.shopping.di

import android.util.Log

object StartInjection {
    operator fun invoke(block: StartInjection.() -> Unit) {
        this.block()
    }

    inline fun <reified T : Any> single(instance: Any) {
        DependencyContainer.add(T::class, instance)
        Log.d("123123", instance.toString())
    }

    // Disposable의 경우도 처리 필요
}
