package br.com.amd.rickandmorty.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.amd.rickandmorty.presentation.MainViewModel
import br.com.amd.rickandmorty.presentation.screen.CharacterDetailsScreen
import br.com.amd.rickandmorty.presentation.screen.CharacterDetailsViewModel
import br.com.amd.rickandmorty.presentation.screen.CharacterListScreen

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
            val viewModel = hiltViewModel<MainViewModel>()
            val items = viewModel.charactersPagingData.collectAsLazyPagingItems()

            CharacterListScreen(
                characters = items,
                modifier = modifier
            ) { id ->
                navController.navigate(Destination.CharacterDetailsScreen(id))
            }
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
    }
}