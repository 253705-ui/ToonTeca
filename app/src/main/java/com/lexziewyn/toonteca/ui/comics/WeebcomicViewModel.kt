package com.lexziewyn.toonteca.ui.comics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lexziewyn.toonteca.domain.model.Weebcomic
import com.lexziewyn.toonteca.domain.repository.WeebcomicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ComicListUiState {
    object Loading : ComicListUiState
    data class Success(val comics: List<Weebcomic>) : ComicListUiState
    object Empty : ComicListUiState
    data class Error(val message: String) : ComicListUiState
}

class WeebcomicViewModel(
    private val repository: WeebcomicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ComicListUiState>(ComicListUiState.Loading)
    val uiState: StateFlow<ComicListUiState> = _uiState.asStateFlow()

    init {
        loadComics()
    }

    fun loadComics() {
        viewModelScope.launch {
            _uiState.value = ComicListUiState.Loading
            try {
                val list = repository.getComics()
                if (list.isEmpty()) {
                    _uiState.value = ComicListUiState.Empty
                } else {
                    _uiState.value = ComicListUiState.Success(list)
                }
            } catch (e: Exception) {
                _uiState.value = ComicListUiState.Error("Error al cargar cómics: ${e.localizedMessage}")
            }
        }
    }

    fun saveComic(comic: Weebcomic, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                if (comic.id == 0) {
                    repository.insertComic(comic)
                } else {
                    repository.updateComic(comic.id, comic)
                }
                loadComics()
                onComplete()
            } catch (e: Exception) {
                _uiState.value = ComicListUiState.Error("Error al guardar cómic")
            }
        }
    }

    fun updateCapitulosVistos(comic: Weebcomic, capitulosVistos: Int) {
        viewModelScope.launch {
            try {
                val updated = comic.copy(capitulosVistos = capitulosVistos)
                repository.updateComic(comic.id, updated)
                loadComics()
            } catch (e: Exception) {
                // error silencioso
            }
        }
    }

    fun deleteComic(id: Int, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.deleteComic(id)
                loadComics()
                onComplete()
            } catch (e: Exception) {
                _uiState.value = ComicListUiState.Error("No se pudo eliminar")
            }
        }
    }

    companion object {
        fun provideFactory(repository: WeebcomicRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return WeebcomicViewModel(repository) as T
                }
            }
    }
}
