package woowacourse.shopping.di

import woowacourse.shopping.di.container.AnnotationQualifier
import woowacourse.shopping.di.container.DependencyType

data class ClassQualifier(
    val dependencyType: DependencyType,
    val annotationQualifier: AnnotationQualifier,
)
