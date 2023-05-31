
import chisel3._
import chisel3.experimental.FixedPoint

class iict1d() extends Module {
  val io = IO(new Bundle {
    val in = Input(Vec(4, SInt(9.W)))
    val out = Output(Vec(4, SInt(9.W)))
  })

  val wire0 = Wire(SInt(9.W))
  val wire1 = Wire(SInt(9.W))
  val wire2 = Wire(SInt(9.W))
  val wire3 = Wire(SInt(9.W))

  wire0 := io.in(0) + io.in(2)
  wire1 := io.in(0) - io.in(2)
  wire2 := (io.in(1) >> 1).asSInt - io.in(3)
  wire3 := io.in(1) + (io.in(3) >> 1).asSInt

  io.out(0) := wire0 + wire3
  io.out(1) := wire1 + wire2
  io.out(2) := wire1 - wire2
  io.out(3) := wire0 - wire3

}