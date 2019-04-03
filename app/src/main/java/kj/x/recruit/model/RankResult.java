package kj.x.recruit.model;

import java.io.Serializable;
import java.util.List;

public class RankResult implements Serializable {
    private static final long serialVersionUID =1L;
    private List<RankInfo> ranks;

    public List<RankInfo> getRanks() {
        return ranks;
    }

    public void setRanks(List<RankInfo> ranks) {
        this.ranks = ranks;
    }
}
