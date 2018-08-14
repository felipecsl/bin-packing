package com.felipecsl

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class OfflineBalancedBinPackingTest {
  private val binPacking = OfflineBalancedBinPacking()
  private val items = listOf(
      item(522), item(765), item(288), item(131),
      item(983), item(23), item(987), item(361),
      item(127), item(296), item(523), item(363),
      item(899), item(977), item(720), item(896),
      item(838), item(86), item(736), item(496),
      item(854), item(355), item(311), item(492),
      item(92), item(535), item(842), item(574),
      item(949), item(493), item(938), item(942),
      item(964), item(932), item(950), item(119),
      item(599), item(604), item(838), item(767),
      item(669), item(687), item(681), item(462),
      item(486), item(605), item(61), item(214),
      item(643), item(922), item(537), item(692),
      item(707), item(140), item(437), item(489),
      item(998), item(122), item(222), item(529),
      item(68), item(613), item(774), item(642),
      item(443), item(831), item(28), item(639),
      item(701), item(349), item(534), item(762),
      item(965), item(614), item(677), item(187),
      item(460), item(223), item(493), item(401),
      item(858), item(757), item(240), item(656),
      item(775), item(359), item(141), item(360),
      item(893), item(421), item(15), item(230),
      item(914), item(741), item(821), item(289),
      item(713), item(236), item(495), item(254)
  )

  @Test fun `test firstFitDecreasing`() {
    val result = binPacking.firstFitDecreasing(items, 7000, 10)
    val lightestBin = result.map(Bin::weight).min()
    val heaviestBin = result.map(Bin::weight).max()
    val weights = result.map(Bin::weight).sortedDescending()
    val counts = result.map(Bin::size).sortedDescending()
    assertThat(weights).isEqualTo(listOf(6984, 6971, 6942, 6917, 6915, 6904, 6732, 6677))
    assertThat(counts).isEqualTo(listOf(23, 15, 14, 11, 10, 9, 9, 9))
    assertThat(lightestBin).isEqualTo(6677)
    assertThat(heaviestBin).isEqualTo(6984)
  }

  @Test fun `make sure the computation finishes`() {
    // all items fit perfectly in 3 bins of weight=7 each
    val items = listOf(item(1), item(2), item(3), item(2), item(1), item(4), item(3), item(1),
        item(2), item(2))
    val result = binPacking.firstFitDecreasing(items, 10, 3)
    assertThat(result.size).isEqualTo(3)
    assertThat(result).containsExactly(
        listOf(item(4), item(2), item(1)),
        listOf(item(3), item(3), item(1)),
        listOf(item(2), item(2), item(2), item(1)))
  }

  @Test fun `manual test`() {
    run("first fit 100", binPacking::firstFitDecreasing)
//    run("best fit 100", binPacking::bestFitDecreasing, 10, 100)
  }

  @Test fun `test bestFitDecreasing`() {
    val result = binPacking.bestFitDecreasing(items, 7000, 10)
    val lightestBin = result.map { it.weight }.min()
    val heaviestBin = result.map { it.weight }.max()
    val weights = result.map { it.weight }.sortedDescending()
    val counts = result.map { it.size }.sortedDescending()
    assertThat(weights).isEqualTo(
        listOf(6282, 6270, 5718, 5546, 5442, 5367, 5335, 5190, 4995, 4897))
    assertThat(counts).isEqualTo(listOf(11, 11, 11, 11, 10, 10, 9, 9, 9, 9))
    assertThat(lightestBin).isEqualTo(4897)
    assertThat(heaviestBin).isEqualTo(6282)
  }

  private fun run(
      label: String,
      func: (List<Item>, Int, Int) -> List<Bin>,
      maxBinSize: Int = Int.MAX_VALUE,
      maxBins: Int = Int.MAX_VALUE
  ) {
    println("------------------\n$label")
    val result = func(items, maxBinSize, maxBins)
    printGroups(result)
    val min = result.map { it.weight }.min()!!
    val max = result.map { it.weight }.max()!!
    println("Lightest bin=$min, Heaviest bin=$max")
  }

  private fun printGroups(itemGroups: List<List<Item>>) {
    itemGroups.sortedByDescending { it.sumBy(Item::weight) }.forEachIndexed { i, it ->
      val totalWeight = it.sumBy(Item::weight)
      val totalItems = it.size
      val weightPerTask = if (totalItems > 0) totalWeight / totalItems else 0
      println(
          "Bin=$i, total weight=$totalWeight, total items=$totalItems, weight/item=$weightPerTask")
    }
  }

  data class TestItem(val w: Int) : Item {
    override val weight: Int
      get() = w
  }

  private fun item(weight: Int) = TestItem(weight)
}
