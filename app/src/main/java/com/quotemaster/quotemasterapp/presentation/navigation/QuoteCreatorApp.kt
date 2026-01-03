package com.quotemaster.quotemasterapp.presentation.navigation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.quotemaster.quotemasterapp.presentation.component.ExitConfirmationDialog
import com.quotemaster.quotemasterapp.presentation.main.CategoriesFullScreen
import com.quotemaster.quotemasterapp.presentation.main.HomeScreen
import com.quotemaster.quotemasterapp.presentation.quote.PostsTimelineScreen
import com.quotemaster.quotemasterapp.presentation.quote.QuoteScreen
import com.quotemaster.quotemasterapp.presentation.quoteEditor.BackgroundGalleryScreen
import com.quotemaster.quotemasterapp.presentation.quoteEditor.QuoteEditorScreen
import com.quotemaster.quotemasterapp.presentation.quoteEditor.SelectQuoteScreen
import com.quotemaster.quotemasterapp.utils.Constants.DEFAULT_QUOTE
import java.net.URLDecoder

@Composable
fun QuoteCreatorApp(
    modifier: Modifier = Modifier,
    onExitApp: () -> Unit,
) {
    val navController = rememberNavController()
    var showExitDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    // Handle back press
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    BackHandler(enabled = true) {
        if (currentRoute == Routes.HOME) {
            showExitDialog = true
        } else {
            navController.popBackStack()
        }
    }

    // Exit confirmation dialog
    if (showExitDialog) {
        ExitConfirmationDialog(
            onConfirmExit = onExitApp,
            onDismiss = { showExitDialog = false }
        )
    }

    MaterialTheme {
        NavHost(
            navController = navController,
            startDestination = Routes.HOME
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    modifier = modifier,
                    navController = navController,
                )
            }
            composable(Routes.QUOTE_CREATOR) {
                QuoteEditorScreen(
                    modifier = modifier,
                    navController = navController
                )
            }

            composable(Routes.POPULAR_QUOTES) {
                QuoteScreen(
                    modifier = modifier,
                    navController = navController,
                    categoryName = "Popular Quotes"
                )
            }

            composable(Routes.POSTS_TIMELINE) {
                PostsTimelineScreen(
                    modifier = modifier,
                    navController = navController
                )
            }

            composable(Routes.ALL_CATEGORIES) {
                CategoriesFullScreen(
                    modifier = modifier,
                    navController = navController,
                )
            }

            composable(Routes.CATEGORY_DETAIL) { backStackEntry ->
                val categoryName =
                    backStackEntry.arguments?.getString("categoryName") ?: DEFAULT_QUOTE
                QuoteScreen(
                    modifier = modifier,
                    navController = navController,
                    categoryName = categoryName
                )
            }

            composable(Routes.ADD_QUOTE_SCREEN) { backStackEntry ->
                val categoryName = backStackEntry.arguments?.getString("queryName") ?: DEFAULT_QUOTE
                SelectQuoteScreen(
                    modifier = modifier,
                    onQuoteSelected = { quote ->
                        val formatted = "\"${quote.content}\" — ${quote.author ?: "Unknown"}"
                        val clipboard =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("quote", formatted)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Quote copied to clipboard", Toast.LENGTH_SHORT)
                            .show()
                    },
                    onDismiss = {
                        if (navController.previousBackStackEntry != null) {
                            navController.popBackStack()
                        }
                    },
                    categoryName = categoryName,
                    showCopyIcon = true,
                    onCopyClick = { quote ->
                        val formatted = "\"${quote.content}\" — ${quote.author ?: "Unknown"}"
                        val clipboard =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("quote", formatted)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Quote copied to clipboard", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
            }

            composable(Routes.QUOTE_EDIT) { backStackEntry ->
                val imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""
                val quoteContent = backStackEntry.arguments?.getString("quoteContent") ?: ""
                val quoteAuthor = backStackEntry.arguments?.getString("quoteAuthor") ?: ""
                val categoryName =
                    backStackEntry.arguments?.getString("categoryName") ?: DEFAULT_QUOTE

                QuoteEditorScreen(
                    modifier = modifier,
                    navController = navController,
                    initialImageUrl = URLDecoder.decode(imageUrl, "UTF-8"),
                    initialQuoteContent = URLDecoder.decode(quoteContent, "UTF-8"),
                    initialQuoteAuthor = URLDecoder.decode(quoteAuthor, "UTF-8"),
                    initialCategory = URLDecoder.decode(categoryName, "UTF-8")
                )
            }

            composable(Routes.BACKGROUND_GALLERY) {
                BackgroundGalleryScreen(
                    modifier = modifier,
                    navController = navController,
                )
            }
        }
    }
}