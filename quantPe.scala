
import chisel3._
import chisel3.experimental.FixedPoint

class quantPe(val loc: Int) extends Module {
  val io = IO(new Bundle {
    val start      = Input(Bool())
    val qbit       = Input(UInt(5.W))
    val f          = Input(SInt(8.W))
    val coeff_in   = Input(SInt(9.W))

    val coeff_out  = Output(SInt(16.W))
    val done_quant = Output(Bool())
  })

  val mult_inst  = Module(new pe_mult(loc))
  val add_inst   = Module(new pe_add)
  val shift_inst = Module(new pe_shift)

  mult_inst.io.coeff   := io.coeff_in

  add_inst.io.m_data   := mult_inst.io.m_out
  add_inst.io.f        := io.f

  shift_inst.io.a_data := add_inst.io.a_out
  shift_inst.io.qbit   := io.qbit

  io.coeff_out         := shift_inst.io.s_out


  val valid_quant = RegInit(0.U(3.W))

  valid_quant := (valid_quant << 1.U).asUInt + Mux(io.start, 1.U, 0.U)

  when(valid_quant(2)) {
    io.done_quant := true.B
  }.otherwise {
    io.done_quant := false.B
  }

}