package woowacourse.shopping.hilt.di.qualifier

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SingleCartQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ViewModelScopeCartQualifier