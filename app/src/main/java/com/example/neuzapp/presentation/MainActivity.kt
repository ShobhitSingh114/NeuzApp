package com.example.neuzapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.neuzapp.presentation.news_screen.NewsScreen
import com.example.neuzapp.presentation.news_screen.NewsScreenViewModel
import com.example.neuzapp.presentation.ui.theme.NeuzAppTheme
import com.example.neuzapp.util.NavGraphSetup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NeuzAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    val viewmodel: NewsScreenViewModel = hiltViewModel()
//                    NewsScreen(
//                        state = viewmodel.state.value,
//                        onEvent = viewmodel::onEvent
//                    )
                    val navController = rememberNavController()
                    NavGraphSetup(navController = navController)
                }
            }
        }
    }
}



//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    NeuzAppTheme {
//        NewsScreen()
//    }
//}