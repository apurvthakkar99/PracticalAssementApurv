package com.example.scotiapracticaltest

import android.content.Context
import com.example.scotiapracticaltest.data.model.Repository
import com.example.scotiapracticaltest.data.model.User
import com.example.scotiapracticaltest.data.repository.GithubRepository
import com.example.scotiapracticaltest.ui.common.NoOpToastNotifier
import com.example.scotiapracticaltest.viewmodel.RepositoryListModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryListModelTest {
    private lateinit var repositoryListModel: RepositoryListModel
    private val mockContext: Context = mockk()
    private val mockRepository: GithubRepository = mockk()
    private val testUser = User("test", "https://avatars.github.com/")
    private val testRepos = listOf(
        Repository(
            1,
            "Repo1",
            forks = 1,
            description = "repo description 1",
            stargazersCount = 1,
            updatedAt = "2024-11-03T10:00:00Z"
        ),
        Repository(
            2,
            "Repo2",
            forks = 2,
            description = "repo description 2",
            stargazersCount = 2,
            updatedAt = "2024-11-04T10:00:00Z"
        )
    )
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher) // Set the main dispatcher to the test dispatcher
        repositoryListModel = RepositoryListModel(mockContext, NoOpToastNotifier(), true).apply {
            repository = mockRepository
        }
    }

    // Test 1: Verify performSearch() when input is unchanged
    @Test
    fun `performSearch does not call fetchUserData or fetchUserRepositories when input is unchanged`() =
        runTest {
            repositoryListModel.userId.value = "test"
            repositoryListModel.lastSearchId = "test"

            repositoryListModel.performSearch()

            coVerify(exactly = 0) { mockRepository.getUser(any()) }
            coVerify(exactly = 0) { mockRepository.getUserRepos(any()) }
        }

    // Test 2: Verify fetchUserData() handles success correctly
    @Test
    fun `fetchUserData sets user and showProfile on success`() = runTest {
        coEvery { mockRepository.getUser("test") } returns Result.success(testUser)

        repositoryListModel.userId.value = "test"
        repositoryListModel.fetchUserData()

        assertEquals(testUser, repositoryListModel.user.first())
        assertTrue(repositoryListModel.showProfile.first())
    }

    // Test 3: Verify fetchUserRepositories() handles success correctly
    @Test
    fun `fetchUserRepositories sets repositories and showList on success`() = runTest {
        coEvery { mockRepository.getUserRepos("test") } returns Result.success(testRepos)

        repositoryListModel.userId.value = "test"
        repositoryListModel.fetchUserRepositories()

        assertEquals(testRepos, repositoryListModel.repositories.first())
        assertTrue(repositoryListModel.showList.first())
    }

    // Test 4: Verify fetchUserRepositories() handles failure correctly
    @Test
    fun `fetchUserRepositories shows error message on failure`() = runTest {
        coEvery { mockRepository.getUserRepos("test") } returns Result.failure(Exception("Error"))

        repositoryListModel.userId.value = "test"
        repositoryListModel.fetchUserRepositories()

        assertTrue(repositoryListModel.repositories.first().isEmpty())
        assertFalse(repositoryListModel.showList.first())
    }

    // Test 5: Verify resetAnimations() resets state and creates new unique key
    @Test
    fun `resetAnimations resets showProfile and showList and updates uniqueAnimationKey`() =
        runTest {
            val oldKey = repositoryListModel.uniqueAnimationKey.first()

            repositoryListModel.resetAnimations()

            assertFalse(repositoryListModel.showProfile.first())
            assertFalse(repositoryListModel.showList.first())
            assertNotEquals(oldKey, repositoryListModel.uniqueAnimationKey.first())
        }

    // Test 6: Verify getRepositoryById returns correct repository or null if not found
    @Test
    fun `getRepositoryById returns correct repository or null if not found`() = runTest {
        coEvery { mockRepository.getUserRepos(any()) } returns Result.success(testRepos)
        repositoryListModel.fetchUserRepositories()
        advanceUntilIdle() // Wait for coroutines to finish

        val repo = repositoryListModel.getRepositoryById(1)
        assertEquals(testRepos[0], repo)

        val missingRepo = repositoryListModel.getRepositoryById(3)
        assertNull(missingRepo)
    }

    // Test 7: Verify performSearch() triggers fetchUserData and fetchUserRepositories
    @Test
    fun `performSearch triggers fetchUserData and fetchUserRepositories`() = runTest {
        repositoryListModel.userId.value = "test"
        repositoryListModel.lastSearchId = null // Reset lastSearchId to ensure search is performed

        coEvery { mockRepository.getUser("test") } returns Result.success(testUser)
        coEvery { mockRepository.getUserRepos("test") } returns Result.success(testRepos)

        // Act
        repositoryListModel.performSearch()
        advanceUntilIdle()

        // Collect StateFlows to capture the latest emitted values
        val collectedUser = repositoryListModel.user.first()
        val collectedShowProfile = repositoryListModel.showProfile.first()
        val collectedRepositories = repositoryListModel.repositories.first()
        val collectedShowList = repositoryListModel.showList.first()

        // Assert
        assertEquals(testUser, collectedUser) // Verify that the user was fetched and updated
        assertTrue(collectedShowProfile)      // Verify that the profile view is shown
        assertEquals(
            testRepos,
            collectedRepositories
        ) // Verify that repositories are fetched and updated
        assertTrue(collectedShowList)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }
}