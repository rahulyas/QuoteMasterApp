package com.quotemaster.quotemasterapp.utils

enum class ImageEffect(val displayName: String) {
    // Basic Effects
    GRAYSCALE("Grayscale"),
    SEPIA("Sepia"),
    INVERT("Invert Colors"),
    BRIGHTNESS("Brightness"),
    CONTRAST("Contrast"),
    SATURATION("Saturation"),
    HUE("Hue"),
    GAMMA("Gamma Correction"),
    SHARPEN("Sharpen"),
    BLUR("Blur"),
    PIXELATE("Pixelate"),
    TINT("Tint Color"),
    ALPHA("Alpha / Opacity"),
    TEMPERATURE("Temperature"),
    VIBRANCE("Vibrance"),
    EXPOSURE("Exposure"),
    HIGHLIGHT("Highlight"),
    SHADOW("Shadow"),

    // Artistic Effects
    SKETCH("Sketch"),
    EMBOSS("Emboss"),
    SOLARIZE("Solarize"),
    VIGNETTE("Vignette"),
    EDGE_DETECTION("Edge Detection"),
    GLITCH("Glitch"),
    TILT_SHIFT("Tilt Shift"),
    BOKEH("Bokeh Blur"),
    VINTAGE("Vintage / Old Film"),
    POSTERIZE("Posterize"),
    NOISE("Noise / Grain"),
    LOMO("Lomo"),
    HDR("HDR"),
    MIRROR("Mirror"),
    CHROMATIC_ABERRATION("Chromatic Aberration"),

    // Transformations
    ROTATE("Rotate"),
    FLIP_HORIZONTAL("Flip Horizontal"),
    FLIP_VERTICAL("Flip Vertical"),
    SCALE("Scale"),
    SKEW("Skew"),
    CIRCLE_CROP("Circle Crop"),
    ROUNDED_CORNERS("Rounded Corners"),
    PERSPECTIVE("Perspective Transform"),
    SPHERE("Sphere Distortion"),

    // Fun Effects
    SWIRL("Swirl"),
    BULGE("Bulge"),
    PINCH("Pinch"),
    KALEIDOSCOPE("Kaleidoscope"),

    // Custom/Complex
    COLOR_MATRIX("Custom Color Matrix"),
    NONE("None") // For original / no effect
}
