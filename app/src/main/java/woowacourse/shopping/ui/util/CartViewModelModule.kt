package woowacourse.shopping.ui.util

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.FakeCartRepository
import javax.inject.Qualifier
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions

@Qualifier
annotation class SharedCartRepository

object DIModule {
    @SharedCartRepository
    fun provideCartRepository(): CartRepository {
        return FakeCartRepository()
    }
}

inline fun <reified T : Annotation> callMethodWithAnnotation(): Any? {
    return DIModule::class.memberFunctions
        .first { it.findAnnotation<T>() != null }
        .call(DIModule)
}
