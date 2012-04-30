package ao.learn.mst.kuhn.play

import ao.learn.mst.kuhn.card.KuhnCardSequence
import ao.learn.mst.kuhn.state.{KuhnPosition, KuhnState, KuhnOutcome}


//----------------------------------------------------------------------------------------------------------------------
class KuhnDealer
{
  //--------------------------------------------------------------------------------------------------------------------
  def playHand(
      firstPlayer : KuhnPlayer,
      lastPlayer  : KuhnPlayer,
      cards       : KuhnCardSequence) : KuhnOutcome =
  {
    playHand(firstPlayer, lastPlayer,
             new KuhnState(cards))
  }

  private def playHand(
      firstPlayer : KuhnPlayer,
      lastPlayer  : KuhnPlayer,
      state       : KuhnState) : KuhnOutcome =
  {
    state.winner match {
      case None => {
        val nextToAct = state.nextToAct.get match {
          case KuhnPosition.FirstToAct => firstPlayer
          case KuhnPosition.LastToAct  => lastPlayer
        }

        playHand(firstPlayer, lastPlayer,
                 state.act( nextToAct.act(state) ))
      }

      case Some(winner) => {
        state.stake.toOutcome(winner)
      }
    }
  }
}