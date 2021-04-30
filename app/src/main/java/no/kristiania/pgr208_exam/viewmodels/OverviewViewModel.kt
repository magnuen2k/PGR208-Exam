package no.kristiania.pgr208_exam.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import no.kristiania.pgr208_exam.data.API
import no.kristiania.pgr208_exam.data.CoinCapService
import no.kristiania.pgr208_exam.data.domain.CcOverview
import kotlinx.coroutines.*
import no.kristiania.pgr208_exam.data.domain.CcHistory
import no.kristiania.pgr208_exam.datastorage.db.DataBase

class OverviewViewModel : ViewModel(){

    private val coinCapService: CoinCapService = API.COIN_CAP_SERVICE

    private val _allCcAssets = MutableLiveData<CcOverview>()
    val allCcAssets: LiveData<CcOverview> get() = _allCcAssets

    private val _CcHistory = MutableLiveData<CcHistory>()
    val ccHistory: LiveData<CcHistory> get() = _CcHistory

    private val _error = MutableLiveData<Unit>()
    val error: LiveData<Unit> get() = _error

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        _error.postValue(Unit)
        Log.d("INFO", e.message!!)
    }

    fun getAssetOverview() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            // Delay?
            val ccOverviewItems = coinCapService.getAssetOverview()
            _allCcAssets.postValue(ccOverviewItems);
        }
    }

    fun getInterval(id: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val ccOverviewItems = coinCapService.getInterval(id)
            _CcHistory.postValue(ccOverviewItems)
        }
    }
}