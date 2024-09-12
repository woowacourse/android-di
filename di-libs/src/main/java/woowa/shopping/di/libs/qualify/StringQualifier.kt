package woowa.shopping.di.libs.qualify

data class StringQualifier(override val qualifier: String) : Qualifier {
    override fun toString(): String {
        return qualifier
    }
}

fun qualifier(name: String): Qualifier = StringQualifier(name)