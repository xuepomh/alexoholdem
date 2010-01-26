package ao.bucket.index.detail;

import ao.bucket.index.canon.flop.FlopLookup;
import ao.bucket.index.canon.hole.HoleLookup;
import ao.bucket.index.canon.turn.TurnLookup;
import ao.bucket.index.detail.flop.FlopDetailFlyweight.CanonFlopDetail;
import ao.bucket.index.detail.turn.TurnDetailFlyweight.CanonTurnDetail;
import org.apache.log4j.Logger;

/**
 * Date: Jan 21, 2009
 * Time: 11:54:48 AM
 */
public class TestCanonDetails
{
    //--------------------------------------------------------------------
    private TestCanonDetails() {}


    //--------------------------------------------------------------------
    private static final Logger LOG =
            Logger.getLogger(TestCanonDetails.class);

    public static void main(String[] args)
    {
//        testHoleDetails();
//        testFlopDetails();
        testTurnDetails();
    }


    //--------------------------------------------------------------------
    public static void testHoleDetails()
    {
        for (char canonHole = 0;
                  canonHole < HoleLookup.CANONS;
                  canonHole++)
        {
            LOG.info( DetailLookup.lookupHole(canonHole) );
        }
    }


    //--------------------------------------------------------------------
    public static void testFlopDetails()
    {
        long turnCountTotal = 0;
        long turnCountMax   = Long.MIN_VALUE;
        long turnCountMin   = Long.MAX_VALUE;

        for (int canonFlop = 0;
                 canonFlop < FlopLookup.CANONS;
                 canonFlop++)
        {
            CanonFlopDetail details =
                    DetailLookup.lookupFlop( canonFlop );
            turnCountTotal += details.canonTurnCount();
            turnCountMax = Math.max(turnCountMax,
                                       details.canonTurnCount());
            turnCountMin = Math.min(turnCountMin,
                                       details.canonTurnCount());
        }

        LOG.info("turnCountTotal = " + turnCountTotal);
        LOG.info("turnCountMax   = " + turnCountMax);
        LOG.info("turnCountMin   = " + turnCountMin);
    }


    //--------------------------------------------------------------------
    public static void testTurnDetails()
    {
//        long riverCountTotal = 0;
//        long riverCountMax   = Long.MIN_VALUE;
//        long riverCountMin   = Long.MAX_VALUE;

        double totalStrength = 0;

        for (int canonTurn = 0;
                 canonTurn < TurnLookup.CANONS;
                 canonTurn++)
        {
            CanonTurnDetail details =
                    DetailLookup.lookupTurn( canonTurn );

            totalStrength += details.strength();
//            riverCountTotal += details.canonRiverCount();
//            riverCountMax = Math.max(riverCountMax,
//                                       details.canonRiverCount());
//            riverCountMin = Math.min(riverCountMin,
//                                       details.canonRiverCount());
        }

        LOG.info("totalStrength/n = " +
                    (totalStrength / TurnLookup.CANONS));

//        System.out.println("riverCountTotal = " + riverCountTotal);
//        System.out.println("riverCountMax   = " + riverCountMax);
//        System.out.println("riverCountMin   = " + riverCountMin);
    }
}