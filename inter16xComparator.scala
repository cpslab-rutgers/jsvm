
import chisel3._
import chisel3.experimental.FixedPoint

class inter16xComparator() extends Module {
  val io = IO(new Bundle {
    val start       = Input(UInt(1.W))
    val sad_res     = Input(UInt(16.W))

    val mvx_min     = Output(SInt(4.W))
    val mvy_min     = Output(SInt(4.W))
    val address_min = Output(UInt(10.W))
    val min_sad     = Output(UInt(16.W))
  })

  val min_sad_reg      = RegInit(65535.U(16.W))
  val min_mvx_reg      = RegInit(0.S(4.W))
  val min_mvy_reg      = RegInit(0.S(4.W))
  val min_address_reg  = RegInit(264.U(10.W))

  // 0 - North | 1 - East | 2 - South | 3 - West | 4 - Middle |
  val pos      = RegInit(0.U(3.W))
  val init_mid = RegInit(264.U(10.W))
  val init_mvx = RegInit(0.S(4.W))
  val init_mvy = RegInit(0.S(4.W))

  when(io.start === 1.U) {
    when(io.sad_res < min_sad_reg) {
      min_sad_reg := io.sad_res
      when(pos === 0.U) {
        min_address_reg := init_mid - 32.U
        min_mvy_reg := init_mvy + 1.S
        min_mvx_reg := init_mvx
      }.elsewhen(pos === 1.U) {
        min_address_reg := init_mid + 1.U
        min_mvx_reg := init_mvx + 1.S
        min_mvy_reg := init_mvy
      }.elsewhen(pos === 2.U) {
        min_address_reg := init_mid + 32.U
        min_mvy_reg := init_mvy - 1.S
        min_mvx_reg := init_mvx
      }.elsewhen(pos === 3.U) {
        min_address_reg := init_mid - 1.U
        min_mvx_reg := init_mvx - 1.S
        min_mvy_reg := init_mvy
      }.otherwise {
        min_address_reg := init_mid
        min_mvx_reg := init_mvx
        min_mvy_reg := init_mvy
      }
    }
    when(pos === 4.U) {
      init_mid := min_address_reg
      init_mvx := min_mvx_reg
      init_mvy := min_mvy_reg
      io.min_sad := min_sad_reg
      io.mvx_min := min_mvx_reg
      io.mvy_min := min_mvy_reg
      io.address_min := min_address_reg
      pos := 0.U
    }.otherwise {
      pos := pos + 1.U
    }
  }

  io.min_sad     := min_sad_reg
  io.mvx_min     := init_mvx
  io.mvy_min     := init_mvy
  io.address_min := init_mid

}


//val best_sad_reg = RegInit(65535.U(16.W))
//val best_mvx_reg = RegInit(0.U(8.W))
//val best_mvy_reg = RegInit(0.U(8.W))
//val best_address_reg = RegInit(328.U(10.W))

//when(io.sad_res < best_sad_reg) {
//  best_sad_reg := io.sad_res