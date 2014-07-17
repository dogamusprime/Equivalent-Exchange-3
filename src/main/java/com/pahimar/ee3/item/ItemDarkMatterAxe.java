package com.pahimar.ee3.item;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.pahimar.ee3.reference.*;
import com.pahimar.ee3.util.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Set;

public class ItemDarkMatterAxe extends ItemToolEE implements IKeyBound, IChargeable, IModalTool
{
    private static final Set blocksEffectiveAgainst = Sets.newHashSet(new Block[]{Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin});

    public ItemDarkMatterAxe()
    {
        super(3.0f, Material.Tools.DARK_MATTER, blocksEffectiveAgainst);
        this.setUnlocalizedName(Names.Tools.DARK_MATTER_AXE);
    }

    @Override
    public float func_150893_a(ItemStack itemStack, Block block)
    {
        return block.getMaterial() != net.minecraft.block.material.Material.wood && block.getMaterial() != net.minecraft.block.material.Material.plants && block.getMaterial() != net.minecraft.block.material.Material.vine ? super.func_150893_a(itemStack, block) : this.efficiencyOnProperMaterial;
    }

    @Override
    public Set<String> getToolClasses(ItemStack itemStack)
    {
        return ImmutableSet.of("axe");
    }

    @Override
    public short getMaxChargeLevel()
    {
        return 3;
    }

    @Override
    public short getChargeLevel(ItemStack itemStack)
    {
        return NBTHelper.getShort(itemStack, Names.NBT.CHARGE_LEVEL);
    }

    @Override
    public void setChargeLevel(ItemStack itemStack, short chargeLevel)
    {
        if (chargeLevel <= this.getMaxChargeLevel())
        {
            NBTHelper.setShort(itemStack, Names.NBT.CHARGE_LEVEL, chargeLevel);
        }
    }

    @Override
    public void increaseChargeLevel(ItemStack itemStack)
    {
        if (NBTHelper.getShort(itemStack, Names.NBT.CHARGE_LEVEL) < this.getMaxChargeLevel())
        {
            NBTHelper.setShort(itemStack, Names.NBT.CHARGE_LEVEL, (short) (NBTHelper.getShort(itemStack, Names.NBT.CHARGE_LEVEL) + 1));
        }
    }

    @Override
    public void decreaseChargeLevel(ItemStack itemStack)
    {
        if (NBTHelper.getShort(itemStack, Names.NBT.CHARGE_LEVEL) > 0)
        {
            NBTHelper.setShort(itemStack, Names.NBT.CHARGE_LEVEL, (short) (NBTHelper.getShort(itemStack, Names.NBT.CHARGE_LEVEL) - 1));
        }
    }

    @Override
    public void doKeyBindingAction(EntityPlayer entityPlayer, ItemStack itemStack, Key key)
    {
        if (key == Key.CHARGE)
        {
            if (!entityPlayer.isSneaking())
            {
                if (getChargeLevel(itemStack) == this.getMaxChargeLevel())
                {
                    entityPlayer.worldObj.playSoundEffect(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, Sounds.FAIL, 1.5f, 1.5f);
                }
                else
                {
                    increaseChargeLevel(itemStack);
                    entityPlayer.worldObj.playSoundEffect(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, Sounds.CHARGE_UP, 0.5F, 0.5F + 0.5F * (getChargeLevel(itemStack) * 1.0F / this.getMaxChargeLevel()));
                }
            }
            else
            {
                if (getChargeLevel(itemStack) == 0)
                {
                    entityPlayer.worldObj.playSoundEffect(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, Sounds.FAIL, 1.5f, 1.5f);
                }
                else
                {
                    decreaseChargeLevel(itemStack);
                    entityPlayer.worldObj.playSoundEffect(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, Sounds.CHARGE_DOWN, 0.5F, 1.0F - (0.5F - 0.5F * (getChargeLevel(itemStack) * 1.0F / this.getMaxChargeLevel())));
                }
            }
        }
        else if (key == Key.EXTRA)
        {
            changeToolMode(itemStack);
        }
    }

    @Override
    public List<ToolMode> getAvailableToolModes()
    {
        // TODO
        return null;
    }

    @Override
    public ToolMode getCurrentToolMode(ItemStack itemStack)
    {
        if (NBTHelper.getShort(itemStack, Names.NBT.MODE) < ToolMode.TYPES.length)
        {
            return ToolMode.TYPES[NBTHelper.getShort(itemStack, Names.NBT.MODE)];
        }

        return null;
    }

    @Override
    public void setToolMode(ItemStack itemStack, ToolMode toolMode)
    {
        NBTHelper.setShort(itemStack, Names.NBT.MODE, (short) toolMode.ordinal());
    }

    @Override
    public void changeToolMode(ItemStack itemStack)
    {
        ToolMode currentToolMode = getCurrentToolMode(itemStack);

        if (getAvailableToolModes().contains(currentToolMode))
        {
            if (getAvailableToolModes().indexOf(currentToolMode) == getAvailableToolModes().size() - 1)
            {
                setToolMode(itemStack, getAvailableToolModes().get(0));
            }
            else
            {
                setToolMode(itemStack, getAvailableToolModes().get(getAvailableToolModes().indexOf(currentToolMode) + 1));
            }
        }
    }
}
