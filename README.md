**Overview**
This Kotlin-based Android app fetches and displays GitHub repositories for a given user. It includes search functionality, a repository list, and a detailed view for each repository, built using Jetpack Compose and following MVVM architecture for scalability and testability. Additionally, comprehensive test cases have been implemented to verify UI components, user interactions, ViewModel logic, and network operations, ensuring a robust and reliable application structure.

**Jetpack Components Used**
Jetpack Compose: For declarative UI.
Navigation Component: Manages navigation using NavController.
ViewModel & StateFlow: Holds and observes UI-related data.
Retrofit: Handles network requests to GitHub API.

**Project Structure**
Main Files:
MainActivity.kt: Sets up Compose UI, initializes RepositoryListModel, and manages navigation.
AppContainer.kt: Manages dependencies (Retrofit, ApiService, GitHubRepository) as a singleton.
RepositoryListModel.kt: Handles business logic and data fetching for GitHub repositories.
Jetpack Compose UI Components:
CommonAppBar: Reusable top bar with title and back navigation.
CommonSearchBar: Search bar with input field and search button.
CommonProfileSection: Displays user profile.
CommonRepositoryList & CommonRepositoryItem: Shows repositories in a LazyColumn.
CommonRepositoryStats & CommonRepositoryInfo: Displays repository details.
RepositoryListActivity & RepositoryDetailActivity: Main screens for repository list and details.

**Key Components and Architecture**
UI Design: Compose defines UI elements with LazyColumn for efficient list loading and AnimatedVisibility for smooth animations.
Navigation: NavHost and NavController manage transitions between repository list and detail views.
Dependency Injection: AppContainer lazily initializes Retrofit, ApiService, and ViewModel instances.
Networking with Retrofit: RetrofitInstance sets up API communication, while ApiService defines endpoints.
ViewModel (RepositoryListModel): Manages search logic, data fetching, and UI state with MutableStateFlow.
Error Handling: Uses ToastNotifier to handle connection errors, and isInternetAvailable to prevent failed API calls.

**Key Coding Practices**
Scalable UI with Compose: Modular UI components enable reuse and facilitate testing.
MVVM Architecture: Keeps UI logic in RepositoryListModel, acting as a single source of truth.
Resource Management: Lazy initialization in AppContainer optimizes resources.
Reactive UI with StateFlow: Ensures responsive UI updates.
Summary of Files
MainActivity.kt: Initializes UI and dependencies.
AppContainer.kt: Provides singleton instances for dependencies.
RetrofitInstance.kt: Sets up Retrofit.
RepositoryListModel.kt: Handles API calls and UI state.
Composable Components: Modular components for app bar, search bar, profile, repository list, and details.

**Test Cases Summary:**
This suite tests core functionalities across UI and ViewModel layers, focusing on user interactions, data flow, and component integration.
MainActivityTest - UI and Integration Tests
Search Bar Visibility: Verifies that the search bar and button are displayed on the main screen.
Search Button State: Ensures the search button is disabled initially and enabled when text is entered.
Search and Display List: Confirms that entering text and clicking search displays the repository list.
Repository Detail Screen: Checks that all repository details (name, description, forks, stars, last updated) display correctly on the detail screen.
Item Click Navigation: Verifies clicking a repository item navigates to the detail screen with correct details.
Star Badge for Popular Repos: Confirms a star badge is shown on detail screen for repositories with high fork counts.

**RepositoryListModelTest - ViewModel Tests**
Avoid Redundant Searches: Ensures performSearch avoids redundant API calls when input is unchanged.
Successful User Fetch: Confirms user data loads correctly and shows the profile section on success.
Successful Repositories Fetch: Verifies repository list populates and displays on a successful API call.
Error Handling: Confirms error response clears the repository list and hides the view.
Reset Animations: Resets profile and list visibility and updates animation key for smooth transitions.
Fetch by ID: Ensures fetching repositories by ID works, returning null if not found.
Search Trigger: Verifies performSearch triggers user and repository fetches when input is new.

**Conclusion**
This project showcases a robust architecture using Jetpack Compose and modern Android development practices. It emphasizes:
Reusable and modular UI components with Compose.
MVVM architecture with ViewModel and StateFlow.
Dependency management through AppContainer.
Reactive UI and smooth animations.
Automated testing for UI, integration and ViewModel.
This structure results in a scalable and testable application that is easy to extend and maintain.
