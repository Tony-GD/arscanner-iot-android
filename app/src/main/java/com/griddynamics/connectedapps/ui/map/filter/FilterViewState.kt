package com.griddynamics.connectedapps.ui.map.filter

import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean

class FilterViewState {
    private val properties = mutableListOf<ObservableBoolean>()
    private lateinit var propertyChangedListener: Observable.OnPropertyChangedCallback

    init {
        propertyChangedListener = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                for (property in properties) {
                    property.removeOnPropertyChangedCallback(propertyChangedListener)
                    if (sender == property) {
                        property.set(true)
                    } else {
                        property.set(false)
                    }
                    property.addOnPropertyChangedCallback(propertyChangedListener)
                }
            }

        }
    }

    val isAll = ObservableBoolean(true).apply {
        properties.add(this)
        this.addOnPropertyChangedCallback(propertyChangedListener)
    }
    val isCo2 = ObservableBoolean().apply {
        properties.add(this)
        this.addOnPropertyChangedCallback(propertyChangedListener)
    }
    val isTemp = ObservableBoolean().apply {
        properties.add(this)
        this.addOnPropertyChangedCallback(propertyChangedListener)
    }
    val isHumidity = ObservableBoolean().apply {
        properties.add(this)
        this.addOnPropertyChangedCallback(propertyChangedListener)
    }
    val isPm25 = ObservableBoolean().apply {
        properties.add(this)
        this.addOnPropertyChangedCallback(propertyChangedListener)
    }
    val isPm1 = ObservableBoolean().apply {
        properties.add(this)
        this.addOnPropertyChangedCallback(propertyChangedListener)
    }
    val isPm10 = ObservableBoolean().apply {
        properties.add(this)
        this.addOnPropertyChangedCallback(propertyChangedListener)
    }
}