package org.firstinspires.ftc.teamcode.taskmanagement;

import com.qualcomm.robotcore.util.ElapsedTime;

public abstract class TimedTask implements Task {

    private ElapsedTime timer = null;
    private int finishTimeMillis = 10000;

    public boolean perform() {
        if (timer == null) {
            timer = new ElapsedTime();
        }
        if (timer.milliseconds() > finishTimeMillis) {
            cancel();
            return true;
        }
        return performInternal();
    }

    protected abstract boolean performInternal();

    protected void setFinishTimeMillis(int finishTimeMillis) {
        this.finishTimeMillis = finishTimeMillis;
    }
}
