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
    val sel = IO(Input(UInt(3.W)))
    val data0, data1, data2, data3, data4, data5 = IO(Input(UInt(4.W)))
    val out = IO(Output(UInt(4.W)))

    out := MuxLookup(sel, 0.U)(Seq(
        0.U -> data0,
        1.U -> data1,
        2.U -> data2,
        3.U -> data3,
        4.U -> data4,
        5.U -> data5
    ))
}