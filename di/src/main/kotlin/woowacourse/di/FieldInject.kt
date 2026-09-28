package woowacourse.di

// 프로퍼티에만 붙이고, 실행 중 리플렉션으로 찾을 수 있도록 애노테이션 정보를 유지한다.
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class FieldInject
