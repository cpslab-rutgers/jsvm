
import chisel3._
import chisel3.experimental.FixedPoint

class intra16xSadVecLine() extends Module {
  val io = IO(new Bundle {
    val valid = Input(Bool())
    val a     = Input(Vec(16, UInt(8.W)))
    val b     = Input(Vec(16, UInt(8.W)))

    val sad   = Output(UInt(12.W))
  })

  when(io.valid) {
    io.sad := io.a.zip(io.b).map{ case (x, y) => Mux(x >= y, x - y, y - x) }.reduce(_ + _)
  }.otherwise{
    io.sad := 4080.U
  }

}