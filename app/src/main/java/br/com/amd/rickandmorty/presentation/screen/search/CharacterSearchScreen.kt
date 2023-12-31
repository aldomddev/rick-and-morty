package br.com.amd.rickandmorty.presentation.screen.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.amd.rickandmorty.R
import br.com.amd.rickandmorty.domain.model.Character
import br.com.amd.rickandmorty.presentation.TestTags.CHARACTERS_LIST
import br.com.amd.rickandmorty.presentation.TestTags.SEARCH_INPUT
import br.com.amd.rickandmorty.presentation.TestTags.SEARCH_INPUT_PLACEHOLDER
import br.com.amd.rickandmorty.presentation.TestTags.STATUS_ITEM
import br.com.amd.rickandmorty.presentation.TestTags.STATUS_ITEM_ICON
import br.com.amd.rickandmorty.presentation.TestTags.STATUS_ITEM_TEXT
import br.com.amd.rickandmorty.presentation.TestTags.STATUS_SELECTOR
import br.com.amd.rickandmorty.presentation.model.CharacterStatusFilter
import br.com.amd.rickandmorty.presentation.screen.list.CharacterItem
import kotlinx.coroutines.flow.Flow

@Composable
fun CharacterSearchScreen(
    modifier: Modifier = Modifier,
    statusList: List<CharacterStatusFilter>,
    streamOfCharacters: Flow<PagingData<Character>>,
    onNameQueryChange: (String) -> Unit,
    onStatusQueryChange: (CharacterStatusFilter) -> Unit,
    navigateToDetails: (id: Int) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            SearchInput(onSearch = { onNameQueryChange(it) })

            StatusSelector(
                statusList = statusList,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                onSelected = { status ->
                    onStatusQueryChange(status)
                }
            )

            FilteredContent(
                streamOfCharacters = streamOfCharacters,
                navigateToDetails = navigateToDetails
            )
        }
    }
}

@Composable
fun SearchInput(
    onSearch: (String) -> Unit
) {
    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(SEARCH_INPUT),
        singleLine = true,
        value = text,
        onValueChange = { newText ->
            text = newText
            active = newText.isNotEmpty()
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch(text) }),
        placeholder = {
            Text(
                modifier = Modifier.testTag(SEARCH_INPUT_PLACEHOLDER),
                text = stringResource(id = R.string.characters_search_input_placeholder)
            )
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = stringResource(id = R.string.characters_search_icon_search_description)
            )
        },
        trailingIcon = {
            if (active) {
                Icon(
                    modifier = Modifier.clickable {
                        text = ""
                        active = false
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.characters_search_icon_close_description)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusSelector(
    modifier: Modifier = Modifier,
    selection: Int = 0,
    statusList: List<CharacterStatusFilter>,
    onSelected: (status: CharacterStatusFilter) -> Unit
) {
    var selected by rememberSaveable { mutableStateOf(selection) }

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 24.dp),
        text = stringResource(id = R.string.characters_search_status_Label)
    )

    LazyRow(
        modifier = modifier.testTag(STATUS_SELECTOR),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(statusList.size) { index ->
            val status = statusList[index]
            val isSelected = status == statusList[selected]

            FilterChip(
                modifier = Modifier.testTag("$STATUS_ITEM$index"),
                selected = isSelected,
                label = {
                    Text(
                        modifier = Modifier.testTag("$STATUS_ITEM_TEXT$index"),
                        text = status.label
                    )
                },
                leadingIcon = {
                    if (isSelected) {
                        Icon(
                            modifier = Modifier.testTag("$STATUS_ITEM_ICON$index"),
                            imageVector = Icons.Default.Check,
                            contentDescription = null
                        )
                    }
                },
                onClick = {
                    selected = index
                    onSelected(statusList[index])
                }
            )
        }
    }
}

@Composable
fun FilteredContent(
    streamOfCharacters: Flow<PagingData<Character>>,
    navigateToDetails: (id: Int) -> Unit
) {
    val charactersFiltered = streamOfCharacters.collectAsLazyPagingItems()

    if (charactersFiltered.loadState.refresh is LoadState.Loading) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }
    }

    if (
        charactersFiltered.loadState.refresh is LoadState.Error &&
        charactersFiltered.itemCount == 0
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            text = "No matching characters found",
            textAlign = TextAlign.Center
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .testTag(CHARACTERS_LIST),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(charactersFiltered.itemCount) { itemIndex ->
                val character = charactersFiltered[itemIndex]
                if (character != null) {
                    CharacterItem(
                        item = character,
                        modifier = Modifier.fillMaxWidth(),
                        onItemClick = navigateToDetails
                    )
                }
            }
            item {
                if (charactersFiltered.loadState.append is LoadState.Loading) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
