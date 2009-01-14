package ao.bucket.abstraction;

import ao.bucket.abstraction.bucketize.BucketManager;
import ao.bucket.abstraction.bucketize.BucketizerImpl;
import ao.bucket.abstraction.tree.BucketTree;
import ao.bucket.index.detail.CanonDetails;
import ao.holdem.model.card.Hole;
import org.apache.log4j.Logger;

/**
 * Date: Oct 14, 2008
 * Time: 8:13:57 PM
 */
public class BucketizerTest
{
    //--------------------------------------------------------------------
    private static final Logger LOG =
            Logger.getLogger(BucketizerTest.class);


    //--------------------------------------------------------------------
    public static void main(String[] args)
    {
        testHoleLookup();
//        testFlopLookup();
//        testTurnLookup();
    }


    //--------------------------------------------------------------------
    public static void testHoleLookup()
    {
        LOG.info("testing hole bucket lookup");

        char  nBuckets = 20;
        int[] counts   = new int[ nBuckets ];

        BucketManager manager =
                new BucketManager( new BucketizerImpl() );

        BucketTree buckets = manager.bucketize(
                (byte) 2, (char) 4, (char) 8, (char) 16);

        for (char i = 0; i < Hole.CANONICAL_COUNT; i++)
        {
            System.out.println(
                    ((int) i)                              + "\t" +
                    CanonDetails.lookupHole( i ).example() + "\t" +
                    (int) buckets.get( i ));
            counts[ buckets.get(i) ]++;
        }

        System.out.println("summary:");
        for (int i = 0; i < counts.length; i++)
            System.out.println(i + "\t" + counts[i]);
    }


//    //--------------------------------------------------------------------
//    public static void testFlopLookup()
//    {
//        LOG.info("testing flop bucket lookup");
//
//        char  numHoleBuckets = 3;
//        HoleBucketLookup holeLookup =
//                new HoleBucketLookupImpl(new HoleBucketizerImpl());
//        BucketSet holeBuckets = holeLookup.buckets( numHoleBuckets );
//
//        char  numFlopBuckets = 9;
//        int[] counts         = new int[ numFlopBuckets ];
//
//        CommunityBucketLookup lookup =
//                new CommBucketLookupImpl(
//                        Round.FLOP, new FlopBucketizerImpl());
//        BucketSet buckets = lookup.buckets(holeBuckets, numFlopBuckets);
//        for (int i = 0; i < FlopLookup.CANONICAL_COUNT; i++)
//        {
//            counts[ buckets.bucketOf(i) ]++;
//        }
//
//        for (int i = 0; i < counts.length; i++)
//            System.out.println(i + "\t" + counts[i]);
//    }
//
//
//    //--------------------------------------------------------------------
//    public static void testTurnLookup()
//    {
//        LOG.info("testing turn bucket lookup");
//
//        char numHoleBuckets = 3;
//        HoleBucketLookup holeLookup =
//                new HoleBucketLookupImpl(new HoleBucketizerImpl());
//        BucketSet holeBuckets = holeLookup.buckets( numHoleBuckets );
//
//        char numFlopBuckets = 9;
//        CommunityBucketLookup flopLookup =
//                new CommBucketLookupImpl(
//                        Round.FLOP, new FlopBucketizerImpl());
//        BucketSet flopBuckets =
//                flopLookup.buckets(holeBuckets, numFlopBuckets);
//
//        char numTurnBuckets = 27;
//        CommunityBucketLookup turnLookup =
//                new CommBucketLookupImpl(
//                        Round.TURN, new TurnBucketizerImpl());
//        BucketSet turnBuckets =
//                turnLookup.buckets(flopBuckets, numTurnBuckets);
//
//        int[] counts = new int[ numTurnBuckets ];
//        for (int i = 0; i < TurnLookup.CANON_TURN_COUNT; i++)
//        {
//            counts[ turnBuckets.bucketOf(i) ]++;
//        }
//
//        for (int i = 0; i < counts.length; i++)
//            System.out.println(i + "\t" + counts[i]);
//    }
}
