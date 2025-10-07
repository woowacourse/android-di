package woowacourse.shopping.di.module

import woowacourse.shopping.di.definition.DefinitionInformation

interface InjectionModule {
    fun provideDefinitions(): List<DefinitionInformation<*>>
}
