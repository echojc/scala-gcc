package sh.echo.lime

class IOGenSpec extends GenBaseSpec {

  describe("integration tests") {
    it("strs things together") {
      val tc = compileAndLoad {
        """(def foo (a b)
          |  (str 1 a 2 b 3))""".stripMargin
      }
      tc.foo(Cons(23: JLong, 42: JLong), "aoeu") shouldBe "1'(23 42)2aoeu3"
    }

    it("prints to stdout") {
      val tc = compileAndLoad {
        """(def foo (a b)
          |  (put 1 a 2 b 3))""".stripMargin
      }
      val out = captureStdout {
        tc.foo(Cons(23: JLong, 42: JLong), "aoeu")
      }
      out shouldBe "1'(23 42)2aoeu3\n"
    }
  }

  describe("code gen") {
    it("strs things together") {
      val ms = compile {
        """(def foo (a)
          |  (str 1 a))""".stripMargin
      }
      ms(s"foo($O)$O") shouldBe List(
        "NEW java/lang/StringBuilder",
        "DUP",
        "INVOKESPECIAL java/lang/StringBuilder.<init> ()V",
        "LDC 1",
        box("J"),
        "INVOKEVIRTUAL java/lang/StringBuilder.append (Ljava/lang/Object;)Ljava/lang/StringBuilder;",
        "ALOAD 0",
        "INVOKEVIRTUAL java/lang/StringBuilder.append (Ljava/lang/Object;)Ljava/lang/StringBuilder;",
        "INVOKEVIRTUAL java/lang/StringBuilder.toString ()Ljava/lang/String;",
        "ARETURN"
      )
    }

    it("cats strings to print and returns them as well as println'ing") {
      val ms = compile {
        """(def foo (a)
          |  (put 1 a))""".stripMargin
      }
      ms(s"foo($O)$O") shouldBe List(
        "GETSTATIC java/lang/System.out : Ljava/io/PrintStream;",
        "NEW java/lang/StringBuilder",
        "DUP",
        "INVOKESPECIAL java/lang/StringBuilder.<init> ()V",
        "LDC 1",
        box("J"),
        "INVOKEVIRTUAL java/lang/StringBuilder.append (Ljava/lang/Object;)Ljava/lang/StringBuilder;",
        "ALOAD 0",
        "INVOKEVIRTUAL java/lang/StringBuilder.append (Ljava/lang/Object;)Ljava/lang/StringBuilder;",
        "INVOKEVIRTUAL java/lang/StringBuilder.toString ()Ljava/lang/String;",
        "DUP",
        "ASTORE 1",
        "INVOKEVIRTUAL java/io/PrintStream.println (Ljava/lang/String;)V",
        "ALOAD 1",
        "ARETURN"
      )
    }
  }
}