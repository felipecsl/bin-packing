package com.felipecsl

import java.util.*

class BinPacking {
  fun firstFitDecreasing(
      items: List<WeightedItem>,
      totalBins: Int
  ): List<List<WeightedItem>> {
    if (totalBins <= 0) return listOf()
    val itemGroups = (0 until totalBins)
        .map { mutableListOf<WeightedItem>() }
        .toMutableList()
    val sortedItems: Queue<WeightedItem> =
        ArrayDeque(items.sortedByDescending(WeightedItem::weight))
    var maxWeightSoFar = 0
    var i = 0
    while (sortedItems.isNotEmpty()) {
      val currentProcessor = itemGroups[i++ % itemGroups.size]
      var currentProcessorWeight = currentProcessor.totalWeight()
      while (currentProcessor.isEmpty()
          || (currentProcessorWeight < maxWeightSoFar && sortedItems.isNotEmpty())) {
        val nextTask = sortedItems.remove()
        currentProcessorWeight += nextTask.weight
        currentProcessor.add(nextTask)
      }
      maxWeightSoFar = Math.max(maxWeightSoFar, currentProcessorWeight)
    }
    return itemGroups
  }

  fun bestFitDecreasing(
      items: List<WeightedItem>,
      totalBins: Int
  ): List<List<WeightedItem>> {
    if (totalBins <= 0) return listOf()
    val sortedTasks = ArrayDeque(items.sortedByDescending(WeightedItem::weight))
    val itemGroups = (0 until totalBins)
        .map { mutableListOf<WeightedItem>() }
        .toMutableList()
    var maxWeightSoFar = 0
    while (sortedTasks.isNotEmpty()) {
      val currentProcessor = itemGroups.minBy { it.totalWeight() }!!
      var currentProcessorWeight = currentProcessor.totalWeight()
      while (sortedTasks.isNotEmpty() &&
          (currentProcessor.isEmpty() || currentProcessorWeight < maxWeightSoFar)) {
        val nextTask = if (maxWeightSoFar > 0
            && (currentProcessorWeight + sortedTasks.peek().weight > maxWeightSoFar)) {
          sortedTasks.removeLast()
        } else {
          sortedTasks.removeFirst()
        }
        currentProcessorWeight += nextTask.weight
        currentProcessor.add(nextTask)
      }
      maxWeightSoFar = Math.max(maxWeightSoFar, currentProcessorWeight)
    }
    return itemGroups
  }

  private fun List<WeightedItem>.totalWeight() = sumBy(WeightedItem::weight)
}