package woowacourse.shopping.di

interface Module {
    fun provideInstance(dependencyRegistry: DependencyRegistry)
}
