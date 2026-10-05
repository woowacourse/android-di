package io.github.firstwoosun.di

data class ScopeKind(
    val name: String
) {
    companion object{
        val Application = ScopeKind("application")
        val ViewModel = ScopeKind("viewModel")
        val Screen = ScopeKind("screen")
    }
}
