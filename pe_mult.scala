
import chisel3._
import chisel3.experimental.FixedPoint

class pe_mult(val loc: Int) extends Module {
  val io = IO(new Bundle {
    val coeff  = Input(SInt(9.W))
    val m_out  = Output(SInt(16.W))
  })

  if (loc == 0) {
    io.m_out := (io.coeff << 13).asSInt
  } else if (loc == 1) {
    io.m_out := (io.coeff << 11).asSInt + (io.coeff << 10).asSInt + (io.coeff << 8).asSInt + (io.coeff << 2).asSInt + (io.coeff << 1).asSInt + io.coeff
  } else {
    io.m_out := (io.coeff << 12).asSInt + (io.coeff << 10).asSInt + (io.coeff << 6).asSInt + (io.coeff << 5).asSInt + (io.coeff << 4).asSInt + (io.coeff << 3).asSInt + (io.coeff << 1).asSInt + io.coeff
  }

}