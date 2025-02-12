package net.cubecraft.world.item.behavior;

import me.gb2022.commons.math.hitting.HitResult;
import net.cubecraft.world.block.access.BlockAccess;
import net.cubecraft.world.block.access.IBlockAccess;
import net.cubecraft.world.block.blocks.Blocks;
import net.cubecraft.world.item.Item;

public class DigableItemBehavior implements ItemBehavior {
    @Override
    public void onDig(HitResult result, Item item, BlockAccess block) {
        block.setBlockId(Blocks.AIR.getId(),false);
    }
}
