
import chisel3._
import chisel3.experimental.FixedPoint


object iQuantTop extends App {
  println("[{(Generating Verilog file)}]")
  (new chisel3.stage.ChiselStage).emitVerilog(new inverse_quant())
}

class inverse_quant() extends Module {
  val io = IO(new Bundle {
    val start      = Input(Bool())
    val qp         = Input(UInt(6.W))
    val quant_in   = Input(Vec(16, SInt(16.W)))

    val iq_out     = Output(Vec(16, SInt(16.W)))
    val done_quant = Output(Bool())
  })

  val reg_bank_inst = Module(new qiqRegBank())

  val params = Seq(
    0, 2, 0, 2,
    2, 1, 2, 1,
    0, 2, 0, 2,
    2, 1, 2, 1
  )
  val pe_vec = VecInit(Seq.tabulate(16)(i => Module(new i_quant_pe(params(i))).io))


  reg_bank_inst.io.set := io.start
  reg_bank_inst.io.coeff := io.quant_in


  for (i <- 0 until 16) {
    pe_vec(i).start    := io.start
    pe_vec(i).qp       := io.qp
    pe_vec(i).coeff_in := reg_bank_inst.io.quant_out(i)

    io.iq_out(i)       := pe_vec(i).coeff_out
  }

  io.done_quant := pe_vec(0).done_quant
}