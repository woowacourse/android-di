package woowacourse.shopping.di

object AppContainer {
    val viewModelFactory = DIViewModelFactory(DIContainer())
}
