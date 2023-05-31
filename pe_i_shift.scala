
import chisel3._
import chisel3.experimental.FixedPoint

class pe_i_shift() extends Module {
  val io = IO(new Bundle {
    val qp     = Input(UInt(6.W))
    val m_data = Input(SInt(16.W))

    val s_out  = Output(SInt(16.W))
  })

  io.s_out := (io.m_data >> (io.qp / 6.U)).asSInt
}