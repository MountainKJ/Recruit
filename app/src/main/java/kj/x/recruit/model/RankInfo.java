package kj.x.recruit.model;

import java.io.Serializable;

public class RankInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String wxId;
    private String wxNick;
    private int festivalType;
    private int costSecond;
    private int rank;

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public String getWxNick() {
        return wxNick;
    }

    public void setWxNick(String wxNick) {
        this.wxNick = wxNick;
    }

    public int getFestivalType() {
        return festivalType;
    }

    public void setFestivalType(int festivalType) {
        this.festivalType = festivalType;
    }

    public int getCostSecond() {
        return costSecond;
    }

    public void setCostSecond(int costSecond) {
        this.costSecond = costSecond;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
