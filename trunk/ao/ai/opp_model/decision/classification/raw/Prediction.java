package ao.ai.opp_model.decision.classification.raw;

import ao.ai.opp_model.decision.classification.RealHistogram;
import ao.ai.opp_model.decision.classification.processed.Classification;
import ao.ai.opp_model.decision.classification.processed.Distribution;
import ao.ai.opp_model.decision.classification.processed.Frequency;
import ao.ai.opp_model.decision.input.processed.data.DataPool;
import ao.ai.opp_model.decision.input.processed.data.LocalDatum;
import ao.ai.opp_model.decision.input.processed.data.State;
import ao.ai.opp_model.decision.input.raw.example.Datum;

/**
 *
 */
public class Prediction
{
    //--------------------------------------------------------------------
    private final Classification DELEGET;
    private final DataPool       POOL;


    //--------------------------------------------------------------------
    public Prediction(Classification classification,
                      DataPool       pool)
    {
        DELEGET = classification;
        POOL    = pool;
    }


    //--------------------------------------------------------------------
    public double probabilityOf(Datum datum)
    {
        return DELEGET.probabilityOf( datum.toDatum(POOL) );
    }


    //--------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public RealHistogram toRealHistogram()
    {
        if (DELEGET instanceof Distribution) return null;

        RealHistogram<LocalDatum> localHist =
                ((Frequency) DELEGET).asRealHistogram();

        RealHistogram rawHist = new RealHistogram();
        for (LocalDatum clazz : localHist.classes())
        {
            rawHist.add(((State) clazz).state(),
                        localHist.valueOf(clazz));
        }
        return rawHist;
    }

    public int sampleSize()
    {
        return DELEGET.sampleSize();
    }


    //--------------------------------------------------------------------
    public String toString()
    {
        return DELEGET.toString();
    }
}