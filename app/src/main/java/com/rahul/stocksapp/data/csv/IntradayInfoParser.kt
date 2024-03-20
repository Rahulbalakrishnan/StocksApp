package com.rahul.stocksapp.data.csv

import com.opencsv.CSVReader
import com.rahul.stocksapp.data.mapper.toIntradayInfo
import com.rahul.stocksapp.data.remote.dto.IntradayInfoDto
import com.rahul.stocksapp.domain.model.IntradayInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntradayInfoParser @Inject constructor() : CSVParser<IntradayInfo> {
    override suspend fun parse(stream: InputStream): List<IntradayInfo> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO) {
            csvReader.readAll().drop(1).mapNotNull { line ->

                val timestamp = line.getOrNull(0) ?: return@mapNotNull null
                val close = line.getOrNull(4) ?: return@mapNotNull null
                val dto = IntradayInfoDto(timestamp, close.toDouble())
                dto.toIntradayInfo()

            }
                .filter {
                    it.date.dayOfMonth == LocalDate.now().minusDays(1).dayOfMonth
                }
                .sortedBy {
                    it.date.hour
                }
                .also {
                    csvReader.close()
                }
        }
    }
}