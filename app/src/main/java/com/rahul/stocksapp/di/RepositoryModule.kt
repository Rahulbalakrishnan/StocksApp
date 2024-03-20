package com.rahul.stocksapp.di

import com.rahul.stocksapp.data.csv.CSVParser
import com.rahul.stocksapp.data.csv.CompanyListingParser
import com.rahul.stocksapp.data.csv.IntradayInfoParser
import com.rahul.stocksapp.data.repository.StockRepositoryImpl
import com.rahul.stocksapp.domain.model.CompanyListing
import com.rahul.stocksapp.domain.model.IntradayInfo
import com.rahul.stocksapp.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsCompanyListingsParser(
        companyListingParser: CompanyListingParser
    ):CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindsIntradayInfoParser(
        intradayInfoParser: IntradayInfoParser
    ):CSVParser<IntradayInfo>


    @Binds
    @Singleton
    abstract fun bindsStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ):StockRepository


}