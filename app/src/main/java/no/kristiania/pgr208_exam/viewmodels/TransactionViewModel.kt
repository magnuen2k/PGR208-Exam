package no.kristiania.pgr208_exam.viewmodels

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import no.kristiania.pgr208_exam.datastorage.db.DataBase
import no.kristiania.pgr208_exam.datastorage.entities.UserPortfolio

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val _userPortfolio = MutableLiveData<UserPortfolio>()
    val userPortfolio: LiveData<UserPortfolio> get() = _userPortfolio

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
}