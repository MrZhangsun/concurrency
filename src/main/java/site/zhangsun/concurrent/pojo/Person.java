package site.zhangsun.concurrent.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * @author Murphy
 */
@Data
@ToString
public class Person {
    private static int steps;
    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        Person.steps = steps;
    }
}
