package br.com.amd.rickandmorty.presentation.model

enum class CharacterStatusFilter(
    val label: String,
    val query: String
) {
    ALL(label = "All", query = ""),
    ALIVE(label = "Alive", query = "Alive"),
    DEAD(label = "Dead", query = "Dead"),
    UNKNOWN(label = "Unknown", query = "unknown")
}