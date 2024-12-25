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

class add1 extends RawModule {
    val a, b, cin = IO(Input(UInt(1.W)))
    val cout, sum = IO(Output(UInt(1.W)))
    cout := (a & b) | (a & cin) | (b & cin)
    sum := a ^ b ^ cin
}

class top_module extends RawModule {
    val a, b = IO(Input(UInt(100.W)))
    val cin = IO(Input(UInt(1.W)))
    val cout, sum = IO(Output(UInt(100.W)))

    val av = a.asBools
    val bv = b.asBools
    val sumv, coutv = Wire(Vec(100, Bool()))
    val adders = Array.fill(100)(Module(new add1))
    for ((adder, i) <- adders.zipWithIndex) {
        adder.a := av(i)
        adder.b := bv(i)
        sumv(i) := adder.sum
        coutv(i) := adder.cout
        if (i > 0) {
            adder.cin := adders(i - 1).cout
        } else {
            adder.cin := cin
        }
    }
    cout := coutv.asUInt
    sum := sumv.asUInt
}