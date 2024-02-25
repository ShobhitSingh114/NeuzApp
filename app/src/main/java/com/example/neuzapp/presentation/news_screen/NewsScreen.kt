package com.example.neuzapp.presentation.news_screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.neuzapp.data.remote.dto.Article
import com.example.neuzapp.presentation.NewsScreenEvent
import com.example.neuzapp.presentation.news_screen.components.BottomSheetContent
import com.example.neuzapp.presentation.news_screen.components.NewsCard
import com.example.neuzapp.presentation.news_screen.components.NewsScreenTopBar
import com.example.neuzapp.presentation.news_screen.components.Retry
import com.example.neuzapp.presentation.news_screen.components.SearchAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun NewsScreen(
    viewModel: NewsScreenViewModel = hiltViewModel(),
    onEvent: (NewsScreenEvent) -> Unit = viewModel::onEvent,
    onReadFullStoryButtonClicked: (String) -> Unit
//    state: NewsScreenState,
//    onEvent: (NewsScreenEvent) -> Unit,
) {
    val state = viewModel.state.value
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val categories = listOf(
        "General", "Business", "Health", "Science", "Sports", "Technology", "Entertainment"
    )
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }
    val pagerState = rememberPagerState {
        categories.size
    }
    // tabClick(kiya hai) to swipe(hua hai)
    LaunchedEffect(selectedTabIndex){
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    // swipe(kiya hai) to tabClick(hua hai)
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress){
        if (!pagerState.isScrollInProgress){
            selectedTabIndex = pagerState.currentPage
        }
    }
    // in this LaunchedEffect = we access the 'Events' of HorizontalPager
    // 'pagerState' used as key = whenever 'pagerState' change then this 'LaunchedEffect' will run
    LaunchedEffect(key1 = pagerState){
        // snapshotFlow = collecting currentPage
        // so whatever is the currentPage we can provide that in the according Event
        snapshotFlow { pagerState.currentPage }.collect() {
            onEvent(NewsScreenEvent.onCategoryChanged(category = categories[it]))
        }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var isSheetOpen by remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()

    // focusRequester = helps in the focusing the textField
    val focusRequester = remember{ FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            // onDismissRequest = when the user clicks outside of the bottom sheet
            onDismissRequest = { isSheetOpen = false },
//            content = {
//
//            }
        ){
            state.selectedArticle?.let {
                BottomSheetContent(
                    article = it,
                    onReadFullStoryButtonClicked = {
                        onReadFullStoryButtonClicked(it.url)
                        // This way we are hiding the bottom sheet when the read full article
                        // button got pressed event happen

                        // invokeOnCompletion = when coroutineScope is completed only
                        // then after its function is excuted
                        coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) isSheetOpen = false
                        }
                    }
                )
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Crossfade = allows to switch between two composable layouts with a crossfade animation
        Crossfade(targetState = state.isSearchBarVisible, label = "") { isVisible ->
            // if searchbar show ho rha hai then show searchAppBar and NewsItemList
            if (isVisible) {
                Column {
                    SearchAppBar(
                        // we need to focus in SearchAppBar textField
                        // Now define that --> when it needs to focus this SearchAppBar
                        // when user click on the search icon then focus on this SearchAppBAr
                        modifier = Modifier.focusRequester(focusRequester),
                        // updated user search query
                        value = state.searchQuery,
                        onInputValueChange = { query ->
                            /**
                             * issue here solve it
                              */
//                            onEvent(NewsScreenEvent.onSearchQueryChanged(query))
                        },
                        onClosedIconClicked = { onEvent(NewsScreenEvent.onClosedIconClicked) },
                        onSearchedIconClicked = {
                            // when user click on searchIcon(search ime action) on keyboard then
                            // 1.keyboard disappear and 2.focus should remove form the SearchAppBar TextField
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    )

                    NewsArticleList(
                        state = state,
                        onCardClicked = { article ->
                            isSheetOpen = true
                            onEvent(NewsScreenEvent.onNewsCardClicked(article))
                        },
                        onRetry = {
                            NewsScreenEvent.onCategoryChanged(state.category)
                        }
                    )

                }
            } else {
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        NewsScreenTopBar(
                            scrollBehavior = scrollBehavior,
                            onSearchIconClicked = {
                                coroutineScope.launch {
                                    // when user click on the search icon searchAppBar will appear
                                    //  and it will take some time
                                    delay(500)
                                    focusRequester.requestFocus()
                                }
                                onEvent(NewsScreenEvent.onSearchIconClicked)
                            }
                        )
                    }
                ) { padding ->

                    Box(
                        modifier = Modifier.fillMaxSize(),
//            contentAlignment = Center
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {
//            CategoryTabRow(
//                pagerState = pagerState,
//                categories = categories,
//                selectedTabIndex = selectedTabIndex
//            )

                            ScrollableTabRow(
                                selectedTabIndex = selectedTabIndex,
                                edgePadding = 0.dp,
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ) {
                                categories.forEachIndexed { index, tabItem ->
                                    // tabItem = categories
                                    Tab(
                                        selected = index == selectedTabIndex,
                                        onClick = { selectedTabIndex = index
//                            viewModel.getNews(tabItem)
                                        },
                                        text = { Text(text = tabItem) }
                                    )
                                }
                            }

                            HorizontalPager(
                                state = pagerState
                            ) {
                                NewsArticleList(
                                    state = state,
                                    onCardClicked = { article ->
                                        isSheetOpen = true
                                        onEvent(NewsScreenEvent.onNewsCardClicked(article))
                                    },
                                    onRetry = {
                                        NewsScreenEvent.onCategoryChanged(state.category)
                                    }
                                )
                            }

//                HorizontalPager(
//                    state = pagerState,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(1f)
//                ) {
//                    LazyColumn(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(16.dp),
//                        verticalArrangement = Arrangement.spacedBy(16.dp),
////                    contentPadding = padding
//                    ) {
//                        state.news?.let {
//                            items(it.articles) { article ->
//                                NewsCard(
//                                    article,
//                                    onCardClicked = {
//                                        isSheetOpen = true
//                                        onEvent(NewsScreenEvent.onNewsCardClicked(article))
//                                    }
//                                )
//                            }
//                        }
//                    }
//                }
                        }
//            if (state.error.isNotBlank()) {
//                Retry(
//                    error = state.error,
//                    onRetry = {
//                        onEvent(
//                            NewsScreenEvent.onCategoryChanged(categories[selectedTabIndex])
////                            NewsScreenEvent.onCategoryChanged(state.category)
//                        )
//                    },
//                    modifier = Modifier.align(Center)
//                )
//            }
//            if (state.isLoading) {
//                CircularProgressIndicator(modifier = Modifier.align(Center))
//            }

                    }

                }
            }
        }
    }
}

@Composable
fun NewsArticleList(
    state: NewsScreenState,
    onCardClicked: (Article) -> Unit,
    onRetry: () -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        state.news?.let {
            items(it.articles) { article ->
                NewsCard(
                    article,
                    onCardClicked = onCardClicked
                )
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state.error.isNotBlank()) {
            Retry(
                error = state.error,
                onRetry = onRetry,
                modifier = Modifier.align(Center)
            )
        }
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Center))
        }

    }
}

