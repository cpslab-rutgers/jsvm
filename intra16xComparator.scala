
import chisel3._
import chisel3.experimental.FixedPoint

class intra16xComparator() extends Module {
  val io = IO(new Bundle {
    val start     = Input(Bool())
    val V_sad     = Input(UInt(16.W))
    val H_sad     = Input(UInt(16.W))
    val DC_sad    = Input(UInt(16.W))

    val sad_best  = Output(UInt(16.W))
    val mode_best = Output(UInt(2.W))
  })

  io.mode_best := 3.U
  io.sad_best  := 0.U

  when(io.start){
    when((io.V_sad < io.H_sad) && (io.V_sad < io.DC_sad)){
      io.mode_best := 0.U
      io.sad_best  := io.V_sad
    }.elsewhen(io.H_sad < io.DC_sad){
      io.mode_best := 1.U
      io.sad_best  := io.H_sad
    }.otherwise{
      io.mode_best := 2.U
      io.sad_best  := io.DC_sad
    }
  }
}