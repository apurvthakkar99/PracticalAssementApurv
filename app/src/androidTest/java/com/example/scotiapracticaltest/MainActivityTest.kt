package com.example.scotiapracticaltest

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.example.scotiapracticaltest.data.model.Repository
import com.example.scotiapracticaltest.navigation.AppNavigation
import com.example.scotiapracticaltest.singelton.AppContainer
import com.example.scotiapracticaltest.singelton.AppContainer.repositoryListModel
import com.example.scotiapracticaltest.ui.main.RepositoryDetailActivity
import com.example.scotiapracticaltest.utils.formatDate
import com.example.scotiapracticaltest.viewmodel.RepositoryListModel
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    private lateinit var instrumentationContext: Context

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        instrumentationContext = getInstrumentation().targetContext.applicationContext
        AppContainer.repositoryListModel = RepositoryListModel(instrumentationContext)
    }

    // Test 1: Verify search bar is displayed with button
    @Test
    fun repositoryListScreen_showsSearchBar() {
        composeTestRule.setContent {
            AppNavigation()
        }

        // Assert that search bar and repository list items are shown
        composeTestRule.onNodeWithText("Enter GitHub User ID").assertIsDisplayed()
        composeTestRule.onNodeWithText("SEARCH").assertIsDisplayed()
    }

    // Test 2: Verify search button disabled initially with no text and enabled when input inserted
    @Test
    fun searchButton_isDisabledEnabledState() {
        composeTestRule.setContent {
            AppNavigation()
        }

        // Verify that the button is disabled initially
        composeTestRule.onNodeWithTag("SearchButton").assertIsNotEnabled()

        composeTestRule.onNodeWithTag("SearchTextField").performTextInput("test")
        // Verify that the button is enabled after text input
        composeTestRule.onNodeWithTag("SearchButton").assertIsEnabled()
    }

    // Test 3: Verify search button click and giving results
    @Test
    fun testSearchAndDisplaysRepositoryList() {
        composeTestRule.setContent {
            AppNavigation()
        }
        // Enter text in the search bar
        composeTestRule.onNodeWithTag("SearchTextField").performTextInput("test")

        // Capture the initial animation key to check for reset
        val initialAnimationKey = repositoryListModel.uniqueAnimationKey.value

        // Click the search button
        composeTestRule.onNodeWithTag("SearchButton").performClick()

        // Confirm that animation key has updated (animation reset occurred)
        assertNotEquals(initialAnimationKey, repositoryListModel.uniqueAnimationKey.value)

        // Simulate delay if necessary for fetching data
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithTag("RepositoryList").fetchSemanticsNodes().isNotEmpty()
        }

        // Check that the repository list is displayed
        composeTestRule.onNodeWithTag("RepositoryList")
            .assertExists("The repository list should be displayed")
    }

    // Test 4: Verify repository detail displaying all repository info
    @Test
    fun repositoryDetailScreen_showsRepositoryDetails() {
        val sampleRepo = Repository(
            id = 1,
            name = "SampleRepo",
            description = "Sample Description",
            forks = 100,
            updatedAt = "2024-11-04T10:00:00Z",
            stargazersCount = 150
        )

        composeTestRule.setContent {
            RepositoryDetailActivity(
                repository = sampleRepo,
                navController = rememberNavController()
            )
        }

        composeTestRule.onNodeWithText("SampleRepo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sample Description").assertIsDisplayed()

        // Retrieve the formatted text from the string resources
        val forksText = instrumentationContext.getString(R.string.forks, sampleRepo.forks)
        val starsText = instrumentationContext.getString(R.string.stars, sampleRepo.stargazersCount)
        val lastUpdatedText = instrumentationContext.getString(
            R.string.last_updated,
            formatDate(sampleRepo.updatedAt)
        )

        composeTestRule.onNodeWithText(forksText).assertIsDisplayed()
        composeTestRule.onNodeWithText(starsText).assertIsDisplayed()
        composeTestRule.onNodeWithText(lastUpdatedText).assertIsDisplayed()
    }

    // Test 5: Verify repository list item click and redirection working to detail screen
    @Test
    fun repositoryListItemClick_navigatesToDetailScreenAndShowsCorrectRepositoryDetails() {
        // Sample repository list
        val sampleRepoList = listOf(
            Repository(
                id = 1,
                name = "SampleRepo",
                description = "Sample Description",
                forks = 100,
                updatedAt = "2024-11-04T10:00:00Z",
                stargazersCount = 150
            )
        )

        composeTestRule.setContent {
            AppNavigation() // Set up the entire app's navigation
        }

        // Mock the repository list and display it in the UI
        repositoryListModel.setTestData(sampleRepoList, true)

        // Find the repository item in the list and click it
        composeTestRule.onNodeWithTag("RepositoryList")
            .onChildAt(0)
            .performClick()

        // Now we are in the repositoryDetail screen
        // Verify that details of the selected repository are displayed correctly
        composeTestRule.onNodeWithText("SampleRepo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sample Description").assertIsDisplayed()

        // Retrieve the formatted text for forks, stars, and last updated date from resources
        val forksText = instrumentationContext.getString(R.string.forks, sampleRepoList[0].forks)
        val starsText =
            instrumentationContext.getString(R.string.stars, sampleRepoList[0].stargazersCount)
        val lastUpdatedText = instrumentationContext.getString(
            R.string.last_updated,
            formatDate(sampleRepoList[0].updatedAt)
        )

        composeTestRule.onNodeWithText(forksText).assertIsDisplayed()
        composeTestRule.onNodeWithText(starsText).assertIsDisplayed()
        composeTestRule.onNodeWithText(lastUpdatedText).assertIsDisplayed()
    }

    // Test 6: Verify repository detail displaying star badge for high forks
    @Test
    fun repositoryDetailScreen_showsStarBadgeWhenForksAreHigh() {
        // Set up a repository with forks > 5000
        val highForkRepo = Repository(
            id = 1,
            name = "High Fork Repo",
            description = "Popular repository",
            forks = 6000,
            updatedAt = "2024-11-04T10:00:00Z",
            stargazersCount = 10000
        )

        composeTestRule.setContent {
            RepositoryDetailActivity(
                repository = highForkRepo,
                navController = rememberNavController()
            )
        }

        composeTestRule.onNodeWithText(instrumentationContext.getString(R.string.star_badge))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("High Fork Repo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Popular repository").assertIsDisplayed()

        // Retrieve the formatted text from the string resources
        val forksText = instrumentationContext.getString(R.string.forks, highForkRepo.forks)
        val starsText =
            instrumentationContext.getString(R.string.stars, highForkRepo.stargazersCount)
        val lastUpdatedText = instrumentationContext.getString(
            R.string.last_updated,
            formatDate(highForkRepo.updatedAt)
        )

        composeTestRule.onNodeWithText(forksText).assertIsDisplayed()
        composeTestRule.onNodeWithText(starsText).assertIsDisplayed()
        composeTestRule.onNodeWithText(lastUpdatedText).assertIsDisplayed()
    }
}