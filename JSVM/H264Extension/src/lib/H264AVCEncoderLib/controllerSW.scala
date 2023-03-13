class controllerSW(a1: Int, a2: Int, frSz: Int, mgSz: Int, mbW: Int, rows: Int) {
  /*
  AXI interconnect
  IN: axiDataIn
  OUT: axiAddr, axiDataOut, w/r flag, d1, d2
  */
  val stride = frSz + 2 * mgSz
  val mbBufferWidth = mbW

  var axiDataIn: List[Int] = List()
  var addr1 = a1
  var addr2 = a2

  var axiDataOut: List[Int] = List()
  var vecOut1: List[Int] = List()
  var vecOut2: List[Int] = List()

  var currState = 0
  var computeSum = 0

  def getSadSW(v1: List[Int], v2: List[Int]): Int = {
    var res = 0
    for (i <- 0 until 4) {
      val absDiff = math.abs(v1(i) - v2(i))
      res += absDiff
    }
    res
  }

  def calcTotalSAD() = {
    for (i <- 0 until rows) {
      vecOut1 = List(1,3,5,7) // fetchData(addr1)
      addr1 += mbBufferWidth

      vecOut2 = List(4,4,4,4) // fetchData(addr2)
      addr2 += stride

      computeSum += getSadSW(vecOut1, vecOut2)
    }
  }

}

object Main {
  def main(): Unit = {
    val ctrl = new controllerSW(0, 0, 20, 10, 32, 10)
    println(ctrl.stride)
    println(ctrl.computeSum)
    ctrl.calcTotalSAD()
    println(ctrl.computeSum)
  }
}