package com.felipecsl

import java.util.*

/** Collection of bin packing algorithms where the full list of items is known beforehand */
class OfflineBinPacking {
  /** https://www.sciencedirect.com/science/article/pii/0885064X85900226?via%3Dihub */
  fun firstFitDecreasing(
      items: List<Item>,
      maxBins: Int = Int.MAX_VALUE
  ): List<List<Item>> {
    if (maxBins <= 0) return listOf()
    val bins = mutableListOf<MutableList<Item>>()
    val sortedItems: Queue<Item> = ArrayDeque(items.sortedByDescending(Item::weight))
    var maxWeightSoFar = 0
    var i = 0
    while (sortedItems.isNotEmpty()) {
      val nextIndex = i++ % maxBins
      val currentProcessor = if (nextIndex < bins.size) {
        bins[nextIndex]
      } else {
        bins.add(mutableListOf())
        bins[nextIndex]
      }
      var currentProcessorWeight = currentProcessor.totalWeight()
      while (currentProcessor.isEmpty()
          || (currentProcessorWeight < maxWeightSoFar && sortedItems.isNotEmpty())) {
        val nextTask = sortedItems.remove()
        currentProcessorWeight += nextTask.weight
        currentProcessor.add(nextTask)
      }
      maxWeightSoFar = Math.max(maxWeightSoFar, currentProcessorWeight)
    }
    return bins.filter { it.isNotEmpty() }
  }

  fun bestFitDecreasing(
      items: List<Item>,
      maxBins: Int = Int.MAX_VALUE
  ): List<List<Item>> {
    if (maxBins <= 0) return listOf()
    val sortedItems = ArrayDeque(items.sortedByDescending(Item::weight))
    val bins = mutableListOf<MutableList<Item>>()
    var maxWeightSoFar = 0
    while (sortedItems.isNotEmpty()) {
      if (bins.size < maxBins) bins.add(mutableListOf())
      val currentProcessor = bins.minBy { it.totalWeight() }!!
      var currentProcessorWeight = currentProcessor.totalWeight()
      while (sortedItems.isNotEmpty() &&
          (currentProcessor.isEmpty() || currentProcessorWeight < maxWeightSoFar)) {
        val nextTask = if (maxWeightSoFar > 0
            && (currentProcessorWeight + sortedItems.peek().weight > maxWeightSoFar)) {
          sortedItems.removeLast()
        } else {
          sortedItems.removeFirst()
        }
        currentProcessorWeight += nextTask.weight
        currentProcessor.add(nextTask)
      }
      maxWeightSoFar = Math.max(maxWeightSoFar, currentProcessorWeight)
    }
    return bins.filter { it.isNotEmpty() }
  }

  private fun List<Item>.totalWeight() = sumBy(Item::weight)
}