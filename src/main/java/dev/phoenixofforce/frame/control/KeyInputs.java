package dev.phoenixofforce.frame.control;

import java.util.HashMap;
import java.util.Map;

public class KeyInputs {

    private final Map<Integer, Boolean> inputs = new HashMap<>();

    public void put(Integer key, Boolean state) {
        inputs.put(key, state);
    }

    public boolean isPressed(int key) {
        return inputs.getOrDefault(key, false);
    }
}
