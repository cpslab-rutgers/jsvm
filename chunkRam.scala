
import chisel3._
import chisel3.experimental.FixedPoint

class chunkRam(addrWidth: Int) extends Module {
  val io = IO(new Bundle {
    val data_in  = Input(Vec(4, UInt(8.W)))
    val addr     = Input(UInt(addrWidth.W))
    val wr_en    = Input(Bool())
    val rd_en    = Input(Bool())

    val data_out = Output(Vec(16, UInt(8.W)))
  })

  val mem = Mem(1 << addrWidth, UInt(32.W))

  when(io.wr_en) {
    for (i <- 0 until 4) {
      mem.write(io.addr + i.U, io.data_in(i))
    }
  }

  val chunk_line = Wire(Vec(16, UInt(8.W)))
  for (i <- 0 until 4) {
    chunk_line(i * 4 + 0) := mem.read(io.addr + i.U)(7, 0)
    chunk_line(i * 4 + 1) := mem.read(io.addr + i.U)(15, 8)
    chunk_line(i * 4 + 2) := mem.read(io.addr + i.U)(23, 16)
    chunk_line(i * 4 + 3) := mem.read(io.addr + i.U)(31, 24)
  }

  io.data_out := chunk_line

}