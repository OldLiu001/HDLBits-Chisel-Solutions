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

class my_dff8 extends BlackBox {
    val io = IO(new Bundle {
        val clk = Input(Bool())
        val d = Input(UInt(8.W))
        val q = Output(UInt(8.W))
    })
}

class top_module extends RawModule {
    val clk = IO(Input(UInt(1.W)))
    val d = IO(Input(UInt(8.W)))
    val sel = IO(Input(UInt(2.W)))
    val q = IO(Output(UInt(8.W)))

    val dffs = Array(Module(new my_dff8), Module(new my_dff8), Module(new my_dff8))
    for ((dff, i) <- dffs.zipWithIndex) {
        dff.io.clk := clk
        if (i > 0) {
            dff.io.d := dffs(i - 1).io.q
        } else {
            dff.io.d := d
        }
    }
    val qs = dffs.map(_.io.q)
    q := MuxLookup(sel, 0.U)(Seq(
        (0.U) -> d,
        (1.U) -> qs(0),
        (2.U) -> qs(1),
        (3.U) -> qs(2)
    ))
}