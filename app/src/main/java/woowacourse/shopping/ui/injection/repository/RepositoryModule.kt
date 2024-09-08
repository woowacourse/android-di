package woowacourse.shopping.ui.injection.repository

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure

class RepositoryModule private constructor() : DefaultLifecycleObserver {
    private lateinit var repositoryMap: Map<String, RepositoryDI>
    private lateinit var repositoryBinder: RepositoryBinder

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        repositoryBinder = RepositoryBinder()
        repositoryMap = createRepositoryMap(repositoryBinder)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        instance = null
        owner.lifecycle.removeObserver(this)
    }

    fun getRepository(repository: KClass<out RepositoryDI>): RepositoryDI {
        return repositoryMap[repository.simpleName]
            ?: error("${repository.simpleName} 해당 interface에 대한 객체가 없습니다.")
    }

    private fun createRepositoryMap(repositoryBinder: RepositoryBinder): Map<String, RepositoryDI> {
        return RepositoryBinder::class.declaredMemberFunctions
            .filter { it.returnType.jvmErasure.isSubclassOf(RepositoryDI::class) }
            .associate { kFunction ->
                val result = kFunction.call(repositoryBinder) as RepositoryDI
                val key =
                    kFunction.returnType.jvmErasure.simpleName
                        ?: error("$result 의 key값을 지정할 수 없습니다.")
                key to result
            }
    }

    companion object {
        private var instance: RepositoryModule? = null

        fun setLifeCycle(context: Context) {
            val newInstance = RepositoryModule()
            when (context) {
                is Application -> ProcessLifecycleOwner.get().lifecycle.addObserver(newInstance)
                is AppCompatActivity -> context.lifecycle.addObserver(newInstance)
                is Fragment -> context.lifecycle.addObserver(newInstance)
                else -> error("해당 로직은 Application, AppCompatActivity, Fragment에서만 쓸 수 있습니다.")
            }
            instance = newInstance
        }

        fun getInstance(): RepositoryModule = instance ?: error("Module에 대한 lifecycle이 지정되지 않았습니다.")
    }
}
