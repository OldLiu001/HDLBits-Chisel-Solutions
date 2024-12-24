//> using scala "2.13.12"
//> using dep "org.chipsalliance::chisel:6.6.0"
//> using plugin "org.chipsalliance:::chisel-plugin:6.6.0"
//> using options "-unchecked", "-deprecation", "-language:reflectiveCalls", "-feature", "-Xcheckinit", "-Xfatal-warnings", "-Ywarn-dead-code", "-Ywarn-unused", "-Ymacro-annotations"

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
    val p1a, p1b, p1c, p1d, p1e, p1f = IO(Input(UInt(1.W)))
    val p2a, p2b, p2c, p2d = IO(Input(UInt(1.W)))
    val p1y, p2y = IO(Output(UInt(1.W)))

    p1y := Cat(p1a, p1b, p1c).andR | Cat(p1d, p1e, p1f).andR
    p2y := (p2a & p2b) | (p2c & p2d)
}
