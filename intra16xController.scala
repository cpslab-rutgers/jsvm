
import chisel3._
import chisel3.util.Enum


class intra16xController() extends Module {
  val io = IO(new Bundle {
    val start     = Input(Bool())

    val mb_rAddr  = Output(UInt(6.W))
    val load_pix  = Output(Bool())
    val start_cmp = Output(Bool())
  })

  io.load_pix      := false.B
  io.start_cmp     := false.B

  val mb_addr = RegInit(0.U(8.W))
  val sad_itr = RegInit(0.U(4.W))

  // FSM
  val idle :: load :: calcSAD :: compare :: Nil = Enum(4)
  val state = RegInit(0.U(2.W))

  when(io.start) {
    io.load_pix        := true.B
    state              := load
  }.otherwise {
    when(state === load) {
      mb_addr          := 4.U
      sad_itr          := 0.U
      state            := calcSAD
    }.elsewhen(state === calcSAD) {
      when(sad_itr === 15.U) {
        mb_addr        := mb_addr + 4.U
        state          := compare
      }.otherwise{
        sad_itr        := sad_itr + 1.U
      }
    }.elsewhen(state === compare) {
      io.start_cmp     := true.B
      state            := idle
    }.otherwise {
      state            := idle
    }
  }

  io.mb_rAddr := mb_addr
}