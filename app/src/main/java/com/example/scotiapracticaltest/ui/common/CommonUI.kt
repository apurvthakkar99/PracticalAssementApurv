package com.example.scotiapracticaltest.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.scotiapracticaltest.R
import com.example.scotiapracticaltest.data.model.Repository
import com.example.scotiapracticaltest.data.model.User
import com.example.scotiapracticaltest.utils.formatDate
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonAppBar(
    title: String,
    onBackPressed: (() -> Unit)? = null
) {
    if (onBackPressed != null) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            navigationIcon = onBackPressed.let {
                {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
        )
    } else {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
fun CommonSearchBar(
    textFieldValue: String,
    onValueChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f)) {
            TextField(
                value = textFieldValue,
                onValueChange = onValueChange,
                label = { Text(stringResource(id = R.string.enter_github_userid)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = (-12).dp)
                    .testTag("SearchTextField"),
                textStyle = TextStyle(fontSize = 18.sp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide(); onSearchClick() }),
                singleLine = true
            )
            Divider(
                color = if (textFieldValue.isEmpty()) Color.Gray else Color.DarkGray,
                thickness = if (textFieldValue.isEmpty()) 1.dp else 2.dp
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onSearchClick,
            shape = RoundedCornerShape(5.dp),
            enabled = textFieldValue.isNotBlank(),
            modifier = Modifier.testTag("SearchButton"),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
                contentColor = Color.Black
            )
        ) {
            Text(stringResource(id = R.string.search).uppercase(), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CommonProfileSection(user: User?, showProfile: Boolean) {
    AnimatedVisibility(
        visible = showProfile,
        enter = slideInVertically(
            initialOffsetY = { it / 2 }
        ) + fadeIn(animationSpec = tween(1000)),
        exit = fadeOut(animationSpec = tween(500))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    user?.avatarUrl ?: "https://picsum.photos/200/300"
                ),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(7.dp))
            Text(
                user?.name ?: stringResource(id = R.string.unknown),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun CommonRepositoryList(
    repositories: List<Repository>,
    showList: Boolean,
    loading: Boolean,
    onRepositoryClick: (Repository) -> Unit
) {
    AnimatedVisibility(
        visible = showList,
        enter = slideInVertically(
            initialOffsetY = { it / 2 }
        ) + fadeIn(animationSpec = tween(1000)),
        exit = fadeOut(animationSpec = tween(500))
    ) {
        if (!loading) {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .testTag("RepositoryList")) {
                itemsIndexed(repositories) { index, repo ->
                    LaunchedEffect(index) { delay(index * 100L) }
                    CommonRepositoryItem(repo = repo, onClick = { onRepositoryClick(repo) })
                }
            }
        }
    }
}

@Composable
fun CommonRepositoryItem(repo: Repository, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .background(color = Color.White)
            .shadow(elevation = 5.dp)
            .clickable { onClick() }
            .testTag("RepositoryItem"),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = repo.name,
                style = TextStyle(
                    fontSize = 19.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.testTag("RepositoryItemName")
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = repo.description ?: stringResource(id = R.string.no_description),
                style = TextStyle(fontSize = 16.sp, color = Color.Gray),
                modifier = Modifier.testTag("RepositoryItemDescription")
            )
        }
    }
}

@Composable
fun CommonRepositoryStats(repository: Repository) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(id = R.string.forks, repository.forks),
            style = MaterialTheme.typography.bodyMedium
        )
        if (repository.forks > 5000) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.star_badge),
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.stars, repository.stargazersCount),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = String.format(
                stringResource(id = R.string.last_updated),
                formatDate(repository.updatedAt)
            ),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun CommonRepositoryInfo(repository: Repository) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = repository.name,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = repository.description ?: stringResource(id = R.string.no_description_available),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}