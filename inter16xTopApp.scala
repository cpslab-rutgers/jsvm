
import chisel3._
import chisel3.experimental.FixedPoint

object inter16xTopApp extends App {
  println("[{(Generating Verilog file)}]")
  (new chisel3.stage.ChiselStage).emitVerilog(new inter16xTop())
}

class inter16xTop() extends Module {
  val io = IO(new Bundle {
    val done_charge = Input(UInt(1.W))
    val cur_data_in = Input(Vec(256, UInt(16.W)))
    val org_data_in = Input(Vec(1024, UInt(16.W)))

    val mvx_min     = Output(SInt(4.W))
    val mvy_min     = Output(SInt(4.W))
    val address_min = Output(UInt(10.W))
    val min_sad     = Output(UInt(16.W))
    val done_ldps   = Output(UInt(1.W))

    val testSumC = Output(UInt(16.W))
    val testSumO = Output(UInt(16.W))
  })

  val controller_inst = Module(new inter16xController())
  val extraction_inst = Module(new inter16xExtraction())
  val comparator_inst = Module(new inter16xComparator())
  val sad16x_inst     = Module(new inter16xSad16x())

  io.testSumC := sad16x_inst.io.testSumCur
  io.testSumO := sad16x_inst.io.testSumOrg

  io.mvx_min                     := comparator_inst.io.mvx_min
  io.mvy_min                     := comparator_inst.io.mvy_min
  io.address_min                 := comparator_inst.io.address_min
  io.min_sad                     := comparator_inst.io.min_sad
  io.done_ldps                   := controller_inst.io.done_ldps

  extraction_inst.io.pix_data    := io.org_data_in
  extraction_inst.io.mvx         := controller_inst.io.mvx
  extraction_inst.io.mvy         := controller_inst.io.mvy
  extraction_inst.io.address     := controller_inst.io.address

  sad16x_inst.io.start           := controller_inst.io.start_sad
  sad16x_inst.io.cur             := io.cur_data_in
  sad16x_inst.io.org             := extraction_inst.io.ref_data

  comparator_inst.io.sad_res     := sad16x_inst.io.out
  comparator_inst.io.start       := controller_inst.io.start_comp

  controller_inst.io.done_charge := io.done_charge
  controller_inst.io.mvx_min     := comparator_inst.io.mvx_min
  controller_inst.io.mvy_min     := comparator_inst.io.mvy_min
  controller_inst.io.address_min := comparator_inst.io.address_min

}
