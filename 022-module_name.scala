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

class mod_a extends BlackBox {
    val io = IO(new Bundle {
        val in1, in2, in3, in4 = Input(Bool())
        val out1, out2 = Output(Bool())
    })
}

class top_module extends RawModule {
    val a, b, c, d = IO(Input(UInt(1.W)))
    val out1, out2 = IO(Output(UInt(1.W)))

    val mod = Module(new mod_a)
    for ((rhs, lhs) <- Array(a, b, c, d, mod.io.out1, mod.io.out2).zip(Array(
        mod.io.in1, mod.io.in2, mod.io.in3, mod.io.in4, out1, out2
    ))) {
        lhs := rhs
    }
}