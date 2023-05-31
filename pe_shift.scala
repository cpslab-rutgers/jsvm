
import chisel3._
import chisel3.experimental.FixedPoint

class pe_shift() extends Module {
  val io = IO(new Bundle {
    val qbit   = Input(UInt(5.W))
    val a_data = Input(SInt(16.W))

    val s_out  = Output(SInt(16.W))
  })

  io.s_out := (io.a_data >> io.qbit).asSInt

}