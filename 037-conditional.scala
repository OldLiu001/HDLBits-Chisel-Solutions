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
    val a, b, c, d = IO(Input(UInt(8.W)))
    val min = IO(Output(UInt(8.W)))
    min := VecInit(a, b, c, d).reduceTree((x, y) => Mux(x < y, x, y))
}