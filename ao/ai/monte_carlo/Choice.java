package ao.ai.monte_carlo;

import ao.ai.opp_model.decision.classification.RealHistogram;
import ao.ai.opp_model.mix.MixedAction;
import ao.holdem.model.BettingRound;
import ao.holdem.model.act.SimpleAction;
import ao.state.HandState;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class Choice
{
    //----------------------------------------------------------------
    private final MixedAction  expected;
    private final SimpleAction actual;
    private final HandState    state;


    //----------------------------------------------------------------
    public Choice(RealHistogram<SimpleAction> expected,
                  SimpleAction                actual,
                  HandState                   state)
    {
        this.expected = MixedAction.fromHistogram( expected );
        this.actual   = actual;
        this.state    = state;
    }


    //----------------------------------------------------------------
    public HandState state()
    {
        return state;
    }

//    public MixedAction expected()
//    {
//        return expected;
//    }

//    public SimpleAction actual()
//    {
//        return actual;
//    }

    public double surprise()
    {
        assert actual != SimpleAction.FOLD;

        double callProb =
                expected.probabilityOf(SimpleAction.CALL);
        double raiseProb =
                expected.probabilityOf(SimpleAction.RAISE);
//        double raiseProb =
//                state.nextToActCanRaise()
//                ? expected.probabilityOf(SimpleAction.RAISE) : 0;

        double toCall = callProb / (callProb + raiseProb);

        return (actual == SimpleAction.CALL)
                ? toCall - 1
                : toCall;
        
//        double value =
//                (actual == SimpleAction.FOLD)
//                 ? -1
//                 : (actual == SimpleAction.CALL)
//                    ? 0 : 1;
//        double exp =
//                state.nextToActCanRaise()
//                ? expected.probabilityOf(SimpleAction.RAISE) -
//                  expected.probabilityOf(SimpleAction.FOLD)
//                : -(expected.probabilityOf(SimpleAction.FOLD) /
//                    (1.0 - expected.probabilityOf(SimpleAction.RAISE)));
//        return value - exp;
    }

    public @NotNull BettingRound round()
    {
        return state.round();
    }


    //----------------------------------------------------------------
    public String toString()
    {
        return actual.toString();
    }
}