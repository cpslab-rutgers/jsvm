
import chisel3._
import chisel3.experimental.FixedPoint

class inter16xSad16x() extends Module {
  val io = IO(new Bundle {
    val start      = Input(UInt(1.W))
    val cur        = Input(Vec(256, UInt(16.W)))
    val org        = Input(Vec(256, UInt(16.W)))

    val out        = Output(UInt(16.W))
    val testSumOrg = Output(UInt(16.W))
    val testSumCur = Output(UInt(16.W))
  })

  io.testSumCur := io.cur.reduce(_ + _)
  io.testSumOrg := io.org.reduce(_ + _)

  when(io.start === 1.U) {
    io.out := io.org.zip(io.cur).map { case (a, b) => Mux(a >= b, a - b, b - a) }.reduce(_ + _)
  }.otherwise {
    io.out := 65535.U
  }
}