package com.rahul.stocksapp.data.repository

import com.rahul.stocksapp.data.csv.CSVParser
import com.rahul.stocksapp.data.local.StockDatabase
import com.rahul.stocksapp.data.mapper.toCompanyInfo
import com.rahul.stocksapp.data.mapper.toCompanyListing
import com.rahul.stocksapp.data.mapper.toCompanyListingEntity
import com.rahul.stocksapp.data.remote.StockApi
import com.rahul.stocksapp.domain.model.CompanyInfo
import com.rahul.stocksapp.domain.model.CompanyListing
import com.rahul.stocksapp.domain.model.IntradayInfo
import com.rahul.stocksapp.domain.repository.StockRepository
import com.rahul.stocksapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val db: StockDatabase,
    private val companyListingParser: CSVParser<CompanyListing>,
    private val intradayInfoParser: CSVParser<IntradayInfo>
) : StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean, query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading())
            val localListings = dao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListings.map {
                    it.toCompanyListing()
                }
            ))

            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListing = try {
                val response = api.getListings()
                companyListingParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data!"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Not connected to the Servers!"))
                null
            }

            remoteListing?.let { listings ->
                dao.clearCompanyListing()
                dao.insertCompanyListings(listings.map {
                    it.toCompanyListingEntity()
                })
                emit(
                    Resource.Success(
                        data = dao
                            .searchCompanyListing("")
                            .map { it.toCompanyListing() }
                    )
                )
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {

        return try {
            val response = api.getIntraDayInfo(symbol)
            val results = intradayInfoParser.parse(response.byteStream())
            Resource.Success(results)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday info."
            )

        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't connect to the server."
            )
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val result = api.getCompanyInfo(symbol)
            Resource.Success(result.toCompanyInfo())
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load company info."
            )

        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't connect to the server."
            )
        }
    }
}