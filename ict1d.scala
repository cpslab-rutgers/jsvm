
import chisel3._
import chisel3.experimental.FixedPoint

class ict1d() extends Module {
  val io = IO(new Bundle {
    val in  = Input(Vec(4, SInt(9.W)))
    val out = Output(Vec(4, SInt(9.W)))
  })

  val wire0 = Wire(SInt(9.W))
  val wire1 = Wire(SInt(9.W))
  val wire2 = Wire(SInt(9.W))
  val wire3 = Wire(SInt(9.W))

  wire0 := io.in(0) + io.in(3)
  wire1 := io.in(1) + io.in(2)
  wire2 := io.in(1) - io.in(2)
  wire3 := io.in(0) - io.in(3)

  io.out(0) := wire0 + wire1
  io.out(2) := wire0 - wire1
  io.out(1) := wire2 + (wire3 << 1).asSInt
  io.out(3) := wire3 - (wire2 << 1).asSInt

}