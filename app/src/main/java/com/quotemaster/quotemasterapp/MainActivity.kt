package com.quotemaster.quotemasterapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import com.quotemaster.quotemasterapp.presentation.navigation.QuoteCreatorApp
import com.quotemaster.quotemasterapp.ui.theme.QuoteMasterAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuoteMasterAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    QuoteCreatorApp(modifier = Modifier.padding(
                        bottom = innerPadding.calculateBottomPadding(), // Add 16dp extra
                        top = innerPadding.calculateTopPadding(),
                        start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                        end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                        ),
                        onExitApp = { finish() },
                    )
                }
            }
        }
    }
}
