package ao.learn.mst.kuhn.adapt.v2

import ao.learn.mst.gen2.player.FiniteAction
import ao.learn.mst.kuhn.card.KuhnCard._
import ao.learn.mst.gen2.prob.ActionProbabilityMass
import ao.learn.mst.kuhn.card.{KuhnCardSequence, KuhnCard}
import ao.learn.mst.gen2.game.{ExtensiveGameDecision, ExtensiveGameChance, ExtensiveGameNode}
import collection.immutable.{SortedSet, SortedMap}
import ao.learn.mst.kuhn.state.KuhnState

/**
 * KuhnGameDecision
 *
 * Date: 03/12/11
 * Time: 8:03 PM
 */
object KuhnGameChance
    extends ExtensiveGameChance
{
  //--------------------------------------------------------------------------------------------------------------------
  def actions : SortedSet[FiniteAction] = {
    val cardPermutations =
      for {
        firstCard  <- KuhnCard.values
        secondCard <- KuhnCard.values
        if secondCard != firstCard
      } yield (firstCard, secondCard)

    val possibilities: Set[Possibility] =
      for {
        ((firstCard, secondCard), index)
        <- cardPermutations.zipWithIndex
      } yield
        Possibility(index, firstCard, secondCard)

    SortedSet[FiniteAction]() ++
      possibilities

//    ( for {
//        ((firstCard, secondCard), index)
//        <- cardPermutations.zipWithIndex
//      } yield
//        Possibility(index, firstCard, secondCard)
//    )(scala.collection.breakOut)
  }


  //--------------------------------------------------------------------------------------------------------------------
  def probabilityMass : ActionProbabilityMass =
  {
    val possibilities = actions

    val uniformProbability : Double =
      1.0 / possibilities.size

    val possibilityProbabilities = SortedMap[FiniteAction, Double]() ++
      possibilities.map(_ -> uniformProbability)

    ActionProbabilityMass(
      possibilityProbabilities)
  }


  //--------------------------------------------------------------------------------------------------------------------
  def child(outcome: FiniteAction) = {
    val instance: Possibility =
      actions.toSeq(outcome.index).asInstanceOf[Possibility]

    val cardSequence =
      KuhnCardSequence(
        instance.firstPlayerCard,
        instance.secondPlayerCard)

    KuhnGameDecision(
      new KuhnState(cardSequence))
  }


  //--------------------------------------------------------------------------------------------------------------------
  case class Possibility(
      override val index : Int,

      firstPlayerCard  : KuhnCard,
      secondPlayerCard : KuhnCard)
        extends FiniteAction(index)
  {
    override def toString =
      firstPlayerCard + " / " + secondPlayerCard
  }
}