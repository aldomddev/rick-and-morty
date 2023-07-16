package br.com.amd.rickandmorty.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.amd.rickandmorty.presentation.model.CharacterStatusFilter
import br.com.amd.rickandmorty.presentation.screen.details.CharacterDetailsScreen
import br.com.amd.rickandmorty.presentation.screen.details.CharacterDetailsViewModel
import br.com.amd.rickandmorty.presentation.screen.list.CharacterListScreen
import br.com.amd.rickandmorty.presentation.screen.list.CharactersListViewModel
import br.com.amd.rickandmorty.presentation.screen.search.CharacterSearchScreen

@Composable
fun MainNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Destination.CharacterListScreen.fullRoute
    ) {
        composable(route = Destination.CharacterListScreen.fullRoute) {
            val charactersListViewModel = hiltViewModel<CharactersListViewModel>()
            val items = charactersListViewModel.charactersListPagingData
            CharacterListScreen(
                streamOfCharacters = items,
                modifier = modifier,
                navigateToDetails = { id ->
                    navController.navigate(Destination.CharacterDetailsScreen(id))
                }
            )
        }

        composable(
            route = Destination.CharacterDetailsScreen.fullRoute,
            arguments = listOf(
                navArgument(
                    name = Destination.CharacterDetailsScreen.CHARACTER_ID_PARAM_KEY
                ) {
                    type = NavType.IntType
                }
            )
        ) {
            val characterId = it.arguments?.getInt(
                Destination.CharacterDetailsScreen.CHARACTER_ID_PARAM_KEY
            ) ?: 0

            val characterDetailsViewModel = hiltViewModel<CharacterDetailsViewModel>()
            val uiState = characterDetailsViewModel.viewState.collectAsStateWithLifecycle()

            CharacterDetailsScreen(
                id = characterId,
                uiState = uiState.value,
                modifier = modifier,
                onGetCharacterDetails = characterDetailsViewModel::getCharacterDetails
            )
        }

        composable(route = Destination.CharacterSearchScreen.fullRoute) {
            val charactersListViewModel = hiltViewModel<CharactersListViewModel>()
            val items =
                charactersListViewModel.charactersSearchPagingData
            val statusList = CharacterStatusFilter.values().toList()
            CharacterSearchScreen(
                modifier = modifier,
                statusList = statusList,
                streamOfCharacters = items,
                onNameQueryChange = charactersListViewModel.onNameQueryChange,
                onStatusQueryChange = charactersListViewModel.onStatusQueryChange,
                navigateToDetails = { id ->
                    navController.navigate(Destination.CharacterDetailsScreen(id))
                }
            )
        }
    }
}
