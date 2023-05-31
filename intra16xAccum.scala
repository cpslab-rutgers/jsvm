
import chisel3._
import chisel3.experimental.FixedPoint

class intra16xAccum() extends Module {
  val io = IO(new Bundle {
    val sad_in    = Input(UInt(12.W))
    val sad_accum = Output(UInt(16.W))
  })

  val total = RegInit(0.U(16.W))

  total := total + io.sad_in

  io.sad_accum := total + io.sad_in
}