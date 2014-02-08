package ao.bucket.index.detail.flop;

import ao.Infrastructure;
import ao.bucket.index.canon.flop.Flop;
import ao.bucket.index.enumeration.HandEnum;
import ao.odds.agglom.Odds;
import ao.odds.agglom.impl.PreciseHeadsUpOdds;
import ao.util.io.Dirs;
import ao.util.pass.Traverser;
import ao.util.persist.PersistentInts;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Arrays;

/**
 * Date: Jan 23, 2009
 * Time: 2:20:04 PM
 */
public class FlopOdds
{
    //--------------------------------------------------------------------
    public static void main(String[] args)
    {
        long minWins = Integer.MAX_VALUE;
        long maxWins = Integer.MIN_VALUE;

        long minLose = Integer.MAX_VALUE;
        long maxLose = Integer.MIN_VALUE;

        long minTies = Integer.MAX_VALUE;
        long maxTies = Integer.MIN_VALUE;

        for (int i = 0; i < Flop.CANONS; i++)
        {
            Odds odds = lookup(i);

            minWins = Math.min(minWins, odds.winOdds());
            maxWins = Math.max(maxWins, odds.winOdds());

            minLose = Math.min(minLose, odds.loseOdds());
            maxLose = Math.max(maxLose, odds.loseOdds());

            minTies = Math.min(minTies, odds.splitOdds());
            maxTies = Math.max(maxTies, odds.splitOdds());
        }

        LOG.info("minWins = " + minWins);
        LOG.info("maxWins = " + maxWins);

        LOG.info("minLose = " + minLose);
        LOG.info("maxLose = " + maxLose);

        LOG.info("minTies = " + minTies);
        LOG.info("maxTies = " + maxTies);
    }


    //--------------------------------------------------------------------
    private FlopOdds() {}
    private static final Logger LOG =
            Logger.getLogger(FlopOdds.class);

    private static final int SENTINAL = -1;


    //--------------------------------------------------------------------
    private static final File DIR = Dirs.get(Infrastructure.path(
            "lookup/canon/detail/flop_odds/"));

    private static final File FLAG_FILE  = new File(DIR,   "flag.int");
    private static final File WIN_FILE   = new File(DIR,   "wins.int");
    private static final File LOSE_FILE  = new File(DIR, "losses.int");
    private static final File SPLIT_FILE = new File(DIR, "splits.int");


    //--------------------------------------------------------------------
    private static final int[] WIN;
    private static final int[] LOSE;
    private static final int[] SPLIT;


    //--------------------------------------------------------------------
    static
    {
        LOG.debug("initializing");

        int[] wins = PersistentInts.retrieve(WIN_FILE);
        if (wins == null)
        {
            LOG.debug("starting from scratch");

            WIN   = oddsComponent();
            LOSE  = oddsComponent();
            SPLIT = oddsComponent();
        }
        else
        {
            WIN   = wins;
            LOSE  = PersistentInts.retrieve(LOSE_FILE);
            SPLIT = PersistentInts.retrieve(SPLIT_FILE);
        }

        if (! getIsPreComputed())
        {
            LOG.debug("resuming computation");

            computeOdds();
//            cheatComputeOdds();
            flush();
            setIsPreComputed();
        }
    }

    private static int[] oddsComponent()
    {
        int[] component = new int[ Flop.CANONS ];
        Arrays.fill( component, SENTINAL );
        return component;
    }


    //--------------------------------------------------------------------
//    private static void cheatComputeOdds()
//    {
//        for (int i = 0; i < FlopLookup.CANONS; i++)
//        {
//            Odds odds = FlopDetails.compact(i).headsUpOdds();
//
//            WIN  [ i ] = (int) odds.winOdds();
//            LOSE [ i ] = (int) odds.loseOdds();
//            SPLIT[ i ] = (int) odds.splitOdds();
//
//            if (odds.  winOdds() > Integer.MAX_VALUE ||
//                odds. loseOdds() > Integer.MAX_VALUE ||
//                odds.splitOdds() > Integer.MAX_VALUE) {
//                System.out.println("!!!!! OVERFLOW " + odds);
//            }
//        }
//    }
    private static void computeOdds()
    {
        LOG.debug("computing odds");

        final int[]  skinCount      = {0};
        final int[]  calcCount      = {0};
        final long[] milestoneStart = {0};

        HandEnum.uniqueFlops(
                new Traverser<Flop>() {
            public void traverse(Flop flop) {
                if (WIN[ flop.canonIndex() ] != SENTINAL) {
                    skinCount[0]++;
                    return;
                } else if (skinCount[0] != 0) {
                    LOG.info("skipped " + skinCount[0]);
                    skinCount[0] = 0;
                }

                Odds odds = PreciseHeadsUpOdds.INSTANCE.compute(
                        flop.hole().reify(), flop.community());

                int wins   = (int) odds.winOdds();
                int losses = (int) odds.loseOdds();
                int splits = (int) odds.splitOdds();

                WIN  [ flop.canonIndex() ] = wins;
                LOSE [ flop.canonIndex() ] = losses;
                SPLIT[ flop.canonIndex() ] = splits;

                checkpoint( calcCount[0]++, milestoneStart );
            }});
    }

    private static void checkpoint(
            int count, long[] milestoneStart)
    {
        if (milestoneStart[0] == 0)
            milestoneStart[0] = System.currentTimeMillis();

        if (count % 100 == 0)
            System.out.print(".");

        boolean milesoneReached = ((count + 1) % 5000 == 0);
        if (milesoneReached) {
            LOG.info(
                    " " + (count + 1) + ", completed 5000 in " +
                    (System.currentTimeMillis() - milestoneStart[0]));
        }

        if ((count + 1) % 100000 == 0) flush();

        if (milesoneReached)
            milestoneStart[0] = System.currentTimeMillis();
    }


    //--------------------------------------------------------------------
    public static Odds lookup(int canonFlopIndex)
    {
        return new Odds( WIN   [ canonFlopIndex ],
                         LOSE  [ canonFlopIndex ],
                         SPLIT [ canonFlopIndex ] );
    }


    //--------------------------------------------------------------------
    private static boolean getIsPreComputed()
    {
        return PersistentInts.retrieve(FLAG_FILE) != null;
    }

    private static void setIsPreComputed()
    {
        PersistentInts.persist(new int[]{1}, FLAG_FILE);
    }


    //--------------------------------------------------------------------
    private synchronized static void flush()
    {
        long start = System.currentTimeMillis();

        PersistentInts.persist(WIN,   WIN_FILE);
        PersistentInts.persist(LOSE,  LOSE_FILE);
        PersistentInts.persist(SPLIT, SPLIT_FILE);

        System.out.println("\nflushed checkpoint, took " +
                            (System.currentTimeMillis() - start));
    }

}
