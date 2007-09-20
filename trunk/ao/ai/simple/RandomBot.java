package ao.ai.simple;

import ao.holdem.def.bot.AbstractBot;
import ao.holdem.model.act.Action;
import ao.util.rand.Rand;


/**
 *
 */
public class RandomBot extends AbstractBot
{
    //--------------------------------------------------------------------
    public Action act(Environment env)
    {
        return Rand.fromArray( Action.values() );
    }


    //--------------------------------------------------------------------
//    @Override
//    public String toString()
//    {
//        return "RandomBot";
//    }
}
