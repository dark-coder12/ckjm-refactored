package gr.spinellis.ckjm;

import java.util.HashSet;

public class ClassMetrics {
    private IntegerMetric wmc;
    private IntegerMetric noc;
    private IntegerMetric rfc;
    private IntegerMetric cbo;
    private IntegerMetric dit;
    private IntegerMetric lcom;
    private StringMetric npm;

    JavaHandler javaInformation;

    public ClassMetrics() {
        this.wmc = new IntegerMetric();
        this.noc = new IntegerMetric();
        this.rfc = new IntegerMetric();
        this.cbo = new IntegerMetric();
        this.dit = new IntegerMetric();
        this.lcom = new IntegerMetric();
        this.npm = new StringMetric();
        javaInformation = new JavaHandler();
    }

    public void incWmc() { wmc.increment(); }

    public int getWmc() {
        return wmc.getValue();
    }

    public void incNoc() {
        noc.increment();
    }

    public int getNoc() {
        return noc.getValue();
    }

    public void setRfc(int r) {
        rfc = new IntegerMetric();
        rfc.value = r;
    }

    public int getRfc() {
        return rfc.getValue();
    }

    public void setDit(int d) {
        dit = new IntegerMetric();
        dit.value = d;
    }

    public int getDit() {
        return dit.getValue();
    }

    public void setCbo(int c) {
        cbo = new IntegerMetric();
        cbo.value = c;
    }

    public int getCbo() {
        return cbo.getValue();
    }

    public int getLcom() {
        return lcom.getValue();
    }

    public void setLcom(int l) {
        lcom = new IntegerMetric();
        lcom.value = l;
    }
    public int getCa() {
        return npm.getCount();
    }

    public String getNpm(){
        return npm.toString();
    }
    public void addAfferentCoupling(String name) {
        npm.addValue(name);
    }

    public void incNpm() {
        npm.increment();
    }

}