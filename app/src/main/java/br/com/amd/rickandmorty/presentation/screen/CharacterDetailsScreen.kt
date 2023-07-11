package br.com.amd.rickandmorty.presentation.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.amd.rickandmorty.domain.model.Character
import coil.compose.AsyncImage

@Composable
fun CharacterDetailsScreen(
    id: Int,
    uiState: CharacterDetailsUiState,
    modifier: Modifier = Modifier,
    onGetCharacterDetails: (id: Int) -> Unit
) {
    LaunchedEffect(key1 = id) {
        onGetCharacterDetails(id)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        when (uiState) {
            CharacterDetailsUiState.Error -> ErrorState()
            CharacterDetailsUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is CharacterDetailsUiState.Success -> SuccessState(character = uiState.data)
        }
    }
}

@Composable
private fun SuccessState(character: Character) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(16.dp)
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = character.name,
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
                modifier = Modifier.fillMaxWidth(),
                text = character.name,
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val statusColor = if (character.status == "Alive") Color.Green else Color.Red
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(shape = CircleShape)
                        .background(statusColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "${character.status} - ${character.species}",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "From:",
                style = MaterialTheme.typography.titleSmall,
                color = Color.LightGray
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = character.origin,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Last known location:",
                style = MaterialTheme.typography.titleSmall,
                color = Color.LightGray
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = character.location,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
private fun ErrorState() {
    Text(text = "Ooops!!!!")
}