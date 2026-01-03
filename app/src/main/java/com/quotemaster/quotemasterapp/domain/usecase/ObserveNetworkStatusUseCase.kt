package com.quotemaster.quotemasterapp.domain.usecase

import com.quotemaster.quotemasterapp.data.network.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveNetworkStatusUseCase @Inject constructor(
    private val networkMonitor: NetworkMonitor
) {
    operator fun invoke(): Flow<Boolean> {
        return networkMonitor.networkStatus
    }
}