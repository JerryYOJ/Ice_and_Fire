package com.github.alexthe666.iceandfire.core;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.*;
import com.github.alexthe666.iceandfire.entity.tile.*;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.item.ItemDragonScales;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

	public static final SoundType SOUND_TYPE_GOLD = new SoundType(1.0F, 1.0F, ModSounds.GOLD_PILE_BREAK, ModSounds.GOLD_PILE_STEP, ModSounds.GOLD_PILE_BREAK, ModSounds.GOLD_PILE_STEP, ModSounds.GOLD_PILE_STEP);

	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":lectern")
	public static Block lectern = new BlockLectern();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":podium")
	public static Block podium = new BlockPodium();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":fire_lily")
	public static Block fire_lily = new BlockElementalFlower(true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":frost_lily")
	public static Block frost_lily = new BlockElementalFlower(false);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":goldpile")
	public static Block goldPile = new BlockGoldPile();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silverpile")
	public static Block silverPile = new BlockSilverPile();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_ore")
	public static Block silverOre = new BlockDragonOre(2, 3.0F, 5.0F, "iceandfire.silverOre", "silver_ore");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":sapphire_ore")
	public static Block sapphireOre = new BlockDragonOre(2, 3.0F, 5.0F, "iceandfire.sapphireOre", "sapphire_ore");
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":silver_block")
	public static Block silverBlock = new BlockGeneric(Material.IRON, "silver_block", "iceandfire.silverBlock", "pickaxe", 2, 3.0F, 10.0F, SoundType.METAL);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":sapphire_block")
	public static Block sapphireBlock = new BlockGeneric(Material.IRON, "sapphire_block", "iceandfire.sapphireBlock", "pickaxe", 2, 3.0F, 10.0F, SoundType.METAL);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":chared_dirt")
	public static Block charedDirt = new BlockReturningState(Material.GROUND, "chared_dirt", "iceandfire.charedDirt", "shovel", 0, 0.5F, 0.0F, SoundType.GROUND, Blocks.DIRT.getDefaultState());
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":chared_grass")
	public static Block charedGrass = new BlockReturningState(Material.GRASS, "chared_grass", "iceandfire.charedGrass", "shovel", 0, 0.6F, 0.0F, SoundType.GROUND, Blocks.GRASS.getDefaultState());
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":chared_stone")
	public static Block charedStone = new BlockReturningState(Material.ROCK, "chared_stone", "iceandfire.charedStone", "pickaxe", 0, 1.5F, 10.0F, SoundType.STONE, Blocks.STONE.getDefaultState());
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":chared_cobblestone")
	public static Block charedCobblestone = new BlockReturningState(Material.ROCK, "chared_cobblestone", "iceandfire.charedCobblestone", "pickaxe", 0, 2F, 10.0F, SoundType.STONE, Blocks.COBBLESTONE.getDefaultState());
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":chared_gravel")
	public static Block charedGravel = new BlockFallingReturningState(Material.GROUND, "chared_gravel", "iceandfire.charedGravel", "pickaxe", 0, 0.6F, 0F, SoundType.GROUND);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":chared_grass_path")
	public static Block charedGrassPath = new BlockCharedPath(true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":ash")
	public static Block ash = new BlockFallingGeneric(Material.SAND, "ash", "iceandfire.ash", "shovel", 0, 0.5F, 0F, SoundType.SAND);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":frozen_dirt")
	public static Block frozenDirt = new BlockReturningState(Material.GROUND, "frozen_dirt", "iceandfire.frozenDirt", "shovel", 0, 0.5F, 0.0F, SoundType.GLASS, true, Blocks.DIRT.getDefaultState());
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":frozen_grass")
	public static Block frozenGrass = new BlockReturningState(Material.GRASS, "frozen_grass", "iceandfire.frozenGrass", "shovel", 0, 0.6F, 0.0F, SoundType.GLASS, true, Blocks.GRASS.getDefaultState());
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":frozen_stone")
	public static Block frozenStone = new BlockReturningState(Material.ROCK, "frozen_stone", "iceandfire.frozenStone", "pickaxe", 0, 1.5F, 10.0F, SoundType.GLASS, true, Blocks.STONE.getDefaultState());
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":frozen_cobblestone")
	public static Block frozenCobblestone = new BlockReturningState(Material.ROCK, "frozen_cobblestone", "iceandfire.frozenCobblestone", "pickaxe", 0, 2F, 10.0F, SoundType.GLASS, true, Blocks.COBBLESTONE.getDefaultState());
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":frozen_gravel")
	public static Block frozenGravel = new BlockFallingReturningState(Material.GROUND, "frozen_gravel", "iceandfire.frozenGravel", "pickaxe", 0, 0.6F, 0F, SoundType.GLASS, true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":frozen_grass_path")
	public static Block frozenGrassPath = new BlockCharedPath(false);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":frozen_splinters")
	public static Block frozenSplinters = new BlockGeneric(Material.WOOD, "frozen_splinters", "iceandfire.frozenSplinters", "pickaxe", 0, 2.0F, 10.0F, SoundType.GLASS, true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragon_ice")
	public static Block dragon_ice = new BlockGeneric(Material.PACKED_ICE, "dragon_ice", "iceandfire.dragon_ice", "pickaxe", 0, 0.5F, 0F, SoundType.GLASS, true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragon_ice_spikes")
	public static Block dragon_ice_spikes = new BlockIceSpikes();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":nest")
	public static Block nest = new BlockGeneric(Material.GRASS, "nest", "iceandfire.nest", "axe", 0, 0.5F, 0F, SoundType.GROUND, false);

	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscale_red")
	public static Block dragonscale_red = new BlockDragonScales("dragonscale_red", EnumDragonEgg.RED);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscale_green")
	public static Block dragonscale_green = new BlockDragonScales("dragonscale_green", EnumDragonEgg.GREEN);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscale_bronze")
	public static Block dragonscale_bronze = new BlockDragonScales("dragonscale_bronze", EnumDragonEgg.BRONZE);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscale_gray")
	public static Block dragonscale_gray = new BlockDragonScales("dragonscale_gray", EnumDragonEgg.GRAY);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscale_blue")
	public static Block dragonscale_blue = new BlockDragonScales("dragonscale_blue", EnumDragonEgg.BLUE);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscale_white")
	public static Block dragonscale_white = new BlockDragonScales("dragonscale_white", EnumDragonEgg.WHITE);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscale_sapphire")
	public static Block dragonscale_sapphire = new BlockDragonScales("dragonscale_sapphire", EnumDragonEgg.SAPPHIRE);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonscale_silver")
	public static Block dragonscale_silver = new BlockDragonScales("dragonscale_silver", EnumDragonEgg.SILVER);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragon_bone_block")
	public static Block dragon_bone_block = new BlockDragonBone();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragon_bone_wall")
	public static Block dragon_bone_block_wall = new BlockDragonBoneWall();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonforge_fire_brick")
	public static Block dragonforge_fire_brick = new BlockDragonforgeBricks(true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonforge_ice_brick")
	public static Block dragonforge_ice_brick = new BlockDragonforgeBricks(false);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonforge_fire_input")
	public static Block dragonforge_fire_input = new BlockDragonforgeInput(true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonforge_ice_input")
	public static Block dragonforge_ice_input = new BlockDragonforgeInput(false);

	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonforge_fire_core_disabled")
	public static Block dragonforge_fire_core = new BlockDragonforgeCore(true, true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonforge_ice_core_disabled")
	public static Block dragonforge_ice_core = new BlockDragonforgeCore(false, true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonforge_fire_core")
	public static Block dragonforge_fire_core_disabled = new BlockDragonforgeCore(true, false);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonforge_ice_core")
	public static Block dragonforge_ice_core_disabled = new BlockDragonforgeCore(false, false);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":egginice")
	public static Block eggInIce = new BlockEggInIce();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":pixie_house")
	public static Block pixieHouse = new BlockPixieHouse();
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":jar_empty")
	public static Block jar_empty = new BlockJar(true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":jar_pixie")
	public static Block jar_pixie = new BlockJar(false);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_resin")
	public static Block myrmex_resin = new BlockMyrmexResin(false);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_resin_sticky")
	public static Block myrmex_resin_sticky = new BlockMyrmexResin(true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":desert_myrmex_cocoon")
	public static Block desert_myrmex_cocoon = new BlockMyrmexCocoon(false);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":jungle_myrmex_cocoon")
	public static Block jungle_myrmex_cocoon = new BlockMyrmexCocoon(true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_desert_biolight")
	public static Block myrmex_desert_biolight = new BlockMyrmexBiolight(false);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_jungle_biolight")
	public static Block myrmex_jungle_biolight = new BlockMyrmexBiolight(true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_desert_resin_block")
	public static Block myrmex_desert_resin_block = new BlockMyrmexConnectedResin(false, false);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_jungle_resin_block")
	public static Block myrmex_jungle_resin_block = new BlockMyrmexConnectedResin(true, false);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_desert_resin_glass")
	public static Block myrmex_desert_resin_glass = new BlockMyrmexConnectedResin(false, true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":myrmex_jungle_resin_glass")
	public static Block myrmex_jungle_resin_glass = new BlockMyrmexConnectedResin(true, true);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonsteel_fire_block")
	public static Block dragonsteel_fire_block = new BlockGeneric(Material.IRON, "dragonsteel_fire_block", "iceandfire.dragonsteel_fire_block", "pickaxe", 3, 10.0F, 1000.0F, SoundType.METAL);
	@GameRegistry.ObjectHolder(IceAndFire.MODID + ":dragonsteel_ice_block")
	public static Block dragonsteel_ice_block = new BlockGeneric(Material.IRON, "dragonsteel_ice_block", "iceandfire.dragonsteel_ice_block", "pickaxe", 3, 10.0F, 1000.0F, SoundType.METAL);
	static {
		GameRegistry.registerTileEntity(TileEntityDummyGorgonHead.class, "dummyGorgonHeadIdle");
		GameRegistry.registerTileEntity(TileEntityDummyGorgonHeadActive.class, "dummyGorgonHeadActive");
		GameRegistry.registerTileEntity(TileEntityMyrmexCocoon.class, "myrmexCocoon");
		GameRegistry.registerTileEntity(TileEntityDragonforge.class, "dragonforge");
		GameRegistry.registerTileEntity(TileEntityDragonforgeInput.class, "dragonforgeInput");
	}

}
