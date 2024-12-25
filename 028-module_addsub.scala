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

class add16 extends BlackBox {
    val io = IO(new Bundle {
        val a, b = Input(UInt(16.W))
        val cin = Input(UInt(1.W))
        val sum = Output(UInt(16.W))
        val cout = Output(Bool())
    })
}

class top_module extends RawModule {
    val a, b = IO(Input(UInt(32.W)))
    val sub = IO(Input(Bool()))
    val sum = IO(Output(UInt(32.W)))

    val n1 = a.asTypeOf(Vec(2, UInt(16.W)))
    val n2 = Mux(sub, ~b, b).asTypeOf(Vec(2, UInt(16.W)))
    val add1 = Module(new add16)
    val add2 = Array(Module(new add16), Module(new add16))

    add1.io.cin := sub
    add1.io.a := n1(0).asUInt
    add1.io.b := n2(0).asUInt

    add2(0).io.cin := 0.U
    add2(0).io.a := n1(1).asUInt
    add2(0).io.b := n2(1).asUInt
    add2(1).io.cin := 1.U
    add2(1).io.a := n1(1).asUInt
    add2(1).io.b := n2(1).asUInt

    sum := Cat(Mux(add1.io.cout, add2(1).io.sum, add2(0).io.sum), add1.io.sum)
}