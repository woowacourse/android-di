package woowacourse.shopping.di.annotation

annotation class Injected

annotation class Qualifier(val type: String) {
    companion object {
        const val IN_MEMORY = "InMemory"
        const val DATABASE = "Database"
    }
}
