package woowacourse.shopping.di

class Dependencies {
    val qualifiers: HashMap<Annotation?, Qualifier> = hashMapOf()

    fun qualifier(annotation: Annotation? = null, init: Qualifier.() -> Unit) {
        qualifiers[annotation] = Qualifier().apply {
            init()
        }
    }
}
