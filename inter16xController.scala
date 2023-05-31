
import chisel3._
import chisel3.experimental.FixedPoint
import chisel3.util.Enum

class inter16xController() extends Module {
  val io = IO(new Bundle {
    val done_charge = Input(UInt(1.W))
    val mvx_min     = Input(SInt(4.W))
    val mvy_min     = Input(SInt(4.W))
    val address_min = Input(UInt(11.W))

    val mvx         = Output(SInt(4.W))
    val mvy         = Output(SInt(4.W))
    val address     = Output(UInt(10.W))
    val start_sad   = Output(UInt(1.W))
    val start_comp  = Output(UInt(1.W))
    val done_ldps   = Output(UInt(1.W))
  })

  // start/finish signals
  io.start_sad := 0.U
  io.start_comp := 0.U
  io.done_ldps := 0.U

  // register values to center of search, update values in FSM
  val mvx_reg = RegInit(0.S(4.W))
  val mvy_reg = RegInit(0.S(4.W))
  val address_reg = RegInit(264.U(10.W))

  // 17x17=289 possible MB in reference area
  val visited = RegInit(VecInit(Seq.fill(512)(false.B)))

  // FSM
  val idle :: north :: calcNorth :: compareNorth :: east :: calcEast :: compareEast :: south :: calcSouth :: compareSouth :: west :: calcWest :: compareWest :: mid :: calcMid :: compareMid :: decision :: finish :: Nil = Enum(18)
  val state = RegInit(0.U(5.W))

  // Diamond
  when(io.done_charge === 0.U) {
    state := idle
  }.elsewhen(state === idle) {
    state := north
  }.elsewhen(state === north) {
    when(visited(io.address_min) === true.B) { //address_reg
      io.done_ldps := 1.U
      state := finish
    }.otherwise {
      address_reg := address_reg - 32.U
      mvy_reg := mvy_reg + 1.S
      state := calcNorth
    }
  }.elsewhen(state === calcNorth) {
    io.start_sad := 1.U
    io.start_comp := 1.U
    state := compareNorth
  }.elsewhen(state === compareNorth) {
    state := east
  }.elsewhen(state === east) {
    address_reg := address_reg + 33.U
    mvx_reg := mvx_reg + 1.S
    mvy_reg := mvy_reg - 1.S
    state := calcEast
  }.elsewhen(state === calcEast) {
    io.start_sad := 1.U
    io.start_comp := 1.U
    state := compareEast
  }.elsewhen(state === compareEast) {
    state := south
  }.elsewhen(state === south) {
    address_reg := address_reg + 31.U
    mvx_reg := mvx_reg - 1.S
    mvy_reg := mvy_reg - 1.S
    state := calcSouth
  }.elsewhen(state === calcSouth) {
    io.start_sad := 1.U
    io.start_comp := 1.U
    state := compareSouth
  }.elsewhen(state === compareSouth) {
    state := west
  }.elsewhen(state === west) {
    address_reg := address_reg - 33.U
    mvx_reg := mvx_reg - 1.S
    mvy_reg := mvy_reg + 1.S
    state := calcWest
  }.elsewhen(state === calcWest) {
    io.start_sad := 1.U
    io.start_comp := 1.U
    state := compareWest
  }.elsewhen(state === compareWest) {
    state := mid
  }.elsewhen(state === mid) {
    address_reg := address_reg + 1.U
    mvx_reg := mvx_reg + 1.S
    visited(address_reg) := true.B
    state := calcMid
  }.elsewhen(state === calcMid) {
    io.start_sad := 1.U
    io.start_comp := 1.U
    state := compareMid
  }.elsewhen(state === compareMid) {
    state := decision
  }.elsewhen(state === decision) {
    // if address min is center then stop
    when(io.address_min === address_reg) {
      io.done_ldps := 1.U
      state := finish
    }.otherwise {
      address_reg := io.address_min
      mvx_reg := io.mvx_min
      mvy_reg := io.mvy_min
      state := north
    }
  }.elsewhen(state === finish) {
    io.done_ldps := 1.U
    state := finish
  }.otherwise {
    state := idle
  }

  io.mvx := mvx_reg
  io.mvy := mvy_reg
  io.address := address_reg

}
