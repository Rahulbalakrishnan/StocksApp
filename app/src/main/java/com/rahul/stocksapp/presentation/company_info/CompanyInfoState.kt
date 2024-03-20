package com.rahul.stocksapp.presentation.company_info

import com.rahul.stocksapp.domain.model.CompanyInfo
import com.rahul.stocksapp.domain.model.IntradayInfo

data class CompanyInfoState(
    val stockInfo: List<IntradayInfo> = emptyList(),
    val company: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
