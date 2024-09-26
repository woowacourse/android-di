package org.aprilgom.androiddi

class ScopeBuilder(
    val name: String,
) {
    val providers: MutableMap<NamedKClass, ScopedProvider<*>> = mutableMapOf()

    fun build(moduleBuilder: ModuleBuilder): Scope {
        moduleBuilder.providers += providers
        return Scope(name)
    }
}
