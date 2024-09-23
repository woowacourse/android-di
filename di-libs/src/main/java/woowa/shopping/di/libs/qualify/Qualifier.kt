package woowa.shopping.di.libs.qualify

import kotlin.reflect.KClass

interface Qualifier {
    val qualifier: String
}

data class StringQualifier(override val qualifier: String) : Qualifier

fun named(name: String): Qualifier = StringQualifier(name)

data class TypeQualifier<T>(val type: Class<T>) : Qualifier {
    override val qualifier: String = type.canonicalName ?: type.name
}

fun qualifier(clazz: KClass<*>): Qualifier = TypeQualifier(clazz.java)

inline fun <reified T> qualifier(): Qualifier = TypeQualifier(T::class.java)
