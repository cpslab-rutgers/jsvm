
import chisel3._
import chisel3.experimental.FixedPoint


object intra4xTopApp extends App {
  println("[{(Generating Verilog file)}]")
  (new chisel3.stage.ChiselStage).emitVerilog(new intra4xTop())
}

class intra4xTop() extends Module {
  val io = IO(new Bundle {
    val neighbor_pixels = Input(Vec(13, UInt(8.W)))
    val valid_IL        = Input(Bool())
    val valid_EH        = Input(Bool())
    val valid_AD        = Input(Bool())
    val valid_M         = Input(Bool())
    val start           = Input(Bool())
    val src_pixels      = Input(Vec(16, UInt(8.W)))

    val sad_best        = Output(UInt(12.W))
    val mode_best       = Output(UInt(4.W))
    val pred_blk_best   = Output(Vec(16, UInt(8.W)))
    val diff_blk_best   = Output(Vec(16, SInt(9.W)))
    val done            = Output(Bool())
  })

  val controller_inst = Module(new intra4xController())
  val predictor_inst  = Module(new intra4xPredictor())
  val comparator_inst = Module(new intra4xComparator())
  val sad4x_inst      = Module(new intra4xSad4x())

  io.sad_best                        := comparator_inst.io.sad_best
  io.mode_best                       := comparator_inst.io.mode_best
  io.pred_blk_best                   := comparator_inst.io.pred_best
  io.diff_blk_best                   := comparator_inst.io.diff_best
  io.done                            := controller_inst.io.done

  controller_inst.io.start           := io.start

  predictor_inst.io.neighbors        := io.neighbor_pixels
  predictor_inst.io.valid_IL         := io.valid_IL
  predictor_inst.io.valid_EH         := io.valid_EH
  predictor_inst.io.valid_AD         := io.valid_AD
  predictor_inst.io.valid_M          := io.valid_M
  predictor_inst.io.capture_pixel    := controller_inst.io.capture_pix
  predictor_inst.io.capture_pixel_DC := controller_inst.io.capture_pix_DC

  sad4x_inst.io.valid                := predictor_inst.io.valid
  sad4x_inst.io.src_pix              := io.src_pixels
  sad4x_inst.io.pred0                := predictor_inst.io.predicted_pix0
  sad4x_inst.io.pred1                := predictor_inst.io.predicted_pix1
  sad4x_inst.io.pred2                := predictor_inst.io.predicted_pix2
  sad4x_inst.io.pred3                := predictor_inst.io.predicted_pix3
  sad4x_inst.io.pred4                := predictor_inst.io.predicted_pix4
  sad4x_inst.io.pred5                := predictor_inst.io.predicted_pix5
  sad4x_inst.io.pred6                := predictor_inst.io.predicted_pix6
  sad4x_inst.io.pred7                := predictor_inst.io.predicted_pix7
  sad4x_inst.io.pred8                := predictor_inst.io.predicted_pix8

  comparator_inst.io.start            := controller_inst.io.capture_sad
  comparator_inst.io.sad_res          := sad4x_inst.io.sad_seq
  comparator_inst.io.db0              := sad4x_inst.io.diff0
  comparator_inst.io.db1              := sad4x_inst.io.diff1
  comparator_inst.io.db2              := sad4x_inst.io.diff2
  comparator_inst.io.db3              := sad4x_inst.io.diff3
  comparator_inst.io.db4              := sad4x_inst.io.diff4
  comparator_inst.io.db5              := sad4x_inst.io.diff5
  comparator_inst.io.db6              := sad4x_inst.io.diff6
  comparator_inst.io.db7              := sad4x_inst.io.diff7
  comparator_inst.io.db8              := sad4x_inst.io.diff8
  comparator_inst.io.pred_pix0        := predictor_inst.io.predicted_pix0
  comparator_inst.io.pred_pix1        := predictor_inst.io.predicted_pix1
  comparator_inst.io.pred_pix2        := predictor_inst.io.predicted_pix2
  comparator_inst.io.pred_pix3        := predictor_inst.io.predicted_pix3
  comparator_inst.io.pred_pix4        := predictor_inst.io.predicted_pix4
  comparator_inst.io.pred_pix5        := predictor_inst.io.predicted_pix5
  comparator_inst.io.pred_pix6        := predictor_inst.io.predicted_pix6
  comparator_inst.io.pred_pix7        := predictor_inst.io.predicted_pix7
  comparator_inst.io.pred_pix8        := predictor_inst.io.predicted_pix8

}