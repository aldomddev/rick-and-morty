package br.com.amd.rickandmorty.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import br.com.amd.rickandmorty.domain.model.Character
import br.com.amd.rickandmorty.ui.theme.RickAndMortyTheme
import coil.compose.AsyncImage

@Composable
fun CharacterListScreen(
    characters: LazyPagingItems<Character>,
    modifier: Modifier = Modifier,
    navigateToDetails: (id: Int) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = characters.loadState) {
        if (characters.loadState.refresh is LoadState.Error) {
            Toast.makeText(context, "Ooops!!!", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        if (characters.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
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
    Card(modifier = modifier.clickable { onItemClick(item.id) }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(16.dp)
        ) {
            AsyncImage(
                model = item.image,
                contentDescription = item.name,
                modifier = Modifier
                    .weight(2f)
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val statusColor = if (item.status == "Alive") Color.Green else Color.Red
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(shape = CircleShape)
                            .background(statusColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        text = "${item.status} - ${item.species}",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                Spacer(modifier = modifier.height(8.dp))
                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = "From:",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.LightGray
                )
                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = item.origin,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = modifier.height(8.dp))
                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = "Last known location:",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.LightGray
                )
                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = item.location,
                    style = MaterialTheme.typography.titleSmall
                )
            }
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