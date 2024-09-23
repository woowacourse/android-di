package olive.di

import kotlin.reflect.KClass

internal val activityInstances: MutableMap<KClass<*>, InstanceProvider> = mutableMapOf()
internal val viewModelInstances: MutableMap<KClass<*>, InstanceProvider> = mutableMapOf()
internal val instances: MutableMap<KClass<*>, Any> = mutableMapOf()
internal val namedInstances: NamedInstances = NamedInstances()
