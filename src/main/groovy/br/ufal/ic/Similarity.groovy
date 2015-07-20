package br.ufal.ic

/**
 * Created by huxley on 18/07/15.
 */
class Similarity {
    private String code1
    private String code2
    private double threshold
    private String output
    public String getCode() {
        return code;
    }

    public void setCode1(code1) {
        this.code1 = code1;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode(code2) {
        this.code2 = code2;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(threshold) {
        this.threshold = threshold;
    }


    public String toString() {
        return threshold;
    }
    public boolean isValid() {
        return code1 && code2 && threshold <= 1 && threshold >= 0
    }
}
