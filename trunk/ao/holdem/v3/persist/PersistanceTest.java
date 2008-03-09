package ao.holdem.v3.persist;

import ao.holdem.v3.model.Avatar;
import ao.holdem.v3.model.Round;
import ao.holdem.v3.model.act.descrete.Action;
import ao.holdem.v3.model.card.chance.DeckCards;
import ao.holdem.v3.model.hand.Hand;
import ao.util.rand.Rand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class PersistanceTest
{
    //--------------------------------------------------------------------
    public static void main(String[] args)
    {
        Rand.nextBoolean();
        HoldemDb    db    = new HoldemDb("hand_db");
        HoldemViews views = new HoldemViews(db);
        HoldemDao   dao   = new HoldemDao(db, views);

        System.out.println("presisting random hands");
//        dao.presist( randomHand() );
        for (int i = 0; i < 10000; i++)
        {
            System.out.print(".");
            dao.presist( randomHand() );

            if ((i + 1) % 100 == 0) System.out.println();
        }
        System.out.println();

        System.out.println("retrieving");
        retrieve( dao );

        System.out.println("by prevalence");
        dao.printByPrevalence(5);

        db.close();
    }

    private static void retrieve(HoldemDao dao)
    {
        Avatar prevalent =
                dao.mostPrevalent(1)
                        .keySet().iterator().next();
        long start = System.currentTimeMillis();
        for (Hand h : dao.retrieve(prevalent, 5))
        {
//            System.out.println(h);
        }
        long delta = System.currentTimeMillis() - start;
        System.out.println("took: " + delta);
    }

    private static Hand randomHand()
    {
        List<String> names = new ArrayList<String>(Arrays.asList(
                "a", "b", "c", "d", "e", "f", "g",
                "h", "i", "j", "k", "l", "m", "n"));

        int name = Rand.nextInt( names.size() );
        Avatar dealer   = Avatar.local(names.get( name ));
        names.remove(name);
        Avatar opponent = Avatar.local(Rand.fromList(names));

        Hand hand = new Hand(Arrays.asList(opponent, dealer),
                             new DeckCards(),
                             Rand.fromArray(Round.VALUES));

        for (int i = Rand.nextInt(10) + 5; i >= 0; i--)
        {
            hand.addAction(opponent, Rand.fromArray(Action.VALUES));
            hand.addAction(dealer,   Rand.fromArray(Action.VALUES));
        }

        return hand;
    }
}
