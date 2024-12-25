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
    val a, b, sel_b1, sel_b2 = IO(Input(Bool()))
    val out_assign, out_always = IO(Output(Bool()))

    out_assign := Mux(sel_b1 && sel_b2, b, a)
    when(sel_b1 && sel_b2) {
        out_always := b
    }.otherwise {
        out_always := a
    }
}