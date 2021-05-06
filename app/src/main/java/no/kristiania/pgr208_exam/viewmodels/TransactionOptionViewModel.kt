package no.kristiania.pgr208_exam.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.kristiania.pgr208_exam.data.API
import no.kristiania.pgr208_exam.data.CoinCapService
import no.kristiania.pgr208_exam.data.domain.SpecificCcData
import no.kristiania.pgr208_exam.datastorage.db.DataBase
import no.kristiania.pgr208_exam.datastorage.entities.UserPortfolio

class TransactionOptionViewModel(application: Application) : AndroidViewModel(application) {

    private val coinCapService: CoinCapService = API.COIN_CAP_SERVICE

    private val _userPortfolio = MutableLiveData<UserPortfolio>()
    val userPortfolio: LiveData<UserPortfolio> get() = _userPortfolio

    private val _currency = MutableLiveData<SpecificCcData>()
    val currency: LiveData<SpecificCcData> get() = _currency

    private val _userUsd = MutableLiveData<UserPortfolio>()
    val userUsd: LiveData<UserPortfolio> get() = _userUsd

    private val _error = MutableLiveData<Unit>()
    val error: LiveData<Unit> get() = _error

    private val _chart = MutableLiveData<Cartesian>()
    val chart: LiveData<Cartesian> get() = _chart

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        _error.postValue(Unit)
        Log.d("INFO", e.message!!)
    }

    fun getPortfolio(symbol: String) {
        viewModelScope.launch {
            val portfolio =
                DataBase.getDatabase(getApplication()).getUserPortfolioDAO().fetch(symbol)
            if (portfolio !== null) {
                _userPortfolio.postValue(portfolio)
            } else {
                _userPortfolio.postValue(UserPortfolio(symbol, "0"))
            }
        }
    }

    fun getCurrency(id: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val currency = coinCapService.getCurrency(id).data
            _currency.postValue(currency)
        }
    }

    fun getUserUsd() {
        viewModelScope.launch {
            val userUsd = DataBase.getDatabase(getApplication()).getUserPortfolioDAO().fetchUsd()
            _userUsd.postValue(userUsd)
        }
    }

    fun getChart(currencyId: String) {
        viewModelScope.launch {
            val intervals = coinCapService.getInterval(currencyId)
            var series = mutableListOf<DataEntry>()

            intervals.data.forEach{ history ->
                series.add(ValueDataEntry(history.date, history.priceUsd?.toFloat()))
            }

            val cartesian = AnyChart.line()
            val xAxis = cartesian.xAxis(0)
            val yAxis = cartesian.yAxis(0)
            val tooltip = cartesian.tooltip();

            xAxis.labels().format("{%Value}{dateTimeFormat:MM-dd}")

            yAxis.labels().format("\${%Value}{scale:(1000)(1000)|(k)(m)}")

            tooltip.titleFormat("{%x}{dateTimeFormat:EEEE MMMM dd yyyy HH:mm:ss}")
            tooltip.format("Price: \${%value}{decimalsCount:6}")

            cartesian.line(series)

            _chart.postValue(cartesian)

        }
    }

}