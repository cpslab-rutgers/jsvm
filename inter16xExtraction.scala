
import chisel3._
import chisel3.util._
import chisel3.experimental.FixedPoint

class inter16xExtraction() extends Module {
  val io = IO(new Bundle {
    val pix_data = Input(Vec(1024, UInt(16.W)))
    val mvx      = Input(SInt(4.W))
    val mvy      = Input(SInt(4.W))
    val address  = Input(UInt(10.W))

    val ref_data = Output(Vec(256, UInt(16.W)))
  })

  io.ref_data := Seq.tabulate(16)(i => io.pix_data(io.address + i.U)) ++
      Seq.tabulate(16)(i => io.pix_data(io.address + 32.U + i.U)) ++
      Seq.tabulate(16)(i => io.pix_data(io.address + 64.U + i.U)) ++
      Seq.tabulate(16)(i => io.pix_data(io.address + 96.U + i.U)) ++
      Seq.tabulate(16)(i => io.pix_data(io.address + 128.U + i.U)) ++
      Seq.tabulate(16)(i => io.pix_data(io.address + 160.U + i.U)) ++
      Seq.tabulate(16)(i => io.pix_data(io.address + 192.U + i.U)) ++
      Seq.tabulate(16)(i => io.pix_data(io.address + 224.U + i.U)) ++
      Seq.tabulate(16)(i => io.pix_data(io.address + 256.U + i.U)) ++
      Seq.tabulate(16)(i => io.pix_data(io.address + 288.U + i.U)) ++
      Seq.tabulate(16)(i => io.pix_data(io.address + 320.U + i.U)) ++
      Seq.tabulate(16)(i => io.pix_data(io.address + 352.U + i.U)) ++
      Seq.tabulate(16)(i => io.pix_data(io.address + 384.U + i.U)) ++
      Seq.tabulate(16)(i => io.pix_data(io.address + 416.U + i.U)) ++
      Seq.tabulate(16)(i => io.pix_data(io.address + 448.U + i.U)) ++
      Seq.tabulate(16)(i => io.pix_data(io.address + 480.U + i.U))

}