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

class my_dff extends BlackBox {
    val io = IO(new Bundle {
        val clk, d = Input(Bool())
        val q = Output(Bool())
    })
}

class top_module extends RawModule {
    val clk, d = IO(Input(UInt(1.W)))
    val q = IO(Output(UInt(1.W)))

    val dffs = Array(Module(new my_dff), Module(new my_dff), Module(new my_dff))
    for ((dff, i) <- dffs.zipWithIndex) {
        dff.io.clk := clk
        if (i > 0) {
            dff.io.d := dffs(i - 1).io.q
        } else {
            dff.io.d := d
        }
    }
    q := dffs(dffs.length - 1).io.q
}