
import chisel3._

class getSad4x() extends Module {
  val io = IO(new Bundle {
    val org   = Input(Vec(4, UInt(16.W)))
    val cur   = Input(Vec(4, UInt(16.W)))
    val out   = Output(UInt(32.W))
  })

  val uiSum = RegInit(0.U(32.W))

  uiSum := io.org.zip(io.cur).map { case (a, b) => Mux(a >= b, a - b, b - a) }.reduce(_ + _)

  io.out := uiSum
}