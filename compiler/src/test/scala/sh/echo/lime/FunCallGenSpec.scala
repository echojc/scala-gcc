package sh.echo.lime

class FunCallGenSpec extends GenBaseSpec {

  it("can call another function") {
    val ms = compile {
      """(def one () 1)
        |(def foo () (one))""".stripMargin
    }
    ms(s"one()$O") shouldBe List(
      "LDC 1",
      box("J"),
      "ARETURN"
    )
    ms(s"foo()$O") shouldBe List(
      "INVOKESTATIC $default.one ()Ljava/lang/Object;",
      "ARETURN"
    )
  }

  it("can call a function with arguments") {
    val ms = compile {
      """(def foo (a) a)
        |(def bar () (foo 1))""".stripMargin
    }
    ms(s"foo($O)$O") shouldBe List(
      "ALOAD 0",
      "ARETURN"
    )
    ms(s"bar()$O") shouldBe List(
      "LDC 1",
      box("J"),
      "INVOKESTATIC $default.foo ()Ljava/lang/Object;",
      "ARETURN"
    )
  }
}