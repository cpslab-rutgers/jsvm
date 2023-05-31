
import chisel3._
import chisel3.experimental.FixedPoint

class pe_add() extends Module {
  val io = IO(new Bundle {
    val f      = Input(SInt(8.W))
    val m_data = Input(SInt(16.W))

    val a_out  = Output(SInt(16.W))
  })

  io.a_out := io.m_data + io.f

}