
import chisel3._
import chisel3.experimental.FixedPoint

class intra4xSad4x() extends Module {
  val io = IO(new Bundle {
    val valid   = Input(Vec(9, Bool()))
    val src_pix = Input(Vec(16, UInt(8.W)))
    val pred0   = Input(Vec(16, UInt(8.W)))
    val pred1   = Input(Vec(16, UInt(8.W)))
    val pred2   = Input(Vec(16, UInt(8.W)))
    val pred3   = Input(Vec(16, UInt(8.W)))
    val pred4   = Input(Vec(16, UInt(8.W)))
    val pred5   = Input(Vec(16, UInt(8.W)))
    val pred6   = Input(Vec(16, UInt(8.W)))
    val pred7   = Input(Vec(16, UInt(8.W)))
    val pred8   = Input(Vec(16, UInt(8.W)))

    val sad_seq = Output(Vec(9, UInt(12.W)))
    val diff0   = Output(Vec(16, SInt(9.W)))
    val diff1   = Output(Vec(16, SInt(9.W)))
    val diff2   = Output(Vec(16, SInt(9.W)))
    val diff3   = Output(Vec(16, SInt(9.W)))
    val diff4   = Output(Vec(16, SInt(9.W)))
    val diff5   = Output(Vec(16, SInt(9.W)))
    val diff6   = Output(Vec(16, SInt(9.W)))
    val diff7   = Output(Vec(16, SInt(9.W)))
    val diff8   = Output(Vec(16, SInt(9.W)))
  })

  when(io.valid(0)) {
    io.sad_seq(0) := io.src_pix.zip(io.pred0).map { case (x, y) => Mux(x >= y, x - y, y - x) }.reduce(_ + _)
    io.diff0 := (io.src_pix.zip(io.pred0)).map { case (s, p) => (s.asSInt - p.asSInt).asSInt }.toVector
  }.otherwise {
    io.sad_seq(0) := 4095.U
    for (i <- 0 until 16) {
      io.diff0(i) := 0.S
    }
  }

  when(io.valid(1)) {
    io.sad_seq(1) := io.src_pix.zip(io.pred1).map { case (x, y) => Mux(x >= y, x - y, y - x) }.reduce(_ + _)
    io.diff1 := (io.src_pix.zip(io.pred1)).map { case (s, p) => (s.asSInt - p.asSInt).asSInt }.toVector
  }.otherwise {
    io.sad_seq(1) := 4095.U
    for (i <- 0 until 16) {
      io.diff1(i) := 0.S
    }
  }

  when(io.valid(2)) {
    io.sad_seq(2) := io.src_pix.zip(io.pred2).map { case (x, y) => Mux(x >= y, x - y, y - x) }.reduce(_ + _)
    io.diff2 := (io.src_pix.zip(io.pred2)).map { case (s, p) => (s.asSInt - p.asSInt).asSInt }.toVector
  }.otherwise {
    io.sad_seq(2) := 4095.U
    for (i <- 0 until 16) {
      io.diff2(i) := 0.S
    }
  }

  when(io.valid(3)) {
    io.sad_seq(3) := io.src_pix.zip(io.pred3).map { case (x, y) => Mux(x >= y, x - y, y - x) }.reduce(_ + _)
    io.diff3 := (io.src_pix.zip(io.pred3)).map { case (s, p) => (s.asSInt - p.asSInt).asSInt }.toVector
  }.otherwise {
    io.sad_seq(3) := 4095.U
    for (i <- 0 until 16) {
      io.diff3(i) := 0.S
    }
  }

  when(io.valid(4)) {
    io.sad_seq(4) := io.src_pix.zip(io.pred4).map { case (x, y) => Mux(x >= y, x - y, y - x) }.reduce(_ + _)
    io.diff4 := (io.src_pix.zip(io.pred4)).map { case (s, p) => (s.asSInt - p.asSInt).asSInt }.toVector
  }.otherwise {
    io.sad_seq(4) := 4095.U
    for (i <- 0 until 16) {
      io.diff4(i) := 0.S
    }
  }

  when(io.valid(5)) {
    io.sad_seq(5) := io.src_pix.zip(io.pred5).map { case (x, y) => Mux(x >= y, x - y, y - x) }.reduce(_ + _)
    io.diff5 := (io.src_pix.zip(io.pred5)).map { case (s, p) => (s.asSInt - p.asSInt).asSInt }.toVector
  }.otherwise {
    io.sad_seq(5) := 4095.U
    for (i <- 0 until 16) {
      io.diff5(i) := 0.S
    }
  }

  when(io.valid(6)) {
    io.sad_seq(6) := io.src_pix.zip(io.pred6).map { case (x, y) => Mux(x >= y, x - y, y - x) }.reduce(_ + _)
    io.diff6 := (io.src_pix.zip(io.pred6)).map { case (s, p) => (s.asSInt - p.asSInt).asSInt }.toVector
  }.otherwise {
    io.sad_seq(6) := 4095.U
    for (i <- 0 until 16) {
      io.diff6(i) := 0.S
    }
  }

  when(io.valid(7)) {
    io.sad_seq(7) := io.src_pix.zip(io.pred7).map { case (x, y) => Mux(x >= y, x - y, y - x) }.reduce(_ + _)
    io.diff7 := (io.src_pix.zip(io.pred7)).map { case (s, p) => (s.asSInt - p.asSInt).asSInt }.toVector
  }.otherwise {
    io.sad_seq(7) := 4095.U
    for (i <- 0 until 16) {
      io.diff7(i) := 0.S
    }
  }

  when(io.valid(8)) {
    io.sad_seq(8) := io.src_pix.zip(io.pred8).map { case (x, y) => Mux(x >= y, x - y, y - x) }.reduce(_ + _)
    io.diff8 := (io.src_pix.zip(io.pred8)).map { case (s, p) => (s.asSInt - p.asSInt).asSInt }.toVector
  }.otherwise {
    io.sad_seq(8) := 4095.U
    for (i <- 0 until 16) {
      io.diff8(i) := 0.S
    }
  }

}