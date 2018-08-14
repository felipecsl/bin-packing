package com.felipecsl

import java.util.*

/**
 * Collection of bin packing algorithms optimized for evenly filling the available bins (balanced).
 * Offline means the full list of items is known beforehand.
 */
class OfflineBalancedBinPacking {
  /** https://www.sciencedirect.com/science/article/pii/0885064X85900226?via%3Dihub */
  fun firstFitDecreasing(
      items: List<Item>,
      maxBinSize: Int,
      maxBins: Int
  ): List<Bin> {
    if (items.sumBy(Item::weight) > (maxBins.toLong() * maxBinSize.toLong())) {
      throw IllegalArgumentException("Can't fit all items in the provided bins.")
    }
    if (items.any { it.weight > maxBinSize }) {
      throw IllegalArgumentException("All items' weights must be under `maxBinSize`.")
    }
    if (maxBins <= 0) return listOf()
    val bins = mutableListOf<Bin>()
    val sortedItems = items.toQueueDesc()
    var maxWeightSoFar = 0
    var i = 0
    val newBin = {
      if (bins.size < maxBins) {
        Bin(maxBinSize).also { bins.add(it) }
      } else {
        throw IllegalArgumentException("Not enough space found to store all items.")
      }
    }
    while (sortedItems.isNotEmpty()) {
      val nextItemWeight = sortedItems.peek().weight
      val currentBin = findBinForItem(bins, i++ % maxBins, nextItemWeight, maxWeightSoFar, maxBins)
          ?: newBin()
      while (currentBin.isEmpty()
          || (bins.all { it.weight == maxWeightSoFar }
              && currentBin.remainingWeight >= nextItemWeight
              && sortedItems.isNotEmpty()
              && bins.size == maxBins)
          || (currentBin.weight < maxWeightSoFar
              && sortedItems.isNotEmpty()
              && currentBin.remainingWeight >= nextItemWeight)
      ) {
        currentBin.add(sortedItems.remove())
      }
      maxWeightSoFar = Math.max(maxWeightSoFar, currentBin.weight)
    }
    return bins
  }

  /** Fills bins more evenly than [firstFitDecreasing] but tends to use more bins. */
  fun bestFitDecreasing(
      items: List<Item>,
      maxBinSize: Int,
      maxBins: Int
  ): List<Bin> {
    if (maxBins <= 0) return listOf()
    val sortedItems = items.toQueueDesc()
    val bins = mutableListOf<Bin>()
    var maxWeightSoFar = 0
    while (sortedItems.isNotEmpty()) {
      if (bins.size < maxBins) bins.add(Bin(maxBinSize))
      val currentBin = bins.minBy { it.weight }!!
      var currentBinWeight = currentBin.weight
      while (sortedItems.isNotEmpty() &&
          (currentBin.isEmpty() || currentBinWeight < maxWeightSoFar)) {
        val nextItem = if (maxWeightSoFar > 0
            && (currentBinWeight + sortedItems.peek().weight > maxWeightSoFar)) {
          sortedItems.removeLast()
        } else {
          sortedItems.removeFirst()
        }
        currentBinWeight += nextItem.weight
        currentBin.add(nextItem)
      }
      maxWeightSoFar = Math.max(maxWeightSoFar, currentBinWeight)
    }
    return bins.filter { it.isNotEmpty() }
  }

  private fun List<Item>.toQueueDesc() = ArrayDeque(sortedByDescending(Item::weight))

  private fun findBinForItem(
      bins: List<Bin>,
      startIndex: Int,
      itemWeight: Int,
      maxWeightSoFar: Int,
      maxBins: Int
  ): Bin? {
    return if (bins.all { it.weight == maxWeightSoFar } && bins.size < maxBins) {
      null
    } else {
      // start searching at startIndex and wrap around
      val search = bins.drop(startIndex + 1) + bins.take(startIndex)
      recurseFindBinForItem(search, itemWeight)
    }
  }

  private fun recurseFindBinForItem(bins: List<Bin>, itemWeight: Int): Bin? {
    if (bins.isEmpty()) return null
    return if (bins[0].remainingWeight >= itemWeight) {
      bins[0]
    } else {
      recurseFindBinForItem(bins.drop(1), itemWeight)
    }
  }
}
