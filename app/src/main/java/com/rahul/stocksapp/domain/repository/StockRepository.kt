package com.rahul.stocksapp.domain.repository

import com.rahul.stocksapp.domain.model.CompanyInfo
import com.rahul.stocksapp.domain.model.CompanyListing
import com.rahul.stocksapp.domain.model.IntradayInfo
import com.rahul.stocksapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>

    suspend fun getIntradayInfo(
        symbol: String
    ): Resource<List<IntradayInfo>>

    suspend fun getCompanyInfo(
        symbol: String
    ): Resource<CompanyInfo>
}

