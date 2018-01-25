package org.pasalab.experiment.automj;

public class Performance {
    private int partitions;
    private String query;
    private double minExecTime;
    private double maxExecTime;
    private double avgExecTime;
    private double execTimeStdDev;
    private double minOptTime;
    private double maxOptTime;
    private double avgOptTime;
    private double optTimeStdDev;
    public Performance(int p, String q, double minE,
                       double maxE, double avgE, double eStd,
                       double minO, double maxO, double avgO, double oStd) {
        this.partitions = p;
        this.query = q;
        this.minExecTime = minE;
        this.maxExecTime = maxE;
        this.avgExecTime = avgE;
        this.execTimeStdDev = eStd;
        this.minOptTime = minO;
        this.maxOptTime = maxO;
        this.avgOptTime = avgO;
        this.optTimeStdDev = oStd;
    }

    public int getPartitions() {
        return partitions;
    }

    public String getQuery() {
        return query;
    }

    public double getMinExecTime() {
        return minExecTime;
    }

    public double getMaxExecTime() {
        return maxExecTime;
    }

    public double getAvgExecTime() {
        return avgExecTime;
    }

    public double getExecTimeStdDev() {
        return execTimeStdDev;
    }

    public double getMinOptTime() {
        return minOptTime;
    }

    public double getMaxOptTime() {
        return maxOptTime;
    }

    public double getAvgOptTime() {
        return avgOptTime;
    }

    public double getOptTimeStdDev() {
        return optTimeStdDev;
    }
}
