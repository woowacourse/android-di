package woowacourse.shopping.di

interface Module {
    fun provideDependencies(dependencyRegistry: DependencyRegistry)
}
