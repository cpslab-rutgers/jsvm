
import chisel3._
import chisel3.experimental.FixedPoint

class i_quant_pe(val loc: Int) extends Module {
  val io = IO(new Bundle {
    val start      = Input(Bool())
    val qp         = Input(UInt(6.W))
    val coeff_in   = Input(SInt(9.W))

    val coeff_out  = Output(SInt(16.W))
    val done_quant = Output(Bool())
  })

  val mult_inst = Module(new pe_i_mult(loc))
  val shift_inst = Module(new pe_i_shift)

  mult_inst.io.coeff := io.coeff_in

  shift_inst.io.m_data := mult_inst.io.m_out
  shift_inst.io.qp := io.qp

  io.coeff_out := shift_inst.io.s_out


  val valid_quant = RegInit(0.U(2.W))

  valid_quant := (valid_quant << 1.U).asUInt + Mux(io.start, 1.U, 0.U)

  when(valid_quant(1)) {
    io.done_quant := true.B
  }.otherwise {
    io.done_quant := false.B
  }
}