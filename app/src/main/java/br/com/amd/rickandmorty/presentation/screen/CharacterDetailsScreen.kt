package br.com.amd.rickandmorty.presentation.screen

import android.content.res.Configuration
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
            CharacterDetailsUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is CharacterDetailsUiState.Success -> SuccessState(character = uiState.data)
            CharacterDetailsUiState.Error -> ErrorState()
        }
    }
}

@Composable
private fun SuccessState(
    character: Character
) {
    val configuration = LocalConfiguration.current

    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        PortraitDetailsContent(character = character)
    } else {
        LandscapeDetailsContent(character = character)
    }
}

@Composable
private fun PortraitDetailsContent(character: Character) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            modifier = Modifier
                .height(200.dp)
                .clip(RoundedCornerShape(size = 8.dp))
        )
        Row(
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp)
                .width(IntrinsicSize.Max)
        ) {
            Column {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val statusColor = getCharacterStatusColor(character.status)
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(shape = CircleShape)
                            .background(statusColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${character.status} - ${character.species}",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "From:",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.LightGray
                )
                Text(
                    text = character.origin,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Last known location:",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.LightGray
                )
                Text(
                    text = character.location,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
private fun LandscapeDetailsContent(
    character: Character,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            modifier = Modifier
                .height(200.dp)
                .clip(RoundedCornerShape(size = 8.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = modifier.fillMaxWidth(),
                text = character.name,
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val statusColor = getCharacterStatusColor(character.status)
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(shape = CircleShape)
                        .background(statusColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = "${character.status} - ${character.species}",
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
                text = character.origin,
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
                text = character.location,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

private fun getCharacterStatusColor(status: String): Color {
    return when (status) {
        "Alive" -> Color.Green
        "Dead" -> Color.Red
        else -> Color.Gray
    }
}

@Composable
private fun ErrorState() {
    Text(text = "Ooops!!!!")
}