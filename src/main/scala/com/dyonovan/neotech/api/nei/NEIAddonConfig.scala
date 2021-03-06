package com.dyonovan.neotech.api.nei

import codechicken.nei.api.{ItemInfo, API, IConfigureNEI}
import codechicken.nei.recipe.{RecipeInfo, TemplateRecipeHandler}
import com.dyonovan.neotech.NeoTech
import com.dyonovan.neotech.api.nei.machines.{RecipeHandlerCrusher, RecipeHandlerFurnace}
import com.dyonovan.neotech.client.gui.machines.GuiElectricCrusher
import com.dyonovan.neotech.lib.Reference
import com.dyonovan.neotech.managers.{ItemManager, BlockManager}
import net.minecraft.item.{ItemStack, Item}
import net.minecraft.util.StatCollector

/**
 * This file was created for NeoTech
 *
 * NeoTech is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since August 17, 2015
 */
class NEIAddonConfig extends IConfigureNEI {

    override def loadConfig(): Unit = {
        registerHandler(new RecipeHandlerFurnace)
        registerHandler(new RecipeHandlerCrusher)

        RecipeInfo.setGuiOffset(classOf[GuiElectricCrusher], -10, 8)

        ItemInfo.hiddenItems.add(new ItemStack(ItemManager.upgradeMBFull))

        NeoTech.nei = new NEICallback
    }

    override def getName: String = StatCollector.translateToLocal("neotech.nei.name")

    override def getVersion: String = Reference.VERSION

    /**
     * Little helper method to register the handlers
     * @param handler The handler to register
     */
    private def registerHandler(handler: TemplateRecipeHandler): Unit = {
        API.registerRecipeHandler(handler)
        API.registerUsageHandler(handler)
    }


}
