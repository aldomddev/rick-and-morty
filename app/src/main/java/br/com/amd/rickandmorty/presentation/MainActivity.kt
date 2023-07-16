package br.com.amd.rickandmorty.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.com.amd.rickandmorty.R
import br.com.amd.rickandmorty.presentation.navigation.Destination
import br.com.amd.rickandmorty.presentation.navigation.MainNavGraph
import br.com.amd.rickandmorty.ui.theme.RickAndMortyTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val currentDestination = navController
                .currentBackStackEntryFlow
                .collectAsStateWithLifecycle(initialValue = null)

            RickAndMortyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        modifier = Modifier.semantics {
                            testTagsAsResourceId = true
                        },
                        topBar = {
                            TopAppBar(
                                title = {
                                    TopAppBarTitle(destination = currentDestination)
                                },
                                navigationIcon = {
                                    NavigationIcon(
                                        navController = navController,
                                        destination = currentDestination
                                    )
                                },
                                actions = {
                                    SearchIcon(
                                        navController = navController,
                                        destination = currentDestination
                                    )
                                }
                            )
                        }
                    ) { paddingValues ->
                        MainNavGraph(
                            navController = navController,
                            modifier = Modifier.padding(paddingValues)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NavigationIcon(
    navController: NavHostController,
    destination: State<NavBackStackEntry?>
) {
    if (!destination.isCurrentDestination(Destination.CharacterListScreen)) {
        IconButton(
            onClick = {
                navController.navigateUp()
            }
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.characters_search_title)
            )
        }
    }
}

@Composable
private fun SearchIcon(
    navController: NavHostController,
    destination: State<NavBackStackEntry?>
) {
    if (!destination.isCurrentDestination(Destination.CharacterSearchScreen)) {
        IconButton(
            onClick = {
                navController.navigate(Destination.CharacterSearchScreen.fullRoute)
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search"
            )
        }
    }
}

@Composable
private fun TopAppBarTitle(destination: State<NavBackStackEntry?>) {
    Text(stringResource(id = destination.getTopBarTitle()))
}

private fun State<NavBackStackEntry?>.isCurrentDestination(destination: Destination): Boolean {
    return this.value?.destination?.route?.contains(destination.fullRoute) == true
}

private fun State<NavBackStackEntry?>.getTopBarTitle(): Int {
    return when (this.value?.destination?.route) {
        Destination.CharacterListScreen.fullRoute -> R.string.characters_list_title
        Destination.CharacterDetailsScreen.fullRoute -> R.string.characters_details_title
        Destination.CharacterSearchScreen.fullRoute -> R.string.characters_search_title
        else -> R.string.app_name
    }
}
