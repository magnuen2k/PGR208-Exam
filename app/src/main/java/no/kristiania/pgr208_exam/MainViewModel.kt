package no.kristiania.pgr208_exam

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import no.kristiania.pgr208_exam.data.API
import no.kristiania.pgr208_exam.data.CoinCapService
import no.kristiania.pgr208_exam.data.domain.Image
import kotlinx.coroutines.*

class MainViewModel : ViewModel(){

    private val API_KEY = "C-ZjCJxgg8vlnSPrYoav8yb-YRZmiaY43RBxVCjd_VU"

    private val coinCapService: CoinCapService = API.COIN_CAP_SERVICE

    private var pageNumber = 0;

    //Modified to take list
    private val _currentImage = MutableLiveData<List<Image>>()
    val currentImage: LiveData<List<Image>> get() = _currentImage

    private val _error = MutableLiveData<Unit>()
    val error: LiveData<Unit> get() = _error

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        _error.postValue(Unit)
        Log.d("INFO", e.message!!)
    }

    init {
        getPaginationPage()
    }

    fun reload() {
        getPaginationPage()
    }

    fun getPaginationPage() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            delay(5000)
            pageNumber++
            val paginationImages = coinCapService.getPaginationPage(pageNumber.toString(), client_id = API_KEY)
            _currentImage.postValue(paginationImages)
        }
    }
}