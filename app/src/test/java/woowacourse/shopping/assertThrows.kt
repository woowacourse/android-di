package woowacourse.shopping

import kotlin.reflect.KClass
import org.junit.Assert.fail

inline fun <reified T : Throwable> assertThrows(block: () -> Unit) {
    val expectedClass: KClass<T> = T::class

    try {
        block()
        fail("Expected ${expectedClass.simpleName} to be thrown, but nothing was thrown.")
    } catch (e: Throwable) {
        if (e::class != expectedClass) {
            fail("Expected ${expectedClass.simpleName} but got ${e::class.simpleName}")
        }
    }
}
