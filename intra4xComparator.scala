
import chisel3._
import chisel3.experimental.FixedPoint

class intra4xComparator() extends Module {
  val io = IO(new Bundle {
    val start     = Input(Bool())
    val sad_res   = Input(Vec(9, UInt(12.W)))
    val db0       = Input(Vec(16, SInt(9.W)))
    val db1       = Input(Vec(16, SInt(9.W)))
    val db2       = Input(Vec(16, SInt(9.W)))
    val db3       = Input(Vec(16, SInt(9.W)))
    val db4       = Input(Vec(16, SInt(9.W)))
    val db5       = Input(Vec(16, SInt(9.W)))
    val db6       = Input(Vec(16, SInt(9.W)))
    val db7       = Input(Vec(16, SInt(9.W)))
    val db8       = Input(Vec(16, SInt(9.W)))
    val pred_pix0 = Input(Vec(16, UInt(8.W)))
    val pred_pix1 = Input(Vec(16, UInt(8.W)))
    val pred_pix2 = Input(Vec(16, UInt(8.W)))
    val pred_pix3 = Input(Vec(16, UInt(8.W)))
    val pred_pix4 = Input(Vec(16, UInt(8.W)))
    val pred_pix5 = Input(Vec(16, UInt(8.W)))
    val pred_pix6 = Input(Vec(16, UInt(8.W)))
    val pred_pix7 = Input(Vec(16, UInt(8.W)))
    val pred_pix8 = Input(Vec(16, UInt(8.W)))

    val sad_best  = Output(UInt(12.W))
    val mode_best = Output(UInt(4.W))
    val pred_best = Output(Vec(16, UInt(8.W)))
    val diff_best = Output(Vec(16, SInt(9.W)))
  })

  when(io.start) {
    val sad0 = io.sad_res(0)
    val sad1 = io.sad_res(1)
    val sad2 = io.sad_res(2)
    val sad3 = io.sad_res(3)
    val sad4 = io.sad_res(4)
    val sad5 = io.sad_res(5)
    val sad6 = io.sad_res(6)
    val sad7 = io.sad_res(7)
    val sad8 = io.sad_res(8)

    val sad0or1 = Wire(UInt(12.W))
    when(sad0 < sad1) {
      sad0or1 := sad0
    }.otherwise{
      sad0or1 := sad1
    }
    val sad2or3 = Wire(UInt(12.W))
    when(sad2 < sad3) {
      sad2or3 := sad2
    }.otherwise {
      sad2or3 := sad3
    }
    val sad4or5 = Wire(UInt(12.W))
    when(sad4 < sad5) {
      sad4or5 := sad4
    }.otherwise {
      sad4or5 := sad5
    }
    val sad6or7 = Wire(UInt(12.W))
    when(sad6 < sad7) {
      sad6or7 := sad6
    }.otherwise {
      sad6or7 := sad7
    }
    val sad0123 = Wire(UInt(12.W))
    when(sad0or1 < sad2or3) {
      sad0123 := sad0or1
    }.otherwise {
      sad0123 := sad2or3
    }
    val sad4567 = Wire(UInt(12.W))
    when(sad4or5 < sad6or7) {
      sad4567 := sad4or5
    }.otherwise {
      sad4567 := sad6or7
    }
    val sad0to7 = Wire(UInt(12.W))
    when(sad0123 < sad4567) {
      sad0to7 := sad0123
    }.otherwise {
      sad0to7 := sad4567
    }
    val minSad = Wire(UInt(12.W))
    when(sad0to7 < sad8) {
      minSad := sad0to7
    }.otherwise {
      minSad := sad8
    }

    val minIndex = Wire(UInt(4.W))
    when(minSad === sad0) {
      minIndex := 0.U
    }.elsewhen(minSad === sad1) {
      minIndex := 1.U
    }.elsewhen(minSad === sad2) {
      minIndex := 2.U
    }.elsewhen(minSad === sad3) {
      minIndex := 3.U
    }.elsewhen(minSad === sad4) {
      minIndex := 4.U
    }.elsewhen(minSad === sad5) {
      minIndex := 5.U
    }.elsewhen(minSad === sad6) {
      minIndex := 6.U
    }.elsewhen(minSad === sad7) {
      minIndex := 7.U
    }.elsewhen(minSad === sad8) {
      minIndex := 8.U
    }.otherwise{
      minIndex := 2.U
    }

    io.sad_best := io.sad_res(minIndex)
    io.mode_best := minIndex

    io.pred_best := Mux(minIndex === 0.U, io.pred_pix0,
      Mux(minIndex === 1.U, io.pred_pix1,
        Mux(minIndex === 2.U, io.pred_pix2,
          Mux(minIndex === 3.U, io.pred_pix3,
            Mux(minIndex === 4.U, io.pred_pix4,
              Mux(minIndex === 5.U, io.pred_pix5,
                Mux(minIndex === 6.U, io.pred_pix6,
                  Mux(minIndex === 7.U, io.pred_pix7,
                    io.pred_pix8))))))))

    io.diff_best := Mux(minIndex === 0.U, io.db0,
      Mux(minIndex === 1.U, io.db1,
        Mux(minIndex === 2.U, io.db2,
          Mux(minIndex === 3.U, io.db3,
            Mux(minIndex === 4.U, io.db4,
              Mux(minIndex === 5.U, io.db5,
                Mux(minIndex === 6.U, io.db6,
                  Mux(minIndex === 7.U, io.db7,
                    io.db8))))))))

  }.otherwise{
    io.sad_best := 4095.U
    io.mode_best := 0.U
    for (i <- 0 until 16) {
      io.pred_best(i) := 0.U
    }
    for (i <- 0 until 16) {
      io.diff_best(i) := 0.S
    }
  }
}