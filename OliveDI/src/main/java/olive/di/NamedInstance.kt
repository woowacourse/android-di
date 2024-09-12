package olive.di

import kotlin.reflect.KClass

class NamedInstance(val qualifierAnnotation: KClass<out Annotation>, val instance: Any)
