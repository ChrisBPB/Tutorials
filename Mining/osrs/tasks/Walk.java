package osrs.tasks;

import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import osrs.Task;
import osrs.Walker;


public class Walk extends Task {

    public Tile pathToBank[];
    private final Walker walker = new Walker(ctx);


    public Walk(ClientContext ctx, Tile[] pathToBank) {
        super(ctx);

        this.pathToBank = pathToBank;

    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count()>27 || (ctx.inventory.select().count()<28 && pathToBank[0].distanceTo(ctx.players.local())>6);
    }

    @Override
    public void execute() {
        if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }

        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
            if(ctx.inventory.select().count()>27){
                walker.walkPath(pathToBank);
            } else {
                walker.walkPathReverse(pathToBank);
            }
        }

    }
}
