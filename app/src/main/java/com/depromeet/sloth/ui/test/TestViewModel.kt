package com.depromeet.sloth.ui.test

import androidx.lifecycle.viewModelScope
import com.depromeet.sloth.data.network.health.HealthRepository
import com.depromeet.sloth.data.network.health.HealthResponse
import com.depromeet.sloth.data.network.health.HealthState
import com.depromeet.sloth.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlin.coroutines.CoroutineContext

class TestViewModel : BaseViewModel() {
    private val repository = HealthRepository()

    /**
     * Activity나 Fragment단에서 작업의 결과값을 리턴하여 State 분기를 편하게 처리할 수 있음
     *
     * @return Result<HealthResponse>
     */
    suspend fun processHealthWork(
        context: CoroutineContext = Dispatchers.IO,
        start: CoroutineStart = CoroutineStart.DEFAULT,
    ): HealthState<HealthResponse> {
        return viewModelScope.async(context = context, start = start) {
            repository.getHealth()
        }.await()
    }
}