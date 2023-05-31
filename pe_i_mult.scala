
import chisel3._
import chisel3.experimental.FixedPoint

class pe_i_mult(val loc: Int) extends Module {
  val io = IO(new Bundle {
    val coeff = Input(SInt(9.W))
    val m_out = Output(SInt(16.W))
  })

  if (loc == 0) {
    io.m_out := (io.coeff << 4).asSInt
  } else if (loc == 1) {
    io.m_out := (io.coeff << 4).asSInt + (io.coeff << 3).asSInt + io.coeff
  } else {
    io.m_out := (io.coeff << 4).asSInt + (io.coeff << 2).asSInt
  }
}