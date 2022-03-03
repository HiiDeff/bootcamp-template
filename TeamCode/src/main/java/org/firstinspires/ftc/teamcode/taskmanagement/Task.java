package org.firstinspires.ftc.teamcode.taskmanagement;

public interface Task {
    boolean perform();
    default void cancel() {}
}
