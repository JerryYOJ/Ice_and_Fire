package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class EntityDragonIceCharge extends AbstractFireballEntity implements IDragonProjectile {

    public int ticksInAir;

    public EntityDragonIceCharge(EntityType type, World worldIn) {
        super(type, worldIn);

    }

    public EntityDragonIceCharge(EntityType type, World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(type, posX, posY, posZ, accelX, accelY, accelZ, worldIn);
        double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.accelerationX = accelX / d0 * 0.07D;
        this.accelerationY = accelY / d0 * 0.07D;
        this.accelerationZ = accelZ / d0 * 0.07D;
    }

    public EntityDragonIceCharge(EntityType type, World worldIn, EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
        super(type, shooter, accelX, accelY, accelZ, worldIn);
        double d0 = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.accelerationX = accelX / d0 * 0.07D;
        this.accelerationY = accelY / d0 * 0.07D;
        this.accelerationZ = accelZ / d0 * 0.07D;
    }


    protected boolean isFireballFiery() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    public void tick() {
        if (this.world.isRemote) {
            for (int i = 0; i < 14; ++i) {
                IceAndFire.PROXY.spawnParticle("dragonice", this.getPosX() + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), this.getPosY() + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), this.getPosZ() + this.rand.nextDouble() * 1 * (this.rand.nextBoolean() ? -1 : 1), 0.0D, 0.0D, 0.0D);
            }
        }
        if (this.world.isRemote || (this.shootingEntity == null || !this.shootingEntity.isAlive()) && this.world.isBlockLoaded(new BlockPos(this))) {
            super.tick();

            if (this.isFireballFiery()) {
                this.setFire(1);
            }

            ++this.ticksInAir;
            Vec3d vec3d = this.getMotion();
            RayTraceResult raytraceresult = ProjectileHelper.rayTrace(this, this.getBoundingBox().expand(vec3d).grow(1.0D), (p_213879_1_) -> {
                return !p_213879_1_.isSpectator() && p_213879_1_ != this.shootingEntity;
            }, RayTraceContext.BlockMode.OUTLINE, true);

            if (raytraceresult != null) {
                this.onImpact(raytraceresult);
            }

            double d0 = this.getPosX() + vec3d.x;
            double d1 = this.getPosY() + vec3d.y;
            double d2 = this.getPosZ() + vec3d.z;
            float f = MathHelper.sqrt(horizontalMag(vec3d));
            this.rotationYaw = (float)(MathHelper.atan2(vec3d.x, vec3d.z) * (double)(180F / (float)Math.PI));
            for(this.rotationPitch = (float)(MathHelper.atan2(vec3d.y, (double)f) * (double)(180F / (float)Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
                ;
            }
            while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = MathHelper.lerp(0.2F, this.prevRotationPitch, this.rotationPitch);
            this.rotationYaw = MathHelper.lerp(0.2F, this.prevRotationYaw, this.rotationYaw);
            float f1 = 0.99F;
            float f2 = 0.06F;


            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    this.world.addParticle(ParticleTypes.BUBBLE, this.getPosX() - this.getMotion().x * 0.25D, this.getPosY() - this.getMotion().y * 0.25D, this.getPosZ() - this.getMotion().z * 0.25D, this.getMotion().x, this.getMotion().y, this.getMotion().z);
                }

                f = 0.8F;
            }
            this.setPosition(d0, d1, d2);
            this.world.addParticle(this.getParticle(), this.getPosX(), this.getPosY() + 0.5D, this.getPosZ(), 0.0D, 0.0D, 0.0D);
            this.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
        } else {
            this.remove();
        }
    }

    @Override
    protected void onImpact(RayTraceResult movingObject) {
        boolean flag = this.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING);
        if (!this.world.isRemote) {
            if (movingObject.getType() == RayTraceResult.Type.ENTITY) {
                Entity entity = ((EntityRayTraceResult) movingObject).getEntity();

                if (entity != null && entity instanceof IDragonProjectile) {
                    return;
                }
                if (entity != null && this.shootingEntity != null && this.shootingEntity instanceof EntityDragonBase && entity != null) {
                    EntityDragonBase dragon = (EntityDragonBase) this.shootingEntity;
                    if (dragon.isOnSameTeam(entity) || dragon.isEntityEqual(entity) || dragon.isPart(entity)) {
                        return;
                    }
                }
                if (entity == null || !(entity instanceof IDragonProjectile) && entity != shootingEntity && this.shootingEntity instanceof EntityDragonBase) {
                    EntityDragonBase dragon = (EntityDragonBase) this.shootingEntity;
                    if (this.shootingEntity != null && (entity == this.shootingEntity || (entity instanceof TameableEntity && ((EntityDragonBase) shootingEntity).isOwner(((EntityDragonBase) shootingEntity).getOwner())))) {
                        return;
                    }
                    if (dragon != null) {
                        dragon.randomizeAttacks();
                    }
                    this.remove();
                }
                if (entity != null && !(entity instanceof IDragonProjectile) && !entity.isEntityEqual(shootingEntity)) {
                    if (this.shootingEntity != null && (entity.isEntityEqual(shootingEntity) || (this.shootingEntity instanceof EntityDragonBase & entity instanceof TameableEntity && ((EntityDragonBase) shootingEntity).getOwner() == ((TameableEntity) entity).getOwner()))) {
                        return;
                    }
                    if (this.shootingEntity != null && this.shootingEntity instanceof EntityDragonBase) {
                        entity.attackEntityFrom(IafDamageRegistry.DRAGON_ICE, 10.0F);
                        if (entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() == 0) {
                            ((EntityDragonBase) this.shootingEntity).randomizeAttacks();
                        }
                    }
                    entity.setFire(5);
                    this.applyEnchantments(this.shootingEntity, entity);
                    this.remove();
                }
            }
        }
        if (this.shootingEntity instanceof EntityDragonBase && IafConfig.dragonGriefing != 2) {
            IafDragonDestructionManager.destroyAreaIceCharge(world, this.getPosition(), ((EntityDragonBase) this.shootingEntity));
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    public float getCollisionBorderSize() {
        return 0F;
    }

}