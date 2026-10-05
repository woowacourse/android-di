package woowacourse.di

data class ScopeType(
    val name: String,
) {
    init {
        require(name.isNotBlank()) { "스코프 타입 이름은 비어 있을 수 없습니다." }
    }
}
