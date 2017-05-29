package osrs;


import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import osrs.tasks.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Script.Manifest(name="QuickMining", description="Tutorial", properties="client=4; author=Chris; topic=999;")

public class QuickMining extends PollingScript<ClientContext> implements PaintListener {

    List<Task> taskList = new ArrayList<Task>();
    int startExp = 0;

    @Override
    public void start(){
        String userOptions[] = {"Bank", "Powermine"};
        String userChoice = ""+(String)JOptionPane.showInputDialog(null, "Bank or Powermine?", "QuickMining", JOptionPane.PLAIN_MESSAGE, null, userOptions, userOptions[1]);

        String oreOptions[] = {"Copper", "Clay", "Tin", "Iron", "Coal", "Gold", "Mith", "Addy"};
        String oreChoice = ""+(String)JOptionPane.showInputDialog(null, "What do you want to mine?", "QuickMining", JOptionPane.PLAIN_MESSAGE, null, oreOptions, oreOptions[0]);


        if(userChoice.equals("Bank")){
            taskList.add(new Bank(ctx));

            String locationOptions[] = {"Lumbridge Swamp", "Falador Dwarven"};
            String locationChoice = ""+(String)JOptionPane.showInputDialog(null, "Where do you want to mine?", "QuickMining", JOptionPane.PLAIN_MESSAGE, null, locationOptions, locationOptions[0]);

            if(locationChoice.equals("Lumbridge Swamp")) {
                taskList.add(new Walk(ctx, MConstants.LUMBRIDGE_SWAMP));
            } else if(locationChoice.equals("Falador Dwarven")){
                List<Tile> finalPath = new ArrayList<Tile>();

                if(oreChoice.equals("Copper")){
                    finalPath.addAll(Arrays.asList(MConstants.DWARVEN_COPPER));
                } else if(oreChoice.equals("Clay")){
                    finalPath.addAll(Arrays.asList(MConstants.DWARVEN_CLAY));
                } else if(oreChoice.equals("Tin")){
                    finalPath.addAll(Arrays.asList(MConstants.DWARVEN_TIN));
                } else if(oreChoice.equals("Iron")){
                    if(Random.nextBoolean()) {
                        finalPath.addAll(Arrays.asList(MConstants.DWARVEN_IRON));
                    } else {
                        finalPath.addAll(Arrays.asList(MConstants.DWARVEN_IRON_TWO));
                    }
                } else if(oreChoice.equals("Coal")){
                    int i = Random.nextInt(1,3);
                    if(i==1) {
                        finalPath.addAll(Arrays.asList(MConstants.DWARVEN_COAL));
                    } else if(i==2) {
                        finalPath.addAll(Arrays.asList(MConstants.DWARVEN_COAL_TWO));
                    } else {
                        finalPath.addAll(Arrays.asList(MConstants.DWARVEN_COAL_THREE));
                    }
                } else if(oreChoice.equals("Gold")){
                    finalPath.addAll(Arrays.asList(MConstants.DWARVEN_GOLD));
                 } else if(oreChoice.equals("Mith")){
                    finalPath.addAll(Arrays.asList(MConstants.DWARVEN_MITH));
                } else if(oreChoice.equals("Addy")){
                    finalPath.addAll(Arrays.asList(MConstants.DWARVEN_ADDY));
                } else {
                    ctx.controller.stop();
                }

                finalPath.addAll(Arrays.asList(MConstants.DWARVEN_MINE));
                taskList.add(new Walk(ctx, finalPath.toArray(new Tile[] {})));
            } else {
                ctx.controller.stop();
            }

        } else if(userChoice.equals("Powermine")){
            taskList.add(new Drop(ctx));
        } else {
            ctx.controller.stop();
        }

        if(oreChoice.equals("Copper")){
            taskList.add(new Mine(ctx, MConstants.COPPER_ROCK_IDS));
        } else if(oreChoice.equals("Clay")){
            taskList.add(new Mine(ctx, MConstants.CLAY_ROCK_IDS));
        } else if(oreChoice.equals("Tin")){
            taskList.add(new Mine(ctx, MConstants.TIN_ROCK_IDS));
        } else if(oreChoice.equals("Coal")){
            taskList.add(new Mine(ctx, MConstants.COAL_ROCK_IDS));
        } else if(oreChoice.equals("Gold")){
            taskList.add(new Mine(ctx, MConstants.GOLD_ROCK_IDS));
        } else if(oreChoice.equals("Mith")){
            taskList.add(new Mine(ctx, MConstants.MITH_ROCK_IDS));
        } else if(oreChoice.equals("Addy")){
            taskList.add(new Mine(ctx, MConstants.ADDY_ROCK_IDS));
        } else if(oreChoice.equals("Iron")){
            taskList.add(new Mine(ctx, MConstants.IRON_ROCK_IDS));
        } else {
            ctx.controller.stop();
        }


        startExp = ctx.skills.experience(Constants.SKILLS_MINING);

    }

    @Override
    public void poll() {
        for(Task task : taskList){
            if(ctx.controller.isStopping()){
                break;
            }

            if(task.activate()){
                task.execute();
                break;
            }
        }

    }

    @Override
    public void repaint(Graphics graphics) {
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000*60) % 60);
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;

        int expGained = ctx.skills.experience(Constants.SKILLS_MINING)-startExp;

        Graphics2D g = (Graphics2D)graphics;

        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, 150, 100);

        g.setColor(new Color(255,255,255));
        g.drawRect(0, 0, 150, 100);

        g.drawString("QuickMiner", 20, 20);
        g.drawString("Running: " + String.format("%02d:%02d:%02d", hours, minutes, seconds), 20, 40);
        g.drawString("Exp/Hour " + ((int)(expGained * (3600000D / milliseconds))), 20, 60);

    }
}
