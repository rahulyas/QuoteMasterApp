package com.quotemaster.quotemasterapp.presentation.main

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.xr.compose.testing.toDp
import com.quotemaster.quotemasterapp.R
import com.quotemaster.quotemasterapp.presentation.component.NoInternetOverlay
import com.quotemaster.quotemasterapp.presentation.navigation.QuoteCreatorViewModel
import com.quotemaster.quotemasterapp.presentation.navigation.Routes
import com.quotemaster.quotemasterapp.presentation.quote.ModernQuoteTopBar
import com.quotemaster.quotemasterapp.utils.QuoteCategory
import com.quotemaster.quotemasterapp.utils.SetStatusBarWhiteWithDarkIcons
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: QuoteCreatorViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val searchText = viewModel.searchText.collectAsState()
    val selectedQuoteType = viewModel.selectedQuoteType.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.observeNetworkStatusUseCase().distinctUntilChanged().collect { isConnected ->
                viewModel.updateIsConnected(isConnected)
            }
        }
    }
    if (!isConnected) {
        NoInternetOverlay()
        return
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFe0c3fc), Color(0xFF8ec5fc))
                )
            )
            .padding(horizontal = 0.dp, vertical = 0.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        ModernHeader()
        Spacer(modifier = Modifier.height(12.dp))
        ModernFeatureCardsRow(navController)
        Spacer(modifier = Modifier.height(18.dp))
        ModernSearchSection(
            searchText = searchText.value.trim(),
            onSearchTextChange = viewModel::updateSearchText,
            selectedQuoteType = selectedQuoteType.value.trim(),
            onQuoteTypeChange = viewModel::updateSelectedQuoteType,
            onSearchClick = {
                if (searchText.value.isEmpty()) {
                    Toast.makeText(context, "Please enter a keyword", Toast.LENGTH_SHORT).show()
                    return@ModernSearchSection
                }
                val capitalizedQuery = searchText.value.trim().replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase() else it.toString()
                }
                val route = when (selectedQuoteType.value) {
                    "Picture" -> Routes.categoryDetail(capitalizedQuery)
                    else -> Routes.searchQuotes(capitalizedQuery)
                }
                navController.navigate(route)
            },
            isLoading = isLoading
        )
        Spacer(modifier = Modifier.height(24.dp))
        ModernPopularCategoriesSection(
            categories = viewModel.getFeaturedCategories(),
            onViewAllClick = { navController.navigate(Routes.ALL_CATEGORIES) },
            onCategoryClick = { category ->
                navController.navigate(Routes.categoryDetail(category.displayName))
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ModernHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF8F5CFF), Color(0xFF7B2FF2)) // Updated to match logo's purple gradient
                ),
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.applogo),
                contentDescription = "App Logo",
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = "QuoteMaster",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Inspire your day with beautiful quotes",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun ModernFeatureCardsRow(navController: NavController) {
    val features = listOf(
        Triple(
            "Popular Quotes",
            Icons.Default.LocalFireDepartment,
            listOf(Color(0xFF8F5CFF), Color(0xFF7B2FF2)) // Use logo purple gradient
        )
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        features.forEach { (title, icon, gradientColors) ->
            ModernFeatureCard(
                title = title,
                icon = icon,
                gradient = Brush.linearGradient(gradientColors),
                onClick = {
                    when (title) {
                        "Popular Quotes" -> navController.navigate(Routes.POPULAR_QUOTES)
                        // Add more navigation as needed
                    }
                }
            )
        }
    }
}

@Composable
fun ModernFeatureCard(
    title: String,
    icon: ImageVector,
    gradient: Brush,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(100.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun ModernSearchSection(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    selectedQuoteType: String,
    onQuoteTypeChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Search for quotes, authors, or categories",
                color = Color(0xFF6B6B6B),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ModernQuoteTypeOption(
                    text = "Text",
                    isSelected = selectedQuoteType == "Text",
                    onClick = { onQuoteTypeChange("Text") }
                )
                ModernQuoteTypeOption(
                    text = "Picture",
                    isSelected = selectedQuoteType == "Picture",
                    onClick = { onQuoteTypeChange("Picture") }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFFF3F3F3)),
                contentAlignment = Alignment.CenterStart
            ) {
                TextField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    placeholder = {
                        Text(
                            "Try 'Motivation', 'Love', 'Einstein'...",
                            color = Color.Gray
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onSearchClick() }),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 64.dp)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF8F5CFF)) // Use logo purple for search button
                        .clickable { onSearchClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ModernQuoteTypeOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        if (isSelected) Color(0xFF8F5CFF) else Color(0xFFE0E0E0), // Use logo purple for selected
        label = "typeBg"
    )
    val textColor by animateColorAsState(
        if (isSelected) Color.White else Color(0xFF333333),
        label = "typeText"
    )
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ModernPopularCategoriesSection(
    categories: List<QuoteCategory>,
    onViewAllClick: () -> Unit,
    onCategoryClick: (QuoteCategory) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Popular Categories",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.black)
            )
            OutlinedButton(
                onClick = onViewAllClick,
                modifier = Modifier.height(40.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF8F5CFF) // Use logo purple for accent
                ),
                border = null
            ) {
                Text(
                    text = "View All",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "View All",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            categories.take(8).forEach { category ->
                ModernCategoryChip(
                    category = category,
                    onClick = { onCategoryClick(category) }
                )
            }
        }
    }
}

@Composable
fun ModernCategoryChip(
    category: QuoteCategory,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .width(110.dp)
            .height(120.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF8F5CFF).copy(alpha = 0.7f), // Use logo purple
                            Color(0xFF7B2FF2).copy(alpha = 0.7f)
                        )
                    )
                )
        ) {
            Image(
                painter = painterResource(id = category.imageRes),
                contentDescription = category.imageDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.25f))
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = category.displayName,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesFullScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    SetStatusBarWhiteWithDarkIcons()
    val density = LocalDensity.current
    val statusBarHeight = WindowInsets.statusBars.getTop(density).toDp()
    val allCategories = QuoteCategory.entries
    var searchQuery by remember { mutableStateOf("") }
    val gridState = rememberLazyGridState()
    val horizontalPadding = with(LocalDensity.current) {
        if (LocalConfiguration.current.screenWidthDp.dp > 600.dp) 24.dp else 16.dp
    }
    // Detect whether the grid is scrolling
    val isScrolling by remember {
        derivedStateOf {
            gridState.isScrollInProgress
        }
    }

    Scaffold(
        topBar = {
            ModernQuoteTopBar(
                title = "All Categories",
                navController = navController,
                topPadding = statusBarHeight + 8.dp
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.quotesBackground))
                .padding(paddingValues)
        ) {
            AnimatedVisibility(visible = isScrolling) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Black,
                    trackColor = Color.Transparent
                )
            }
            Spacer(modifier = Modifier.height(8.dp))



            // 🔍 Search bar with horizontal padding
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = horizontalPadding), // Add horizontal padding here
                placeholder = { Text("Search categories...", color = Color.Black) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                textStyle = TextStyle(color = Color.Black),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
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
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filtered categories grid
            val filteredCategories = allCategories.filter {
                it.displayName.contains(searchQuery, ignoreCase = true)
            }

            // Categories grid (3 columns) with horizontal padding
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = horizontalPadding), // Add horizontal padding here
                state = gridState
            ) {
                items(filteredCategories) { category ->
                    CategoryCard(
                        category = category,
                        modifier = Modifier.aspectRatio(1f),
                        onClick = {
                            navController.navigate(Routes.categoryDetail(category.displayName))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: QuoteCategory,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clip(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            Image(
                painter = painterResource(id = category.imageRes),
                contentDescription = category.imageDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Optional: semi-transparent overlay for better text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)) // or remove if not needed
            )

            // Foreground Text
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.displayName,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
            }
        }
    }
}

