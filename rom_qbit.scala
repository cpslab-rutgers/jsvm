
import chisel3._
import chisel3.experimental.FixedPoint

class rom_qbit() extends Module {
  val io = IO(new Bundle {
    val set  = Input(Bool())
    val qp   = Input(UInt(6.W))

    val qbit = Output(UInt(5.W))
  })

  val qbit_mem = VecInit(Seq.tabulate(52)(i => RegInit((i / 4 + 15).U(5.W))))

  val curr_qp  = RegInit(15.U(6.W))

  io.qbit   := qbit_mem(io.qp)
  when(io.set){
    curr_qp := io.qp
  }.otherwise{
    io.qbit := qbit_mem(curr_qp)
  }

}