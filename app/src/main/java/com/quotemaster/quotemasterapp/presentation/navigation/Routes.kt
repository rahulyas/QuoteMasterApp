package com.quotemaster.quotemasterapp.presentation.navigation

object Routes {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val QUOTE_CREATOR = "quote_creator"
    const val POPULAR_QUOTES = "popular_quotes"
    const val POSTS_TIMELINE = "posts_timeline"
    const val ALL_CATEGORIES = "all_categories"
    const val CATEGORY_DETAIL = "category_detail/{categoryName}"
    const val QUOTE_EDIT = "quote_edit/{imageUrl}/{quoteContent}/{quoteAuthor}/{categoryName}"
    const val BACKGROUND_GALLERY = "backgroundGallery"
    const val ADD_QUOTE_SCREEN = "add_quote/{queryName}"

    fun categoryDetail(categoryName: String) = "category_detail/$categoryName"
    fun searchQuotes(queryName: String) = "add_quote/$queryName"
    fun quoteEdit(imageUrl: String, quoteContent: String, quoteAuthor: String,categoryName: String) =
        "quote_edit/${java.net.URLEncoder.encode(imageUrl, "UTF-8")}/${java.net.URLEncoder.encode(quoteContent, "UTF-8")}/${java.net.URLEncoder.encode(quoteAuthor, "UTF-8")}/${java.net.URLEncoder.encode(categoryName, "UTF-8")}"
}