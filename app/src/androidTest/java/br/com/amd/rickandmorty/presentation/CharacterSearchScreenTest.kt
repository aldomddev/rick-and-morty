package br.com.amd.rickandmorty.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.paging.PagingData
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.amd.rickandmorty.domain.model.Character
import br.com.amd.rickandmorty.presentation.model.CharacterStatusFilter
import br.com.amd.rickandmorty.presentation.screen.search.CharacterSearchScreen
import br.com.amd.rickandmorty.ui.theme.RickAndMortyTheme
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterSearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val characterData = Character(
        id = 1,
        name = "name",
        status = "unknown",
        species = "species",
        gender = "gender",
        origin = "origin",
        location = "location",
        image = "image"
    )

    private val pagingData = PagingData.from(listOf(characterData))
    private val fakeDataFlow = MutableStateFlow(pagingData)
    private val statusList = CharacterStatusFilter.values().toList()

    @Test
    fun onDataAvailable_Assert_ListHasItems() {
        composeTestRule.setContent {
            RickAndMortyTheme {
                CharacterSearchScreen(
                    statusList = statusList,
                    streamOfCharacters = fakeDataFlow,
                    onNameQueryChange = {},
                    onStatusQueryChange = {},
                    navigateToDetails = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag(TestTags.CHARACTERS_LIST)
            .onChildAt(0)
            .assertTextEquals("name")
    }

    @Test
    fun onLoad_Assert_DefaultNameQueryIsEmpty() {
        composeTestRule.setContent {
            val pagingData = PagingData.from(listOf(characterData))
            val fakeDataFlow = MutableStateFlow(pagingData)

            RickAndMortyTheme {
                CharacterSearchScreen(
                    statusList = CharacterStatusFilter.values().toList(),
                    streamOfCharacters = fakeDataFlow,
                    onNameQueryChange = {},
                    onStatusQueryChange = {},
                    navigateToDetails = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag(TestTags.SEARCH_INPUT_PLACEHOLDER, true)
            .assertTextEquals("Type a character name")
    }

    @Test
    fun onLoad_Assert_StatusSelectionWithDefaultSelectionIsPresent() {
        composeTestRule.setContent {
            RickAndMortyTheme {
                CharacterSearchScreen(
                    statusList = statusList,
                    streamOfCharacters = fakeDataFlow,
                    onNameQueryChange = {},
                    onStatusQueryChange = {},
                    navigateToDetails = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("${TestTags.STATUS_ITEM}0")
            .assertIsSelected()

        composeTestRule
            .onNodeWithTag("${TestTags.STATUS_ITEM_TEXT}0", true)
            .assertTextEquals(CharacterStatusFilter.ALL.label)

        composeTestRule
            .onNodeWithTag("${TestTags.STATUS_ITEM_ICON}0", true)
            .assertIsDisplayed()
    }
}
