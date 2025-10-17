package woowacourse.peto.di.annotation

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class SingletonScope

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelScope

enum class Scope {
    SINGLETON,
    ACTIVITY,
    VIEWMODEL,
}
