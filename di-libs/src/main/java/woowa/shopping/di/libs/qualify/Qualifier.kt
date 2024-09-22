package woowa.shopping.di.libs.qualify

interface Qualifier {
    val qualifier: String
}

data class StringQualifier(override val qualifier: String) : Qualifier

fun qualifier(name: String): Qualifier = StringQualifier(name)

data class TypeQualifier<T>(val type: Class<T>) : Qualifier {
    override val qualifier: String = type.canonicalName ?: type.name
}

inline fun <reified T> qualifier(): Qualifier = TypeQualifier(T::class.java)
