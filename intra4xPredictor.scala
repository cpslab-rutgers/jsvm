
import chisel3._
import chisel3.experimental.FixedPoint

class intra4xPredictor() extends Module {
  val io = IO(new Bundle {
    val neighbors        = Input(Vec(13, UInt(8.W)))
    val valid_IL         = Input(Bool())
    val valid_EH         = Input(Bool())
    val valid_AD         = Input(Bool())
    val valid_M          = Input(Bool())
    val capture_pixel    = Input(Bool())
    val capture_pixel_DC = Input(Bool())

    val valid            = Output(Vec(9, Bool()))
    val predicted_pix0   = Output(Vec(16, UInt(8.W)))
    val predicted_pix1   = Output(Vec(16, UInt(8.W)))
    val predicted_pix2   = Output(Vec(16, UInt(8.W)))
    val predicted_pix3   = Output(Vec(16, UInt(8.W)))
    val predicted_pix4   = Output(Vec(16, UInt(8.W)))
    val predicted_pix5   = Output(Vec(16, UInt(8.W)))
    val predicted_pix6   = Output(Vec(16, UInt(8.W)))
    val predicted_pix7   = Output(Vec(16, UInt(8.W)))
    val predicted_pix8   = Output(Vec(16, UInt(8.W)))
  })

  when(io.capture_pixel) {
    val eq0 = io.neighbors(0) + io.neighbors(1) + 1.U
    val eq1 = io.neighbors(1) + io.neighbors(2) + 1.U
    val eq2 = io.neighbors(2) + io.neighbors(3) + 1.U
    val eq3 = io.neighbors(3) + io.neighbors(4) + 1.U
    val eq4 = io.neighbors(4) + io.neighbors(5) + 1.U
    val eq5 = io.neighbors(5) + io.neighbors(6) + 1.U
    val eq6 = io.neighbors(6) + io.neighbors(7) + 1.U
    val eq7 = io.neighbors(8) + io.neighbors(9) + 1.U
    val eq8 = io.neighbors(9) + io.neighbors(10) + 1.U
    val eq9 = io.neighbors(10) + io.neighbors(11) + 1.U
    val eq10 = io.neighbors(12) + io.neighbors(0) + 1.U
    val eq11 = io.neighbors(12) + io.neighbors(8) + 1.U
    val eq12 = (io.neighbors(7) * 2.U) + 1.U
    val eq13 = (io.neighbors(11) * 2.U) + 1.U

    val eq14 = eq0 + eq10
    val eq15 = eq0 + eq1
    val eq16 = eq1 + eq2
    val eq17 = eq2 + eq3
    val eq18 = eq3 + eq4
    val eq19 = eq4 + eq5
    val eq20 = eq5 + eq6
    val eq21 = eq7 + eq11
    val eq22 = eq7 + eq8
    val eq23 = eq8 + eq9
    val eq24 = eq10 + eq11
    val eq25 = eq6 + eq12
    val eq26 = eq9 + eq13
    val eq27 = eq0 + eq2
    val eq28 = eq7 + eq9
    val eq29 = eq27 + eq28

    when(io.valid_AD) {
      io.predicted_pix0(0) := io.neighbors(0)
      io.predicted_pix0(1) := io.neighbors(1)
      io.predicted_pix0(2) := io.neighbors(2)
      io.predicted_pix0(3) := io.neighbors(3)
      io.predicted_pix0(4) := io.neighbors(0)
      io.predicted_pix0(5) := io.neighbors(1)
      io.predicted_pix0(6) := io.neighbors(2)
      io.predicted_pix0(7) := io.neighbors(3)
      io.predicted_pix0(8) := io.neighbors(0)
      io.predicted_pix0(9) := io.neighbors(1)
      io.predicted_pix0(10) := io.neighbors(2)
      io.predicted_pix0(11) := io.neighbors(3)
      io.predicted_pix0(12) := io.neighbors(0)
      io.predicted_pix0(13) := io.neighbors(1)
      io.predicted_pix0(14) := io.neighbors(2)
      io.predicted_pix0(15) := io.neighbors(3)
      io.valid(0) := true.B
    }.otherwise {
      io.valid(0) := false.B
      for (i <- 0 until 16) {
        io.predicted_pix0(i) := 0.U
      }
    }

    when(io.valid_IL) {
      io.predicted_pix1(0) := io.neighbors(8)
      io.predicted_pix1(1) := io.neighbors(8)
      io.predicted_pix1(2) := io.neighbors(8)
      io.predicted_pix1(3) := io.neighbors(8)
      io.predicted_pix1(4) := io.neighbors(9)
      io.predicted_pix1(5) := io.neighbors(9)
      io.predicted_pix1(6) := io.neighbors(9)
      io.predicted_pix1(7) := io.neighbors(9)
      io.predicted_pix1(8) := io.neighbors(10)
      io.predicted_pix1(9) := io.neighbors(10)
      io.predicted_pix1(10) := io.neighbors(10)
      io.predicted_pix1(11) := io.neighbors(10)
      io.predicted_pix1(12) := io.neighbors(11)
      io.predicted_pix1(13) := io.neighbors(11)
      io.predicted_pix1(14) := io.neighbors(11)
      io.predicted_pix1(15) := io.neighbors(11)
      io.valid(1) := true.B
    }.otherwise {
      io.valid(1) := false.B
      for (i <- 0 until 16) {
        io.predicted_pix1(i) := 0.U
      }
    }

    when(io.valid_AD && io.valid_IL) {
      for (i <- 0 until 16) {
        io.predicted_pix2(i) := eq29
      }
      io.valid(2) := true.B
    }.otherwise {
      io.valid(2) := false.B
      for (i <- 0 until 16) {
        io.predicted_pix2(i) := 0.U
      }
    }

    when(io.valid_AD && io.valid_EH) {
      io.predicted_pix3(0) := eq15
      io.predicted_pix3(1) := eq16
      io.predicted_pix3(2) := eq17
      io.predicted_pix3(3) := eq18
      io.predicted_pix3(4) := eq16
      io.predicted_pix3(5) := eq17
      io.predicted_pix3(6) := eq18
      io.predicted_pix3(7) := eq19
      io.predicted_pix3(8) := eq17
      io.predicted_pix3(9) := eq18
      io.predicted_pix3(10) := eq19
      io.predicted_pix3(11) := eq20
      io.predicted_pix3(12) := eq18
      io.predicted_pix3(13) := eq19
      io.predicted_pix3(14) := eq20
      io.predicted_pix3(15) := eq25
      io.valid(3) := true.B
    }.otherwise {
      io.valid(3) := false.B
      for (i <- 0 until 16) {
        io.predicted_pix3(i) := 0.U
      }
    }

    when(io.valid_AD && io.valid_IL && io.valid_M) {
      io.predicted_pix4(0) := eq24
      io.predicted_pix4(1) := eq14
      io.predicted_pix4(2) := eq15
      io.predicted_pix4(3) := eq16
      io.predicted_pix4(4) := eq21
      io.predicted_pix4(5) := eq24
      io.predicted_pix4(6) := eq14
      io.predicted_pix4(7) := eq15
      io.predicted_pix4(8) := eq22
      io.predicted_pix4(9) := eq21
      io.predicted_pix4(10) := eq24
      io.predicted_pix4(11) := eq14
      io.predicted_pix4(12) := eq23
      io.predicted_pix4(13) := eq22
      io.predicted_pix4(14) := eq21
      io.predicted_pix4(15) := eq24
      io.valid(4) := true.B
    }.otherwise {
      io.valid(4) := false.B
      for (i <- 0 until 16) {
        io.predicted_pix4(i) := 0.U
      }
    }

    when(io.valid_AD && io.valid_IL && io.valid_M) {
      io.predicted_pix5(0) := eq10
      io.predicted_pix5(1) := eq0
      io.predicted_pix5(2) := eq1
      io.predicted_pix5(3) := eq2
      io.predicted_pix5(4) := eq24
      io.predicted_pix5(5) := eq14
      io.predicted_pix5(6) := eq15
      io.predicted_pix5(7) := eq16
      io.predicted_pix5(8) := eq21
      io.predicted_pix5(9) := eq10
      io.predicted_pix5(10) := eq0
      io.predicted_pix5(11) := eq1
      io.predicted_pix5(12) := eq22
      io.predicted_pix5(13) := eq24
      io.predicted_pix5(14) := eq14
      io.predicted_pix5(15) := eq15
      io.valid(5) := true.B
    }.otherwise {
      io.valid(5) := false.B
      for (i <- 0 until 16) {
        io.predicted_pix5(i) := 0.U
      }
    }

    when(io.valid_AD && io.valid_IL && io.valid_M) {
      io.predicted_pix6(0) := eq11
      io.predicted_pix6(1) := eq24
      io.predicted_pix6(2) := eq14
      io.predicted_pix6(3) := eq16
      io.predicted_pix6(4) := eq7
      io.predicted_pix6(5) := eq21
      io.predicted_pix6(6) := eq11
      io.predicted_pix6(7) := eq24
      io.predicted_pix6(8) := eq8
      io.predicted_pix6(9) := eq22
      io.predicted_pix6(10) := eq7
      io.predicted_pix6(11) := eq21
      io.predicted_pix6(12) := eq9
      io.predicted_pix6(13) := eq23
      io.predicted_pix6(14) := eq8
      io.predicted_pix6(15) := eq22
      io.valid(6) := true.B
    }.otherwise {
      io.valid(6) := false.B
      for (i <- 0 until 16) {
        io.predicted_pix6(i) := 0.U
      }
    }

    when(io.valid_AD && io.valid_EH) {
      io.predicted_pix7(0) := eq0
      io.predicted_pix7(1) := eq1
      io.predicted_pix7(2) := eq2
      io.predicted_pix7(3) := eq3
      io.predicted_pix7(4) := eq15
      io.predicted_pix7(5) := eq16
      io.predicted_pix7(6) := eq17
      io.predicted_pix7(7) := eq18
      io.predicted_pix7(8) := eq1
      io.predicted_pix7(9) := eq2
      io.predicted_pix7(10) := eq3
      io.predicted_pix7(11) := eq4
      io.predicted_pix7(12) := eq16
      io.predicted_pix7(13) := eq17
      io.predicted_pix7(14) := eq18
      io.predicted_pix7(15) := eq19
      io.valid(7) := true.B
    }.otherwise {
      io.valid(7) := false.B
      for (i <- 0 until 16) {
        io.predicted_pix7(i) := 0.U
      }
    }

    when(io.valid_IL) {
      io.predicted_pix8(0) := eq7
      io.predicted_pix8(1) := eq22
      io.predicted_pix8(2) := eq8
      io.predicted_pix8(3) := eq23
      io.predicted_pix8(4) := eq8
      io.predicted_pix8(5) := eq23
      io.predicted_pix8(6) := eq9
      io.predicted_pix8(7) := eq26
      io.predicted_pix8(8) := eq9
      io.predicted_pix8(9) := eq26
      io.predicted_pix8(10) := io.neighbors(11)
      io.predicted_pix8(11) := io.neighbors(11)
      io.predicted_pix8(12) := io.neighbors(11)
      io.predicted_pix8(13) := io.neighbors(11)
      io.predicted_pix8(14) := io.neighbors(11)
      io.predicted_pix8(15) := io.neighbors(11)
      io.valid(8) := true.B
    }.otherwise {
      io.valid(8) := false.B
      for (i <- 0 until 16) {
        io.predicted_pix8(i) := 0.U
      }
    }
  }.otherwise{
    for (i <- 0 until 9) {
      io.valid(i) := false.B
    }
    for (i <- 0 until 16) {
      io.predicted_pix0(i) := 0.U
    }
    for (i <- 0 until 16) {
      io.predicted_pix1(i) := 0.U
    }
    for (i <- 0 until 16) {
      io.predicted_pix2(i) := 0.U
    }
    for (i <- 0 until 16) {
      io.predicted_pix3(i) := 0.U
    }
    for (i <- 0 until 16) {
      io.predicted_pix4(i) := 0.U
    }
    for (i <- 0 until 16) {
      io.predicted_pix5(i) := 0.U
    }
    for (i <- 0 until 16) {
      io.predicted_pix6(i) := 0.U
    }
    for (i <- 0 until 16) {
      io.predicted_pix7(i) := 0.U
    }
    for (i <- 0 until 16) {
      io.predicted_pix8(i) := 0.U
    }
  }

}