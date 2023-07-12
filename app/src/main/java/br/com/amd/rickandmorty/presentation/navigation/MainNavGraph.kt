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
import br.com.amd.rickandmorty.presentation.MainViewModel
import br.com.amd.rickandmorty.presentation.model.CharacterStatusFilter
import br.com.amd.rickandmorty.presentation.screen.CharacterDetailsScreen
import br.com.amd.rickandmorty.presentation.screen.CharacterDetailsViewModel
import br.com.amd.rickandmorty.presentation.screen.CharacterListScreen
import br.com.amd.rickandmorty.presentation.screen.CharacterSearchScreen

@Composable
fun MainNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<MainViewModel>()

    NavHost(
        navController = navController,
        startDestination = Destination.CharacterListScreen.fullRoute
    ) {
        composable(route = Destination.CharacterListScreen.fullRoute) {
            val items = viewModel.charactersListPagingData.collectAsLazyPagingItems()

            CharacterListScreen(
                characters = items,
                modifier = modifier,
                navigateToDetails = { id ->
                    navController.navigate(Destination.CharacterDetailsScreen(id))
                }
            )
        }

        composable(
            route = Destination.CharacterDetailsScreen.fullRoute,
            arguments = listOf(
                navArgument(name = Destination.CharacterDetailsScreen.CHARACTER_ID_PARAM_KEY) {
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
            val items = viewModel.charactersSearchPagingData.collectAsLazyPagingItems()

            val statusList = CharacterStatusFilter.values().toList()
            CharacterSearchScreen(
                modifier = modifier,
                statusList = statusList,
                charactersFiltered = items,
                onNameQueryChange = viewModel.onNameQueryChange,
                onStatusQueryChange = viewModel.onStatusQueryChange,
                navigateToDetails = { id ->
                    navController.navigate(Destination.CharacterDetailsScreen(id))
                }
            )
        }
    }
}