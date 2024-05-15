package com.tinderlikeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.timberlikeapp.presentation.component.CharacterInfo
import com.timberlikeapp.presentation.component.SwipableCard
import com.tinderlikeapp.ui.screens.mainscreen.MainScreenEvent
import com.tinderlikeapp.ui.screens.mainscreen.MainScreenIntent
import com.tinderlikeapp.ui.screens.mainscreen.MainScreenViewModel
import com.tinderlikeapp.ui.screens.mainscreen.MainViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scaffoldState = rememberScaffoldState()
            androidx.compose.material.Scaffold(
                scaffoldState = scaffoldState,
                modifier = Modifier.fillMaxSize(),
            ) { innerPadding ->
                MainScreen(
                    modifier = Modifier.padding(innerPadding),
                    scaffoldState = scaffoldState,
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
) {
    val vm: MainScreenViewModel = hiltViewModel()
    val uiState = vm.viewStateFlow.collectAsStateWithLifecycle().value
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(key1 = true) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            vm.eventFlow.collect { event ->
                when (event) {
                    is MainScreenEvent.ShowToast -> {
                        scaffoldState.snackbarHostState.showSnackbar(event.message)
                    }
                }
            }
        }
    }
    when {
        uiState.isLoading -> LoadingScreen(
            uiState = uiState,
            modifier = modifier
        )

        uiState.showNoData -> NoDataScreen(
            uiState = uiState,
            modifier = modifier,
            onAction = {
                vm.onAction(it)
            }
        )

        else -> SwipableCardStack(
            uiState = uiState,
            onAction = {
                vm.onAction(it)
            }
        )
    }
}


@Composable
fun LoadingScreen(
    uiState: MainViewState,
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = uiState.loadingMessage,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.size(16.dp))
        CircularProgressIndicator()
    }
}

@Composable
fun NoDataScreen(
    modifier: Modifier,
    uiState: MainViewState,
    onAction: (MainScreenIntent) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = uiState.noDataMessage,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.size(16.dp))
        IconButton(
            onClick = {
                onAction.invoke(MainScreenIntent.OnRefresh)
            },
            modifier = Modifier.size(36.dp)
        ) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
        }
    }
}

@Composable
fun SwipableCardStack(
    uiState: MainViewState,
    onAction: (MainScreenIntent) -> Unit = {},
) {
    uiState.charactersUiList.forEach {
        SwipableCard(
            onSwipeLeft = {
                onAction(MainScreenIntent.OnSwipeLeft(it.id))
            },
            onSwipeRight = {
                onAction(MainScreenIntent.OnSwipeRight(it.id))
            },
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = 0.dp,
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )
                CharacterInfo(it)
            }
        }
    }
}