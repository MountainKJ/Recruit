package kj.x.recruit.model;

import java.io.Serializable;

public class BooleanResult implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean result;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
