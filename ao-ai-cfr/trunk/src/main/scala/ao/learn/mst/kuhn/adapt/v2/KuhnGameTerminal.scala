package ao.learn.mst.kuhn.adapt.v2

import ao.learn.mst.kuhn.state.{KuhnOutcome, KuhnState}
import ao.learn.mst.gen2.game.{ExtensiveGameTerminal, ExtensiveGameDecision}
import ao.learn.mst.gen2.solve.ExpectedValue

/**
 * User: ao
 * Date: 26/07/12
 * Time: 9:33 PM
 */

case class KuhnGameTerminal(delegate : KuhnState)
    extends ExtensiveGameTerminal
{
  def payoff = {
    val outcome: KuhnOutcome =
      delegate.stake.toOutcome(delegate.winner.get)

    ExpectedValue(
      outcome.toSeq)
  }
}
