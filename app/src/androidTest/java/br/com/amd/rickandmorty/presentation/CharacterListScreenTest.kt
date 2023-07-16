package br.com.amd.rickandmorty.presentation

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithTag
import androidx.paging.PagingData
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.amd.rickandmorty.domain.model.Character
import br.com.amd.rickandmorty.presentation.screen.list.CharacterListScreen
import br.com.amd.rickandmorty.ui.theme.RickAndMortyTheme
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterListScreenTest {

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

    @Test
    fun onDataAvailable_Assert_ListHasItems() {
        composeTestRule.setContent {
            val pagingData = PagingData.from(listOf(characterData))
            val fakeDataFlow = MutableStateFlow(pagingData)

            RickAndMortyTheme {
                CharacterListScreen(streamOfCharacters = fakeDataFlow, navigateToDetails = {})
            }
        }

        composeTestRule
            .onNodeWithTag(TestTags.CHARACTERS_LIST)
            .onChild()
            .assertTextEquals("name")
    }
}
