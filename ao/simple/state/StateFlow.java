package ao.simple.state;

import ao.simple.KuhnAction;

/**
 *
 */
public class StateFlow
{
    //--------------------------------------------------------------------
    public static final StateFlow FIRST_ACTION =
            new StateFlow(KuhnState.FIRST_ACTION, null);


    //--------------------------------------------------------------------
    private final KuhnState   STATE;
    private final KuhnOutcome OUTCOME;


    //--------------------------------------------------------------------
    public StateFlow(KuhnState   state,
                        KuhnOutcome outcome)
    {
        STATE   = state;
        OUTCOME = outcome;
    }


    //--------------------------------------------------------------------
    public KuhnState state()
    {
        return STATE;
    }

    public KuhnOutcome outcome()
    {
        return OUTCOME;
    }


    //--------------------------------------------------------------------
    public StateFlow advance(KuhnAction action)
    {
        return STATE.advance( action );
    }

    public boolean endOfHand()
    {
        return OUTCOME != null;
    }

    public boolean firstIsNextToAct()
    {
        return STATE.firstToAct();
    }
}
