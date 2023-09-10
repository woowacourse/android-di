package woowacourse.shopping.di

interface Module {
    operator fun plus(module: Module) = listOf(module, this)
}
