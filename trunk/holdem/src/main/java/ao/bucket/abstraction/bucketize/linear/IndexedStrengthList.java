package ao.bucket.abstraction.bucketize.linear;

import ao.bucket.abstraction.access.tree.BucketTree;
import ao.bucket.index.detail.CanonDetail;
import ao.bucket.index.detail.range.CanonRange;
import ao.bucket.index.detail.river.ProbabilityEncoding;
import ao.bucket.index.detail.river.RiverEvalLookup;
import ao.bucket.index.detail.turn.TurnDetails;
import ao.holdem.model.Round;
import ao.util.math.Calc;

import java.util.Arrays;

/**
 * User: alex
 * Date: 15-May-2009
 * Time: 5:47:00 PM
 */
public class IndexedStrengthList
{
    //--------------------------------------------------------------------
    public static IndexedStrengthList
            strengths(BucketTree.Branch branch)
    {
        if (branch.round() == Round.RIVER) {
            return strengthsRiver(branch);
        } else {
            return strengthsPreRiver(branch);
        }
    }

    private static IndexedStrengthList
            strengthsRiver(BucketTree.Branch branch)
    {
        int        nRivers       = 0;
        CanonRange toBucketize[] =
                new CanonRange[ branch.parentCanons().length ];
        for (int i = 0; i < branch.parentCanons().length; i++) {

            int canonIndex = branch.parentCanons()[i];
            toBucketize[ i ] = TurnDetails.lookup(canonIndex).range();
            nRivers += toBucketize[ i ].count();
        }
        Arrays.sort(toBucketize);

        final int                 nextIndex[] = {0};
        final IndexedStrengthList rivers =
                new IndexedStrengthList( nRivers );
        RiverEvalLookup.traverse(
                toBucketize,
                new RiverEvalLookup.VsRandomVisitor() {
                    public void traverse(
                            long   canonIndex,
                            double strengthVsRandom,
                            byte   represents) {

                        rivers.set(nextIndex[0]++,
                                   canonIndex,
                                   strengthVsRandom,
                                   represents);
                    }
                });

        return rivers;
    }

    private static IndexedStrengthList
            strengthsPreRiver(BucketTree.Branch branch)
    {
        CanonDetail details[] = branch.details();
        IndexedStrengthList strengths =
                new IndexedStrengthList(details.length);

        for (int i = 0; i < details.length; i++) {
            strengths.set(i,
                          details[ i ].canonIndex(),
                          details[ i ].strength(),
                          details[ i ].represents());
        }

        return strengths;
    }


    //--------------------------------------------------------------------
    private final int  index     [];
    private final char strength  [];
    private final byte represents[];


    //--------------------------------------------------------------------
    public IndexedStrengthList(int length) {
        index      = new int [ length ];
        strength   = new char[ length ];
        represents = new byte[ length ];
    }


    //--------------------------------------------------------------------
    public int length() {
        return index.length;
    }


    //--------------------------------------------------------------------
    public void set(int    i,
                    long   canonIndex,
                    double handStrength,
                    byte   sequenceRepresents) {
        index     [i] = (int) canonIndex;
        strength  [i] = ProbabilityEncoding.encodeWinProb( handStrength );
        represents[i] = sequenceRepresents;
    }


    //--------------------------------------------------------------------
    public long index(int i) {
        return Calc.unsigned(index[i]);
    }

    public double realStrength(int i)
    {
        return ProbabilityEncoding.decodeWinProb(strength[ i ]);
    }
    public char strength(int i)
    {
        return strength[ i ];
    }

    public byte represents(int i)
    {
        return represents[ i ];
    }
}
