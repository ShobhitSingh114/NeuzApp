package com.example.neuzapp.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.neuzapp.presentation.article_screen.ArticleScreen
import com.example.neuzapp.presentation.news_screen.NewsScreen
import com.example.neuzapp.presentation.Screen

@Composable
fun NavGraphSetup(
    navController: NavHostController
) {
    // optional to use this
    val argKey = "web_url"

    NavHost(
        navController = navController,
        startDestination = "news_screen"
    ) {
        composable(
            route = "news_screen"
        ) {
            NewsScreen(
                onReadFullStoryButtonClicked = { url ->
                    navController.navigate(route = "article_screen?$argKey=$url")
//                    navController.navigate(route = "article_screen?web_url=$it")
                }
            )
        }
        composable(
            // ? = shows there will be an argument, and its name we used = web_url
            // {web_url} = argument value
            route = "article_screen?$argKey={$argKey}",
            // arguments = here we give name and type to the argument
            arguments = listOf(navArgument(name = argKey){
                type = NavType.StringType
            })
        ) {
            // it = url which come when ReadFullStoryButton pressed in NewsScreen
            ArticleScreen(
                url = it.arguments?.getString(argKey),
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }
    }
}