package ao.ai.opp_model.decision.context.immediate;

import ao.ai.opp_model.decision.attr.AttributePool;
import ao.ai.opp_model.decision.context.ContextDomain;
import ao.ai.opp_model.decision.context.ActionContext;
import ao.ai.opp_model.decision.data.ContextImpl;
import ao.ai.opp_model.decision.domain.*;

/**
 *
 */
public class FirstActContext
        extends ContextImpl
        implements ActionContext
{
    //--------------------------------------------------------------------
    public FirstActContext(AttributePool pool, GenericContext ctx)
    {
        add(pool.fromEnum(
                BetsToCall.fromBets(ctx.betsToCall())));

        add(pool.fromEnum(
                ActivePosition.fromPosition(
                        ctx.numOpps(), ctx.activePosition())));

        add(pool.fromEnum(
                ActiveOpponents.fromActiveOpps(
                        ctx.numActiveOpps())));

        add(pool.fromEnum(
                BetRatio.fromBetRatio(
                        ctx.betRatio())));
        add(pool.fromEnum(
                PotOdds.fromPotOdds(
                        ctx.immedatePotOdds())));
        add(pool.fromEnum(
                PotRatio.fromPotRatio(
                        ctx.potRatio())));
    }

    
    //--------------------------------------------------------------------
    public ContextDomain domain()
    {
        return ContextDomain.FIRST_ACT;
    }
}