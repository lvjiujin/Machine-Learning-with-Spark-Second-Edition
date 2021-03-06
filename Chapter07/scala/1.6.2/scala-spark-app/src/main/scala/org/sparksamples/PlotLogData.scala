package org.sparksamples

//import org.sparksamples.Util

//import _root_.scalax.chart.ChartFactories
import java.awt.Font

import org.jfree.chart.axis.CategoryLabelPositions

import scala.collection.immutable.ListMap
import scalax.chart.module.ChartFactories

/**
  * @author Rajdeep Dua
  * Date 5/7/16.
  */

object PlotLogData {

  def main(args: Array[String]) {
    val records = Util.getRecords()._1
    val records_x = records.map(r => Math.log(r(r.length -1).toDouble))
    var records_int = new Array[Int](records_x.collect().length)
    print(records_x.first())
    val records_collect = records_x.collect()

    for (i <- 0 until records_collect.length){
      records_int(i) = records_collect(i).toInt
    }
    val min_1 = records_int.min
    val max_1 = records_int.max

    val min = min_1.toFloat
    val max = max_1.toFloat
    val bins = 10
    val step = (max/bins).toFloat

    var mx = Map(0.0.toString -> 0)
    for (i <- step until (max + step) by step) {
      mx += (i.toString -> 0);
    }

    for(i <- 0 until records_collect.length){
      for (j <- 0.0 until (max + step) by step) {
        if(records_int(i) >= (j) && records_int(i) < (j + step)){
          mx = mx + (j.toString -> (mx(j.toString) + 1))
        }
      }
    }
    val mx_sorted = ListMap(mx.toSeq.sortBy(_._1.toFloat):_*)
    val ds = new org.jfree.data.category.DefaultCategoryDataset
    var i = 0
    mx_sorted.foreach{ case (k,v) => ds.addValue(v,"", k)}

    val chart = ChartFactories.BarChart(ds)
    val font = new Font("Dialog", Font.PLAIN,4);

    chart.peer.getCategoryPlot.getDomainAxis().
      setCategoryLabelPositions(CategoryLabelPositions.UP_90);
    chart.peer.getCategoryPlot.getDomainAxis.setLabelFont(font)
    chart.show()
    Util.sc.stop()
  }
}
