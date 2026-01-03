package com.quotemaster.quotemasterapp.presentation.quoteEditor

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quotemaster.quotemasterapp.data.local.entity.ImageEntity
import com.quotemaster.quotemasterapp.data.local.entity.QuoteEntity
import com.quotemaster.quotemasterapp.domain.model.EmojiItem
import com.quotemaster.quotemasterapp.domain.model.GalleryImage
import com.quotemaster.quotemasterapp.domain.model.PixabayImage
import com.quotemaster.quotemasterapp.domain.usecase.GetAllImagesUseCase
import com.quotemaster.quotemasterapp.domain.usecase.GetAllQuotesUseCase
import com.quotemaster.quotemasterapp.domain.usecase.GetCachedQuotesWithImagesAndStylesUseCase
import com.quotemaster.quotemasterapp.domain.usecase.GetGalleryImagesUseCase
import com.quotemaster.quotemasterapp.domain.usecase.GetQuotesWithImagesUseCase
import com.quotemaster.quotemasterapp.domain.usecase.RefreshQuotesDataUseCase
import com.quotemaster.quotemasterapp.domain.usecase.SaveImageToGalleryUseCase
import com.quotemaster.quotemasterapp.domain.usecase.SearchImagesUseCase
import com.quotemaster.quotemasterapp.presentation.state.QuoteEditorUiState
import com.quotemaster.quotemasterapp.utils.BackgroundTab
import com.quotemaster.quotemasterapp.utils.BackgroundType
import com.quotemaster.quotemasterapp.utils.Constants.INITIAL_PAGE_SIZE
import com.quotemaster.quotemasterapp.utils.Constants.PER_PAGE
import com.quotemaster.quotemasterapp.utils.Constants.TOTAL_DESIRED_ITEMS
import com.quotemaster.quotemasterapp.utils.FrameStyle
import com.quotemaster.quotemasterapp.utils.ImageEffect
import com.quotemaster.quotemasterapp.utils.toQuoteEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QuoteEditorViewModel @Inject constructor(
    private val getCachedQuotesWithImagesAndStylesUseCase: GetCachedQuotesWithImagesAndStylesUseCase,
    private val refreshQuotesDataUseCase: RefreshQuotesDataUseCase,
    private val getQuotesWithImagesUseCase: GetQuotesWithImagesUseCase,
    private val getAllImagesUseCase: GetAllImagesUseCase,
    private val searchImagesUseCase: SearchImagesUseCase,
    private val getGalleryImagesUseCase: GetGalleryImagesUseCase,
    private val saveImageToGalleryUseCase: SaveImageToGalleryUseCase
    ) : ViewModel()
{

    private val _uiState = MutableStateFlow(QuoteEditorUiState())
    val uiState: StateFlow<QuoteEditorUiState> = _uiState.asStateFlow()

    private val _quotes = MutableStateFlow<List<QuoteEntity>>(emptyList())
    val quotes: StateFlow<List<QuoteEntity>> = _quotes.asStateFlow()

    private val _searchResults = MutableStateFlow<List<QuoteEntity>>(emptyList())
    val searchResults: StateFlow<List<QuoteEntity>> = _searchResults.asStateFlow()

    // Background-related state
    private val _backgroundImages = MutableStateFlow<List<ImageEntity>>(emptyList())
    val backgroundImages: StateFlow<List<ImageEntity>> = _backgroundImages.asStateFlow()

    private val _searchedImages = MutableStateFlow<List<PixabayImage>>(emptyList())
    val searchedImages: StateFlow<List<PixabayImage>> = _searchedImages.asStateFlow()

    private val _galleryImages = MutableStateFlow<List<GalleryImage>>(emptyList())
    val galleryImages: StateFlow<List<GalleryImage>> = _galleryImages.asStateFlow()

    private val _showCamera = MutableStateFlow(false)
    val showCamera: StateFlow<Boolean> = _showCamera.asStateFlow()

    private var isInitialized = false

    fun initializeEditor(
        initialImageUrl: String,
        initialQuoteContent: String,
        initialQuoteAuthor: String,
        category: String
    ) {
        if (isInitialized) return // Prevent re-initialization

        viewModelScope.launch {
            _uiState.updateLoading(true)

            runCatching {
                val quoteText = if (initialQuoteContent.isNotEmpty())
                    "$initialQuoteContent - $initialQuoteAuthor"
                else ""

                val imageUrl = initialImageUrl.ifBlank {
                    val cached = getCachedQuotesWithImagesAndStylesUseCase(category)
                    val image = if (cached.isNotEmpty()) {
                        cached.random().image.largeImageURL
                    } else {
                        val refreshed = refreshQuotesDataUseCase(category, PER_PAGE).getOrThrow()
                        refreshed.randomOrNull()?.image?.largeImageURL.orEmpty()
                    }
                    image
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    backgroundImageUrl = imageUrl,
                    quoteText = quoteText,
                    quoteAuthor = initialQuoteAuthor,
                    backgroundType = BackgroundType.IMAGE
                )
            }.onFailure {
                _uiState.setError("Failed to initialize editor: ${it.message}")
            }
        }
        isInitialized = true

    }

    // Background functionality
    fun onBackgroundClick() {
        _uiState.value = _uiState.value.copy(
            showBackgroundOptions = true,
            selectedBackgroundTab = BackgroundTab.IMAGES,
            showImagePicker = true
        )
        loadBackgroundImages()
    }

    fun closeBackgroundOptions() {
        _uiState.value = _uiState.value.copy(
            showBackgroundOptions = false,
            showImagePicker = false,
            showColorPicker = false,
            showOverlayOptions = false,
            showEffectOptions = false
        )
    }

    fun onImagesTabClick() {
        _uiState.value = _uiState.value.copy(
            selectedBackgroundTab = BackgroundTab.IMAGES,
            showImagePicker = true,
            showImageColorPicker = false,
            showOverlayOptions = false,
            showEffectOptions = false
        )
        loadBackgroundImages()
    }

    fun onGalleryTabClick() {
        _uiState.value = _uiState.value.copy(
            showImagePicker = false,
            selectedBackgroundTab = BackgroundTab.GALLERY
        )
        loadGalleryImages()
    }

    fun onColorTabClick() {
        _uiState.value = _uiState.value.copy(
            showImageColorPicker = true,
            selectedBackgroundTab = BackgroundTab.COLOR
        )
    }

    fun onOverlayTabClick() {
        if (_uiState.value.backgroundType == BackgroundType.IMAGE) {
            _uiState.value = _uiState.value.copy(
                showOverlayOptions = true,
                selectedBackgroundTab = BackgroundTab.OVERLAY
            )
        }
    }

    fun onEffectTabClick() {
        if (_uiState.value.backgroundType == BackgroundType.IMAGE) {
            _uiState.value = _uiState.value.copy(
                showEffectOptions = true,
                selectedBackgroundTab = BackgroundTab.EFFECT
            )
        } else {
            _uiState.value = _uiState.value.copy(
                toastMessage = "Please set the background image to set the effect"
            )
        }
    }

    fun onCameraTabClick() {
        _uiState.value = _uiState.value.copy(selectedBackgroundTab = BackgroundTab.CAMERA)
    }

    fun onShowCamera() {
        _showCamera.value = true
    }

    fun loadBackgroundImages() {
        viewModelScope.launch {
            _uiState.updateLoading(true)

            runCatching {
                // First try to get images from database
                val cachedImages = getAllImagesUseCase()

                if (cachedImages.isNotEmpty()) {
                    _backgroundImages.value = cachedImages
                    _uiState.updateLoading(false)
                } else {
                    // If no images in DB, search from API
                    searchBackgroundImages("nature background wallpaper")
                }
            }.onFailure {
                _uiState.setError("Failed to load background images: ${it.message}")
            }
        }
    }

    fun searchBackgroundImages(query: String) {
        viewModelScope.launch {
            _uiState.updateLoading(true)

            runCatching {
                val searchResults = searchImagesUseCase(query, 20)
                _searchedImages.value = searchResults
                _uiState.updateLoading(false)
            }.onFailure {
                _uiState.setError("Failed to search images: ${it.message}")
            }
        }
    }

    private fun selectBackgroundImage(imageUrl: String) {
        _uiState.value = _uiState.value.copy(
            backgroundImageUrl = imageUrl,
            backgroundColor = null,
            backgroundType = BackgroundType.IMAGE,
            showBackgroundOptions = false,
            showImagePicker = false,
            showImageColorPicker = false,
            showOverlayOptions = false,
            showEffectOptions = false
        )
    }

    fun selectBackgroundImageFromEntity(image: ImageEntity) {
        selectBackgroundImage(image.largeImageURL)
    }

    fun selectBackgroundImageFromPixabay(image: PixabayImage) {
        selectBackgroundImage(image.largeImageURL)
    }

    fun selectBackgroundColor(color: Color) {
        _uiState.value = _uiState.value.copy(
            backgroundColor = color,
            backgroundImageUrl = "",
            backgroundType = BackgroundType.COLOR,
            showBackgroundOptions = false,
            showColorPicker = false
        )
    }

    fun selectBackgroundFromGallery(uri: String) {
        _uiState.value = _uiState.value.copy(
            backgroundImageUrl = uri,
            backgroundColor = null,
            backgroundType = BackgroundType.IMAGE,
            showBackgroundOptions = false,
            showImagePicker = false
        )
    }

    fun onCameraImageCaptured(imageUri: String) {
        selectBackgroundFromCamera(imageUri)
        _showCamera.value = false
    }

    private fun selectBackgroundFromCamera(uri: String) {
        _uiState.value = _uiState.value.copy(
            backgroundImageUrl = uri,
            showBackgroundOptions = false
        )
    }

    fun updateOverlayBrightness(brightness: Float) {
        _uiState.value = _uiState.value.copy(overlayBrightness = brightness)
    }

    fun selectEffect(effect: ImageEffect) {
        if (_uiState.value.backgroundType == BackgroundType.IMAGE) {
            _uiState.value = _uiState.value.copy(
                selectedEffect = effect,
                showEffectOptions = false
            )
        }
    }

    fun clearToastMessage() {
        _uiState.value = _uiState.value.copy(toastMessage = null)
    }

    fun loadQuotesForSelection(category: String) {
        viewModelScope.launch {
            _uiState.updateLoading(true)

            runCatching {
                val result = getQuotesWithImagesUseCase(category, false,
                    INITIAL_PAGE_SIZE,isInitialLoad = true)

                result.onSuccess { quotesWithImages ->
                    if (quotesWithImages.size < TOTAL_DESIRED_ITEMS) {
                        launch {
                            delay(2000) // 2 second delay
                            val remainingResult = getQuotesWithImagesUseCase(
                                category,
                                false,
                                TOTAL_DESIRED_ITEMS - quotesWithImages.size,
                                isInitialLoad = false
                            )
                            remainingResult.onSuccess { moreQuotes ->
                                val quoteEntities = moreQuotes.map {
                                    it.quote.toQuoteEntity(
                                        imageId = it.image.id,
                                        category = category,
                                        textStyle = it.textStyle
                                    )
                                }
                                _quotes.value += quoteEntities
                                _searchResults.value += quoteEntities
                                _uiState.updateLoading(false)
                            }
                        }
                    }
                }.onFailure { exception ->
                    when (exception) {
                        is retrofit2.HttpException -> {
                            when (exception.code()) {
                                429 -> _uiState.setError("Too many requests. Please wait a moment and try again.")
                                401 -> _uiState.setError("Authentication failed. Please check your API keys.")
                                403 -> _uiState.setError("Access forbidden. Please check your API permissions.")
                                else -> _uiState.setError("Failed to load quotes: ${exception.message()}")
                            }
                        }
                        else -> _uiState.setError("Failed to load quotes: ${exception.localizedMessage}")
                    }
                    _uiState.updateLoading(false)
                }
            }.onFailure {
                _uiState.updateLoading(false)
                _uiState.setError("Failed to load quotes: ${it.message}")
            }
        }
    }

    fun searchQuotes(query: String) = viewModelScope.launch {
        runCatching {
            val results = if (query.isBlank()) _quotes.value else _quotes.value.filter {
                it.content.contains(query, true) || it.author!!.contains(query, true)
            }
            _searchResults.value = results
        }.onFailure {
            _uiState.setError("Search failed: ${it.message}")
        }
    }

    fun saveQuote(content: String, author: String) = viewModelScope.launch {
        runCatching {
            _uiState.value = _uiState.value.copy(
                quoteText = "$content - $author",
                quoteAuthor = author,
                showQuoteInput = false
            )
        }.onFailure {
            _uiState.setError("Failed to save quote: ${it.message}")
        }
    }

    fun updateQuoteText(text: String) { _uiState.value = _uiState.value.copy(quoteText = text) }

    private fun MutableStateFlow<QuoteEditorUiState>.updateLoading(isLoading: Boolean) {
        value = value.copy(isLoading = isLoading)
    }

    private fun MutableStateFlow<QuoteEditorUiState>.setError(message: String) {
        value = value.copy(isLoading = false, error = message)
    }

    fun onQuoteButtonClick() {
        _uiState.value = _uiState.value.copy(showQuoteInput = true)
    }

    fun onTapToAddQuoteClick() {
        _uiState.value = _uiState.value.copy(showQuoteInput = true)
    }

    fun closeColorPicker() {
        _uiState.value = _uiState.value.copy(showColorPicker = false)
    }

    fun closeFontPicker() {
        _uiState.value = _uiState.value.copy(showFontPicker = false)
    }

    fun closeSizePicker() {
        _uiState.value = _uiState.value.copy(showSizePicker = false)
    }

    fun closeAlignPicker() {
        _uiState.value = _uiState.value.copy(showAlignPicker = false)
    }

    fun closeShadowPicker() {
        _uiState.value = _uiState.value.copy(showShadowPicker = false)
    }

    fun closeSpacingPicker() {
        _uiState.value = _uiState.value.copy(showSpacingPicker = false)
    }

    fun closeTextActions() {
        _uiState.value = _uiState.value.copy(
            showTextActions = false,
            showColorPicker = false,
            showFontPicker = false,
            showSizePicker = false,
            showAlignPicker = false,
            showShadowPicker = false,
            showSpacingPicker = false
        )
    }

    fun closeQuoteInput() {
        _uiState.value = _uiState.value.copy(showQuoteInput = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun onTextButtonClick() {
        if (_uiState.value.quoteText.isEmpty()) {
            _uiState.value = _uiState.value.copy(showQuoteInput = true)
        } else {
            _uiState.value = _uiState.value.copy(showTextActions = true)
        }
    }

    fun onColorPickerClick() {
        _uiState.value = _uiState.value.copy(showColorPicker = true)
    }

    fun onFontPickerClick() {
        _uiState.value = _uiState.value.copy(showFontPicker = true)
    }

    fun onSizePickerClick() {
        _uiState.value = _uiState.value.copy(showSizePicker = true)
    }

    fun onAlignPickerClick() {
        _uiState.value = _uiState.value.copy(showAlignPicker = true)
    }

    fun onShadowPickerClick() {
        _uiState.value = _uiState.value.copy(showShadowPicker = true)
    }

    fun onSpacingPickerClick() {
        _uiState.value = _uiState.value.copy(showSpacingPicker = true)
    }

    fun updateTextColor(color: Color) {
        val currentStyle = _uiState.value.textStyle
        _uiState.value = _uiState.value.copy(
            textStyle = currentStyle.copy(color = color)
        )
    }

    fun updateFontSize(size: TextUnit) {
        val currentStyle = _uiState.value.textStyle
        _uiState.value = _uiState.value.copy(
            textStyle = currentStyle.copy(fontSize = size)
        )
    }

    fun updateFontWeight(weight: FontWeight) {
        val currentStyle = _uiState.value.textStyle
        _uiState.value = _uiState.value.copy(
            textStyle = currentStyle.copy(fontWeight = weight)
        )
    }

    fun updateTextAlign(align: TextAlign) {
        val currentStyle = _uiState.value.textStyle
        _uiState.value = _uiState.value.copy(
            textStyle = currentStyle.copy(textAlign = align)
        )
    }

    fun updateShadow(radius: Float, color: Color) {
        val currentStyle = _uiState.value.textStyle
        _uiState.value = _uiState.value.copy(
            textStyle = currentStyle.copy(
                shadowRadius = radius,
                shadowColor = color
            )
        )
    }

    fun updateLetterSpacing(spacing: TextUnit) {
        val currentStyle = _uiState.value.textStyle
        _uiState.value = _uiState.value.copy(
            textStyle = currentStyle.copy(letterSpacing = spacing)
        )
    }

    fun updateLineHeight(height: TextUnit) {
        val currentStyle = _uiState.value.textStyle
        _uiState.value = _uiState.value.copy(
            textStyle = currentStyle.copy(lineHeight = height)
        )
    }

    fun loadGalleryImages() {
        viewModelScope.launch {
            _uiState.updateLoading(true)
            try {
                val images = getGalleryImagesUseCase()
                _galleryImages.value = images
                _uiState.updateLoading(false)
            } catch (e: Exception) {
                _uiState.setError("Failed to load gallery images: ${e.message}")
            }
        }
    }

    fun onCameraClosed() {
        _showCamera.value = false
    }

    fun onGradientTabClick() {
        _uiState.value = _uiState.value.copy(
            selectedBackgroundTab = BackgroundTab.GRADIENT,
            showImagePicker = false,
            showImageColorPicker = false,
            showOverlayOptions = false,
            showEffectOptions = false
        )
    }

    fun selectBackgroundGradient(brush: Brush) {
        _uiState.value = _uiState.value.copy(
            backgroundGradient = brush,
            backgroundImageUrl = "",
            backgroundColor = null,
            backgroundType = BackgroundType.GRADIENT,
            showBackgroundOptions = false
        )
    }

    fun onFrameClick() {
        _uiState.value = _uiState.value.copy(
            showFrameOptions = true,
            showFrameColorPicker = false,
            showBackgroundOptions = false,
            showTextActions = false
        )
    }

    fun onFrameColorClick() {
        _uiState.value = _uiState.value.copy(
            showFrameColorPicker = true,
            showFrameOptions = false,
            showBackgroundOptions = false,
            showTextActions = false
        )
    }

    fun selectFrame(frame: FrameStyle) {
        _uiState.value = _uiState.value.copy(
            selectedFrame = frame,
            customFrameColor = frame.defaultBorderColor,
            showFrameOptions = false
        )
    }

    fun selectFrameColor(color: Color) {
        _uiState.value = _uiState.value.copy(
            customFrameColor = color,
            showFrameColorPicker = false
        )
    }

    fun closeFrameOptions() {
        _uiState.value = _uiState.value.copy(
            showFrameOptions = false,
        )
    }

    fun closeFrameColorPicker() {
        _uiState.value = _uiState.value.copy(
            showFrameColorPicker = false
        )
    }

    fun onEmojiClick() {
        _uiState.value = _uiState.value.copy(
            showEmojiPicker = true,
            showBackgroundOptions = false,
            showTextActions = false
        )
    }

    fun closeEmojiPicker() {
        _uiState.value = _uiState.value.copy(showEmojiPicker = false)
    }

    fun addEmoji(emoji: String) {
        val newEmoji = EmojiItem(
            emoji = emoji,
            x = 0.5f, // Center position (normalized)
            y = 0.5f
        )

        _uiState.value = _uiState.value.copy(
            placedEmojis = _uiState.value.placedEmojis + newEmoji,
            showEmojiPicker = false
        )
    }

    fun selectEmoji(emojiId: String) {
        _uiState.value = _uiState.value.copy(selectedEmojiId = emojiId)
    }

    fun updateEmojiPosition(emojiId: String, x: Float, y: Float) {
        _uiState.value = _uiState.value.copy(
            placedEmojis = _uiState.value.placedEmojis.map { emoji ->
                if (emoji.id == emojiId) {
                    emoji.copy(x = x, y = y)
                } else {
                    emoji
                }
            }
        )
    }

    fun updateEmojiScale(emojiId: String, scale: Float) {
        val updatedEmojis = _uiState.value.placedEmojis.map { emoji ->
            if (emoji.id == emojiId) {
                emoji.copy(scale = scale.coerceIn(0.5f, 3f))
            } else emoji
        }

        _uiState.value = _uiState.value.copy(placedEmojis = updatedEmojis)
    }

    fun updateEmojiRotation(emojiId: String, rotation: Float) {
        val updatedEmojis = _uiState.value.placedEmojis.map { emoji ->
            if (emoji.id == emojiId) {
                emoji.copy(rotation = rotation)
            } else emoji
        }

        _uiState.value = _uiState.value.copy(placedEmojis = updatedEmojis)
    }

    fun deleteEmoji(emojiId: String) {
        _uiState.value = _uiState.value.copy(
            placedEmojis = _uiState.value.placedEmojis.filter { it.id != emojiId },
            selectedEmojiId = null
        )
    }

    suspend fun saveImageToGallery(bitmap: Bitmap, filename: String): Result<String> {
        return saveImageToGalleryUseCase(bitmap, filename)
    }
}
