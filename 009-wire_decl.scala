//> using scala "2.13.12"
//> using dep "org.chipsalliance::chisel:6.6.0"
//> using plugin "org.chipsalliance:::chisel-plugin:6.6.0"
//> using options "-unchecked", "-deprecation", "-language:reflectiveCalls", "-feature", "-Xcheckinit", "-Xfatal-warnings", "-Ywarn-dead-code", "-Ywarn-unused", "-Ymacro-annotations"

import chisel3._
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
    val a, b, c, d = IO(Input(UInt(1.W)))
    val out, out_n = IO(Output(UInt(1.W)))
    val a_and_b = Wire(UInt(1.W))
    val c_and_d = Wire(UInt(1.W))
    a_and_b := a & b
    c_and_d := c & d
    out := a_and_b | c_and_d
    out_n := ~out
}
