//> using scala "2.13.12"
//> using dep "org.chipsalliance::chisel:6.6.0"
//> using plugin "org.chipsalliance:::chisel-plugin:6.6.0"
//> using options "-unchecked", "-deprecation", "-language:reflectiveCalls", "-feature", "-Xcheckinit", "-Ywarn-dead-code", "-Ywarn-unused", "-Ymacro-annotations"

import chisel3._
import chisel3.util._
import _root_.circt.stage.ChiselStage

object Main extends App {
    println(ChiselStage.emitSystemVerilog(
        new top_module, firtoolOpts = Array("-disable-all-randomization", "-strip-debug-info")
    ))
}

class top_module extends RawModule {
    val scancode = IO(Input(UInt(16.W)))
    val left, down, right, up = IO(Output(UInt(1.W)))

    val w = WireDefault("b0000".U(4.W))
    val bools = MuxLookup(scancode, "b0000".U)(Seq(
        "he06b".U -> "b1000".U,
        "he072".U -> "b0100".U,
        "he074".U -> "b0010".U,
        "he075".U -> "b0001".U,
    )).asBools
    left := bools(3)
    down := bools(2)
    right := bools(1)
    up := bools(0)
}