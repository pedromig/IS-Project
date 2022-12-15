package mei.dei.uc.serdes;

public class AveragePair {
    public Double key;
    public Integer value;

   public AveragePair()  {

   };

    public AveragePair(Double key, Integer value) {
        this.key = key;
        this.value = value;
    }
    public static AveragePair from(Double key, Integer value) {
        return new AveragePair(key, value);
    }

    public Double getKey() {
        return key;
    }

    public Integer getValue() {
        return value;
    }

    public void setKey(Double key) {
        this.key = key;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
