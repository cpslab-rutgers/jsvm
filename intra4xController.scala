
import chisel3._
import chisel3.experimental.FixedPoint
import chisel3.util.Enum


class intra4xController() extends Module {
  val io = IO(new Bundle {
    val start          = Input(Bool())

    val capture_pix    = Output(Bool())
    val capture_pix_DC = Output(Bool())
    val capture_sad    = Output(Bool())
    val done           = Output(Bool())
  })

  io.capture_pix    := false.B
  io.capture_pix_DC := false.B
  io.capture_sad    := false.B
  io.done           := false.B

  // FSM
  val idle :: predict :: calcSAD :: compare :: Nil = Enum(4)
  val state = RegInit(0.U(2.W))

  when(io.start) {
    io.capture_pix   := true.B
    state            := predict
  }.otherwise{
    when(state === predict) {
      state          := calcSAD
    }.elsewhen(state === calcSAD) {
      io.capture_sad := true.B
      state          := compare
    }.elsewhen(state === compare) {
      io.done        := true.B
      state          := idle
    }.otherwise {
      state          := idle
    }
  }

}