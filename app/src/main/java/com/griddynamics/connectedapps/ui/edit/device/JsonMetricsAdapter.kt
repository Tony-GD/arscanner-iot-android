package com.griddynamics.connectedapps.ui.edit.device

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.databinding.JsonFormItemLayoutBinding
import com.griddynamics.connectedapps.model.metrics.JsonMetricViewState

class JsonMetricsAdapter(private val metrics: MutableList<JsonMetricViewState>) :
    RecyclerView.Adapter<JsonMetricsAdapter.JsonMetricsViewHolder>() {

    class JsonMetricsViewHolder(val binding: JsonFormItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JsonMetricsViewHolder {
        return JsonMetricsViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.json_form_item_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return metrics.size
    }

    override fun onBindViewHolder(holder: JsonMetricsViewHolder, position: Int) {
        val metric = metrics[position]
        holder.binding.viewState = metric
        val context = holder.binding.root.context
        val metricTypes = context.resources.getStringArray(R.array.metric_types)
        if (metric.measurement.get()?.isEmpty() == true) {
            metric.measurement.set(metricTypes[0])
        }
        holder.binding.spJsonMetricType.apply {
            val arrayAdapter = ArrayAdapter(
                context, R.layout.spinner_item_layout, metricTypes
            )
            arrayAdapter.setDropDownViewResource(R.layout.spinner_item_layout)
            adapter = arrayAdapter
            setSelection(metricTypes.indexOf(metric.measurement.get()))
            onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        //NOP
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        metric.measurement.set(metricTypes[position])
                    }

                }
        }
        holder.binding.btnJsonAdd.apply {
            visibility =
                if (position == itemCount - 1) View.VISIBLE else View.GONE
            setOnClickListener {
                it.visibility = View.GONE
                metrics.add(JsonMetricViewState())
                notifyDataSetChanged()
            }
        }
    }
}