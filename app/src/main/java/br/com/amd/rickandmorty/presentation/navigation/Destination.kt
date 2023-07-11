package br.com.amd.rickandmorty.presentation.navigation

sealed class Destination(protected val route: String, vararg params: String) {

    val fullRoute: String = if (params.isEmpty()) route else {
        val builder = StringBuilder(route)
        params.forEach { builder.append("/{${it}}") }
        builder.toString()
    }

    sealed class NoArgumentsDestination(route: String) : Destination(route) {
        operator fun invoke(): String = route
    }

    object CharacterListScreen : NoArgumentsDestination(route = "CharacterList")
    object CharacterSearchScreen : NoArgumentsDestination(route = "CharacterSearch")
    object CharacterDetailsScreen : Destination(
        route = "CharacterDetails",
        "id"
    ) {
        const val CHARACTER_ID_PARAM_KEY = "id"

        operator fun invoke(id: Int): String = route.appendParams(CHARACTER_ID_PARAM_KEY to id)
    }

    internal fun String.appendParams(vararg params: Pair<String, Any?>): String {
        val builder = StringBuilder(this)

        params.forEach {
            it.second?.toString()?.let { arg ->
                builder.append("/$arg")
            }
        }

        return builder.toString()
    }
}
