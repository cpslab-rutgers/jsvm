
import chisel3._
import chisel3.experimental.FixedPoint


object quantTop extends App {
  println("[{(Generating Verilog file)}]")
  (new chisel3.stage.ChiselStage).emitVerilog(new quantization())
}

class quantization() extends Module {
  val io = IO(new Bundle {
    val start       = Input(Bool())
    val qp          = Input(UInt(6.W))
    val coeff_trans = Input(Vec(16, SInt(9.W)))

    val quant_out   = Output(Vec(16, SInt(16.W)))
    val done_quant  = Output(Bool())
  })

  val reg_bank_inst = Module(new qiqRegBank())
  val rom_f_inst    = Module(new rom_f())
  val rom_qbit_inst = Module(new rom_qbit())

  val params = Seq(
    0, 2, 0, 2,
    2, 1, 2, 1,
    0, 2, 0, 2,
    2, 1, 2, 1
  )
  val pe_vec = VecInit(Seq.tabulate(16)(i => Module(new quantPe(params(i))).io))


  reg_bank_inst.io.set   := io.start
  reg_bank_inst.io.coeff := io.coeff_trans

  rom_f_inst.io.set      := io.start
  rom_f_inst.io.qp       := io.qp

  rom_qbit_inst.io.set   := io.start
  rom_qbit_inst.io.qp    := io.qp

  for (i <- 0 until 16) {
    pe_vec(i).start    := io.start
    pe_vec(i).qbit     := rom_qbit_inst.io.qbit
    pe_vec(i).f        := rom_f_inst.io.f
    pe_vec(i).coeff_in := reg_bank_inst.io.quant_out(i)

    io.quant_out(i)    := pe_vec(i).coeff_out
  }

  io.done_quant := pe_vec(0).done_quant

}


//val pe0  = Module(new quantPe)
//val pe1  = Module(new quantPe)
//val pe2  = Module(new quantPe)
//val pe3  = Module(new quantPe)
//val pe4  = Module(new quantPe)
//val pe5  = Module(new quantPe)
//val pe6  = Module(new quantPe)
//val pe7  = Module(new quantPe)
//val pe8  = Module(new quantPe)
//val pe9  = Module(new quantPe)
//val pe10 = Module(new quantPe)
//val pe11 = Module(new quantPe)
//val pe12 = Module(new quantPe)
//val pe13 = Module(new quantPe)
//val pe14 = Module(new quantPe)
//val pe15 = Module(new quantPe)