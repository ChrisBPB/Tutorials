package osrs.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
import osrs.Task;

import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public class Drop extends Task{


    public Drop(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count()>27;
    }

    @Override
    public void execute() {



        for(Item copperOre : ctx.inventory.select().name(Pattern.compile("(.*ore)|(Clay)|(Coal)"))){

            if(ctx.controller.isStopping()){
                break;
            }

            final int startAmtInventory = ctx.inventory.select().count();
            copperOre.interact("Drop");

            Condition.wait(new Callable<Boolean>(){
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count() != startAmtInventory;
                }
            }, 25, 20);

        }

    }
}
