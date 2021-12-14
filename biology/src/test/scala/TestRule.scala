import org.scalatest.flatspec._
import org.scalatest.matchers._


class TestRule extends AnyFlatSpec with should.Matchers {
  "Rule should" should "conevrt (int, int, int)" in {
    val rule110 = Main.rule110(_)

    rule110(1, 1, 1) should be (0)
    rule110(1, 1, 0) should be (1)
    rule110(1, 0, 1) should be (1)
    rule110(1, 0, 0) should be (0)
    rule110(0, 1, 1) should be (1)
    rule110(0, 1, 0) should be (1)
    rule110(0, 0, 1) should be (1)
    rule110(0, 0, 0) should be (0)
  }

}
