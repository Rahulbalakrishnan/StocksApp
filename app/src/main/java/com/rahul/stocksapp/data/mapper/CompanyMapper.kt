package com.rahul.stocksapp.data.mapper

import com.rahul.stocksapp.data.local.CompanyListingEntity
import com.rahul.stocksapp.data.remote.dto.CompanyInfoDto
import com.rahul.stocksapp.domain.model.CompanyInfo
import com.rahul.stocksapp.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing {
    return CompanyListing(
        name = name, symbol = symbol, exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        name = name, symbol = symbol, exchange = exchange
    )
}

fun CompanyInfoDto.toCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        symbol = symbol ?: "",
        description = description ?: "",
        name = name ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )
}