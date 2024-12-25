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

class bcd_fadd extends BlackBox {
    val io = IO(new Bundle {
        val a, b = Input(UInt(4.W))
        val cin = Input(UInt(1.W))
        val sum = Output(UInt(4.W))
        val cout = Output(UInt(1.W))
    })
}

class top_module extends RawModule {
    val a, b = IO(Input(UInt(400.W)))
    val cin = IO(Input(UInt(1.W)))
    val cout = IO(Output(UInt(1.W)))
    val sum = IO(Output(UInt(400.W)))

    val av = a.asTypeOf(Vec(100, UInt(4.W)))
    val bv = b.asTypeOf(Vec(100, UInt(4.W)))
    val sumv = Wire(Vec(100, UInt(4.W)))
    val adders = Array.fill(100)(Module(new bcd_fadd))
    for ((adder, i) <- adders.zipWithIndex) {
        adder.io.a := av(i)
        adder.io.b := bv(i)
        sumv(i) := adder.io.sum
        if (i > 0) {
            adder.io.cin := adders(i - 1).io.cout
        } else {
            adder.io.cin := cin
        }
    }
    cout := adders(adders.length - 1).io.cout
    sum := sumv.asUInt
}