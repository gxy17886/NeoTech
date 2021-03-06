package com.dyonovan.neotech.pipes.tiles.energy

import cofh.api.energy.{EnergyStorage, IEnergyReceiver}
import com.dyonovan.neotech.pipes.entities.{ResourceEntity, EnergyResourceEntity}
import com.dyonovan.neotech.pipes.types.{SimplePipe, SinkPipe}
import net.minecraft.util.{BlockPos, EnumFacing}

/**
 * This file was created for NeoTech
 *
 * NeoTech is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis pauljoda
 * @since August 17, 2015
 */
class EnergySinkPipe extends SinkPipe[EnergyStorage, EnergyResourceEntity] {
    override def canConnect(facing: EnumFacing): Boolean =
        getWorld.getTileEntity(getPos.offset(facing)).isInstanceOf[SimplePipe] ||
                (getWorld.getTileEntity(pos.offset(facing)).isInstanceOf[IEnergyReceiver] && getWorld.getTileEntity(pos.offset(facing)).asInstanceOf[IEnergyReceiver].canConnectEnergy(facing.getOpposite))

    /**
     * Used to check if this pipe can accept a resource
     *
     * You should not actually change anything, all simulation
     * @param resourceEntity
     * @return
     */
    override def willAcceptResource(resourceEntity: ResourceEntity[_]): Boolean = {
        if(resourceEntity == null || !resourceEntity.isInstanceOf[EnergyResourceEntity] || resourceEntity.resource == null)
            return false

        val resource = resourceEntity.asInstanceOf[EnergyResourceEntity]

        //Try and insert the energy
        for(dir <- EnumFacing.values()) {
            worldObj.getTileEntity(pos.offset(dir)) match {
                case receiver : IEnergyReceiver =>
                    if(receiver.receiveEnergy(dir.getOpposite, resource.resource.getEnergyStored, true) > 0)
                        return true
                case _ =>
            }
        }
        false
    }

    /**
     * Try and insert the resource into an inventory.
     *
     * It is pretty good practice to send the resource back if you can't remove all of it
     * @param resource
     */
    override def tryInsertResource(resource: EnergyResourceEntity): Unit = {
        if(resource == null || resource.resource == null)
            return

        //Try and insert the energy
        for(dir <- EnumFacing.values()) {
            worldObj.getTileEntity(pos.offset(dir)) match {
                case receiver : IEnergyReceiver if !resource.isDead =>
                    receiver.receiveEnergy(dir.getOpposite, resource.resource.extractEnergy(resource.resource.getEnergyStored, false), false)
                    if(resource.resource.getEnergyStored <= 0)
                        resource.isDead = true
                case _ =>
            }
        }

        //If we couldn't fill, move back to source
        if(!resource.isDead) {
            val tempLocation = new BlockPos(resource.from)
            resource.from = new BlockPos(pos)
            resource.destination = new BlockPos(tempLocation)
            resource.findPathToDestination()
        }
    }

    def pingAmountNeeded() : Int = {
        //Try and insert the energy
        for(dir <- EnumFacing.values()) {
            worldObj.getTileEntity(pos.offset(dir)) match {
                case receiver : IEnergyReceiver =>
                    val requested = receiver.receiveEnergy(dir.getOpposite, receiver.getEnergyStored(dir.getOpposite), true)
                    if(requested > 0)
                        return requested
                case _ =>
            }
        }
        -1
    }
}
