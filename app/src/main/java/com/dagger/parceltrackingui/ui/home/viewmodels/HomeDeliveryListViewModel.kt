package com.dagger.parceltrackingui.ui.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeDeliveryListViewModel
@Inject
constructor() : ViewModel() {

    private val _mutableHomeDeliveryListDataState: MutableStateFlow<HomeDeliveryListState> = MutableStateFlow(HomeDeliveryListState.HomeDeliveryInitial)
    val homeDeliveryListDataState: StateFlow<HomeDeliveryListState> = _mutableHomeDeliveryListDataState.asStateFlow()
    private lateinit var homeDeliveryListJob: Job

    fun loadHomeDeliveryList(category: String) {
        if (this::homeDeliveryListJob.isInitialized)
            homeDeliveryListJob.cancel()
        homeDeliveryListJob = viewModelScope.launch {
            _mutableHomeDeliveryListDataState.value = HomeDeliveryListState.HomeDeliveryLoading
            delay(3000)
            _mutableHomeDeliveryListDataState.value = HomeDeliveryListState.HomeDeliveryLoaded(getHomeDeliveryListByCategory(category))
        }

    }

    private fun getHomeDeliveryListByCategory(category: String): List<Pair<String, String>> {
        return when (category) {
            "Current" -> {
                listOf(
                    Pair("Brooklyn", "New York"),
                    Pair("California", "San Francisco")
                )
            }
            "All" -> {
                listOf(
                    Pair("Texas", "Chicago"),
                    Pair("California", "San Francisco"),
                    Pair("Brooklyn", "New York"),
                )
            }
            "History" -> {
                listOf(
                    Pair("Las Colinas", "Montgomery"),
                    Pair("Las Venturas", "San Fierro"),
                    Pair("Capri", "Amalfi Coast"),
                )
            }
            else -> {
                listOf(
                    Pair("Las Colinas", "Montgomery"),
                    Pair("Las Venturas", "San Fierro"),
                    Pair("Capri", "Amalfi Coast"),
                )
            }
        }
    }

}

sealed class HomeDeliveryListState {

    object HomeDeliveryInitial : HomeDeliveryListState()
    object HomeDeliveryLoading : HomeDeliveryListState()
    data class HomeDeliveryLoaded(val listData: List<Pair<String, String>>): HomeDeliveryListState()

}