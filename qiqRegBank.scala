
import chisel3._
import chisel3.experimental.FixedPoint

class qiqRegBank() extends Module {
  val io = IO(new Bundle {
    val set       = Input(Bool())
    val coeff     = Input(Vec(16, SInt(9.W)))

    val quant_out = Output(Vec(16, SInt(9.W)))
  })

  val curr_coeff = RegInit(VecInit(Seq.fill(16)(0.S(9.W))))

  io.quant_out   := io.coeff
  when(io.set){
    curr_coeff   := io.coeff
  }.otherwise{
    io.quant_out := curr_coeff
  }

}