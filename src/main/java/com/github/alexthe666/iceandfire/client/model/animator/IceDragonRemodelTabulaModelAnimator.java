package com.github.alexthe666.iceandfire.client.model.animator;

import com.github.alexthe666.iceandfire.client.model.util.EnumRemodelDragonAnimations;
import com.github.alexthe666.iceandfire.client.model.util.IIceAndFireTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.util.IceAndFireTabulaModel;
import com.github.alexthe666.iceandfire.client.model.util.LegArticulator;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class IceDragonRemodelTabulaModelAnimator extends IceAndFireTabulaModelAnimator implements IIceAndFireTabulaModelAnimator<EntityIceDragon> {

    public IceDragonRemodelTabulaModelAnimator() {
        super(EnumRemodelDragonAnimations.GROUND_POSE.icedragon_model);
    }

    @Override
    public void setRotationAngles(IceAndFireTabulaModel model, EntityIceDragon entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
        model.resetToDefaultPose();
        animate(model, entity, limbSwing, limbSwingAmount, ageInTicks, rotationYaw, rotationPitch, scale);
        IceAndFireTabulaModel[] walkPoses = {EnumRemodelDragonAnimations.WALK1.icedragon_model, EnumRemodelDragonAnimations.WALK2.icedragon_model, EnumRemodelDragonAnimations.WALK3.icedragon_model, EnumRemodelDragonAnimations.WALK4.icedragon_model};
        IceAndFireTabulaModel[] flyPoses = {EnumRemodelDragonAnimations.FLIGHT1.icedragon_model,
                EnumRemodelDragonAnimations.FLIGHT2.icedragon_model,
                EnumRemodelDragonAnimations.FLIGHT3.icedragon_model,
                EnumRemodelDragonAnimations.FLIGHT4.icedragon_model,
                EnumRemodelDragonAnimations.FLIGHT5.icedragon_model,
                EnumRemodelDragonAnimations.FLIGHT6.icedragon_model};
        boolean walking = !entity.isHovering() && !entity.isFlying() && entity.hoverProgress <= 0 && entity.flyProgress <= 0;
        int currentIndex = walking ? (entity.walkCycle / 10) : (entity.flightCycle / 10);
        int prevIndex = currentIndex - 1;
        float dive = (10 - entity.diveProgress) * 0.1F;
        if (prevIndex < 0) {
            prevIndex = walking ? 3 : 5;
        }
        IceAndFireTabulaModel currentPosition = walking ? walkPoses[currentIndex] : flyPoses[currentIndex];
        IceAndFireTabulaModel prevPosition = walking ? walkPoses[prevIndex] : flyPoses[prevIndex];
        float delta = ((walking ? entity.walkCycle : entity.flightCycle) / 10.0F) % 1.0F;
        float deltaTicks = delta + (LLibrary.PROXY.getPartialTicks() / 10.0F);
        if(delta == 0){
            deltaTicks = 0;
        }
        AdvancedModelRenderer[] neckParts = { model.getCube("Neck1"), model.getCube("Neck2"), model.getCube("Neck3"), model.getCube("Neck3"), model.getCube("Head")};
        AdvancedModelRenderer[] tailParts = { model.getCube("Tail1"), model.getCube("Tail2"), model.getCube("Tail3"), model.getCube("Tail4")};
        AdvancedModelRenderer[] tailPartsWBody = { model.getCube("BodyLower"), model.getCube("Tail1"), model.getCube("Tail2"), model.getCube("Tail3"), model.getCube("Tail4")};
        AdvancedModelRenderer[] toesPartsL = { model.getCube("ToeL1"), model.getCube("ToeL2"), model.getCube("ToeL3")};
        AdvancedModelRenderer[] toesPartsR = { model.getCube("ToeR1"), model.getCube("ToeR2"), model.getCube("ToeR3")};
        AdvancedModelRenderer[] clawL = { model.getCube("ClawL")};
        AdvancedModelRenderer[] clawR = { model.getCube("ClawR")};

        for(AdvancedModelRenderer cube : model.getCubes().values()) {
            this.genderMob(entity, cube);
            if (walking && entity.flyProgress <= 0.0F && entity.hoverProgress <= 0.0F && entity.modelDeadProgress <= 0.0F) {
                AdvancedModelRenderer walkPart = EnumRemodelDragonAnimations.GROUND_POSE.icedragon_model.getCube(cube.boxName);
                if(prevPosition.getCube(cube.boxName) != null) {
                    float prevX = prevPosition.getCube(cube.boxName).rotateAngleX;
                    float prevY = prevPosition.getCube(cube.boxName).rotateAngleY;
                    float prevZ = prevPosition.getCube(cube.boxName).rotateAngleZ;
                    float x = currentPosition.getCube(cube.boxName).rotateAngleX;
                    float y = currentPosition.getCube(cube.boxName).rotateAngleY;
                    float z = currentPosition.getCube(cube.boxName).rotateAngleZ;
                    if (isWing(model, cube) && (entity.getAnimation() == EntityDragonBase.ANIMATION_WINGBLAST || entity.getAnimation() == EntityDragonBase.ANIMATION_EPIC_ROAR)) {
                        this.addToRotateAngle(cube, limbSwingAmount, walkPart.rotateAngleX, walkPart.rotateAngleY, walkPart.rotateAngleZ);
                    } else {
                        this.addToRotateAngle(cube, limbSwingAmount, prevX + deltaTicks * distance(prevX, x), prevY + deltaTicks * distance(prevY, y), prevZ + deltaTicks * distance(prevZ, z));
                    }
                }
            }
            if (entity.modelDeadProgress > 0.0F) {
                if (!isPartEqual(cube, EnumRemodelDragonAnimations.DEAD.icedragon_model.getCube(cube.boxName))) {
                    transitionTo(cube, EnumRemodelDragonAnimations.DEAD.icedragon_model.getCube(cube.boxName), entity.modelDeadProgress, 20, cube.boxName.equals("ThighR") || cube.boxName.equals("ThighL"));
                }
            }
            if (entity.sleepProgress > 0.0F) {
                if (!isPartEqual(cube, EnumRemodelDragonAnimations.SLEEPING_POSE.icedragon_model.getCube(cube.boxName))) {
                    transitionTo(cube, EnumRemodelDragonAnimations.SLEEPING_POSE.icedragon_model.getCube(cube.boxName), entity.sleepProgress, 20, false);
                }
            }
            if (entity.hoverProgress > 0.0F) {
                if (!isPartEqual(cube, EnumRemodelDragonAnimations.HOVERING_POSE.icedragon_model.getCube(cube.boxName)) && !isWing(model, cube)) {
                    transitionTo(cube, EnumRemodelDragonAnimations.HOVERING_POSE.icedragon_model.getCube(cube.boxName), entity.hoverProgress, 20, false);
                }
            }
            if (entity.flyProgress > 0.0F) {
                if (!isPartEqual(cube, EnumRemodelDragonAnimations.FLYING_POSE.icedragon_model.getCube(cube.boxName))) {
                    transitionTo(cube, EnumRemodelDragonAnimations.FLYING_POSE.icedragon_model.getCube(cube.boxName), entity.flyProgress - entity.diveProgress * 2, 20, false);
                }
            }
            if (entity.sitProgress > 0.0F) {
                if (!entity.isRiding()) {
                    if (!isPartEqual(cube, EnumRemodelDragonAnimations.SITTING_POSE.icedragon_model.getCube(cube.boxName))) {
                        transitionTo(cube, EnumRemodelDragonAnimations.SITTING_POSE.icedragon_model.getCube(cube.boxName), entity.sitProgress, 20, false);
                    }
                }
            }
            if (entity.ridingProgress > 0.0F){
                if (!isPartEqual(cube, EnumRemodelDragonAnimations.SIT_ON_PLAYER_POSE.icedragon_model.getCube(cube.boxName))) {
                    transitionTo(cube, EnumRemodelDragonAnimations.SIT_ON_PLAYER_POSE.icedragon_model.getCube(cube.boxName), entity.ridingProgress, 20, false);
                    if(cube.boxName.equals("BodyUpper")){
                        cube.rotationPointZ -= ((Math.toRadians(-15F) - cube.rotationPointZ) / 20) * entity.ridingProgress;
                    }

                }
            }
            if(entity.tackleProgress > 0.0F){
                if(!isPartEqual(EnumRemodelDragonAnimations.TACKLE.icedragon_model.getCube(cube.boxName), EnumRemodelDragonAnimations.FLYING_POSE.icedragon_model.getCube(cube.boxName)) && !isWing(model, cube)){
                    transitionTo(cube, EnumRemodelDragonAnimations.TACKLE.icedragon_model.getCube(cube.boxName), entity.tackleProgress, 5, false);
                }
            }
            if (entity.diveProgress > 0.0F) {
                if (!isPartEqual(cube, EnumRemodelDragonAnimations.DIVING_POSE.icedragon_model.getCube(cube.boxName))) {
                    transitionTo(cube, EnumRemodelDragonAnimations.DIVING_POSE.icedragon_model.getCube(cube.boxName), entity.diveProgress, 10, false);
                }
            }
            if(entity.fireBreathProgress > 0.0F) {
                if (!isPartEqual(cube, EnumRemodelDragonAnimations.STREAM_BREATH.icedragon_model.getCube(cube.boxName)) && !isWing(model, cube) && !cube.boxName.contains("Finger")) {
                    if (entity.prevFireBreathProgress <= entity.fireBreathProgress) {
                        transitionTo(cube, EnumRemodelDragonAnimations.BLAST_CHARGE3.icedragon_model.getCube(cube.boxName), MathHelper.clamp(entity.fireBreathProgress, 0, 5), 5, false);
                    }
                    transitionTo(cube, EnumRemodelDragonAnimations.STREAM_BREATH.icedragon_model.getCube(cube.boxName), MathHelper.clamp(entity.fireBreathProgress - 5, 0, 5), 5, false);

                }
            }
            if(!walking){
                AdvancedModelRenderer flightPart = EnumRemodelDragonAnimations.FLYING_POSE.icedragon_model.getCube(cube.boxName);
                float prevX = prevPosition.getCube(cube.boxName).rotateAngleX;
                float prevY = prevPosition.getCube(cube.boxName).rotateAngleY;
                float prevZ = prevPosition.getCube(cube.boxName).rotateAngleZ;
                float x = currentPosition.getCube(cube.boxName).rotateAngleX;
                float y = currentPosition.getCube(cube.boxName).rotateAngleY;
                float z = currentPosition.getCube(cube.boxName).rotateAngleZ;
                if(x != flightPart.rotateAngleX || y != flightPart.rotateAngleY || z != flightPart.rotateAngleZ) {
                    this.setRotateAngle(cube, 1F, prevX + deltaTicks * distance(prevX, x), prevY + deltaTicks * distance(prevY, y), prevZ + deltaTicks * distance(prevZ, z));
                }
            }
        }
        float speed_walk = 0.2F;
        float speed_idle = 0.05F;
        float speed_fly = 0.2F;
        float degree_walk = 0.5F;
        float degree_idle = 0.5F;
        float degree_fly = 0.5F;
        if(!entity.isAIDisabled()) {
            if (!walking) {
                model.bob(model.getCube("BodyUpper"), -speed_fly, degree_fly * 5, false, ageInTicks, 1);
                model.walk(model.getCube("BodyUpper"), -speed_fly, degree_fly * 0.1F, false, 0, 0, ageInTicks, 1);
                model.chainWave(tailPartsWBody, speed_fly, degree_fly * -0.1F, 0, ageInTicks, 1);
                model.chainWave(neckParts, speed_fly, degree_fly * 0.2F, -4, ageInTicks, 1);
                model.chainWave(toesPartsL, speed_fly, degree_fly * 0.2F, -2, ageInTicks, 1);
                model.chainWave(toesPartsR, speed_fly, degree_fly * 0.2F, -2, ageInTicks, 1);
                model.walk(model.getCube("ThighR"), -speed_fly, degree_fly * 0.1F, false, 0, 0, ageInTicks, 1);
                model.walk(model.getCube("ThighL"), -speed_fly, degree_fly * 0.1F, true, 0, 0, ageInTicks, 1);
            } else {
                model.bob(model.getCube("BodyUpper"), speed_walk * 2, degree_walk * 1.7F, false, limbSwing, limbSwingAmount);
                model.bob(model.getCube("ThighR"), speed_walk, degree_walk * 1.7F, false, limbSwing, limbSwingAmount);
                model.bob(model.getCube("ThighL"), speed_walk, degree_walk * 1.7F, false, limbSwing, limbSwingAmount);
                model.chainSwing(tailParts, speed_walk, degree_walk * 0.25F, -2, limbSwing, limbSwingAmount);
                model.chainWave(tailParts, speed_walk, degree_walk * 0.15F, 2, limbSwing, limbSwingAmount);
                model.chainSwing(neckParts, speed_walk, degree_walk * 0.15F, 2, limbSwing, limbSwingAmount);
                model.chainWave(neckParts, speed_walk, degree_walk * 0.05F, -2, limbSwing, limbSwingAmount);
                model.chainSwing(tailParts, speed_idle, degree_idle * 0.25F, -2, ageInTicks, 1);
                model.chainWave(tailParts, speed_idle, degree_idle * 0.15F, -2, ageInTicks, 1);
                model.chainWave(neckParts, speed_idle, degree_idle * -0.15F, -3, ageInTicks, 1);
                model.walk(model.getCube("Neck1"), speed_idle, degree_idle * 0.05F, false, 0, 0, ageInTicks, 1);
            }
            model.bob(model.getCube("BodyUpper"), speed_idle, degree_idle * 1.3F, false, ageInTicks, 1);
            model.bob(model.getCube("ThighR"), speed_idle, -degree_idle * 1.3F, false, ageInTicks, 1);
            model.bob(model.getCube("ThighL"), speed_idle, -degree_idle * 1.3F, false, ageInTicks, 1);
            model.bob(model.getCube("armR1"), speed_idle, -degree_idle * 1.3F, false, ageInTicks, 1);
            model.bob(model.getCube("armL1"), speed_idle, -degree_idle * 1.3F, false, ageInTicks, 1);
            if(entity.getAnimation() != EntityDragonBase.ANIMATION_SHAKEPREY || entity.getAnimation() != EntityDragonBase.ANIMATION_ROAR){
                model.faceTarget(rotationYaw, rotationPitch, 4, neckParts);
            }
            if(entity.isActuallyBreathingFire()){
                float speed_shake = 0.7F;
                float degree_shake = 0.1F;
                model.chainFlap(neckParts, speed_shake, degree_shake, 2, ageInTicks, 1);
                model.chainSwing(neckParts, speed_shake * 0.65F, degree_shake * 0.1F, 1, ageInTicks, 1);
            }
        }
        if(!entity.isModelDead()){
            entity.turn_buffer.applyChainSwingBuffer(neckParts);
            entity.tail_buffer.applyChainSwingBuffer(tailPartsWBody);
            if(entity.flyProgress > 0 || entity.hoverProgress > 0){
                entity.roll_buffer.applyChainFlapBuffer(model.getCube("BodyUpper"));
                entity.pitch_buffer_body.applyChainWaveBuffer(model.getCube("BodyUpper"));
                entity.pitch_buffer.applyChainWaveBufferReverse(tailPartsWBody);
            }

        }
        if (entity.width >= 2 && entity.flyProgress == 0 && entity.hoverProgress == 0) {
            LegArticulator.articulateQuadruped(entity, entity.legSolver, model.getCube("BodyUpper"), model.getCube("BodyLower"), model.getCube("Neck1"),
                    model.getCube("ThighL"), model.getCube("LegL"), toesPartsL,
                    model.getCube("ThighR"), model.getCube("LegR"), toesPartsR,
                    model.getCube("armL1"), model.getCube("armL2"), clawL,
                    model.getCube("armR1"), model.getCube("armR2"), clawR,
                    1.0F, 0.5F, 0.5F, -0.15F, -0.15F, 0F,
                    Minecraft.getMinecraft().getRenderPartialTicks()
            );
        }
    }

    private void genderMob(EntityIceDragon entity, AdvancedModelRenderer cube) {
        if(!entity.isMale()){
            IceAndFireTabulaModel maleModel = EnumRemodelDragonAnimations.MALE.icedragon_model;
            IceAndFireTabulaModel femaleModel = EnumRemodelDragonAnimations.FEMALE.icedragon_model;
            float x = femaleModel.getCube(cube.boxName).rotateAngleX;
            float y = femaleModel.getCube(cube.boxName).rotateAngleY;
            float z = femaleModel.getCube(cube.boxName).rotateAngleZ;
            if(x != maleModel.getCube(cube.boxName).rotateAngleX || y != maleModel.getCube(cube.boxName).rotateAngleY || z != maleModel.getCube(cube.boxName).rotateAngleZ) {
                this.setRotateAngle(cube, 1F, x, y, z);
            }
        }
    }

    private boolean isWing(IceAndFireTabulaModel model, AdvancedModelRenderer modelRenderer){

        return model.getCube("armL1") == modelRenderer || model.getCube("armR1") == modelRenderer || model.getCube("armL1").childModels.contains(modelRenderer) || model.getCube("armR1").childModels.contains(modelRenderer);
    }

    public void animate(IceAndFireTabulaModel model, EntityIceDragon entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
        model.llibAnimator.update(entity);
        model.llibAnimator.setAnimation(EntityIceDragon.ANIMATION_FIRECHARGE);
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumRemodelDragonAnimations.BLAST_CHARGE1.icedragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumRemodelDragonAnimations.BLAST_CHARGE2.icedragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPose(model, EnumRemodelDragonAnimations.BLAST_CHARGE3.icedragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(5);
        model.llibAnimator.setAnimation(EntityIceDragon.ANIMATION_SPEAK);
        model.llibAnimator.startKeyframe(5);
        this.rotate(model.llibAnimator, model.getCube("Jaw"), 18, 0, 0);
        model.llibAnimator.move(model.getCube("Jaw"), 0, 0, 0.2F);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.setStaticKeyframe(5);
        model.llibAnimator.startKeyframe(5);
        this.rotate(model.llibAnimator, model.getCube("Jaw"), 18, 0, 0);
        model.llibAnimator.move(model.getCube("Jaw"), 0, 0, 0.2F);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(5);

        model.llibAnimator.setAnimation(EntityIceDragon.ANIMATION_BITE);
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumRemodelDragonAnimations.BITE1.icedragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPose(model, EnumRemodelDragonAnimations.BITE2.icedragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPose(model, EnumRemodelDragonAnimations.BITE3.icedragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(10);

        model.llibAnimator.setAnimation(EntityIceDragon.ANIMATION_SHAKEPREY);
        model.llibAnimator.startKeyframe(15);
        moveToPose(model, EnumRemodelDragonAnimations.GRAB1.icedragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumRemodelDragonAnimations.GRAB2.icedragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumRemodelDragonAnimations.GRAB_SHAKE1.icedragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumRemodelDragonAnimations.GRAB_SHAKE2.icedragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumRemodelDragonAnimations.GRAB_SHAKE3.icedragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(10);
        model.llibAnimator.setAnimation(EntityIceDragon.ANIMATION_TAILWHACK);
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumRemodelDragonAnimations.TAIL_WHIP1.icedragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumRemodelDragonAnimations.TAIL_WHIP2.icedragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumRemodelDragonAnimations.TAIL_WHIP3.icedragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(10);
        model.llibAnimator.setAnimation(EntityIceDragon.ANIMATION_WINGBLAST);
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumRemodelDragonAnimations.WING_BLAST1.icedragon_model);
        model.llibAnimator.move(model.getCube("BodyUpper"), 0, -4F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPose(model, EnumRemodelDragonAnimations.WING_BLAST2.icedragon_model);
        model.llibAnimator.move(model.getCube("BodyUpper"), 0, -4F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPose(model, EnumRemodelDragonAnimations.WING_BLAST3.icedragon_model);
        model.llibAnimator.move(model.getCube("BodyUpper"), 0, -4F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPose(model, EnumRemodelDragonAnimations.WING_BLAST4.icedragon_model);
        model.llibAnimator.move(model.getCube("BodyUpper"), 0, -4F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPose(model, EnumRemodelDragonAnimations.WING_BLAST5.icedragon_model);
        model.llibAnimator.move(model.getCube("BodyUpper"), 0, -4F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPose(model, EnumRemodelDragonAnimations.WING_BLAST6.icedragon_model);
        model.llibAnimator.move(model.getCube("BodyUpper"), 0, -4F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPose(model, EnumRemodelDragonAnimations.WING_BLAST5.icedragon_model);
        model.llibAnimator.move(model.getCube("BodyUpper"), 0, -4F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(10);
        model.llibAnimator.setAnimation(EntityIceDragon.ANIMATION_ROAR);
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumRemodelDragonAnimations.ROAR1.icedragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumRemodelDragonAnimations.ROAR2.icedragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumRemodelDragonAnimations.ROAR3.icedragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(10);

        model.llibAnimator.setAnimation(EntityIceDragon.ANIMATION_EPIC_ROAR);
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumRemodelDragonAnimations.EPIC_ROAR1.icedragon_model);
        model.llibAnimator.move(model.getCube("BodyUpper"), 0, -6.8F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumRemodelDragonAnimations.EPIC_ROAR2.icedragon_model);
        model.llibAnimator.move(model.getCube("BodyUpper"), 0, -6.8F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumRemodelDragonAnimations.EPIC_ROAR3.icedragon_model);
        model.llibAnimator.move(model.getCube("BodyUpper"), 0, -6.8F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumRemodelDragonAnimations.EPIC_ROAR2.icedragon_model);
        model.llibAnimator.move(model.getCube("BodyUpper"), 0, -6.8F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumRemodelDragonAnimations.EPIC_ROAR3.icedragon_model);
        model.llibAnimator.move(model.getCube("BodyUpper"), 0, -6.8F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(10);
    }


}
