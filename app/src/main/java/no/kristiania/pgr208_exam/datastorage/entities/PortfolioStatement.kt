package no.kristiania.pgr208_exam.datastorage.entities

data class PortfolioStatement(
    val symbol: String,
    val volume: String,
    val recentRate: String,
    val totalValue: String
)