package edu.phystech.hw4.stepper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author kzlv4natoly
 */

public class Stepper {

    public enum Side {
        LEFT, RIGHT
    }

    private final List<Side> history = new ArrayList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition canStepLeft = lock.newCondition();
    private final Condition canStepRight = lock.newCondition();
    private boolean isLeftTurn = true;

    public void leftStep() {
        lock.lock();
        try {
            while (!isLeftTurn) {
                canStepLeft.await();
            }
            history.add(Side.LEFT);
            isLeftTurn = false;
            canStepRight.signalAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public void rightStep() {
        lock.lock();
        try {
            while (isLeftTurn) {
                canStepRight.await();
            }
            history.add(Side.RIGHT);
            isLeftTurn = true;
            canStepLeft.signalAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public List<Side> getHistory() {
        lock.lock();
        try {
            return new ArrayList<>(history);
        } finally {
            lock.unlock();
        }
    }
}
