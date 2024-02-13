package com.jalloft.lero.ui.screens.loggedin.registration.city

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jalloft.lero.data.nominatim.Place
import com.jalloft.lero.repositories.CitySearchRepository
import com.jalloft.lero.util.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class CityViewModel @Inject constructor(private val repo: CitySearchRepository) : ViewModel() {

    var searchResults by mutableStateOf<List<Place>>(emptyList())
    var isSearchLoading by mutableStateOf(false)
    var searchFailure by mutableStateOf<Exception?>(null)

    private val searchThrottle = MutableStateFlow("")

    init {
        viewModelScope.launch {
            searchThrottle.debounce(1000).collectLatest { query ->
                if (query.trim().isNotEmpty()) {
                    searchCity(query)
                }
            }
        }
    }


    fun setSearchQuery(query: String) {
        searchThrottle.value = query
        isSearchLoading = true
        if (query.trim().isEmpty()) {
            searchResults = mutableStateListOf()
            isSearchLoading = false
        }
    }

    private fun searchCity(query: String, limit: Int = 15) {
        viewModelScope.launch {
            searchFailure = null
            isSearchLoading = true
            try {
                repo.searchCity(query, limit).collectLatest { response ->
                    when (response) {
                        is ResponseState.Success -> {
                            searchResults = response.data
                        }

                        is ResponseState.Failure -> {
                            searchFailure = response.exception
                        }

                        ResponseState.Loading -> {
                            // Handle loading state if necessary
                        }
                    }
                }
            } finally {
                isSearchLoading = false
            }
        }
    }
}