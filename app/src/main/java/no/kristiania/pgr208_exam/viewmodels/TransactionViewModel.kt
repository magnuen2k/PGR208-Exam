package no.kristiania.pgr208_exam.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import no.kristiania.pgr208_exam.data.API
import no.kristiania.pgr208_exam.data.CoinCapService
import no.kristiania.pgr208_exam.data.domain.CcHistory
import no.kristiania.pgr208_exam.datastorage.db.DataBase
import no.kristiania.pgr208_exam.datastorage.entities.UserPortfolio


class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val _userPortfolio = MutableLiveData<UserPortfolio>()
    val userPortfolio: LiveData<UserPortfolio> get() = _userPortfolio

    private val coinCapService: CoinCapService = API.COIN_CAP_SERVICE

    private val _CcHistory = MutableLiveData<CcHistory>()
    val ccHistory: LiveData<CcHistory> get() = _CcHistory

    private val _error = MutableLiveData<Unit>()
    val error: LiveData<Unit> get() = _error

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        _error.postValue(Unit)
        Log.d("INFO", e.message!!)
    }

    fun getPortfolio(symbol: String) {
        viewModelScope.launch {
            val portfolio = DataBase.getDatabase(getApplication()).getUserPortfolioDAO().fetch(symbol)
            if (portfolio !== null) {
                _userPortfolio.postValue(portfolio)
            } else {
                _userPortfolio.postValue(UserPortfolio(symbol, "0"))
            }
        }
    }

    fun getInterval(id: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val ccOverviewItems = coinCapService.getInterval(id)
            _CcHistory.postValue(ccOverviewItems)
        }
    }
}