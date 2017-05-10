package com.bratek.devicemanager.SSHConnector;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by bratek on 24.03.17.
 */
public class DeviceStatistic {

    private String name;
    private Double rrqms;
    private Double wrqms;
    private Double rs;
    private Double ws;
    private Double rmbs;
    private Double wmbs;
    private Double avgrqsz;
    private Double avgrusz;
    private Double await;
    private Double rawait;
    private Double wawait;
    private Double svctm;
    private Double util;


    public DeviceStatistic(String s) {
        parseStatistics(s);
    }

    @Override
    public String toString() {
        return "DeviceStatistic{" +
                "name='" + name + '\'' +
                ", rrqms=" + rrqms +
                ", wrqms=" + wrqms +
                ", rs=" + rs +
                ", ws=" + ws +
                ", rmbs=" + rmbs +
                ", wmbs=" + wmbs +
                ", avgrqsz=" + avgrqsz +
                ", avgrusz=" + avgrusz +
                ", await=" + await +
                ", rawait=" + rawait +
                ", wawait=" + wawait +
                ", svctm=" + svctm +
                ", util=" + util +
                '}';
    }

    private void parseStatistics(String statistic){
        String name = StringUtils.substringBefore(statistic," ");

        String withoutName = statistic.replace(name,"").replaceAll(" ","");
        withoutName = withoutName.replaceAll(",",".");
        withoutName = withoutName.replaceAll(".[0-9]{2}", "$0 ");
        String[] parts = withoutName.split(" ");

        this.name = name;
        rrqms = Double.parseDouble(parts[0]);
        wrqms = Double.parseDouble(parts[1]);
        rs = Double.parseDouble(parts[2]);
        ws = Double.parseDouble(parts[3]);
        rmbs = Double.parseDouble(parts[4]);
        wmbs = Double.parseDouble(parts[5]);
        avgrqsz = Double.parseDouble(parts[6]);
        avgrusz = Double.parseDouble(parts[7]);
        await = Double.parseDouble(parts[8]);
        rawait = Double.parseDouble(parts[9]);
        wawait = Double.parseDouble(parts[10]);
        svctm = Double.parseDouble(parts[11]);
        util = Double.parseDouble(parts[12]);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRrqms() {
        return rrqms;
    }

    public void setRrqms(Double rrqms) {
        this.rrqms = rrqms;
    }

    public Double getWrqms() {
        return wrqms;
    }

    public void setWrqms(Double wrqms) {
        this.wrqms = wrqms;
    }

    public Double getRs() {
        return rs;
    }

    public void setRs(Double rs) {
        this.rs = rs;
    }

    public Double getWs() {
        return ws;
    }

    public void setWs(Double ws) {
        this.ws = ws;
    }

    public Double getRmbs() {
        return rmbs;
    }

    public void setRmbs(Double rmbs) {
        this.rmbs = rmbs;
    }

    public Double getWmbs() {
        return wmbs;
    }

    public void setWmbs(Double wmbs) {
        this.wmbs = wmbs;
    }

    public Double getAvgrqsz() {
        return avgrqsz;
    }

    public void setAvgrqsz(Double avgrqsz) {
        this.avgrqsz = avgrqsz;
    }

    public Double getAvgrusz() {
        return avgrusz;
    }

    public void setAvgrusz(Double avgrusz) {
        this.avgrusz = avgrusz;
    }

    public Double getAwait() {
        return await;
    }

    public void setAwait(Double await) {
        this.await = await;
    }

    public Double getRawait() {
        return rawait;
    }

    public void setRawait(Double rawait) {
        this.rawait = rawait;
    }

    public Double getWawait() {
        return wawait;
    }

    public void setWawait(Double wawait) {
        this.wawait = wawait;
    }

    public Double getSvctm() {
        return svctm;
    }

    public void setSvctm(Double svctm) {
        this.svctm = svctm;
    }

    public Double getUtil() {
        return util;
    }

    public void setUtil(Double util) {
        this.util = util;
    }
}
