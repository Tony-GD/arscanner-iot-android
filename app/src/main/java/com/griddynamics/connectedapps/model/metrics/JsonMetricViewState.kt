package com.griddynamics.connectedapps.model.metrics

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

class JsonMetricViewState {
    var isPublic =  ObservableBoolean(true)
    var name =  ObservableField<String>("")
    var measurement = ObservableField<String>("")
    override fun toString(): String {
        return "JsonMetricViewState(isPublic=${isPublic.get()}, name=${name.get()}, measurement=${measurement.get()})"
    }

}