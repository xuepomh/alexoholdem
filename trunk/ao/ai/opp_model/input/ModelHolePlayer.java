package ao.ai.opp_model.input;

import ao.ai.opp_model.classifier.raw.Classifier;
import ao.ai.opp_model.classifier.raw.Predictor;
import ao.ai.opp_model.decision.classification.raw.Prediction;
import ao.ai.opp_model.decision.input.raw.example.Context;
import ao.ai.opp_model.decision.input.raw.example.Datum;
import ao.ai.opp_model.decision.input.raw.example.Example;
import ao.ai.opp_model.model.domain.HandStrength;
import ao.holdem.model.act.RealAction;
import ao.holdem.model.card.Community;
import ao.holdem.model.card.Hole;
import ao.odds.*;
import ao.persist.HandHistory;
import ao.persist.PlayerHandle;
import ao.state.HandState;
import ao.state.StateManager;

/**
 * 
 */
public class ModelHolePlayer extends LearningPlayer
{
    //--------------------------------------------------------------------
    public static class Factory
                            implements LearningPlayer.Factory
    {
        public LearningPlayer newInstance(
                boolean      publishActions,
                HandHistory  history,
                PlayerHandle player,
                Classifier   addTo,
                Predictor predictWith)
        {
            return new ModelHolePlayer(
                    publishActions, history, player, addTo, predictWith);
        }
    }


    //--------------------------------------------------------------------
    public ModelHolePlayer(
            boolean      publishActions,
            HandHistory  history,
            PlayerHandle player,
            Classifier   addTo,
            Predictor    predictWith)
    {
        super(publishActions, history, player, addTo, predictWith);
    }


    //--------------------------------------------------------------------
    protected Example makeExampleOf(
            StateManager env,
            Context      ctx,
            RealAction   act)
    {
        HandState state = env.head();

        Hole hole = env.cards().holeFor(
                        state.nextToAct().handle() );
        if (hole == null || !hole.bothCardsVisible()) return null;
        //if (!(actsLeft() == 0 && !act.isFold())) return null;

        Community community = env.cards().community();

        OddFinder oddFinder = new ApproximateOddFinder();
        Odds      actual    =
                    oddFinder.compute(
                            hole, community, state.numActivePlayers()-1);

        BlindOddFinder expectationFinder = new ApproxBlindOddFinder();
        Odds           expected          =
                            expectationFinder.compute(
                                    community, state.numActivePlayers());

        HandStrength actualDelta =
                HandStrength.fromPercent(
                                    actual.strengthVsRandom() -
                                        expected.strengthVsRandom());

        Prediction prediction = predict(ctx);
        if (prediction != null)
        {
//            System.out.println(playerId()                + "\t" +
//                               ctx.bufferedData().size() + "\t" +
//                               prediction                + "\t" +
//                               actualDelta);
        }
        return ctx.withTarget(new Datum( actualDelta ));
    }
}

