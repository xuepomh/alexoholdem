package ao.bucket.index.post_flop.common;

import ao.bucket.index.card.CanonSuit;
import static ao.bucket.index.card.CanonSuit.*;

import java.util.*;

/**
 * Date: Sep 15, 2008
 * Time: 4:09:49 PM
 */
public enum CanonSuitSet
{
    //--------------------------------------------------------------------
    F(FIRST),
    FS(FIRST, SECOND),
    FST(FIRST, SECOND, THIRD),
    FSR(FIRST, SECOND, FOURTH),
    FSTR(FIRST, SECOND, THIRD, FOURTH),
    FT(FIRST, THIRD),
    FTR(FIRST, THIRD, FOURTH),
    FR(FIRST, FOURTH),

    S(SECOND),
    ST(SECOND, THIRD),
    STF(SECOND, THIRD, FOURTH),
    SF(SECOND, FOURTH),

    T(THIRD),
    TR(THIRD, FOURTH),

    R(FOURTH),
    ;

    public static CanonSuitSet VALUES[] = values();


    //--------------------------------------------------------------------
    public static CanonSuitSet valueOf(Set<CanonSuit> cases)
    {
        for (CanonSuitSet riverCaseSet : VALUES)
        {
            if (riverCaseSet.SUIT_CASES.equals( cases ))
            {
                return riverCaseSet;
            }
        }
        System.err.println("unknown set: " + cases);
        return null;
    }


    //--------------------------------------------------------------------
    private final EnumSet<CanonSuit> SUIT_CASES;
    private final int                INDEXES[];


    //--------------------------------------------------------------------
    private CanonSuitSet(CanonSuit... suitCases)
    {
        SUIT_CASES = EnumSet.copyOf(Arrays.asList(suitCases));
        INDEXES     = computeIndex();
    }


    //--------------------------------------------------------------------
    public int size()
    {
        return SUIT_CASES.size();
    }

    public int index(CanonSuit of)
    {
        return INDEXES[ of.ordinal() ];
    }
    private int[] computeIndex()
    {
        List<CanonSuit> asList =
                new ArrayList<CanonSuit>(SUIT_CASES);
        int indexes[] = new int[ CanonSuit.VALUES.length ];
        for (CanonSuit suitCase : CanonSuit.VALUES)
        {
            indexes[ suitCase.ordinal() ] =
                    asList.indexOf( suitCase );
        }
        return indexes;
    }
}