
import chisel3._
import chisel3.experimental.FixedPoint

object mecTop extends App {
  println("[{(Generating Verilog file)}]")
  (new chisel3.stage.ChiselStage).emitVerilog(new topLevel())
}

class topLevel() extends Module {
  val io = IO(new Bundle {
    val initAddr1     = Input(UInt(64.W))
    val initAddr2     = Input(UInt(64.W))
    val mbBufferWidth = Input(UInt(32.W))
    val stride        = Input(UInt(32.W))
    val numRows       = Input(UInt(32.W))
    // AXI interface
    val axiRData      = Input(Vec(4, UInt(16.W)))
    val axiAddr       = Output(UInt(64.W))
    val axiAWrFlag    = Output(UInt(1.W))
    val axiWData      = Output(Vec(4, UInt(16.W)))
    val topAccOut     = Output(UInt(32.W))
  })

  val controller_inst = Module(new controller())
  val getSad4x_inst = Module(new getSad4x())

  // Connect inputs and outputs of getSad4x
  getSad4x_inst.io.org := controller_inst.io.vec1
  getSad4x_inst.io.cur := controller_inst.io.vec2
  controller_inst.io.computeDataIn := getSad4x_inst.io.out

  // Connect AXI interface
  io.axiAddr    := controller_inst.io.axiAddr
  io.axiAWrFlag := controller_inst.io.axiAWrFlag
  controller_inst.io.axiDataIn := io.axiRData
  io.axiWData   := controller_inst.io.axiDataOut

  // Connect other inputs to the controller
  controller_inst.io.initAddr1 := io.initAddr1
  controller_inst.io.initAddr2 := io.initAddr2
  controller_inst.io.mbBufferWidth := io.mbBufferWidth
  controller_inst.io.stride := io.stride
  controller_inst.io.numRows := io.numRows
  io.topAccOut := controller_inst.io.accOut

}