
import chisel3._
import chisel3.experimental.FixedPoint

class intra16xSadByteLine() extends Module {
  val io = IO(new Bundle {
    val valid = Input(Bool())
    val a     = Input(UInt(8.W))
    val b     = Input(Vec(16, UInt(8.W)))

    val sad   = Output(UInt(12.W))
  })

  when(io.valid) {
    io.sad := io.b.map { case (num) => Mux(num >= io.a, num - io.a, io.a - num) }.reduce(_ + _)
  }.otherwise {
    io.sad := 4080.U
  }

}