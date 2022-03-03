package org.firstinspires.ftc.teamcode.taskmanagement;

import java.util.Arrays;
import java.util.List;

public class SeriesTask implements Task {

    private final List<Task> tasks;
    private int index;

    public SeriesTask(Task... tasks) {
        this.tasks = Arrays.asList(tasks);
        index = 0;
    }

    @Override
    public boolean perform() {
        if (index >= tasks.size()) {
            return true;
        }
        if (tasks.get(index).perform()) {
            index++;
        }
        return index >= tasks.size();
    }

    @Override
    public void cancel() {
        if (index < tasks.size()) {
            tasks.get(index).cancel();
        }
    }
}
