package com.example.ratbvkotlin.viewmodels

import androidx.lifecycle.*
import com.example.ratbvkotlin.data.BusRepository
import com.example.ratbvkotlin.data.models.BusTimetableModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

enum class TimetableTypes {
    WeekDays, Saturday, Sunday
}

class BusTimetablesViewModel(private val _repository: BusRepository,
                             private val _scheduleLink: String,
                             private val _busStationId: Int,
                             val busStationName: String)
    : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    private val _timeOfWeek = MutableStateFlow(TimetableTypes.WeekDays)
    private val _busTimetables = MutableStateFlow<List<BusTimetableItemViewModel>>(emptyList())

    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    val timeOfWeek: StateFlow<TimetableTypes> = _timeOfWeek
    val busTimetables: StateFlow<List<BusTimetableItemViewModel>> = _busTimetables

    // Sets the lastUpdated value based on the first item of the list since all will have the same value
    @OptIn(ExperimentalCoroutinesApi::class)
    val lastUpdated: StateFlow<String> = _busTimetables.mapLatest { busTimetables ->
        busTimetables.firstOrNull()?.lastUpdateDate ?: "Never"
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = "Never"
    )

    /**
     * Gets the bus stations data from the repository as Flow
     */
    suspend fun getBusTimetables(timetableType: TimetableTypes,
                                 isForcedRefresh: Boolean = false) {

        _isRefreshing.value = true

        _timeOfWeek.value = timetableType

        _busTimetables.value = _repository.getBusTimetables(_scheduleLink,
            _busStationId,
            isForcedRefresh)
            .filter { busTimetableModel -> busTimetableModel.timeOfWeek == timetableType.name }
            .map { busTimetableModel -> BusTimetableItemViewModel(busTimetableModel) }

        _isRefreshing.value = false
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