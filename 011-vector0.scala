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
    val vec = IO(Input(UInt(3.W)))
    val outv = IO(Output(UInt(3.W)))
    val o0, o1, o2 = IO(Output(UInt(1.W)))

    outv := vec
    val bools = VecInit(vec.asBools)
    var i = 0
    for ( x <- Array(o0, o1, o2) ) {
        x := bools(i)
        i += 1
    }
}
