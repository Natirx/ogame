package data.propertyproxy;

import lombok.ToString;

import java.io.Serializable;

@ToString
public class IntegerProperty implements GeneratedProperty<Integer>, Serializable {
    private static final long serialVersionUID = -347165975569407292L;
    private final int generateCount;

    public IntegerProperty(int min, int max) {
        generateCount = (int)(Math.random()*((max-min)+1))+min;
    }

    public Integer getValue() {
        return generateCount;
    }
}