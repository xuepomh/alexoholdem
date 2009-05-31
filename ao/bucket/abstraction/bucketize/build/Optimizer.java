package ao.bucket.abstraction.bucketize.build;

import lpsolve.LpSolve;
import lpsolve.LpSolveException;

/**
 * User: Cross Creek Marina
 * Date: 24-May-2009
 * Time: 8:18:36 PM
 */
public class Optimizer
{
    //--------------------------------------------------------------------
    public static void main(String[] args) {
//        double errors[][] =
//                {{3, 2, 1},
//                 {4, 3, 2},
//                 {5, 4, 3.1}};

        int holes        = 8192;
        int flopsPerHole = 8;

        double errors[][] = new double[holes][flopsPerHole * 2];
        for (int i = 0; i < errors.length; i++)
        {
            for (int j = 0; j < errors[i].length; j++)
            {
                errors[i][j] = Math.random() * 10;
            }
        }

        long before     = System.currentTimeMillis();
        byte solution[] = optimize(errors, holes * flopsPerHole);
        System.out.println(
                "took " + (System.currentTimeMillis() - before));
//        System.out.println(Arrays.toString(solution));

    }


    //-------------------------------------------------------------------
    public static byte[] optimize(
            int    parentBucketReachPaths[],
            byte   subBucketCounts       [][],
            double subBucketingErrors    [][],
            int    nBuckets)
    {
        LpSolve solver = null;
        try
        {
            solver = LpSolve.makeLp(0, 0);
//            solver.setVerbose(LpSolve.IMPORTANT);

            return doOptimize(solver,
                              parentBucketReachPaths,
                              subBucketCounts,
                              subBucketingErrors,
                              nBuckets);
        }
        catch (LpSolveException e)
        {
            throw new Error( e );
        }
        finally
        {
            if (solver != null) {
                solver.deleteLp();
            }
        }
    }

    public static byte[] doOptimize(
            LpSolve solver,
            int     parentBucketReachPaths[],
            byte    subBucketCounts       [][],
            double  subBucketingErrors    [][],
            int     nBuckets) throws LpSolveException
    {
        validate(parentBucketReachPaths,
                 subBucketCounts,
                 subBucketingErrors);

        int nVars = 0;
        for (double bucketErr[] : subBucketingErrors) {
            nVars += bucketErr.length;
        }

        // add variables, objective function is sum of errors (WORKS)
        for (int i = 0; i < subBucketingErrors.length; i++)
        {
            for (double err : subBucketingErrors[i])
            {
                solver.addColumn(new double[]{
                        parentBucketReachPaths[i] * err});
            }
        }

        for (int i = 1; i <= nVars; i++) {
            solver.setInt(i, true);
        }

        // total buckets <= nBuckets (WORKS)
        double totalBuckets[]        = new double[nVars + 1];
        int    nextBucketGranularity = 0;
        for (byte subBucketCount[] : subBucketCounts) {
            for (int count : subBucketCount) {
                totalBuckets[ ++nextBucketGranularity ] = count;
            }
        }
        solver.addConstraint(totalBuckets, LpSolve.LE, nBuckets);

        // offsets into matrix
        int cumulativeOffset   = 0;
        int subBucketOffsets[] = new int[ subBucketCounts.length ];
        for (int i = 0; i < (subBucketCounts.length - 1); i++) {
            cumulativeOffset += subBucketCounts[i].length;
            subBucketOffsets[ i + 1 ] = cumulativeOffset;
        }

        // for each parent bucket, only one sub-bucketing (FAILS)
        for (int i = 0; i < subBucketCounts.length; i++) {
            double singleBucketing[] = new double[nVars + 1];
            for (int j = 0; j < subBucketCounts[i].length; j++) {
                singleBucketing[ subBucketOffsets[i] + j + 1 ] = 1;
            }
            solver.addConstraint(singleBucketing, LpSolve.EQ, 1);
        }

        // set objective function
//        solver.strSetObjFn("2 3 -2 3");

        solver.setMinim();

//        solver.setScaling(LpSolve.SCALE_GEOMETRIC |
//                          LpSolve.SCALE_EQUILIBRATE |
//                          LpSolve.SCALE_INTEGERS);

        // PRESOLVE_REDUCEGCD PRESOLVE_PROBEFIX
        //   PRESOLVE_PROBEREDUCE PRESOLVE_IMPLIEDSLK PRESOLVE_SENSDUALS
//        solver.setPresolve(
//                LpSolve.PRESOLVE_SENSDUALS,
//                solver.getPresolveloops());
        solver.solve();

        // (WORKS)
        byte   solution[] = new byte[ parentBucketReachPaths.length ];
        double var     [] = solver.getPtrVariables();
//        System.out.println(Arrays.toString(var));

        int nextVar = 0;
        for (int i = 0; i < subBucketCounts.length; i++) {
            for (int j = 0; j < subBucketCounts[i].length; j++) {
                solution[ i ] += subBucketCounts[i][j] * var[ nextVar++ ];
            }
        }

//        System.out.println(
//                "Value of objective function: " + solver.getObjective());
        return solution;
    }


    //--------------------------------------------------------------------
    public static byte[] optimize(
            double subBucketingErrors[][],
            int    nBuckets)
    {
        int parentBucketReachPaths[] =
                new int[ subBucketingErrors.length ];

        for (int i = 0; i < parentBucketReachPaths.length; i++)
        {
            parentBucketReachPaths[i] = 24 * 1000 * 1000;
        }

        return optimize(parentBucketReachPaths,
                        subBucketingErrors,
                        nBuckets);
    }
    public static byte[] optimize(
            int    parentBucketReachPaths[],
            double subBucketingErrors    [][],
            int    nBuckets)
    {
        byte subBucketCounts[][] =
                new byte[subBucketingErrors.length][];

        for (int i = 0; i < parentBucketReachPaths.length; i++)
        {
            subBucketCounts[i] = new byte[ subBucketingErrors[i].length ];
            for (byte j = 0; j < subBucketingErrors[i].length; j++)
            {
                subBucketCounts[i][j] = (byte)(j + 1);
            }
        }

        return optimize(parentBucketReachPaths,
                        subBucketCounts,
                        subBucketingErrors,
                        nBuckets);
    }

    private static void validate(
            int    parentBucketReachPaths[],
            byte   subBucketCounts[][],
            double subBucketingErrors[][])
    {
        assert parentBucketReachPaths.length == subBucketCounts.length &&
               parentBucketReachPaths.length == subBucketingErrors.length;

        for (int i = 0; i < parentBucketReachPaths.length; i++)
        {
            assert subBucketCounts[i].length == subBucketingErrors[i].length;
        }
    }
}
