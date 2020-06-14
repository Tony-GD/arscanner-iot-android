package com.griddynamics.connectedapps.ui.map.filter

import androidx.databinding.ObservableBoolean

class FilterViewState {
    val isAll = ObservableBoolean()
    val isCo2 = ObservableBoolean()
    val isTemp = ObservableBoolean()
    val isHumidity = ObservableBoolean()
    val isPm25 = ObservableBoolean()
    val isPm1 = ObservableBoolean()
    val isPm10 = ObservableBoolean()
}