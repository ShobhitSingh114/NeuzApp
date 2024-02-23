package com.example.neuzapp.presentation.news_screen.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryTabRow(
    pagerState: PagerState,
    categories: List<String>,
//    onTabClick: (Int) -> Unit
    selectedTabIndex: Int

) {
//
//    ScrollableTabRow(
//        selectedTabIndex = selectedTabIndex,
//        edgePadding = 0.dp,
//        containerColor = MaterialTheme.colorScheme.primaryContainer,
//        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
//    ) {
//        categories.forEachIndexed { tabIndex, category ->
//            Tab(
//                selected = tabIndex == selectedTabIndex,
//                onClick = { selectedTabIndex = tabIndex }
//            ) {
//                Text(
//                    text = category,
//                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp)
//                )
//            }
//        }
//    }

}