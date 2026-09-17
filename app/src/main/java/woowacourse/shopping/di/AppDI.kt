package woowacourse.shopping.di

object AppDI {
    val viewModelFactory: ReflectionViewModelFactory = ReflectionViewModelFactory(container = DependencyContainer())
}
