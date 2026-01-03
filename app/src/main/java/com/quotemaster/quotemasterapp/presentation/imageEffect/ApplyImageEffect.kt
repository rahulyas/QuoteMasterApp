package com.quotemaster.quotemasterapp.presentation.imageEffect

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.quotemaster.quotemasterapp.utils.ImageEffect
import java.lang.StrictMath.pow

@Composable
fun applyImageEffectToPainter(effect: ImageEffect?): ColorFilter? {
    return when (effect) {
        ImageEffect.GRAYSCALE -> ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
        ImageEffect.SEPIA -> ColorFilter.colorMatrix(sepiaMatrix())
        ImageEffect.INVERT -> ColorFilter.colorMatrix(invertMatrix())
        ImageEffect.BRIGHTNESS -> ColorFilter.colorMatrix(brightnessMatrix(0.2f))
        ImageEffect.CONTRAST -> ColorFilter.colorMatrix(contrastMatrix(1.5f))
        ImageEffect.SATURATION -> ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(1.5f) })
        ImageEffect.TINT -> ColorFilter.tint(Color.Blue, BlendMode.Overlay)
        ImageEffect.HUE -> ColorFilter.colorMatrix(hueMatrix(30f)) // 30 degree hue shift
        ImageEffect.GAMMA -> ColorFilter.colorMatrix(gammaMatrix(1.5f))
        ImageEffect.TEMPERATURE -> ColorFilter.colorMatrix(temperatureMatrix(500f)) // Warmer
        ImageEffect.VIBRANCE -> ColorFilter.colorMatrix(vibranceMatrix(0.3f))
        ImageEffect.EXPOSURE -> ColorFilter.colorMatrix(exposureMatrix(0.5f))
        ImageEffect.HIGHLIGHT -> ColorFilter.colorMatrix(highlightMatrix(0.2f))
        ImageEffect.SHADOW -> ColorFilter.colorMatrix(shadowMatrix(0.2f))
        ImageEffect.SOLARIZE -> ColorFilter.colorMatrix(solarizeMatrix())
        ImageEffect.VINTAGE -> ColorFilter.colorMatrix(vintageMatrix())
        ImageEffect.POSTERIZE -> ColorFilter.colorMatrix(posterizeMatrix())
        ImageEffect.LOMO -> ColorFilter.colorMatrix(lomoMatrix())
        ImageEffect.HDR -> ColorFilter.colorMatrix(hdrMatrix())
        ImageEffect.CHROMATIC_ABERRATION -> ColorFilter.colorMatrix(chromaticAberrationMatrix())
        ImageEffect.COLOR_MATRIX -> ColorFilter.colorMatrix(customColorMatrix())
        else -> null
    }
}

fun applyImageModifier(effect: ImageEffect?): Modifier {
    return when (effect) {
        ImageEffect.BLUR -> Modifier.blur(8.dp)
        ImageEffect.ALPHA -> Modifier.alpha(0.7f)
        ImageEffect.PIXELATE -> Modifier.blur(2.dp) // Approximation with heavy blur
        ImageEffect.SHARPEN -> Modifier.blur(0.5.dp) // Subtle blur removal effect
        ImageEffect.VIGNETTE -> Modifier.drawBehind {
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    center = center,
                    radius = size.minDimension * 0.8f
                ),
                blendMode = BlendMode.Multiply
            )
        }
        ImageEffect.TILT_SHIFT -> Modifier.blur(6.dp) // Approximation
        ImageEffect.BOKEH -> Modifier.blur(10.dp)
        ImageEffect.NOISE -> Modifier.drawBehind {
            // Simple noise overlay
            repeat(1000) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.1f),
                    radius = 1f,
                    center = Offset(
                        (Math.random() * size.width).toFloat(),
                        (Math.random() * size.height).toFloat()
                    )
                )
            }
        }
        ImageEffect.ROUNDED_CORNERS -> Modifier.clip(RoundedCornerShape(16.dp))
        ImageEffect.CIRCLE_CROP -> Modifier.clip(CircleShape)
        ImageEffect.ROTATE -> Modifier.rotate(45f)
        ImageEffect.FLIP_HORIZONTAL -> Modifier.scale(scaleX = -1f, scaleY = 1f)
        ImageEffect.FLIP_VERTICAL -> Modifier.scale(scaleX = 1f, scaleY = -1f)
        ImageEffect.SCALE -> Modifier.scale(1.2f)
        ImageEffect.SKEW -> Modifier.graphicsLayer {
            transformOrigin = TransformOrigin.Center
            rotationX = 15f
            rotationY = 10f
        }
        ImageEffect.PERSPECTIVE -> Modifier.graphicsLayer {
            transformOrigin = TransformOrigin.Center
            rotationX = 30f
            cameraDistance = 12f
        }
        ImageEffect.SWIRL -> Modifier.graphicsLayer {
            transformOrigin = TransformOrigin.Center
            rotationZ = 360f
            scaleX = 0.8f
            scaleY = 0.8f
        }
        ImageEffect.BULGE -> Modifier.graphicsLayer {
            transformOrigin = TransformOrigin.Center
            scaleX = 1.3f
            scaleY = 1.3f
        }
        ImageEffect.PINCH -> Modifier.graphicsLayer {
            transformOrigin = TransformOrigin.Center
            scaleX = 0.7f
            scaleY = 0.7f
        }
        ImageEffect.SPHERE -> Modifier.graphicsLayer {
            transformOrigin = TransformOrigin.Center
            rotationX = 45f
            rotationY = 45f
            cameraDistance = 8f
        }
        ImageEffect.MIRROR -> Modifier.drawBehind {
            // Draw the mirrored version
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.3f))
                ),
                topLeft = Offset(size.width / 2, 0f),
                size = Size(size.width / 2, size.height)
            )
        }
        ImageEffect.SKETCH -> Modifier.drawBehind {
            drawRect(
                color = Color.White,
                blendMode = BlendMode.Overlay
            )
        }
        ImageEffect.EMBOSS -> Modifier.drawBehind {
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(Color.White, Color.Gray, Color.Black),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height)
                ),
                blendMode = BlendMode.Overlay
            )
        }
        ImageEffect.EDGE_DETECTION -> Modifier.drawBehind {
            drawRect(
                color = Color.Black,
                blendMode = BlendMode.Overlay
            )
        }
        ImageEffect.GLITCH -> Modifier.drawBehind {
            // Random glitch lines
            repeat(10) {
                val y = (Math.random() * size.height).toFloat()
                drawLine(
                    color = Color.Red.copy(alpha = 0.3f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 2f
                )
            }
        }
        ImageEffect.KALEIDOSCOPE -> Modifier.drawBehind {
            // Create kaleidoscope pattern
            repeat(6) { i ->
                val angle = (i * 60f) * Math.PI / 180f
                val x = (kotlin.math.cos(angle) * size.width / 4).toFloat()
                val y = (kotlin.math.sin(angle) * size.height / 4).toFloat()
                drawCircle(
                    color = Color.Blue.copy(alpha = 0.2f),
                    radius = 20f,
                    center = Offset(size.width/2 + x, size.height/2 + y)
                )
            }
        }
        else -> Modifier
    }
}

fun applyColorBackgroundEffect(baseColor: Color, effect: ImageEffect?): Color {
    return when (effect) {
        ImageEffect.GRAYSCALE -> {
            val gray = (baseColor.red * 0.299f + baseColor.green * 0.587f + baseColor.blue * 0.114f)
            Color(gray, gray, gray, baseColor.alpha)
        }
        ImageEffect.BRIGHTNESS -> baseColor.copy(
            red = (baseColor.red * 1.2f).coerceIn(0f, 1f),
            green = (baseColor.green * 1.2f).coerceIn(0f, 1f),
            blue = (baseColor.blue * 1.2f).coerceIn(0f, 1f)
        )
        ImageEffect.ALPHA -> baseColor.copy(alpha = 0.7f)
        ImageEffect.SATURATION -> {
            val hsv = FloatArray(3)
            android.graphics.Color.colorToHSV(baseColor.toArgb(), hsv)
            hsv[1] = (hsv[1] * 1.5f).coerceIn(0f, 1f)
            Color(android.graphics.Color.HSVToColor(hsv))
        }
        ImageEffect.INVERT -> Color(1f - baseColor.red, 1f - baseColor.green, 1f - baseColor.blue, baseColor.alpha)
        ImageEffect.HUE -> {
            val hsv = FloatArray(3)
            android.graphics.Color.colorToHSV(baseColor.toArgb(), hsv)
            hsv[0] = (hsv[0] + 60f) % 360f // Shift hue by 60 degrees
            Color(android.graphics.Color.HSVToColor(hsv))
        }
        ImageEffect.TEMPERATURE -> {
            // Warm temperature effect
            baseColor.copy(
                red = (baseColor.red * 1.1f).coerceIn(0f, 1f),
                blue = (baseColor.blue * 0.9f).coerceIn(0f, 1f)
            )
        }
        ImageEffect.VIBRANCE -> {
            val hsv = FloatArray(3)
            android.graphics.Color.colorToHSV(baseColor.toArgb(), hsv)
            hsv[1] = (hsv[1] * 1.3f).coerceIn(0f, 1f)
            Color(android.graphics.Color.HSVToColor(hsv))
        }
        ImageEffect.EXPOSURE -> baseColor.copy(
            red = (baseColor.red * 1.5f).coerceIn(0f, 1f),
            green = (baseColor.green * 1.5f).coerceIn(0f, 1f),
            blue = (baseColor.blue * 1.5f).coerceIn(0f, 1f)
        )
        ImageEffect.SEPIA -> {
            val r = (baseColor.red * 0.393f + baseColor.green * 0.769f + baseColor.blue * 0.189f).coerceIn(0f, 1f)
            val g = (baseColor.red * 0.349f + baseColor.green * 0.686f + baseColor.blue * 0.168f).coerceIn(0f, 1f)
            val b = (baseColor.red * 0.272f + baseColor.green * 0.534f + baseColor.blue * 0.131f).coerceIn(0f, 1f)
            Color(r, g, b, baseColor.alpha)
        }
        ImageEffect.VINTAGE -> baseColor.copy(
            red = (baseColor.red * 0.9f + 0.1f).coerceIn(0f, 1f),
            green = (baseColor.green * 0.8f + 0.05f).coerceIn(0f, 1f),
            blue = (baseColor.blue * 0.6f).coerceIn(0f, 1f)
        )
        ImageEffect.POSTERIZE -> {
            val levels = 4f
            Color(
                (kotlin.math.floor(baseColor.red * levels) / levels).coerceIn(0f, 1f),
                (kotlin.math.floor(baseColor.green * levels) / levels).coerceIn(0f, 1f),
                (kotlin.math.floor(baseColor.blue * levels) / levels).coerceIn(0f, 1f),
                baseColor.alpha
            )
        }
        ImageEffect.SOLARIZE -> {
            Color(
                if (baseColor.red > 0.5f) 1f - baseColor.red else baseColor.red,
                if (baseColor.green > 0.5f) 1f - baseColor.green else baseColor.green,
                if (baseColor.blue > 0.5f) 1f - baseColor.blue else baseColor.blue,
                baseColor.alpha
            )
        }
        else -> baseColor
    }
}

// Additional helper matrices for new effects
private fun hueMatrix(degrees: Float): ColorMatrix {
    val radians = degrees * Math.PI / 180.0
    val cos = kotlin.math.cos(radians).toFloat()
    val sin = kotlin.math.sin(radians).toFloat()

    return ColorMatrix(
        floatArrayOf(
            0.213f + cos * 0.787f - sin * 0.213f, 0.715f - cos * 0.715f - sin * 0.715f, 0.072f - cos * 0.072f + sin * 0.928f, 0f, 0f,
            0.213f - cos * 0.213f + sin * 0.143f, 0.715f + cos * 0.285f + sin * 0.140f, 0.072f - cos * 0.072f - sin * 0.283f, 0f, 0f,
            0.213f - cos * 0.213f - sin * 0.787f, 0.715f - cos * 0.715f + sin * 0.715f, 0.072f + cos * 0.928f + sin * 0.072f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
}

private fun gammaMatrix(gamma: Float): ColorMatrix {
    val invGamma = 1f / gamma
    return ColorMatrix(
        floatArrayOf(
            invGamma, 0f, 0f, 0f, 0f,
            0f, invGamma, 0f, 0f, 0f,
            0f, 0f, invGamma, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
}

private fun temperatureMatrix(temperature: Float): ColorMatrix {
    val temp = temperature / 100f
    val red = if (temp <= 66f) 255f else 329.698727446f * pow((temp - 60).toDouble(), -0.1332047592).toFloat()
    val green = if (temp <= 66f) 99.4708025861f * kotlin.math.ln(temp) - 161.1195681661f else 288.1221695283f * pow(
        (temp - 60).toDouble(), -0.0755148492).toFloat()
    val blue = if (temp >= 66f) 255f else if (temp <= 19f) 0f else 138.5177312231f * kotlin.math.ln(temp - 10) - 305.0447927307f

    return ColorMatrix(
        floatArrayOf(
            red / 255f, 0f, 0f, 0f, 0f,
            0f, green / 255f, 0f, 0f, 0f,
            0f, 0f, blue / 255f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
}

private fun vibranceMatrix(vibrance: Float): ColorMatrix {
    val v = vibrance + 1f
    return ColorMatrix(
        floatArrayOf(
            v, 0f, 0f, 0f, 0f,
            0f, v, 0f, 0f, 0f,
            0f, 0f, v, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
}

private fun exposureMatrix(exposure: Float): ColorMatrix {
    val exp = pow(2.0, exposure.toDouble()).toFloat()
    return ColorMatrix(
        floatArrayOf(
            exp, 0f, 0f, 0f, 0f,
            0f, exp, 0f, 0f, 0f,
            0f, 0f, exp, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
}

private fun highlightMatrix(highlight: Float): ColorMatrix {
    val h = 1f + highlight
    return ColorMatrix(
        floatArrayOf(
            h, 0f, 0f, 0f, 0f,
            0f, h, 0f, 0f, 0f,
            0f, 0f, h, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
}

private fun shadowMatrix(shadow: Float): ColorMatrix {
    val s = 1f - shadow
    return ColorMatrix(
        floatArrayOf(
            s, 0f, 0f, 0f, shadow * 255f,
            0f, s, 0f, 0f, shadow * 255f,
            0f, 0f, s, 0f, shadow * 255f,
            0f, 0f, 0f, 1f, 0f
        )
    )
}

private fun solarizeMatrix(): ColorMatrix {
    return ColorMatrix(
        floatArrayOf(
            -1f, 0f, 0f, 0f, 255f,
            0f, -1f, 0f, 0f, 255f,
            0f, 0f, -1f, 0f, 255f,
            0f, 0f, 0f, 1f, 0f
        )
    )
}

private fun vintageMatrix(): ColorMatrix {
    return ColorMatrix(
        floatArrayOf(
            0.9f, 0.1f, 0.1f, 0f, 10f,
            0.1f, 0.8f, 0.1f, 0f, 5f,
            0.1f, 0.1f, 0.6f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
}

private fun posterizeMatrix(): ColorMatrix {
    return ColorMatrix(
        floatArrayOf(
            0.8f, 0.2f, 0.2f, 0f, 0f,
            0.2f, 0.8f, 0.2f, 0f, 0f,
            0.2f, 0.2f, 0.8f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
}

private fun lomoMatrix(): ColorMatrix {
    return ColorMatrix(
        floatArrayOf(
            1.5f, -0.3f, -0.3f, 0f, 0f,
            -0.3f, 1.5f, -0.3f, 0f, 0f,
            -0.3f, -0.3f, 1.5f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
}

private fun hdrMatrix(): ColorMatrix {
    return ColorMatrix(
        floatArrayOf(
            1.2f, 0.1f, 0.1f, 0f, 0f,
            0.1f, 1.2f, 0.1f, 0f, 0f,
            0.1f, 0.1f, 1.2f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
}

private fun chromaticAberrationMatrix(): ColorMatrix {
    return ColorMatrix(
        floatArrayOf(
            1f, 0.1f, 0f, 0f, 0f,
            0f, 1f, 0.1f, 0f, 0f,
            0.1f, 0f, 1f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
}

private fun customColorMatrix(): ColorMatrix {
    return ColorMatrix(
        floatArrayOf(
            1f, 0f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f, 0f,
            0f, 0f, 1f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
}

// Helper matrices for color effects
private fun sepiaMatrix(): ColorMatrix {
    return ColorMatrix(
        floatArrayOf(
            0.393f, 0.769f, 0.189f, 0f, 0f,
            0.349f, 0.686f, 0.168f, 0f, 0f,
            0.272f, 0.534f, 0.131f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
}

private fun invertMatrix(): ColorMatrix {
    return ColorMatrix(
        floatArrayOf(
            -1f, 0f, 0f, 0f, 255f,
            0f, -1f, 0f, 0f, 255f,
            0f, 0f, -1f, 0f, 255f,
            0f, 0f, 0f, 1f, 0f
        )
    )
}

private fun brightnessMatrix(brightness: Float): ColorMatrix {
    return ColorMatrix(
        floatArrayOf(
            1f, 0f, 0f, 0f, brightness * 255f,
            0f, 1f, 0f, 0f, brightness * 255f,
            0f, 0f, 1f, 0f, brightness * 255f,
            0f, 0f, 0f, 1f, 0f
        )
    )
}

private fun contrastMatrix(contrast: Float): ColorMatrix {
    val translate = (1f - contrast) / 2f * 255f
    return ColorMatrix(
        floatArrayOf(
            contrast, 0f, 0f, 0f, translate,
            0f, contrast, 0f, 0f, translate,
            0f, 0f, contrast, 0f, translate,
            0f, 0f, 0f, 1f, 0f
        )
    )
}
