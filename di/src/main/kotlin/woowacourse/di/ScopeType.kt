package woowacourse.di

data class ScopeType(
    val name: String,
) {
    init {
        require(name.isNotBlank())
    }

    companion object {
        val ROOT = ScopeType("root")
    }
}
