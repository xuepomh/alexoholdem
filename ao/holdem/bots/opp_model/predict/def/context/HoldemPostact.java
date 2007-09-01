package ao.holdem.bots.opp_model.predict.def.context;

import static ao.holdem.bots.opp_model.predict.def.NeuralUtils.asDouble;
import ao.holdem.def.state.env.TakenAction;

/**
 *
 */
public class HoldemPostact extends HoldemPreact
{
    //--------------------------------------------------------------------
    public HoldemPostact(GenericContext ctx)
    {
        super(ctx);

        double lastBetsToCallBool =
                asDouble(ctx.lastBetsToCalled() > 0);

        double lastActRaiseBool =
                asDouble(ctx.lastAct() == TakenAction.RAISE);

        addNeuralInput(
                lastBetsToCallBool,
                lastActRaiseBool);
    }
}