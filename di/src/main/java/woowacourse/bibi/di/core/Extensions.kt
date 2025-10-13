package woowacourse.bibi.di.core

fun <T, R> MutableSet<T>.withElement(
    element: T,
    block: () -> R,
): R {
    if (!add(element)) {
        error("순환 의존성 감지: $element\n현재 체인: $this")
    }
    return try {
        block()
    } finally {
        remove(element)
    }
}
