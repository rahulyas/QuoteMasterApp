package com.quotemaster.quotemasterapp.presentation.state

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.quotemaster.quotemasterapp.domain.model.EmojiItem
import com.quotemaster.quotemasterapp.utils.BackgroundTab
import com.quotemaster.quotemasterapp.utils.BackgroundType
import com.quotemaster.quotemasterapp.utils.FrameStyle
import com.quotemaster.quotemasterapp.utils.ImageEffect


data class QuoteEditorUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val toastMessage: String? = null,

    // Quote content
    val quoteText: String = "",
    val quoteAuthor: String = "",
    val showQuoteInput: Boolean = false,

    // Background
    val backgroundImageUrl: String = "",
    val backgroundColor: Color? = null,
    val backgroundType: BackgroundType = BackgroundType.IMAGE,
    val overlayBrightness: Float = 0.0f,
    val selectedEffect: ImageEffect = ImageEffect.NONE,
    val backgroundGradient: Brush? = null,

    // Background options
    val showBackgroundOptions: Boolean = false,
    val showImagePicker: Boolean = false,
    val showOverlayOptions: Boolean = false,
    val showEffectOptions: Boolean = false,
    val selectedBackgroundTab: BackgroundTab = BackgroundTab.IMAGES,

    // Text styling
    val textStyle: QuoteTextStyle = QuoteTextStyle(),
    val showTextActions: Boolean = false,
    val showColorPicker: Boolean = false,
    val showFontPicker: Boolean = false,
    val showSizePicker: Boolean = false,
    val showAlignPicker: Boolean = false,
    val showShadowPicker: Boolean = false,
    val showSpacingPicker: Boolean = false,
    val showImageColorPicker :Boolean = false,

    //frame
    val showFrameOptions: Boolean = false,
    val showFrameColorPicker: Boolean = false,
    val selectedFrame: FrameStyle = FrameStyle.NONE,
    val customFrameColor: Color = Color.White,

    //emoji
    val showEmojiPicker: Boolean = false,
    val placedEmojis: List<EmojiItem> = emptyList(),
    val selectedEmojiId: String? = null,
    val isSaving: Boolean = false,
    val saveProgress: Float = 0f

    )
data class QuoteTextStyle(
    val color: Color = Color.White,
    val fontSize: TextUnit = 24.sp,
    val fontWeight: FontWeight = FontWeight.Medium,
    val textAlign: TextAlign = TextAlign.Center,
    val shadowRadius: Float = 0f,
    val shadowColor: Color = Color.Black,
    val letterSpacing: TextUnit = 0.sp,
    val lineHeight: TextUnit = 30.sp
)