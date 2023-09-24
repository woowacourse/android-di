package woowacourse.shopping.dependency

data class DependencyValue(
    val instance: Any,
    val annotations: List<Annotation>,
)
