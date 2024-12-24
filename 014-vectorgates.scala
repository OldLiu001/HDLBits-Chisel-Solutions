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
    val a, b = IO(Input(UInt(3.W)))
    val out_or_bitwise = IO(Output(UInt(3.W)))
    val out_or_logical = IO(Output(UInt(1.W)))
    val out_not = IO(Output(UInt(6.W)))

    out_or_bitwise := a | b
    out_or_logical := a.orR || b.orR
    out_not := ~Cat(b, a)
}