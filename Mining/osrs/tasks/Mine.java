package osrs.tasks;


import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import osrs.Task;

import java.util.concurrent.Callable;

public class Mine extends Task {

    int rockIds[];

    Tile rockLocation = Tile.NIL;

    public Mine(ClientContext ctx, int[] rockIds) {
        super(ctx);
        this.rockIds = rockIds;
    }

    @Override
    public boolean activate() {
        return ctx.objects.select().at(rockLocation).id(rockIds).poll().equals(ctx.objects.nil()) || ctx.players.local().animation()==-1;
    }

    @Override
    public void execute() {
        GameObject rockToMine = ctx.objects.select().id(rockIds).nearest().poll();
        rockLocation = rockToMine.tile();
        rockToMine.interact("Mine");

        Condition.wait(new Callable<Boolean>(){
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation()!=-1;
            }
        }, 200, 10);

    }
}
