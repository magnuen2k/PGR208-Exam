package no.kristiania.pgr208_exam.viewmodels
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.kristiania.pgr208_exam.PortfolioStatement
import no.kristiania.pgr208_exam.data.API
import no.kristiania.pgr208_exam.data.CoinCapService
import no.kristiania.pgr208_exam.datastorage.db.DataBase

class UserPortfolioViewModel(application: Application) : AndroidViewModel(application) {

    private val coinCapService: CoinCapService = API.COIN_CAP_SERVICE

    private val database = DataBase.getDatabase(getApplication())

    private val _portfolioStatements = MutableLiveData<List<PortfolioStatement>>()
    val portfolioStatements: LiveData<List<PortfolioStatement>> get() = _portfolioStatements

    private val _error = MutableLiveData<Unit>()
    val error: LiveData<Unit> get() = _error

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        _error.postValue(Unit)
        Log.d("INFO", e.message!!)
    }

    fun getPortfolioStatements() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val userPortfolios = database.getUserPortfolioDAO().fetchAll()

            val ccOverviewItems = coinCapService.getAssetOverview()

            val statements = mutableListOf<PortfolioStatement>()

            val usdPortfolio= userPortfolios.find { portfolio -> portfolio.symbol.equals("USD") }

            usdPortfolio?.let { portfolio ->
                statements.add(PortfolioStatement(portfolio.symbol, portfolio.volume, "", ""))
            }

            userPortfolios.forEach { portfolio ->

                val specificCcData = ccOverviewItems.data.find {ccO -> ccO.symbol.equals(portfolio.symbol)}

                if (specificCcData !== null) {

                    statements.add(
                        PortfolioStatement(
                            portfolio.symbol,
                            portfolio.volume,
                            specificCcData.priceUsd.toString(),
                            "${portfolio.volume.toDouble() * specificCcData.priceUsd!!.toDouble()}"
                        )
                    )
                }
            }
            _portfolioStatements.postValue(statements)
        }
    }
}