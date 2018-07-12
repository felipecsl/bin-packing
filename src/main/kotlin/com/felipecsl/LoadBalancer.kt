package com.felipecsl

import java.util.*

class LoadBalancer {
  fun balance1(tasks: List<WeightedTask>, totalProcessors: Int): List<List<WeightedTask>> {
    val sortedTasks: Queue<WeightedTask> = ArrayDeque(tasks.sortedByDescending(WeightedTask::weight))
    val groups = (0 until totalProcessors)
        .map { mutableListOf<WeightedTask>() }
        .toMutableList()
    if (groups.isEmpty()) {
      return listOf()
    } else {
      var maxWeightSoFar = 0
      var i = 0
      while (sortedTasks.isNotEmpty()) {
        val currentProcessor = groups[i++ % groups.size]
        var currentProcessorWeight = currentProcessor.sumBy(WeightedTask::weight)
        while (currentProcessor.isEmpty()
            || (currentProcessorWeight < maxWeightSoFar && sortedTasks.isNotEmpty())) {
          val nextTask = sortedTasks.remove()
          currentProcessorWeight += nextTask.weight
          currentProcessor.add(nextTask)
        }
        maxWeightSoFar = Math.max(maxWeightSoFar, currentProcessorWeight)
      }
    }
    return groups
  }

  fun balance2(tasks: List<WeightedTask>, totalProcessors: Int): List<List<WeightedTask>> {
    val sortedTasks = ArrayDeque(tasks.sortedByDescending(WeightedTask::weight))
    val groups = (0 until totalProcessors)
        .map { mutableListOf<WeightedTask>() }
        .toMutableList()
    if (groups.isEmpty()) {
      return listOf()
    } else {
      var maxWeightSoFar = 0
      while (sortedTasks.isNotEmpty()) {
        val currentProcessor = groups.minBy { it.sumBy(WeightedTask::weight) }!!
        var currentProcessorWeight = currentProcessor.sumBy(WeightedTask::weight)
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
    }
    return groups
  }
}