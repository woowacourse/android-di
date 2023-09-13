package woowacourse.shopping.data.di

interface Container {
    val instances: MutableMap<AnnotationType, Any>

    fun addInstance(instance: Any)

    fun getInstance(annotationType: AnnotationType): Any?
}