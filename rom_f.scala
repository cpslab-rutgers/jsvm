
import chisel3._
import chisel3.experimental.FixedPoint

class rom_f() extends Module {
  val io = IO(new Bundle {
    val set = Input(Bool())
    val qp  = Input(UInt(6.W))

    val f   = Output(SInt(8.W))
  })

  val f_vals = RegInit(VecInit(Seq.fill(52)(0.S(8.W))))

  val curr_qp = RegInit(15.U(6.W))

  io.f := f_vals(io.qp)
  when(io.set) {
    curr_qp := io.qp
  }.otherwise {
    io.f := f_vals(curr_qp)
  }

}