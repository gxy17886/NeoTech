package com.dyonovan.neotech

import java.io.File

import com.dyonovan.neotech.api.nei.NEICallback
import com.dyonovan.neotech.common.CommonProxy
import com.dyonovan.neotech.lib.Reference
import com.dyonovan.neotech.managers._
import com.dyonovan.neotech.network.PacketDispatcher
import com.dyonovan.neotech.registries._
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.common.{Mod, SidedProxy}
import org.apache.logging.log4j.LogManager


/**
  * This file was created for NeoTech
  *
  * NeoTech is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Dyonovan
  * @since August 07, 2015
  */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION,
    dependencies = Reference.DEPENDENCIES, modLanguage = "scala")
object NeoTech {

    //The logger. For logging
    final val logger = LogManager.getLogger(Reference.MOD_NAME)

    var nei : NEICallback = null

    var configFolderLocation : String = ""

    @SidedProxy(clientSide = "com.dyonovan.neotech.client.ClientProxy",
                serverSide = "com.dyonovan.neotech.common.CommonProxy")
    var proxy : CommonProxy = null

    val tabNeoTech: CreativeTabs = new CreativeTabs("tabNeoTech") {
        override def getTabIconItem: Item = Item.getItemFromBlock(Blocks.furnace)
    }

    val tabPipes = new CreativeTabs("tabNeoTechPipes") {
        override def getTabIconItem : Item = Item.getItemFromBlock(BlockManager.pipeItemSource)
    }

    @EventHandler def preInit(event : FMLPreInitializationEvent) = {
        configFolderLocation = event.getModConfigurationDirectory.getAbsolutePath + File.separator + "NeoTech"
        ConfigRegistry.preInit()
        BlockManager.preInit()
        ItemManager.preInit()
        proxy.preInit()
        GameRegistry.registerWorldGenerator(WorldGenManager, 2)
        CraftingRecipeManager.preInit()
    }

    @EventHandler def init(event : FMLInitializationEvent) =  {
        CrusherRecipeRegistry.init()
        FluidFuelValues.init()
        PacketDispatcher.initPackets()
        EventManager.init()
        proxy.init()
    }

    @EventHandler def postInit(event : FMLPostInitializationEvent) = {
        proxy.postInit()
    }
 }
