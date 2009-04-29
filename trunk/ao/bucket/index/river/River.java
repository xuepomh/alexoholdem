package ao.bucket.index.river;

import ao.bucket.index.CanonIndexed;
import ao.bucket.index.card.CanonCard;
import ao.bucket.index.card.Order;
import ao.bucket.index.turn.Turn;
import ao.holdem.model.card.Card;
import ao.holdem.model.card.Community;
import ao.holdem.model.card.Hole;
import ao.odds.agglom.hist.RiverStrengths;
import ao.odds.eval.eval7.Eval7Faster;

/**
 * Date: Sep 1, 2008
 * Time: 8:36:13 PM
 */
public class River implements CanonIndexed
{
    //--------------------------------------------------------------------
    private final CanonCard RIVER;
    private final Card      RIVER_CARD;
    private final RiverCase CASE;
    private final Turn      TURN_CARDS;
    private final int       RANK_OFFSET;

    private       long      canonIndex = -1;


    //--------------------------------------------------------------------
    public River(
            Turn turnCards,
            Card riverCard)
    {
        Order     order  = turnCards.refineOrder(
                               Order.suited(riverCard.suit()));
        CanonCard hole[] = turnCards.refineHole(order);
        CanonCard flop[] = turnCards.refineFlop(order);
        CanonCard turn   = turnCards.refineTurn(order);
        RIVER            = order.asCanon(riverCard);

        {
            int precedences = 0, offset = 0;
            if (RIVER.suit() == hole[0].suit()) {
                precedences++;
                if (RIVER.rank().comesAfter( hole[0].rank() )) offset++;
            }
            if (RIVER.suit() == hole[1].suit()) {
                precedences++;
                if (RIVER.rank().comesAfter( hole[1].rank() )) offset++;
            }
            if (RIVER.suit() == flop[0].suit()) {
                precedences++;
                if (RIVER.rank().comesAfter( flop[0].rank() )) offset++;
            }
            if (RIVER.suit() == flop[1].suit()) {
                precedences++;
                if (RIVER.rank().comesAfter( flop[1].rank() )) offset++;
            }
            if (RIVER.suit() == flop[2].suit()) {
                precedences++;
                if (RIVER.rank().comesAfter( flop[2].rank() )) offset++;
            }
            if (RIVER.suit() == turn.suit()) {
                precedences++;
                if (RIVER.rank().comesAfter( turn.rank() )) offset++;
            }
            RANK_OFFSET = offset;
            CASE        = RiverCase.valueOf(
                            RIVER.suit(), precedences);
        }

        TURN_CARDS = turnCards;
        RIVER_CARD = riverCard;
    }


    //--------------------------------------------------------------------
    public RiverCase riverCase()
    {
        return CASE;
    }
    public CanonCard riverCard()
    {
        return RIVER;
    }
    public Turn turn()
    {
        return TURN_CARDS;
    }

    public long canonIndex()
    {
        if (canonIndex == -1) {
            canonIndex = calcCanonIndex();
        }
        return canonIndex;
    }
    private long calcCanonIndex()
    {
        long globalOffset =
                RiverLookup.offset(TURN_CARDS.canonIndex());
        RiverCaseSet caseSet =
                RiverRawLookup.caseSet(TURN_CARDS.canonIndex());
        int caseOffset = caseSet.offsetOf( CASE );

        return globalOffset +
               caseOffset +
               (RIVER.rank().ordinal() - RANK_OFFSET);
    }
    public long packedCanonIndex()
    {
        return canonIndex();
    }

    public short eval()
    {
        Hole      h = TURN_CARDS.hole().reify();
        Community c = TURN_CARDS.community();
        short     v = Eval7Faster.valueOf(
                h.a(), h.b(),
                c.flopA(),c.flopB(), c.flopC(),
                c.turn(), RIVER_CARD);
        return RiverStrengths.lookup( v );
    }


    //--------------------------------------------------------------------
    @Override public String toString() {
        return TURN_CARDS + "\t" + RIVER_CARD;
    }
}