package com.felipecsl

import com.github.javafaker.Faker
import org.junit.Test

class OfflineBinPackingTest {
  @Test fun `manual test`() {
    val balancer = OfflineBinPacking()
    val faker = Faker()
    val maxTasks = 100
    val maxWeight = 1000
    val items = (0 until maxTasks).map {
      WeightedItem(faker.name().firstName(), (Math.random() * maxWeight).toInt())
    }
    println("Lightest item: ${items.minBy(WeightedItem::weight)}")
    println("Heaviest item: ${items.maxBy(WeightedItem::weight)}")
    run("firstFitDecreasing", balancer::firstFitDecreasing, items)
    run("bestFitDecreasing", balancer::bestFitDecreasing, items)
  }

  private fun run(
      label: String,
      func: (List<WeightedItem>, Int) -> List<List<WeightedItem>>,
      items: List<WeightedItem>,
      totalProcessors: Int = 10
  ) {
    println("------------------\n$label")
    val result = func(items, totalProcessors)
    printGroups(result)
    val min = result.map { it.sumBy(WeightedItem::weight) }.min()!!
    val max = result.map { it.sumBy(WeightedItem::weight) }.max()!!
    println("Lightest bin=$min, Heaviest bin=$max")
  }

  private fun printGroups(itemGroups: List<List<WeightedItem>>) {
    itemGroups.sortedByDescending { it.sumBy(WeightedItem::weight) }.forEach {
      val totalWeight = it.sumBy(WeightedItem::weight)
      val totalTasks = it.size
      val weightPerTask = totalWeight / totalTasks
      println("Total weight=$totalWeight, total items=$totalTasks, weight/item=$weightPerTask")
    }
  }
}
