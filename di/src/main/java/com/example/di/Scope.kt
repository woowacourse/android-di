package com.example.di

import kotlin.reflect.KClass

class Scope(
    val kind: String,
    private var parent: Scope? = null,
) {
    private val instances = mutableMapOf<Pair<KClass<*>, KClass<*>?>, Any>()
    var isClosed: Boolean = false
        private set
    val instanceCount: Int get() = instances.size

    fun owner(kind: String): Scope {
        check(!isClosed) { "종료된 스코프입니다: ${this.kind}" }
        return if (this.kind == kind) this else parent?.owner(kind)
            ?: error("필요한 스코프가 없습니다: $kind")
    }

    fun getOrCreate(key: Pair<KClass<*>, KClass<*>?>, create: () -> Any): Any {
        check(!isClosed) { "종료된 스코프입니다: $kind" }
        return instances.getOrPut(key, create)
    }

    fun close() {
        instances.clear()
        parent = null
        isClosed = true
    }
}
