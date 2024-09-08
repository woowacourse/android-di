package woowacourse.shopping.ui.injection

import woowacourse.shopping.ui.injection.repository.RepositoryDI
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

interface DIInjection {
    fun checkConstructor() {
        this::class.primaryConstructor?.parameters?.all {
            it.type.jvmErasure.isSubclassOf(RepositoryDI::class)
        }
    }
}
