package model;


import model.characters.*;
import model.movement.Direction;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import static controller.AudioHandler.random;
import static controller.UserInterfaceController.*;
import static controller.constants.WaveConstants.MAX_ENEMY_SPAWN_RADIUS;
import static controller.constants.WaveConstants.MIN_ENEMY_SPAWN_RADIUS;
import static model.Utils.*;

public class SpawnThread extends Thread {

    public void setRunning(boolean running) {
        this.running = running;
    }

    private boolean running = true;


    @Override
    public void run() {
        while (true) {
            if (running) {
                try {
                    sleep((int) (3500 / (Math.pow(WaveManager.wave + 1, 0.2))));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                Point location = roundPoint(addUpPoints(EpsilonModel.getINSTANCE().getAnchor(),
                        multiplyPoint(new Direction(random.nextFloat(0, 360)).getDirectionVector(),
                                random.nextFloat(MIN_ENEMY_SPAWN_RADIUS.getValue(), MAX_ENEMY_SPAWN_RADIUS.getValue()))));
                Class<?>[] classes = Enemies.class.getClasses();
                GeoShapeModel model;
                Random random = new Random();
                int randomInt = random.nextInt(2);
                Class<?> entity = classes[randomInt];
                Constructor constructor;
                if (WaveManager.wave == 0 && entity.equals(Enemies.TrigorathModel.class)) {
                    entity = Enemies.SquarantineModel.class;
                }
                try {
                    constructor = entity.getConstructor( Point.class, String.class);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                try {
                    model = (GeoShapeModel) constructor.newInstance( location, getMainMotionPanelId());
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                WaveManager.waveEntities.add(model);
                model.getMovement().lockOnTarget(EpsilonModel.getINSTANCE().getModelId());
            }
            try {
                sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
