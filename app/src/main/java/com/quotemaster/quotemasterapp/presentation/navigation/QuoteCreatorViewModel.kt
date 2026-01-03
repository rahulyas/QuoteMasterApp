package com.quotemaster.quotemasterapp.presentation.navigation

import androidx.lifecycle.ViewModel
import com.quotemaster.quotemasterapp.data.local.entity.QuoteEntity
import com.quotemaster.quotemasterapp.domain.usecase.ObserveNetworkStatusUseCase
import com.quotemaster.quotemasterapp.utils.QuoteCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class QuoteCreatorViewModel @Inject constructor(
    val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
    ): ViewModel() {
    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _selectedQuoteType = MutableStateFlow("Text")
    val selectedQuoteType: StateFlow<String> = _selectedQuoteType

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _quotes = MutableStateFlow<List<QuoteEntity>>(emptyList())
    val quotes: StateFlow<List<QuoteEntity>> = _quotes.asStateFlow()

    fun updateIsConnected(isConnected: Boolean) {
        _isConnected.value = isConnected
    }

    fun updateSearchText(text: String) {
        _searchText.value = text
    }

    fun updateSelectedQuoteType(type: String) {
        _selectedQuoteType.value = type
    }

    fun getFeaturedCategories(): List<QuoteCategory> {
        return listOf(
            QuoteCategory.INSPIRATIONAL,
            QuoteCategory.MOTIVATIONAL,
            QuoteCategory.LOVE,
            QuoteCategory.FRIENDSHIP,
            QuoteCategory.HAPPINESS,
            QuoteCategory.LIFE,
            QuoteCategory.WISDOM,
            QuoteCategory.WAR,
            QuoteCategory.POETRY
        )
    }

}