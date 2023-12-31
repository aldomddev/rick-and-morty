package br.com.amd.rickandmorty.presentation.screen.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.amd.rickandmorty.domain.model.Character
import br.com.amd.rickandmorty.presentation.TestTags.CHARACTERS_LIST
import br.com.amd.rickandmorty.presentation.TestTags.LOADING
import br.com.amd.rickandmorty.ui.theme.RickAndMortyTheme
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow

@Composable
fun CharacterListScreen(
    modifier: Modifier = Modifier,
    streamOfCharacters: Flow<PagingData<Character>>,
    navigateToDetails: (id: Int) -> Unit
) {
    val characters = streamOfCharacters.collectAsLazyPagingItems()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        if (characters.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .testTag(LOADING)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag(CHARACTERS_LIST),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(characters.itemCount) { itemIndex ->
                    val character = characters[itemIndex]
                    if (character != null) {
                        CharacterItem(
                            item = character,
                            modifier = Modifier.fillMaxWidth(),
                            onItemClick = navigateToDetails
                        )
                    }
                }
                item {
                    if (characters.loadState.append is LoadState.Loading) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterItem(
    item: Character,
    modifier: Modifier = Modifier,
    onItemClick: (id: Int) -> Unit
) {
    Card(
        modifier = modifier.clickable { onItemClick(item.id) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.image,
                contentDescription = item.name,
                modifier = Modifier
                    .height(150.dp)
                    .clip(RoundedCornerShape(size = 8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview
@Composable
fun CharacterItemPreview() {
    RickAndMortyTheme {
        CharacterItem(
            item = Character(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                gender = "Male",
                origin = "Earth (C-137)",
                location = "Citadel of Ricks",
                image = "https://rickandmortyapi.com/api/location/1"
            ),
            onItemClick = {}
        )
    }
}
