package com.griddynamics.connectedapps.ui.history.week

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.data.Set
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.griddynamics.connectedapps.R
import com.griddynamics.connectedapps.model.metrics.MetricsResponse
import kotlinx.android.synthetic.main.week_history_chart_item.view.*

class WeekHistoryItemsAdapter(private val metrics: List<MetricsResponse>) :
    RecyclerView.Adapter<WeekHistoryItemsAdapter.WeekHistoryItemViewHolder>() {
    class WeekHistoryItemViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekHistoryItemViewHolder {
        return WeekHistoryItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.week_history_chart_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return metrics.size
    }

    override fun onBindViewHolder(holder: WeekHistoryItemViewHolder, position: Int) {
        val anyChartView = holder.view.any_chart_view
        anyChartView.setProgressBar(holder.view.progress_bar_week)

        val cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10, 20, 5, 20.0)

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
            .yLabel(true)
        // TODO ystroke

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Trend of Sales of the Most Popular Products of ACME Corp.");

        cartesian.yAxis(0).title("Number of Bottles Sold (thousands)");
        cartesian.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0);

        val seriesData = ArrayList<DataEntry>();
        seriesData.add(CustomDataEntry("1986", 3.6, 2.3, 2.8));
        seriesData.add(CustomDataEntry("1987", 7.1, 4.0, 4.1));
        seriesData.add(CustomDataEntry("1988", 8.5, 6.2, 5.1));
        seriesData.add(CustomDataEntry("1989", 9.2, 11.8, 6.5));
        seriesData.add(CustomDataEntry("1990", 10.1, 13.0, 12.5));
        seriesData.add(CustomDataEntry("1991", 11.6, 13.9, 18.0));
        seriesData.add(CustomDataEntry("1992", 16.4, 18.0, 21.0));
        seriesData.add(CustomDataEntry("1993", 18.0, 23.3, 20.3));
        seriesData.add(CustomDataEntry("1994", 13.2, 24.7, 19.2));
        seriesData.add(CustomDataEntry("1995", 12.0, 18.0, 14.4));
        seriesData.add(CustomDataEntry("1996", 3.2, 15.1, 9.2));
        seriesData.add(CustomDataEntry("1997", 4.1, 11.3, 5.9));
        seriesData.add(CustomDataEntry("1998", 6.3, 14.2, 5.2));
        seriesData.add(CustomDataEntry("1999", 9.4, 13.7, 4.7));
        seriesData.add(CustomDataEntry("2000", 11.5, 9.9, 4.2));
        seriesData.add(CustomDataEntry("2001", 13.5, 12.1, 1.2));
        seriesData.add(CustomDataEntry("2002", 14.8, 13.5, 5.4));
        seriesData.add(CustomDataEntry("2003", 16.6, 15.1, 6.3));
        seriesData.add(CustomDataEntry("2004", 18.1, 17.9, 8.9));
        seriesData.add(CustomDataEntry("2005", 17.0, 18.9, 10.1));
        seriesData.add(CustomDataEntry("2006", 16.6, 20.3, 11.5));
        seriesData.add(CustomDataEntry("2007", 14.1, 20.7, 12.2));
        seriesData.add(CustomDataEntry("2008", 15.7, 21.6, 10));
        seriesData.add(CustomDataEntry("2009", 12.0, 22.5, 8.9));

        val set = Set.instantiate();
        set.data(seriesData);
        val series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        val series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
        val series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");

        val series1 = cartesian.line(series1Mapping);
        series1.name("Brandy");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0);
        series1.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0);

        val series2 = cartesian.line(series2Mapping);
        series2.name("Whiskey");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0);
        series2.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0);

        val series3 = cartesian.line(series3Mapping)
        series3.name("Tequila");
        series3.hovered().markers().enabled(true);
        series3.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0);
        series3.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13.0);
        cartesian.legend().padding(0, 0, 10, 0.0);

        anyChartView.setChart(cartesian);
    }
}

private class CustomDataEntry(x: String, value: Number, value2: Number, value3: Number) :
    ValueDataEntry(x, value) {

    init {
        setValue("value2", value2);
        setValue("value3", value3);
    }


}