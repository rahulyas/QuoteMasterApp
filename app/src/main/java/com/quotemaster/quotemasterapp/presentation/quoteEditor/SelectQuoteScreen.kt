package com.quotemaster.quotemasterapp.presentation.quoteEditor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.quotemaster.quotemasterapp.R
import com.quotemaster.quotemasterapp.data.local.entity.QuoteEntity
import com.quotemaster.quotemasterapp.presentation.component.QuoteCategoryFilterDialog
import com.quotemaster.quotemasterapp.utils.QuoteCategory
import com.quotemaster.quotemasterapp.utils.SetStatusBarWhiteWithDarkIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectQuoteScreen(
    modifier: Modifier = Modifier,
    onQuoteSelected: (QuoteEntity) -> Unit,
    onDismiss: () -> Unit,
    categoryName: String,
    showCopyIcon: Boolean = false,
    onCopyClick: (QuoteEntity) -> Unit = {},
    viewModel: QuoteEditorViewModel = hiltViewModel()
) {
    val searchResults by viewModel.searchResults.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(QuoteCategory.INSPIRATIONAL) }

    SetStatusBarWhiteWithDarkIcons()

    // Load quotes when screen opens
    LaunchedEffect(Unit) {
        viewModel.loadQuotesForSelection(categoryName)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Select Quote",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2C3E50)
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.quotesBackground))
                .padding(paddingValues)
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.searchQuotes(it)
                },
                label = { Text("Search quotes...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            searchQuery = ""
                            viewModel.searchQuotes("")
                        }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear"
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor =  Color.Black,
                    unfocusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedPlaceholderColor = Color.Black,
                    unfocusedPlaceholderColor = Color.Black,
                    focusedLeadingIconColor = Color.Black,
                    unfocusedLeadingIconColor = Color.Black,
                    disabledBorderColor = Color.Black,
                    errorBorderColor = Color.Red,
                ),
                singleLine = true
            )

            // Loading indicator
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF2C3E50)
                    )
                }
            }

            // Quotes list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(searchResults) { quote ->
                    QuoteItem(
                        quote = quote,
                        onClick = { onQuoteSelected(quote) },
                        showCopyIcon = showCopyIcon,
                        onCopyClick = { onCopyClick(quote) }
                    )
                }

                // Show message if no quotes found
                if (searchResults.isEmpty() && !uiState.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (searchQuery.isEmpty()) "No quotes available" else "No quotes found for \"$searchQuery\"",
                                color = Color.Gray,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        if (showDialog) {
            QuoteCategoryFilterDialog(
                currentSelection = selectedCategory,
                onCategorySelected = {
                    selectedCategory = it
                    viewModel.loadQuotesForSelection(it.displayName)
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}