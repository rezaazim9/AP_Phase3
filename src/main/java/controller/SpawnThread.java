package controller;


import model.WaveManager;
import model.characters.*;
import model.movement.Direction;
import model.movement.Movable;

import java.awt.*;

import static controller.AudioHandler.random;
import static controller.UserInterfaceController.eliminateView;
import static controller.UserInterfaceController.getMainMotionPanelId;
import static controller.constants.WaveConstants.MAX_ENEMY_SPAWN_RADIUS;
import static controller.constants.WaveConstants.MIN_ENEMY_SPAWN_RADIUS;
import static model.Utils.*;
import static model.WaveManager.waveEntities;
import static model.characters.GeoShapeModel.allShapeModelsList;
import static model.collision.Collidable.collidables;

public class SpawnThread extends Thread {
    public static SpawnThread spawnThread=new SpawnThread();

    public void setRunning(boolean running) {
        this.running = running;
    }

    private boolean running = true;


    @Override
    public void run() {
        while (true) {
            while (running) {
                if (WaveManager.wave > 1) {
                    for (GeoShapeModel shapeModel : waveEntities) {
                        allShapeModelsList.remove(shapeModel);
                        collidables.remove(shapeModel);
                        Movable.movables.remove(shapeModel);
                        eliminateView(shapeModel.getModelId(), shapeModel.getMotionPanelId());
                    }
                    waveEntities.clear();
                    break;

                }
                try {
                    sleep((int) (2700 / (Math.pow(WaveManager.wave +1, 0.2))));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                Point location = roundPoint(addUpPoints(EpsilonModel.getINSTANCE().getAnchor(),
                        multiplyPoint(new Direction(random.nextFloat(0, 360)).getDirectionVector(),
                                random.nextFloat(MIN_ENEMY_SPAWN_RADIUS.getValue(), MAX_ENEMY_SPAWN_RADIUS.getValue()))));
                GeoShapeModel model;
                if (WaveManager.wave == 1) {
                    model = new SquarantineModel(location, getMainMotionPanelId());
                } else {
                    model = switch (random.nextInt(0, 2)) {
                        case 0 -> new SquarantineModel(location, getMainMotionPanelId());
                        case 1 -> new TrigorathModel(location, getMainMotionPanelId());
                        default -> null;
                    };
                }
                if (model != null) {
                    WaveManager.waveEntities.add(model);
                    model.getMovement().lockOnTarget(EpsilonModel.getINSTANCE().getModelId());
                }

            }
            try {
                sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
