
import chisel3._
import chisel3.experimental.FixedPoint

object iict2dtop extends App {
  println("[{(Generating Verilog file)}]")
  (new chisel3.stage.ChiselStage).emitVerilog(new iict2d())
}

class iict2d() extends Module {
  val io = IO(new Bundle {
    val start = Input(Bool())
    val residuals = Input(Vec(16, SInt(9.W)))

    val coeff_trans = Output(Vec(16, SInt(9.W)))
    val done_iict = Output(Bool())
  })

  val ict0b0 = Module(new ict1d())
  val ict1b0 = Module(new ict1d())
  val ict2b0 = Module(new ict1d())
  val ict3b0 = Module(new ict1d())
  val ict0b1 = Module(new ict1d())
  val ict1b1 = Module(new ict1d())
  val ict2b1 = Module(new ict1d())
  val ict3b1 = Module(new ict1d())

  ict0b0.io.in(0) := io.residuals(0)
  ict0b0.io.in(1) := io.residuals(1)
  ict0b0.io.in(2) := io.residuals(2)
  ict0b0.io.in(3) := io.residuals(3)
  ict1b0.io.in(0) := io.residuals(4)
  ict1b0.io.in(1) := io.residuals(5)
  ict1b0.io.in(2) := io.residuals(6)
  ict1b0.io.in(3) := io.residuals(7)
  ict2b0.io.in(0) := io.residuals(8)
  ict2b0.io.in(1) := io.residuals(9)
  ict2b0.io.in(2) := io.residuals(10)
  ict2b0.io.in(3) := io.residuals(11)
  ict3b0.io.in(0) := io.residuals(12)
  ict3b0.io.in(1) := io.residuals(13)
  ict3b0.io.in(2) := io.residuals(14)
  ict3b0.io.in(3) := io.residuals(15)

  ict0b1.io.in(0) := ict0b0.io.out(0)
  ict0b1.io.in(1) := ict1b0.io.out(0)
  ict0b1.io.in(2) := ict2b0.io.out(0)
  ict0b1.io.in(3) := ict3b0.io.out(0)
  ict1b1.io.in(0) := ict0b0.io.out(1)
  ict1b1.io.in(1) := ict1b0.io.out(1)
  ict1b1.io.in(2) := ict2b0.io.out(1)
  ict1b1.io.in(3) := ict3b0.io.out(1)
  ict2b1.io.in(0) := ict0b0.io.out(2)
  ict2b1.io.in(1) := ict1b0.io.out(2)
  ict2b1.io.in(2) := ict2b0.io.out(2)
  ict2b1.io.in(3) := ict3b0.io.out(2)
  ict3b1.io.in(0) := ict0b0.io.out(3)
  ict3b1.io.in(1) := ict1b0.io.out(3)
  ict3b1.io.in(2) := ict2b0.io.out(3)
  ict3b1.io.in(3) := ict3b0.io.out(3)

  io.coeff_trans(0) := ict0b0.io.out(0)
  io.coeff_trans(1) := ict1b0.io.out(0)
  io.coeff_trans(2) := ict2b0.io.out(0)
  io.coeff_trans(3) := ict3b0.io.out(0)
  io.coeff_trans(4) := ict0b0.io.out(1)
  io.coeff_trans(5) := ict1b0.io.out(1)
  io.coeff_trans(6) := ict2b0.io.out(1)
  io.coeff_trans(7) := ict3b0.io.out(1)
  io.coeff_trans(8) := ict0b0.io.out(2)
  io.coeff_trans(9) := ict1b0.io.out(2)
  io.coeff_trans(10) := ict2b0.io.out(2)
  io.coeff_trans(11) := ict3b0.io.out(2)
  io.coeff_trans(12) := ict0b0.io.out(3)
  io.coeff_trans(13) := ict1b0.io.out(3)
  io.coeff_trans(14) := ict2b0.io.out(3)
  io.coeff_trans(15) := ict3b0.io.out(3)

  val valid_coeff  = RegInit(false.B)

  when(io.start) {
    valid_coeff := true.B
  }.otherwise {
    valid_coeff := false.B
  }

  when(valid_coeff) {
    io.done_iict := true.B
  }.otherwise {
    io.done_iict := false.B
  }
}