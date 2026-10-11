package woowacourse.di

enum class DependencyLifetime(
    internal val sharesAcrossScopes: Boolean,
) {
    CONTAINER(true),
    EACH_SCOPE(false),
}
