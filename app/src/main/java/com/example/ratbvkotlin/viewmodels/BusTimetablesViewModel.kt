package com.example.ratbvkotlin.viewmodels

import androidx.lifecycle.*
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.models.BusTimetableModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class TimetableTypes {
    WeekDays, Saturday, Sunday
}

class BusTimetablesViewModel(private val _repository: BusRepository,
                             private val _scheduleLink: String,
                             private val _busStationId: Long,
                             val busStationName: String)
    : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    private val _timeOfWeek = MutableStateFlow(TimetableTypes.WeekDays)

    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    val timeOfWeek: StateFlow<TimetableTypes> = _timeOfWeek

    /**
     * Observes the bus station data from the repository as a Flow
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val busTimetables: StateFlow<List<BusTimetableItemViewModel>> =
        timeOfWeek.flatMapLatest { timeOfWeek ->
            _repository.observeBusTimetables(_busStationId)
                .map { busTimetableModels ->
                    busTimetableModels
                        .filter { busTimetableModel ->
                            busTimetableModel.timeOfWeek == timeOfWeek.name
                        }
                        .map { BusTimetableModel ->
                            BusTimetableItemViewModel(BusTimetableModel)
                        }
                }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyList()
        )

    // Sets the lastUpdated value based on the first item of the list since all will have the same value
    @OptIn(ExperimentalCoroutinesApi::class)
    val lastUpdated: StateFlow<String> = busTimetables.mapLatest { busTimetables ->
        busTimetables.firstOrNull()?.lastUpdateDate ?: "Never"
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = "Never"
    )

    init {
        viewModelScope.launch {
            refreshBusTimetables(isForcedRefresh = false)
        }
    }

    /**
     * Forces a refresh of the data in the database by doing a web fetch
     */
    suspend fun refreshBusTimetables(isForcedRefresh: Boolean) {

        _isRefreshing.value = true

        _repository.refreshBusTimetables(
            _busStationId,
            _scheduleLink,
            isForcedRefresh)

        _isRefreshing.value = false
    }

    /**
     * Updates the bus type based on the bottom tab being viewed
     */
    fun updateViewedTimeOfWeek(timetableType: TimetableTypes) {
        _timeOfWeek.value = timetableType
    }

    /**
     * [ViewModel] for a specific [BusTimetableItemComponent], which contains the item's [busTimetable]
     */
     class BusTimetableItemViewModel (private val busTimetable: BusTimetableModel)
        : ViewModel() {

            val hour: String = busTimetable.hour
            val minutes: String = busTimetable.minutes
            val lastUpdateDate: String = busTimetable.lastUpdateDate
        }
}