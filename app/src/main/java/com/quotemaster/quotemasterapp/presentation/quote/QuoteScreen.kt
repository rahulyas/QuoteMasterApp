package com.quotemaster.quotemasterapp.presentation.quote

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.xr.compose.testing.toDp
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.quotemaster.quotemasterapp.utils.SetStatusBarWhiteWithDarkIcons
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun QuoteScreen(
    viewModel: QuoteViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    navController: NavController,
    categoryName: String
) {
    SetStatusBarWhiteWithDarkIcons()

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFF3EFFF), Color(0xFFFFFFFF))
    )
    val context = LocalContext.current
    val quotesWithImagesAndStyles by viewModel.quotesWithImagesAndStyles.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val showMessage by viewModel.showMessage.collectAsState()
    val imageQuotePairs = quotesWithImagesAndStyles.map { it.image to it.quote }
    val textStyles = quotesWithImagesAndStyles.map { it.textStyle }
    val pagerState = rememberPagerState(pageCount = { imageQuotePairs.size })
    val currentTextStyle = textStyles.getOrNull(pagerState.currentPage) ?: defaultTextStyle
    val density = LocalDensity.current
    val statusBarHeight = WindowInsets.statusBars.getTop(density).toDp()
    LaunchedEffect(categoryName) {
        viewModel.fetchQuotesWithImagesAndStyles(categoryName)
    }
    showMessage?.let { message ->
        LaunchedEffect(message) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            ModernQuoteTopBar(
                title = categoryName,
                navController = navController,
                topPadding = statusBarHeight
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient)
                .padding(paddingValues)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF8F5CFF))
                }
            } else {
                // Fullscreen image slider with actions and quote overlay
                if (imageQuotePairs.isNotEmpty()) {
                    val coroutineScope = rememberCoroutineScope()
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        // Pager
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 0.dp),
                            pageSpacing = 0.dp,
                            key = { it }
                        ) { page ->
                            val (image, quote) = imageQuotePairs[page]
                            val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
                            val scale = 1f - 0.12f * pageOffset
                            var alpha = 1f - 0.4f * pageOffset
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                        alpha = alpha
                                        shadowElevation = if (pageOffset < 0.1f) 32f else 8f
                                    }
                                    .zIndex(if (page == pagerState.currentPage) 1f else 0f),
                                contentAlignment = Alignment.Center
                            ) {
                                // Fullscreen image
                                val painter = rememberAsyncImagePainter(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(image.largeImageURL)
                                        .crossfade(true)
                                        .diskCachePolicy(CachePolicy.ENABLED)
                                        .memoryCachePolicy(CachePolicy.ENABLED)
                                        .build()
                                )
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Image(
                                        painter = painter,
                                        contentDescription = quote.content,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    if (painter.state is coil.compose.AsyncImagePainter.State.Loading) {
                                        // Show a loading effect (spinner) over the image area
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color.Black.copy(alpha = 0.1f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(color = Color.White)
                                        }
                                    }
                                }
                                // Quote overlay at bottom
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .fillMaxWidth()
                                        .padding(bottom = 48.dp, end = 72.dp)
                                        .blur(24.dp)
                                        .background(
                                            Color.Black.copy(alpha = 0.55f),
                                            shape = RoundedCornerShape(0.dp)
                                        )
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .fillMaxWidth()
                                        .padding(bottom = 48.dp, start = 24.dp, end = 72.dp)
                                ) {
                                    val completeText = if (quote.author != null && !quote.author.contains("UNKNOWN")) {
                                        "“${quote.content}”\n — ${quote.author}"
                                    } else {
                                        "“${quote.content}”"
                                    }
                                    Text(
                                        text = completeText,
                                        style = currentTextStyle.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 28.sp
                                        ),
                                        modifier = Modifier
                                            .background(Color.Black.copy(alpha = 0.0f), shape = RoundedCornerShape(0.dp))
                                            .padding(20.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                                // Instagram Reels-style vertical action buttons
                                if (page == pagerState.currentPage) {
                                    Column(
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .padding(end = 12.dp)
                                            .zIndex(3f),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        ModernReelsActionButton(
                                            icon = Icons.Default.Share,
                                            onClick = { if (imageQuotePairs.isNotEmpty()) {
                                                val currentPair = imageQuotePairs[pagerState.currentPage]
                                                viewModel.shareCurrentQuote(
                                                    currentPair.first.largeImageURL,
                                                    currentPair.second,
                                                    context,
                                                    currentTextStyle
                                                )
                                            } },
                                            tint = Color(0xFF8F5CFF),
                                            enabled = true
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        ModernReelsActionButton(
                                            icon = Icons.Default.ContentCopy,
                                            onClick = { if (imageQuotePairs.isNotEmpty()) {
                                                val currentQuote = imageQuotePairs[pagerState.currentPage].second
                                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                val clip = ClipData.newPlainText("quote", "${currentQuote.content} - ${currentQuote.author}")
                                                clipboard.setPrimaryClip(clip)
                                                Toast.makeText(context, "Quote copied to clipboard", Toast.LENGTH_SHORT).show()
                                            } },
                                            tint = Color(0xFF8F5CFF),
                                            enabled = true
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        ModernReelsActionButton(
                                            icon = Icons.Default.Download,
                                            onClick = { if (imageQuotePairs.isNotEmpty()) {
                                                val currentPair = imageQuotePairs[pagerState.currentPage]
                                                viewModel.downloadCurrentQuote(
                                                    currentPair.first.largeImageURL,
                                                    currentPair.second,
                                                    context,
                                                    currentTextStyle
                                                )
                                            } },
                                            tint = Color(0xFF8F5CFF),
                                            enabled = true
                                        )
                                    }
                                }
                            }
                        }
                        // Bottom navigation row: left arrow, dots, right arrow
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Left Arrow
                            IconButton(
                                onClick = { if (pagerState.currentPage > 0) coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } },
                                enabled = pagerState.currentPage > 0,
                                modifier = Modifier
                                    .size(44.dp)
                                    .shadow(8.dp, CircleShape)
                                    .background(Color.White, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Previous",
                                    tint = if (pagerState.currentPage > 0) Color(0xFF8F5CFF) else Color.LightGray
                                )
                            }
                            // Dots (limited window, active dot is white)
                            val maxDots = 5
                            val totalPages = imageQuotePairs.size
                            val currentPage = pagerState.currentPage
                            val startDot = when {
                                totalPages <= maxDots -> 0
                                currentPage <= maxDots / 2 -> 0
                                currentPage >= totalPages - maxDots / 2 -> totalPages - maxDots
                                else -> currentPage - maxDots / 2
                            }
                            val endDot = (startDot + maxDots).coerceAtMost(totalPages)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                for (i in startDot until endDot) {
                                    val isSelected = currentPage == i
                                    Box(
                                        modifier = Modifier
                                            .size(if (isSelected) 14.dp else 8.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isSelected) Color.White else Color.LightGray.copy(alpha = 0.5f)
                                            )
                                    )
                                    if (i < endDot - 1) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                }
                            }
                            // Right Arrow
                            IconButton(
                                onClick = { if (pagerState.currentPage < imageQuotePairs.lastIndex) coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
                                enabled = pagerState.currentPage < imageQuotePairs.lastIndex,
                                modifier = Modifier
                                    .size(44.dp)
                                    .shadow(8.dp, CircleShape)
                                    .background(Color.White, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Next",
                                    tint = if (pagerState.currentPage < imageQuotePairs.lastIndex) Color(0xFF8F5CFF) else Color.LightGray
                                )
                            }
                        }
                    }
                }
                if (isProcessing) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = Color(0xFF8F5CFF))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Processing image...",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ModernQuoteTopBar(title: String, navController: NavController, topPadding: Dp) {
    Surface(
        shadowElevation = 8.dp,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = topPadding)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF8F5CFF), Color(0xFF7B2FF2))
                    )
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(56.dp)
            ) {
                IconButton(onClick = { if (navController.previousBackStackEntry != null) navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ModernReelsActionButton(icon: ImageVector, onClick: () -> Unit, tint: Color, enabled: Boolean = true) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .size(48.dp)
            .shadow(8.dp, CircleShape)
            .background(Color.White, CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (enabled) tint else tint.copy(alpha = 0.4f),
            modifier = Modifier.size(28.dp)
        )
    }
}

val defaultTextStyle = TextStyle(
    fontSize = 24.sp,
    fontWeight = FontWeight.Normal,
    fontFamily = FontFamily.SansSerif
)

