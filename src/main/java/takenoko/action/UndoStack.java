package takenoko.action;

import java.util.ArrayList;
import java.util.List;

public class UndoStack {
    private final List<UndoAction> stack;

    public UndoStack() {
        stack = new ArrayList<>();
    }

    public void push(UndoAction action) {
        stack.add(action);
    }

    public UndoAction pop() {
        return stack.remove(stack.size() - 1);
    }

    public void clear() {
        stack.clear();
    }

    public int size() {
        return stack.size();
    }
}
