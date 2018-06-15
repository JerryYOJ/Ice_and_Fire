package com.github.alexthe666.iceandfire.inventory;

import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerHippocampus extends Container {
    private final IInventory hippocampusInventory;
    private final EntityHippocampus hippocampus;
    private final EntityPlayer player;

    public ContainerHippocampus(final EntityHippocampus hippocampus, EntityPlayer player) {
        this.hippocampusInventory = hippocampus.hippocampusInventory;
        this.hippocampus = hippocampus;
        this.player = player;
        int i = 3;
        hippocampusInventory.openInventory(player);
        int j = -18;
        this.addSlotToContainer(new Slot(hippocampusInventory, 0, 8, 18) {
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == Items.SADDLE && !this.getHasStack();
            }

            @SideOnly(Side.CLIENT)
            public boolean isEnabled() {
                return true;
            }
        });
        this.addSlotToContainer(new Slot(hippocampusInventory, 1, 8, 36) {
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == Item.getItemFromBlock(Blocks.CHEST) && !this.getHasStack();
            }

            public void onSlotChanged() {
                ContainerHippocampus.this.hippocampus.refreshInventory();
            }

            @SideOnly(Side.CLIENT)
            public boolean isEnabled() {
                return true;
            }
        });
        this.addSlotToContainer(new Slot(hippocampusInventory, 2, 8, 52) {

            public boolean isItemValid(ItemStack stack) {
                return hippocampus.getIntFromArmor(stack) != 0;
            }

            public int getSlotStackLimit() {
                return 1;
            }

            @SideOnly(Side.CLIENT)
            public boolean isEnabled() {
                return true;
            }
        });

        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 5; ++l) {
                this.addSlotToContainer(new Slot(hippocampusInventory, 3 + l + k * 5, 80 + l * 18, 18 + k * 18) {
                    @SideOnly(Side.CLIENT)
                    public boolean isEnabled() {
                        return ContainerHippocampus.this.hippocampus.isChested();
                    }

                    public boolean isItemValid(ItemStack stack) {
                        return ContainerHippocampus.this.hippocampus.isChested();
                    }
                });
            }
        }

        for (int i1 = 0; i1 < 3; ++i1) {
            for (int k1 = 0; k1 < 9; ++k1) {
                this.addSlotToContainer(new Slot(player.inventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 102 + i1 * 18 + -18));
            }
        }

        for (int j1 = 0; j1 < 9; ++j1) {
            this.addSlotToContainer(new Slot(player.inventory, j1, 8 + j1 * 18, 142));
        }
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.hippocampusInventory.isUsableByPlayer(playerIn) && this.hippocampus.isEntityAlive() && this.hippocampus.getDistance(playerIn) < 8.0F;
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < this.hippocampusInventory.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, this.hippocampusInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(2).isItemValid(itemstack1) && !this.getSlot(2).getHasStack()) {
                if (!this.mergeItemStack(itemstack1, 2, 3, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).isItemValid(itemstack1) && !this.getSlot(1).getHasStack()) {
                if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).isItemValid(itemstack1)) {
                if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.hippocampusInventory.getSizeInventory() <= 3 || !this.mergeItemStack(itemstack1, 2, this.hippocampusInventory.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()){
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        this.hippocampusInventory.closeInventory(playerIn);
    }
}