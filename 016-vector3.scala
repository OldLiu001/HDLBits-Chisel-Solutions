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
    val a, b, c, d, e, f = IO(Input(UInt(5.W)))
    val w, x, y, z = IO(Output(UInt(8.W)))

    val t = Cat(a, b, c, d, e, f, "b11".U).asTypeOf(Vec(4, UInt(8.W)))
    var i = 0
    for (x <- Array(w, x, y, z).reverseIterator) {
        x := t(i)
        i += 1
    }
}