package com.felipecsl

import com.github.javafaker.Faker
import com.google.common.math.Stats
import org.junit.Test

class LoadBalancerTest {
  @Test fun balance() {
    val balancer = LoadBalancer()
    val faker = Faker()
    val maxTasks = 100
    val maxWeight = 1000
    val tasks = (0 until maxTasks).map {
      WeightedTask(faker.name().firstName(), (Math.random() * maxWeight).toInt())
    }
    println("balance1:")
    val taskGroups1 = balancer.balance1(tasks, 10)
    printGroups(taskGroups1)
    val min1 = taskGroups1.map { it.sumBy(WeightedTask::weight) }.min()!!
    val max1 = taskGroups1.map { it.sumBy(WeightedTask::weight) }.max()!!
    println("min weight=$min1, max weight=$max1")
    println("balance2:")
    val taskGroups2 = balancer.balance2(tasks, 10)
    printGroups(taskGroups2)
    val min2 = taskGroups2.map { it.sumBy(WeightedTask::weight) }.min()!!
    val max2 = taskGroups2.map { it.sumBy(WeightedTask::weight) }.max()!!
    println("min weight=$min2, max weight=$max2")
  }

  private fun printGroups(taskGroups: List<List<WeightedTask>>) {
    taskGroups.sortedByDescending { it.sumBy(WeightedTask::weight) }.forEach {
      val totalWeight = it.sumBy(WeightedTask::weight)
      val totalTasks = it.size
      val weightPerTask = totalWeight / totalTasks
      println("Total weight=$totalWeight, total tasks=$totalTasks, weight/task=$weightPerTask")
    }
  }
}