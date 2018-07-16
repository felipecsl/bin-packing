package com.felipecsl

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class OfflineBinPackingTest {
  private val binPacking = OfflineBinPacking()
  private val items = listOf(
      Item(522), Item(765), Item(288), Item(131),
      Item(983), Item(23), Item(987), Item(361),
      Item(127), Item(296), Item(523), Item(363),
      Item(899), Item(977), Item(720), Item(896),
      Item(838), Item(86), Item(736), Item(496),
      Item(854), Item(355), Item(311), Item(492),
      Item(92), Item(535), Item(842), Item(574),
      Item(949), Item(493), Item(938), Item(942),
      Item(964), Item(932), Item(950), Item(119),
      Item(599), Item(604), Item(838), Item(767),
      Item(669), Item(687), Item(681), Item(462),
      Item(486), Item(605), Item(61), Item(214),
      Item(643), Item(922), Item(537), Item(692),
      Item(707), Item(140), Item(437), Item(489),
      Item(998), Item(122), Item(222), Item(529),
      Item(68), Item(613), Item(774), Item(642),
      Item(443), Item(831), Item(28), Item(639),
      Item(701), Item(349), Item(534), Item(762),
      Item(965), Item(614), Item(677), Item(187),
      Item(460), Item(223), Item(493), Item(401),
      Item(858), Item(757), Item(240), Item(656),
      Item(775), Item(359), Item(141), Item(360),
      Item(893), Item(421), Item(15), Item(230),
      Item(914), Item(741), Item(821), Item(289),
      Item(713), Item(236), Item(495), Item(254)
  )

  @Test fun `test firstFitDecreasing`() {
    val result = binPacking.firstFitDecreasing(items, 10)
    val lightestBin = result.map { it.sumBy(Item::weight) }.min()
    val heaviestBin = result.map { it.sumBy(Item::weight) }.max()
    val weights = result.map { it.sumBy(Item::weight) }.sortedDescending()
    val counts = result.map { it.size }.sortedDescending()
    assertThat(weights).isEqualTo(
        listOf(6377, 6244, 6175, 5908, 5767, 5611, 5495, 5123, 4563, 3779))
    assertThat(counts).isEqualTo(listOf(23, 15, 12, 11, 9, 8, 7, 6, 5, 4))
    assertThat(lightestBin).isEqualTo(3779)
    assertThat(heaviestBin).isEqualTo(6377)
  }

  @Test fun `manual test`() {
    run("first fit 100", binPacking::firstFitDecreasing, 100)
    run("best fit 100", binPacking::bestFitDecreasing, 100)
  }

  @Test fun `test bestFitDecreasing`() {
    val result = binPacking.bestFitDecreasing(items, 10)
    val lightestBin = result.map { it.sumBy(Item::weight) }.min()
    val heaviestBin = result.map { it.sumBy(Item::weight) }.max()
    val weights = result.map { it.sumBy(Item::weight) }.sortedDescending()
    val counts = result.map { it.size }.sortedDescending()
    assertThat(weights).isEqualTo(
        listOf(6282, 6270, 5718, 5546, 5442, 5367, 5335, 5190, 4995, 4897))
    assertThat(counts).isEqualTo(listOf(11, 11, 11, 11, 10, 10, 9, 9, 9, 9))
    assertThat(lightestBin).isEqualTo(4897)
    assertThat(heaviestBin).isEqualTo(6282)
  }

  private fun run(
      label: String,
      func: (List<Item>, Int) -> List<List<Item>>,
      maxBins: Int = 10
  ) {
    println("------------------\n$label")
    val result = func(items, maxBins)
    printGroups(result)
    val min = result.map { it.sumBy(Item::weight) }.min()!!
    val max = result.map { it.sumBy(Item::weight) }.max()!!
    println("Lightest bin=$min, Heaviest bin=$max")
  }

  private fun printGroups(itemGroups: List<List<Item>>) {
    itemGroups.sortedByDescending { it.sumBy(Item::weight) }.forEachIndexed { i, it ->
      val totalWeight = it.sumBy(Item::weight)
      val totalItems = it.size
      val weightPerTask = if (totalItems > 0) totalWeight / totalItems else 0
      println("Bin=$i, total weight=$totalWeight, total items=$totalItems, weight/item=$weightPerTask")
    }
  }
}
