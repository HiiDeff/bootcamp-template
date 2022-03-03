package org.firstinspires.ftc.teamcode.taskmanagement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ParallelTask implements Task {

    private final Set<Task> tasks;

    public ParallelTask(Task... tasks) {
        this.tasks = new HashSet<>(Arrays.asList(tasks));
    }

    @Override
    public boolean perform() {
        Set<Task> remove = new HashSet<>();
        for (Task task : tasks) {
            if (task.perform()) {
                remove.add(task);
            }
        }
        for (Task task : remove) {
            tasks.remove(task);
        }
        return tasks.size() == 0;
    }

    @Override
    public void cancel() {
        for (Task task : tasks) {
            task.cancel();
        }
    }
}
