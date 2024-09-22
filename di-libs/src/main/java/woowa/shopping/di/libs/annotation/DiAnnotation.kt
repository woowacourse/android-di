package woowa.shopping.di.libs.annotation

@RequiresOptIn(
    message = "이 어노테이션이 붙은 API는 내부에서만 사용되어야 합니다. 외부에서 사용하지 마세요!",
    level = RequiresOptIn.Level.ERROR,
)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY,
)
annotation class InternalApi
