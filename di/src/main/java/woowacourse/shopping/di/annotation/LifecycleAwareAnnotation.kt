package woowacourse.shopping.di.annotation

// 우선 내 코드에서 lifecycle에 aware한 의존성을 주입시키려면,
// lifecycleOwner의 생성자 파라미터가 아니라 내부 프로퍼티이어야 한다.
// 이유는 LifecycleAwareDependencyInjector의 lifecycleOwner를 획득하는 로직의 순서 때문.
// 따라서 lifecycle 에 종속적인 의존성에 어노테이션을 붙이기 위해서는, 내부 필드(멤버 property)에만 붙어야한다.
// 파라미터 안됨!

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class LifecycleAware

@LifecycleAware
annotation class ApplicationLifecycleAware

@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
)
@LifecycleAware
annotation class ActivityLifecycleAware

@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
)
@LifecycleAware
annotation class FragmentLifecycleAware

@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
)
@LifecycleAware
annotation class ViewModelLifecycleAware
