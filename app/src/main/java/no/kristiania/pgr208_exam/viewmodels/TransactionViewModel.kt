package no.kristiania.pgr208_exam.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import no.kristiania.pgr208_exam.datastorage.db.DataBase
import no.kristiania.pgr208_exam.datastorage.entities.UserPortfolio
import no.kristiania.pgr208_exam.datastorage.entities.UserTransaction


class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val _userPortfolio = MutableLiveData<UserPortfolio>()
    val userPortfolio: LiveData<UserPortfolio> get() = _userPortfolio

    private val _userUsd = MutableLiveData<UserPortfolio>()
    val userUsd: LiveData<UserPortfolio> get() = _userUsd

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

    fun insertPortfolio(symbol: String, newVolume: String, ccVolume: String, usdBuyAmount: String, time: String, type: String) {
        viewModelScope.launch {
            DataBase.getDatabase(getApplication()).getUserPortfolioDAO().insertPortfolio(symbol, newVolume)
            DataBase.getDatabase(getApplication()).getUserTransactionsDAO().insert(UserTransaction(time, symbol, ccVolume, usdBuyAmount, type))
        }
    }

    fun updateUsd(volume: String) {
        viewModelScope.launch {
            DataBase.getDatabase(getApplication()).getUserPortfolioDAO().insertPortfolio("USD", volume)
        }
    }

    fun getUserUsd() {
        viewModelScope.launch {
            val userUsd = DataBase.getDatabase(getApplication()).getUserPortfolioDAO().fetchUsd()
            _userUsd.postValue(userUsd)
        }
    }
}