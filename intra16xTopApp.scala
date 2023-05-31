
import chisel3._
import chisel3.experimental.FixedPoint


object intra16xTopApp extends App {
  println("[{(Generating Verilog file)}]")
  (new chisel3.stage.ChiselStage).emitVerilog(new intra16xTop())
}

class intra16xTop() extends Module {
  val io = IO(new Bundle {
    val start     = Input(Bool())
    val src_in    = Input(Vec(16, UInt(8.W)))
    val H_pix     = Input(Vec(16, UInt(8.W)))
    val V_pix     = Input(Vec(16, UInt(8.W)))
    val H_val     = Input(Bool())
    val V_val     = Input(Bool())

    val src_out   = Output(Vec(16, UInt(8.W)))
    val pred_pix  = Output(Vec(16, UInt(8.W)))
    val sad_best  = Output(UInt(16.W))
    val mode_best = Output(UInt(2.W))
  })

  val controller_inst = Module(new intra16xController())
  val predictor_inst  = Module(new intra16xPredictor())
  val comparator_inst = Module(new intra16xComparator())
  val sadVec_inst     = Module(new intra16xSadByteLine())
  val sadByte_inst0   = Module(new intra16xSadVecLine())
  val sadByte_inst1   = Module(new intra16xSadVecLine())
  val accum_inst0     = Module(new intra16xAccum())
  val accum_inst1     = Module(new intra16xAccum())
  val accum_inst2     = Module(new intra16xAccum())

  io.src_out   := io.src_in
  // io.pred_pix  := USE BEST MODE TO GET PRED PIX
  io.sad_best  := comparator_inst.io.sad_best
  io.mode_best := comparator_inst.io.mode_best

  controller_inst.io.start  := io.start

  predictor_inst.io.start   := io.start
  predictor_inst.io.Hpix    := io.H_pix
  predictor_inst.io.Vpix    := io.V_pix
  predictor_inst.io.valid_H := io.H_val
  predictor_inst.io.valid_V := io.V_val

  sadVec_inst.io.valid      :=
  sadVec_inst.io.a          :=
  sadVec_inst.io.b          :=


}