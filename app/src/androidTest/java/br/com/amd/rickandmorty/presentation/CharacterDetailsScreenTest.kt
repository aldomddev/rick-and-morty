package br.com.amd.rickandmorty.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.amd.rickandmorty.domain.model.Character
import br.com.amd.rickandmorty.presentation.TestTags.LOADING
import br.com.amd.rickandmorty.presentation.screen.details.CharacterDetailsScreen
import br.com.amd.rickandmorty.presentation.screen.details.CharacterDetailsUiState
import br.com.amd.rickandmorty.ui.theme.RickAndMortyTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterDetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testData = Character(
        id = 1,
        name = "name",
        status = "unknown",
        species = "species",
        gender = "gender",
        origin = "origin",
        location = "location",
        image = "image"
    )

    private lateinit var uiState: CharacterDetailsUiState

    @Test
    fun onStateLoading_Assert_ProgressIsDisplayed() {
        uiState = CharacterDetailsUiState.Loading

        composeTestRule.setContent {
            RickAndMortyTheme {
                CharacterDetailsScreen(
                    id = 1,
                    uiState = uiState,
                    onGetCharacterDetails = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(LOADING).assertIsDisplayed()
    }

    @Test
    fun onStateSuccess_Assert_ContentIsDisplayed() {
        uiState = CharacterDetailsUiState.Success(testData)

        composeTestRule.setContent {
            RickAndMortyTheme {
                CharacterDetailsScreen(
                    id = 1,
                    uiState = uiState,
                    onGetCharacterDetails = {}
                )
            }
        }

        composeTestRule.onNodeWithText(testData.name).assertIsDisplayed()
        composeTestRule.onNodeWithText("${testData.status} - ${testData.species}").assertIsDisplayed()
        composeTestRule.onNodeWithText(testData.origin).assertIsDisplayed()
        composeTestRule.onNodeWithText(testData.location).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(testData.name).assertIsDisplayed()
    }

    @Test
    fun onStateError_Assert_MessageIsDisplayed() {
        uiState = CharacterDetailsUiState.Error

        composeTestRule.setContent {
            RickAndMortyTheme {
                CharacterDetailsScreen(
                    id = 1,
                    uiState = uiState,
                    onGetCharacterDetails = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Sorry!").assertIsDisplayed()
    }
}
