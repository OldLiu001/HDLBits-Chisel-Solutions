//> using scala "2.13.12"
//> using dep "org.chipsalliance::chisel:6.6.0"
//> using plugin "org.chipsalliance:::chisel-plugin:6.6.0"
//> using options "-unchecked", "-deprecation", "-language:reflectiveCalls", "-feature", "-Xcheckinit", "-Ywarn-dead-code", "-Ywarn-unused", "-Ymacro-annotations"

import chisel3._
import chisel3.util._
import _root_.circt.stage.ChiselStage

object Main extends App {
    println(
        ChiselStage.emitSystemVerilog(
            new top_module,
            firtoolOpts = Array("-disable-all-randomization", "-strip-debug-info")
        )
    )
}

class top_module extends RawModule {
    val in = IO(Input(UInt(32.W)))
    val out = IO(Output(UInt(32.W)))

    val w = in.asTypeOf(Vec(4, UInt(8.W)))
    val w2 = Wire(Vec(4, UInt(8.W)))
    for (i <- 0 until 4) {
        w2(i) := w(3 - i)
    }
    out := w2.asUInt
}