package com.daedan.di.scope

interface Scope {
    override fun toString(): String

    override fun hashCode(): Int

    override fun equals(other: Any?): Boolean
}
