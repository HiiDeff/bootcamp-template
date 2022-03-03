package org.firstinspires.ftc.teamcode.taskmanagement;

public class SleepTask extends TimedTask {
    public SleepTask(int milliseconds) {
        setFinishTimeMillis(milliseconds);
    }

    @Override
    protected boolean performInternal() {
        return false; // This task is time-based, so TimedTask logic takes care of deciding when the task is finished.
    }
}
