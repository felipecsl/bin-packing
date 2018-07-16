package com.felipecsl

import java.util.ArrayList

class Bin(val maxSize: Int) : ArrayList<Item>() {
  var weight = 0

  override fun add(element: Item): Boolean {
    if (remainingWeight < element.weight) {
      throw IllegalArgumentException("Bin cannot fit new element of weight ${element.weight}. " +
          "Current weight=$weight, max size=$maxSize")
    }
    weight += element.weight
    return super.add(element)
  }

  val remainingWeight: Int
    get() {
      return if (weight >= maxSize) 0 else maxSize - weight
    }
}