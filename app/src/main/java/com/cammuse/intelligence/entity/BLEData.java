package com.cammuse.intelligence.entity;

public class BLEData {

    private int equit_type;
    private int equit_id;// 为16进制
    private int mode_id;
    private boolean isAT;
    private int mode_bar;
    private int gas_degree;

    public BLEData() {
    }

    public BLEData(int equit_type, int equit_id, int mode_id, boolean isAT,
                   int mode_bar, int gas_degree) {
        this.equit_type = equit_type;
        this.equit_id = equit_id;
        this.mode_id = mode_id;
        this.isAT = isAT;
        this.mode_bar = mode_bar;
        this.gas_degree = gas_degree;
    }

    public int getEquit_type() {
        return equit_type;
    }

    public void setEquit_type(int equit_type) {
        this.equit_type = equit_type;
    }

    public int getEquit_id() {
        return equit_id;
    }

    public void setEquit_id(int equit_id) {
        this.equit_id = equit_id;
    }

    public int getMode_id() {
        return mode_id;
    }

    public void setMode_id(int mode_id) {
        this.mode_id = mode_id;
    }

    public boolean isAT() {
        return isAT;
    }

    public void setAT(boolean isAT) {
        this.isAT = isAT;
    }

    public int getMode_bar() {
        return mode_bar;
    }

    public void setMode_bar(int mode_bar) {
        this.mode_bar = mode_bar;
    }

    public int getGas_degree() {
        return gas_degree;
    }

    public void setGas_degree(int gas_degree) {
        this.gas_degree = gas_degree;
    }

}
