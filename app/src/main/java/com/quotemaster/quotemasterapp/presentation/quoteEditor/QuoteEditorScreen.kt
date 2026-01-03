package com.quotemaster.quotemasterapp.presentation.quoteEditor

import android.view.View
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SpaceBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.quotemaster.quotemasterapp.R
import com.quotemaster.quotemasterapp.data.local.entity.ImageEntity
import com.quotemaster.quotemasterapp.domain.model.GalleryImage
import com.quotemaster.quotemasterapp.domain.model.PixabayImage
import com.quotemaster.quotemasterapp.presentation.camera.CameraScreen
import com.quotemaster.quotemasterapp.presentation.camera.CameraTabContent
import com.quotemaster.quotemasterapp.presentation.component.AlignPickerContent
import com.quotemaster.quotemasterapp.presentation.component.EmojiPickerDialog
import com.quotemaster.quotemasterapp.presentation.component.FontPickerContent
import com.quotemaster.quotemasterapp.presentation.component.FrameColorPickerContent
import com.quotemaster.quotemasterapp.presentation.component.FramePickerContent
import com.quotemaster.quotemasterapp.presentation.component.GradientPickerContent
import com.quotemaster.quotemasterapp.presentation.component.InteractiveEmoji
import com.quotemaster.quotemasterapp.presentation.component.ShadowPickerContent
import com.quotemaster.quotemasterapp.presentation.component.SizePickerContent
import com.quotemaster.quotemasterapp.presentation.component.SpacingPickerContent
import com.quotemaster.quotemasterapp.presentation.component.TextColorPickerContent
import com.quotemaster.quotemasterapp.presentation.component.ViewCapture
import com.quotemaster.quotemasterapp.presentation.component.createFrameModifier
import com.quotemaster.quotemasterapp.presentation.imageEffect.applyColorBackgroundEffect
import com.quotemaster.quotemasterapp.presentation.imageEffect.applyImageEffectToPainter
import com.quotemaster.quotemasterapp.presentation.imageEffect.applyImageModifier
import com.quotemaster.quotemasterapp.presentation.navigation.Routes
import com.quotemaster.quotemasterapp.presentation.permissions.HandlePermissions
import com.quotemaster.quotemasterapp.presentation.permissions.PermissionDeniedMessage
import com.quotemaster.quotemasterapp.presentation.state.QuoteEditorUiState
import com.quotemaster.quotemasterapp.utils.BackgroundTab
import com.quotemaster.quotemasterapp.utils.BackgroundType
import com.quotemaster.quotemasterapp.utils.Constants.DEFAULT_QUOTE
import com.quotemaster.quotemasterapp.utils.Constants.LIST_MAX_SIZE
import com.quotemaster.quotemasterapp.utils.ImageCaptureHelper
import com.quotemaster.quotemasterapp.utils.ImageEffect
import com.quotemaster.quotemasterapp.utils.SetStatusBarWhiteWithDarkIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteEditorScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: QuoteEditorViewModel = hiltViewModel(),
    initialImageUrl: String = "",
    initialQuoteContent: String = "",
    initialQuoteAuthor: String = "",
    initialCategory: String = DEFAULT_QUOTE
) {
    val uiState by viewModel.uiState.collectAsState()
    val backgroundImages by viewModel.backgroundImages.collectAsState()
    val searchedImages by viewModel.searchedImages.collectAsState()
    val galleryImages by viewModel.galleryImages.collectAsState()
    val permissionState = remember { mutableStateOf(false) }
    val permissionDenied = remember { mutableStateOf(false) }
    val showCamera by viewModel.showCamera.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var captureTarget by remember { mutableStateOf<View?>(null) }
    val imageCaptureHelper = remember { ImageCaptureHelper(context, scope, viewModel) }

    HandlePermissions(
        onGranted = {
            permissionState.value = true
            permissionDenied.value = false
            viewModel.loadGalleryImages()
        },
        onDenied = {
            permissionDenied.value = true
        }
    )

    if (permissionDenied.value && !permissionState.value) {
        PermissionDeniedMessage()
        return
    }


    // Initialize the editor when the screen is first loaded
    LaunchedEffect(Unit) {
        viewModel.initializeEditor(initialImageUrl, initialQuoteContent, initialQuoteAuthor, DEFAULT_QUOTE)
    }

    // Show error messages
    uiState.error?.let { error ->
        LaunchedEffect(error) {
            // Show snackbar or toast with error message
            viewModel.clearError()
        }
    }

    uiState.toastMessage?.let { message ->
        LaunchedEffect(message) {
            viewModel.clearToastMessage()
        }
    }

    SetStatusBarWhiteWithDarkIcons()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create Quote",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    } }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2C3E50)
                )
            )
        },
        bottomBar = {
            if (!uiState.showTextActions) {
                BottomActionBar(
                    modifier = modifier,
                    onQuoteClick = { viewModel.onQuoteButtonClick() },
                    onTextClick = { viewModel.onTextButtonClick() },
                    onBackgroundClick = { viewModel.onBackgroundClick() },
                    onEmojiClick = { viewModel.onEmojiClick() },
                    onFrameClick = { viewModel.onFrameClick() },
                    onSaveClick = {
                        imageCaptureHelper.captureAndSave(captureTarget)
                    }
                )
            } else {
                TextActionBar(
                    modifier = modifier,
                    onColorClick = { viewModel.onColorPickerClick() },
                    onFontClick = { viewModel.onFontPickerClick() },
                    onSizeClick = { viewModel.onSizePickerClick() },
                    onShadowClick = { viewModel.onShadowPickerClick() },
                    onAlignClick = { viewModel.onAlignPickerClick() },
                    onSpacingClick = { viewModel.onSpacingPickerClick() },
                    onCancelClick = { viewModel.closeTextActions() }
                )
            }
            if (uiState.showBackgroundOptions) {
                BackgroundActionBar(
                    modifier = modifier,
                    uiState = uiState,
                    backgroundImages = backgroundImages,
                    searchedImages = searchedImages,
                    onTabClick = { tab ->
                        when (tab) {
                            BackgroundTab.IMAGES -> viewModel.onImagesTabClick()
                            BackgroundTab.COLOR -> viewModel.onColorTabClick()
                            BackgroundTab.GALLERY -> viewModel.onGalleryTabClick()
                            BackgroundTab.OVERLAY -> viewModel.onOverlayTabClick()
                            BackgroundTab.EFFECT -> viewModel.onEffectTabClick()
                            BackgroundTab.CAMERA -> viewModel.onCameraTabClick()
                            BackgroundTab.GRADIENT -> viewModel.onGradientTabClick() // Add others if needed
                        }
                    },
                    onImageSelected = {
                        when (it) {
                            is ImageEntity -> viewModel.selectBackgroundImageFromEntity(it)
                            is PixabayImage -> viewModel.selectBackgroundImageFromPixabay(it)
                        }
                    },
                    onColorSelected = { viewModel.selectBackgroundColor(it) },
                    onOverlayBrightnessChanged = { viewModel.updateOverlayBrightness(it) },
                    onEffectSelected = { viewModel.selectEffect(it) },
                    onMoreImagesClick = { navController.navigate(Routes.BACKGROUND_GALLERY) },
                    onCancelClick = { viewModel.closeBackgroundOptions() },
                    galleryImages = galleryImages,
                    onGalleryImageSelected = { viewModel.selectBackgroundFromGallery(it.uri.toString()) },
                    onCameraClick = { viewModel.onShowCamera() },
                    onGradientSelected = { viewModel.selectBackgroundGradient(it) }
                )
            }
            // Text Style Content
            if (uiState.showTextActions) {
                when {
                    uiState.showColorPicker -> {
                        TextColorPickerContent(
                            currentColor = uiState.textStyle.color,
                            onColorSelected = { viewModel.updateTextColor(it) },
                            onCancelClick = { viewModel.closeColorPicker() },
                            modifier = modifier
                        )
                    }
                    uiState.showFontPicker -> {
                        FontPickerContent(
                            currentWeight = uiState.textStyle.fontWeight,
                            onFontSelected = { viewModel.updateFontWeight(it) },
                            onCancelClick = { viewModel.closeFontPicker() },
                            modifier = modifier
                        )
                    }
                    uiState.showSizePicker -> {
                        SizePickerContent(
                            currentSize = uiState.textStyle.fontSize,
                            onSizeSelected = { viewModel.updateFontSize(it) },
                            onCancelClick = { viewModel.closeSizePicker() },
                            modifier = modifier
                        )
                    }
                    uiState.showAlignPicker -> {
                        AlignPickerContent(
                            currentAlign = uiState.textStyle.textAlign,
                            onAlignSelected = { viewModel.updateTextAlign(it) },
                            onCancelClick = { viewModel.closeAlignPicker() },
                            modifier = modifier
                        )
                    }
                    uiState.showShadowPicker -> {
                        ShadowPickerContent(
                            currentRadius = uiState.textStyle.shadowRadius,
                            currentColor = uiState.textStyle.shadowColor,
                            onShadowChanged = { radius, color -> viewModel.updateShadow(radius, color) },
                            onCancelClick = { viewModel.closeShadowPicker() },
                            modifier = modifier
                        )
                    }
                    uiState.showSpacingPicker -> {
                        SpacingPickerContent(
                            currentLetterSpacing = uiState.textStyle.letterSpacing,
                            currentLineHeight = uiState.textStyle.lineHeight,
                            onLetterSpacingChanged = { viewModel.updateLetterSpacing(it) },
                            onLineHeightChanged = { viewModel.updateLineHeight(it) },
                            onCancelClick = { viewModel.closeSpacingPicker() },
                            modifier = modifier
                        )
                    }
                }
            }

            // Frame Color Picker
            if (uiState.showFrameColorPicker) {
                FrameColorPickerContent(
                    onColorSelected = { viewModel.selectFrameColor(it) },
                    modifier = modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2C3E50))
                        .padding(8.dp),
                    onCancelClick = { viewModel.closeFrameColorPicker() }
                )
            }

            // Frame Options
            if (uiState.showFrameOptions) {
                FramePickerContent(
                    onFrameSelected = { viewModel.selectFrame(it) },
                    onColorClick = { viewModel.onFrameColorClick() },
                    selectedFrame = uiState.selectedFrame,
                    currentFrameColor = uiState.customFrameColor,
                    modifier = modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2C3E50))
                        .padding(8.dp),
                    onCancelClick = { viewModel.closeFrameOptions() }
                )
            }

        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(colorResource(R.color.quotesBackground))
                .padding(paddingValues)
        ) {
            // Background content
            ViewCapture(
                modifier = Modifier.fillMaxSize(),
                onViewReady = { captureTarget = it }
            ) {
                BackgroundContent(
                    viewModel = viewModel,
                    uiState = uiState,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Show loading indicator
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Black
                )
            }

            // Quote text overlay
            if (uiState.quoteText.isNotEmpty() && !uiState.showQuoteInput) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(32.dp)
                        .background(
                            Color.Black.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp)
                        .clickable { viewModel.onTapToAddQuoteClick() }
                ) {
                    Text(
                        text = uiState.quoteText,
                        color = uiState.textStyle.color,
                        fontSize = uiState.textStyle.fontSize,
                        fontWeight = uiState.textStyle.fontWeight,
                        textAlign = uiState.textStyle.textAlign,
                        letterSpacing = uiState.textStyle.letterSpacing,
                        lineHeight = uiState.textStyle.lineHeight,
                        style = TextStyle(
                            shadow = if (uiState.textStyle.shadowRadius > 0) {
                                Shadow(
                                    color = uiState.textStyle.shadowColor,
                                    blurRadius = uiState.textStyle.shadowRadius
                                )
                            } else null
                        )
                    )
                }
            }

            // Quote input prompt
            if (uiState.showQuoteInput || uiState.quoteText.isEmpty()) {
                QuoteInputSection(
                    quoteText = uiState.quoteText,
                    onQuoteTextChange = { viewModel.updateQuoteText(it) },
                    onTapToAddQuote = { viewModel.onTapToAddQuoteClick() },
                    modifier = Modifier.align(Alignment.Center)
                )
            }


            if (uiState.showEmojiPicker) {
                EmojiPickerDialog(
                    onEmojiSelected = { emoji -> viewModel.addEmoji(emoji) },
                    onDismiss = { viewModel.closeEmojiPicker() }
                )
            }

        }
    }

    // Show Add Quote Screen
    if (uiState.showQuoteInput) {
        AddQuoteScreen(
            onQuoteSelected = { content, author ->
                viewModel.saveQuote(content, author)
            },
            onDismiss = { viewModel.closeQuoteInput() },
            modifier = modifier,
            categoryName = initialCategory
        )
    }

    // Add camera overlay
    if (showCamera) {
        CameraScreen(
            modifier = modifier,
            onImageCaptured = { imageUri ->
                viewModel.onCameraImageCaptured(imageUri)
            },
            onCameraClosed = {
                viewModel.onCameraClosed()
            }
        )
    }
}

@Composable
fun BackgroundContent(
    viewModel: QuoteEditorViewModel,
    uiState: QuoteEditorUiState,
    modifier: Modifier = Modifier,
) {
    val containerSize = remember { mutableStateOf(IntSize.Zero) }
    val frameModifier = createFrameModifier(uiState.selectedFrame, uiState.customFrameColor)
    Box(modifier = modifier
        .then(frameModifier)
        .onSizeChanged { size ->
            if (size.width > 100 && size.height > 100) {
                containerSize.value = size
            }        }
        .clipToBounds()
    ) {
        when (uiState.backgroundType) {
            BackgroundType.IMAGE -> {
                if (uiState.backgroundImageUrl.isNotEmpty()) {
                    val painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(uiState.backgroundImageUrl)
                            .crossfade(true)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .build()
                    )

                    Box(modifier = modifier) {
                        Image(
                            painter = painter,
                            contentDescription = "Background",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .then(applyImageModifier(uiState.selectedEffect)),
                            colorFilter = applyImageEffectToPainter(uiState.selectedEffect)
                        )

                        // Apply overlay if brightness is adjusted
                        if (uiState.overlayBrightness != 0f) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        if (uiState.overlayBrightness > 0) {
                                            Color.White.copy(alpha = uiState.overlayBrightness)
                                        } else {
                                            Color.Black.copy(alpha = -uiState.overlayBrightness)
                                        }
                                    )
                            )
                        }
                    }
                } else {
                    // Show placeholder when no image is selected
                    Box(
                        modifier = modifier.background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No background image selected",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }
            BackgroundType.COLOR -> {
                uiState.backgroundColor?.let { color ->
                    val newColor = applyColorBackgroundEffect(color, uiState.selectedEffect)

                    Box(
                        modifier = modifier
                            .background(newColor)
                            .then(applyImageModifier(uiState.selectedEffect)),
                    )
                }
            }
            BackgroundType.GRADIENT -> {
                uiState.backgroundGradient?.let { gradient ->
                    Box(
                        modifier = modifier
                            .background(gradient)
                            .then(applyImageModifier(uiState.selectedEffect)),
                    )
                } ?: run {
                    // Fallback if gradient is null
                    Box(
                        modifier = modifier.background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No gradient selected",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
        if (containerSize.value.width > 0 && containerSize.value.height > 0) {
            // Placed emojis - Make sure they're rendered on top
            uiState.placedEmojis.forEach { emoji ->
                InteractiveEmoji(
                    modifier = Modifier.fillMaxSize(),
                    emoji = emoji,
                    isSelected = uiState.selectedEmojiId == emoji.id,
                    onPositionChange = { x, y ->
                        println("Position change for emoji ${emoji.id}: x=$x, y=$y") // Debug log
                        viewModel.updateEmojiPosition(emoji.id, x, y)
                    },
                    onScaleChange = { scale ->
                        viewModel.updateEmojiScale(emoji.id, scale)
                    },
                    onRotationChange = { rotation ->
                        viewModel.updateEmojiRotation(emoji.id, rotation)
                    },
                    onSelect = {
                        viewModel.selectEmoji(emoji.id)
                    },
                    onDelete = {
                        viewModel.deleteEmoji(emoji.id)
                    },
                    containerSize = containerSize.value
                )
            }
        }

        // Clear selection when tapping background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures {
                        if (uiState.selectedEmojiId != null) {
                            viewModel.selectEmoji("")
                        }
                    }
                }
        )
    }

}

@Composable
fun BackgroundActionBar(
    uiState: QuoteEditorUiState,
    backgroundImages: List<ImageEntity>,
    searchedImages: List<PixabayImage>,
    onTabClick: (BackgroundTab) -> Unit,
    onImageSelected: (Any) -> Unit,
    onColorSelected: (Color) -> Unit,
    onOverlayBrightnessChanged: (Float) -> Unit,
    onEffectSelected: (ImageEffect) -> Unit,
    onMoreImagesClick: () -> Unit,
    onCancelClick: () -> Unit,
    galleryImages: List<GalleryImage>,
    onGalleryImageSelected: (GalleryImage) -> Unit,
    onCameraClick: () -> Unit,
    onGradientSelected: (Brush) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF2C3E50))
            .padding(8.dp)
    ) {
        // Header with Cancel button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Background Options",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            IconButton(onClick = onCancelClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel",
                    tint = Color.White
                )
            }
        }

        // Tabs
        ScrollableTabRow(
            selectedTabIndex = uiState.selectedBackgroundTab.ordinal,
            edgePadding = 0.dp,
            containerColor = Color(0xFF2C3E50),
            contentColor = Color.White
        ) {
            BackgroundTab.entries.forEach { tab ->
                Tab(
                    selected = uiState.selectedBackgroundTab == tab,
                    onClick = { onTabClick(tab) },
                    text = {
                        Text(
                            text = tab.name.lowercase().replaceFirstChar { it.uppercase() },
                            color = Color.White
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Tab content
        when (uiState.selectedBackgroundTab) {
            BackgroundTab.IMAGES -> {
                ImagePickerContent(
                    backgroundImages = backgroundImages,
                    searchedImages = searchedImages,
                    onImageSelected = onImageSelected,
                    onMoreImagesClick = onMoreImagesClick
                )
            }

            BackgroundTab.GALLERY -> {
                GalleryPickerContent(
                    galleryImages = galleryImages,
                    onImageSelected = onGalleryImageSelected
                )
            }

            BackgroundTab.COLOR -> {
                ImageColorPickerContent(onColorSelected = onColorSelected)
            }

            BackgroundTab.OVERLAY -> {
                OverlayOptionsContent(
                    currentBrightness = uiState.overlayBrightness,
                    onBrightnessChanged = onOverlayBrightnessChanged
                )
            }

            BackgroundTab.EFFECT -> {
                EffectOptionsContent(
                    selectedEffect = uiState.selectedEffect,
                    onEffectSelected = onEffectSelected
                )
            }

            BackgroundTab.CAMERA -> {
                CameraTabContent(
                    onCameraClick
                )
            }

            BackgroundTab.GRADIENT ->{
                GradientPickerContent(
                    onGradientSelected = { brush ->
                        onGradientSelected(brush)
                    }
                )
            }
        }
    }
}

@Composable
fun ImagePickerContent(
    backgroundImages: List<ImageEntity>,
    searchedImages: List<PixabayImage>,
    onImageSelected: (Any) -> Unit,
    onMoreImagesClick: () -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        // Horizontal image preview row (max 15)
        val previewList = backgroundImages.take(LIST_MAX_SIZE)
        val previewList1 = searchedImages.take(LIST_MAX_SIZE)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(previewList) { image ->
                AsyncImage(
                    model = image.largeImageURL,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onImageSelected(image) }
                )
            }

            items(previewList1) { image ->
                AsyncImage(
                    model = image.largeImageURL,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onImageSelected(image) }
                )
            }

            if (backgroundImages.size > LIST_MAX_SIZE) {
                item {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.1f))
                            .clickable { onMoreImagesClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "More",
                            color = Color.White,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun ImageColorPickerContent(
    onColorSelected: (Color) -> Unit
) {
    val colors = listOf(
        Color.Red, Color.Green, Color.Blue, Color.Yellow,
        Color.Magenta, Color.Cyan, Color.Black, Color.White,
        Color.Gray, Color(0xFF8B4513), Color(0xFF00CED1), Color(0xFFFF1493)
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    )  {
        items(colors) { color ->
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .aspectRatio(1f)
                    .background(color, RoundedCornerShape(8.dp))
                    .clickable { onColorSelected(color) }
            )
        }
    }
}

@Composable
fun OverlayOptionsContent(
    currentBrightness: Float,
    onBrightnessChanged: (Float) -> Unit
) {
    Column {
        Text(
            text = "Brightness",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Slider(
            value = currentBrightness,
            onValueChange = onBrightnessChanged,
            valueRange = -1f..1f,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.White.copy(alpha = 0.3f)
            )
        )

        Text(
            text = "Brightness: ${(currentBrightness * 100).toInt()}%",
            color = Color.White,
            fontSize = 14.sp
        )
    }
}

@Composable
fun EffectOptionsContent(
    selectedEffect: ImageEffect?,
    onEffectSelected: (ImageEffect) -> Unit
) {
    val effects = ImageEffect.entries.toTypedArray()

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(effects) { effect ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEffectSelected(effect) },
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedEffect == effect) {
                        Color.White.copy(alpha = 0.3f)
                    } else {
                        Color.Transparent
                    }
                ),
                border = BorderStroke(
                    1.dp,
                    if (selectedEffect == effect) Color.White else Color.White.copy(alpha = 0.3f)
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = effect.name,
                        color = Color.White,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@Composable
fun QuoteInputSection(
    quoteText: String,
    onQuoteTextChange: (String) -> Unit,
    onTapToAddQuote: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(32.dp)
            .background(
                Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
            .clickable { onTapToAddQuote() }
    ) {
        if (quoteText.isEmpty()) {
            Text(
                text = "Tap to add quote",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        } else {
            OutlinedTextField(
                value = quoteText,
                onValueChange = onQuoteTextChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                    cursorColor = Color.White
                )
            )
        }
    }
}

@Composable
fun BottomActionBar(
    modifier: Modifier = Modifier,
    onQuoteClick: () -> Unit,
    onTextClick: () -> Unit,
    onBackgroundClick: () -> Unit,
    onEmojiClick: () -> Unit,
    onFrameClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Black,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            EditorActionButton(
                icon = Icons.Default.Edit,
                label = "Quote",
                onClick = onQuoteClick
            )
            EditorActionButton(
                icon = Icons.Default.Edit,
                label = "Text",
                onClick = onTextClick
            )
            EditorActionButton(
                icon = Icons.Default.Image,
                label = "BG",
                onClick = onBackgroundClick
            )
            /*EditorActionButton(
                icon = Icons.Default.EmojiEmotions,
                label = "Emoji",
                onClick = onEmojiClick
            )*/
            EditorActionButton(
                icon = Icons.Default.CropFree,
                label = "Frame",
                onClick = onFrameClick
            )
            EditorActionButton(
                icon = Icons.Default.Save,
                label = "Save",
                onClick = onSaveClick
            )
        }
    }
}

@Composable
fun TextActionBar(
    modifier: Modifier = Modifier,
    onColorClick: () -> Unit,
    onFontClick: () -> Unit,
    onSizeClick: () -> Unit,
    onShadowClick: () -> Unit,
    onAlignClick: () -> Unit,
    onSpacingClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFF2C3E50),
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            EditorActionButton(
                icon = Icons.Default.Palette,
                label = "Color",
                onClick = onColorClick
            )
            EditorActionButton(
                icon = Icons.Default.FontDownload,
                label = "Font",
                onClick = onFontClick
            )
            EditorActionButton(
                icon = Icons.Default.FormatSize,
                label = "Size",
                onClick = onSizeClick
            )
            EditorActionButton(
                icon = Icons.Default.SpaceBar,
                label = "Shadow",
                onClick = onShadowClick
            )
            EditorActionButton(
                icon = Icons.Default.FormatAlignCenter,
                label = "Align",
                onClick = onAlignClick
            )
            EditorActionButton(
                icon = Icons.Default.SpaceBar,
                label = "Spacing",
                onClick = onSpacingClick
            )
            EditorActionButton(
                icon = Icons.Default.Close,
                label = "Cancel",
                onClick = onCancelClick
            )
        }
    }
}

@Composable
fun EditorActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

@Composable
fun GalleryPickerContent(
    galleryImages: List<GalleryImage>,
    onImageSelected: (GalleryImage) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(16.dp))

        // Horizontal gallery image preview row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(galleryImages) { image ->
                AsyncImage(
                    model = image.uri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onImageSelected(image) }
                )
            }
        }
    }
}

