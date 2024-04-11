package com.maximumg9.packetutils.util;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.mappingio.MappingReader;
import net.fabricmc.mappingio.tree.MappingTree;
import net.fabricmc.mappingio.tree.MemoryMappingTree;
import net.fabricmc.mappingio.tree.VisitableMappingTree;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.*;
import net.minecraft.network.packet.c2s.config.ReadyC2SPacket;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import net.minecraft.network.packet.c2s.login.EnterConfigurationC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginKeyC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginQueryResponseC2SPacket;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.network.packet.c2s.query.QueryPingC2SPacket;
import net.minecraft.network.packet.c2s.query.QueryRequestC2SPacket;
import net.minecraft.network.packet.s2c.common.*;
import net.minecraft.network.packet.s2c.config.DynamicRegistriesS2CPacket;
import net.minecraft.network.packet.s2c.config.FeaturesS2CPacket;
import net.minecraft.network.packet.s2c.config.ReadyS2CPacket;
import net.minecraft.network.packet.s2c.login.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.network.packet.s2c.query.PingResultS2CPacket;
import net.minecraft.network.packet.s2c.query.QueryResponseS2CPacket;

import java.lang.reflect.Field;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MappingHelper {
    private static final Object2ObjectMap<String,Class<? extends Packet<?>>> stringToClassMap = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectMap<Class<? extends Packet<?>>,String> classToStringMap = new Object2ObjectOpenHashMap<>();

    public static Class<? extends Packet<?>> get(String str) {
        return stringToClassMap.get(str);
    }
    private static final VisitableMappingTree tree = new MemoryMappingTree();
    private static final int INTERMEDIARY_ID;
    private static final int NAMED_ID;

    public static List<Class<? extends Packet<?>>> getContains(String str) {
        str = str.toLowerCase();
        List<Class<? extends Packet<?>>> list = new ArrayList<>();
        for(String key : stringToClassMap.keySet()) {
            if(key.toLowerCase().contains(str)) {
                list.add(stringToClassMap.get(key));
            }
        }
        return list;
    }

    public static String get(Class<? extends Packet> clazz) {
        return classToStringMap.get(clazz);
    }

    static {

        try {
            URI uri = Objects.requireNonNull(MappingHelper.class.getClassLoader().getResource(
                    "yarn-tiny-1.20.4+build.local"
            )).toURI();

            if("jar".equals(uri.getScheme())){
                for (FileSystemProvider provider: FileSystemProvider.installedProviders()) {
                    if (provider.getScheme().equalsIgnoreCase("jar")) {
                        try {
                            provider.getFileSystem(uri);
                        } catch (FileSystemNotFoundException e) {
                            // in this case we need to initialize it first:
                            provider.newFileSystem(uri, Collections.emptyMap());
                        }
                    }
                }
            }
            Path path = Paths.get(uri);

            LogUtils.getLogger().info(path.toAbsolutePath().toString());

            long startMemory = Runtime.getRuntime().freeMemory();
            MappingReader.read(path, tree);
            long endMemory = Runtime.getRuntime().freeMemory();

            System.out.println(endMemory-startMemory);

            INTERMEDIARY_ID = tree.getNamespaceId("intermediary");
            NAMED_ID = tree.getNamespaceId("named");
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
        //Hooray for CC0 I get to use the yarn mapping names if I want to!!!!
        stringToClassMap.put(getClassName(ClientOptionsC2SPacket.class),ClientOptionsC2SPacket.class);
        stringToClassMap.put(getClassName(CommonPongC2SPacket.class),CommonPongC2SPacket.class);
        stringToClassMap.put(getClassName(CustomPayloadC2SPacket.class),CustomPayloadC2SPacket.class);
        stringToClassMap.put(getClassName(KeepAliveC2SPacket.class),KeepAliveC2SPacket.class);
        stringToClassMap.put(getClassName(ResourcePackStatusC2SPacket.class),ResourcePackStatusC2SPacket.class);
        stringToClassMap.put(getClassName(ReadyC2SPacket.class),ReadyC2SPacket.class);
        stringToClassMap.put(getClassName(HandshakeC2SPacket.class),HandshakeC2SPacket.class);
        stringToClassMap.put(getClassName(EnterConfigurationC2SPacket.class),EnterConfigurationC2SPacket.class);
        stringToClassMap.put(getClassName(LoginHelloC2SPacket.class),LoginHelloC2SPacket.class);
        stringToClassMap.put(getClassName(LoginKeyC2SPacket.class),LoginKeyC2SPacket.class);
        stringToClassMap.put(getClassName(LoginQueryResponseC2SPacket.class),LoginQueryResponseC2SPacket.class);
        stringToClassMap.put(getClassName(AcknowledgeChunksC2SPacket.class),AcknowledgeChunksC2SPacket.class);
        stringToClassMap.put(getClassName(AcknowledgeReconfigurationC2SPacket.class),AcknowledgeReconfigurationC2SPacket.class);
        stringToClassMap.put(getClassName(AdvancementTabC2SPacket.class),AdvancementTabC2SPacket.class);
        stringToClassMap.put(getClassName(BoatPaddleStateC2SPacket.class),BoatPaddleStateC2SPacket.class);
        stringToClassMap.put(getClassName(BookUpdateC2SPacket.class),BookUpdateC2SPacket.class);
        stringToClassMap.put(getClassName(ButtonClickC2SPacket.class),ButtonClickC2SPacket.class);
        stringToClassMap.put(getClassName(ChatMessageC2SPacket.class),ChatMessageC2SPacket.class);
        stringToClassMap.put(getClassName(ClickSlotC2SPacket.class),ClickSlotC2SPacket.class);
        stringToClassMap.put(getClassName(ClientCommandC2SPacket.class),ClientCommandC2SPacket.class);
        stringToClassMap.put(getClassName(ClientStatusC2SPacket.class),ClientStatusC2SPacket.class);
        stringToClassMap.put(getClassName(CloseHandledScreenC2SPacket.class),CloseHandledScreenC2SPacket.class);
        stringToClassMap.put(getClassName(CommandExecutionC2SPacket.class),CommandExecutionC2SPacket.class);
        stringToClassMap.put(getClassName(CraftRequestC2SPacket.class),CraftRequestC2SPacket.class);
        stringToClassMap.put(getClassName(CreativeInventoryActionC2SPacket.class),CreativeInventoryActionC2SPacket.class);
        stringToClassMap.put(getClassName(HandSwingC2SPacket.class),HandSwingC2SPacket.class);
        stringToClassMap.put(getClassName(JigsawGeneratingC2SPacket.class),JigsawGeneratingC2SPacket.class);
        stringToClassMap.put(getClassName(MessageAcknowledgmentC2SPacket.class),MessageAcknowledgmentC2SPacket.class);
        stringToClassMap.put(getClassName(PickFromInventoryC2SPacket.class),PickFromInventoryC2SPacket.class);
        stringToClassMap.put(getClassName(PlayerActionC2SPacket.class),PlayerActionC2SPacket.class);
        stringToClassMap.put(getClassName(PlayerInputC2SPacket.class),PlayerInputC2SPacket.class);
        stringToClassMap.put(getClassName(PlayerInteractBlockC2SPacket.class),PlayerInteractBlockC2SPacket.class);
        stringToClassMap.put(getClassName(PlayerInteractEntityC2SPacket.class),PlayerInteractEntityC2SPacket.class);
        stringToClassMap.put(getClassName(PlayerInteractItemC2SPacket.class),PlayerInteractItemC2SPacket.class);
        stringToClassMap.put(getClassName(PlayerMoveC2SPacket.Full.class),PlayerMoveC2SPacket.Full.class);
        stringToClassMap.put(getClassName(PlayerMoveC2SPacket.LookAndOnGround.class),PlayerMoveC2SPacket.LookAndOnGround.class);
        stringToClassMap.put(getClassName(PlayerMoveC2SPacket.OnGroundOnly.class),PlayerMoveC2SPacket.OnGroundOnly.class);
        stringToClassMap.put(getClassName(PlayerMoveC2SPacket.PositionAndOnGround.class),PlayerMoveC2SPacket.PositionAndOnGround.class);
        stringToClassMap.put(getClassName(PlayerSessionC2SPacket.class),PlayerSessionC2SPacket.class);
        stringToClassMap.put(getClassName(QueryBlockNbtC2SPacket.class),QueryBlockNbtC2SPacket.class);
        stringToClassMap.put(getClassName(QueryEntityNbtC2SPacket.class),QueryEntityNbtC2SPacket.class);
        stringToClassMap.put(getClassName(RecipeBookDataC2SPacket.class),RecipeBookDataC2SPacket.class);
        stringToClassMap.put(getClassName(RecipeCategoryOptionsC2SPacket.class),RecipeCategoryOptionsC2SPacket.class);
        stringToClassMap.put(getClassName(RenameItemC2SPacket.class),RenameItemC2SPacket.class);
        stringToClassMap.put(getClassName(RequestCommandCompletionsC2SPacket.class),RequestCommandCompletionsC2SPacket.class);
        stringToClassMap.put(getClassName(SelectMerchantTradeC2SPacket.class),SelectMerchantTradeC2SPacket.class);
        stringToClassMap.put(getClassName(SpectatorTeleportC2SPacket.class),SpectatorTeleportC2SPacket.class);
        stringToClassMap.put(getClassName(TeleportConfirmC2SPacket.class),TeleportConfirmC2SPacket.class);
        stringToClassMap.put(getClassName(UpdateBeaconC2SPacket.class),UpdateBeaconC2SPacket.class);
        stringToClassMap.put(getClassName(UpdateCommandBlockC2SPacket.class),UpdateCommandBlockC2SPacket.class);
        stringToClassMap.put(getClassName(UpdateCommandBlockMinecartC2SPacket.class),UpdateCommandBlockMinecartC2SPacket.class);
        stringToClassMap.put(getClassName(UpdateDifficultyC2SPacket.class),UpdateDifficultyC2SPacket.class);
        stringToClassMap.put(getClassName(UpdateDifficultyLockC2SPacket.class),UpdateDifficultyLockC2SPacket.class);
        stringToClassMap.put(getClassName(UpdateJigsawC2SPacket.class),UpdateJigsawC2SPacket.class);
        stringToClassMap.put(getClassName(UpdatePlayerAbilitiesC2SPacket.class),UpdatePlayerAbilitiesC2SPacket.class);
        stringToClassMap.put(getClassName(UpdateSelectedSlotC2SPacket.class),UpdateSelectedSlotC2SPacket.class);
        stringToClassMap.put(getClassName(UpdateSignC2SPacket.class),UpdateSignC2SPacket.class);
        stringToClassMap.put(getClassName(UpdateStructureBlockC2SPacket.class),UpdateStructureBlockC2SPacket.class);
        stringToClassMap.put(getClassName(VehicleMoveC2SPacket.class),VehicleMoveC2SPacket.class);
        stringToClassMap.put(getClassName(QueryPingC2SPacket.class),QueryPingC2SPacket.class);
        stringToClassMap.put(getClassName(QueryRequestC2SPacket.class),QueryRequestC2SPacket.class);
        stringToClassMap.put(getClassName(CommonPingS2CPacket.class),CommonPingS2CPacket.class);
        stringToClassMap.put(getClassName(CustomPayloadS2CPacket.class),CustomPayloadS2CPacket.class);
        stringToClassMap.put(getClassName(DisconnectS2CPacket.class),DisconnectS2CPacket.class);
        stringToClassMap.put(getClassName(KeepAliveS2CPacket.class),KeepAliveS2CPacket.class);
        stringToClassMap.put(getClassName(ResourcePackSendS2CPacket.class),ResourcePackSendS2CPacket.class);
        stringToClassMap.put(getClassName(SynchronizeTagsS2CPacket.class),SynchronizeTagsS2CPacket.class);
        stringToClassMap.put(getClassName(DynamicRegistriesS2CPacket.class),DynamicRegistriesS2CPacket.class);
        stringToClassMap.put(getClassName(FeaturesS2CPacket.class),FeaturesS2CPacket.class);
        stringToClassMap.put(getClassName(ReadyS2CPacket.class),ReadyS2CPacket.class);
        stringToClassMap.put(getClassName(LoginCompressionS2CPacket.class),LoginCompressionS2CPacket.class);
        stringToClassMap.put(getClassName(LoginDisconnectS2CPacket.class),LoginDisconnectS2CPacket.class);
        stringToClassMap.put(getClassName(LoginHelloS2CPacket.class),LoginHelloS2CPacket.class);
        stringToClassMap.put(getClassName(LoginQueryRequestS2CPacket.class),LoginQueryRequestS2CPacket.class);
        stringToClassMap.put(getClassName(LoginSuccessS2CPacket.class),LoginSuccessS2CPacket.class);
        stringToClassMap.put(getClassName(AdvancementUpdateS2CPacket.class),AdvancementUpdateS2CPacket.class);
        stringToClassMap.put(getClassName(BlockBreakingProgressS2CPacket.class),BlockBreakingProgressS2CPacket.class);
        stringToClassMap.put(getClassName(BlockEntityUpdateS2CPacket.class),BlockEntityUpdateS2CPacket.class);
        stringToClassMap.put(getClassName(BossBarS2CPacket.class),BossBarS2CPacket.class);
        stringToClassMap.put(getClassName(BundleS2CPacket.class),BundleS2CPacket.class);
        stringToClassMap.put(getClassName(ChatMessageS2CPacket.class),ChatMessageS2CPacket.class);
        stringToClassMap.put(getClassName(ChatSuggestionsS2CPacket.class),ChatSuggestionsS2CPacket.class);
        stringToClassMap.put(getClassName(ChunkBiomeDataS2CPacket.class),ChunkBiomeDataS2CPacket.class);
        stringToClassMap.put(getClassName(ChunkDataS2CPacket.class),ChunkDataS2CPacket.class);
        stringToClassMap.put(getClassName(ChunkDeltaUpdateS2CPacket.class),ChunkDeltaUpdateS2CPacket.class);
        stringToClassMap.put(getClassName(ChunkLoadDistanceS2CPacket.class),ChunkLoadDistanceS2CPacket.class);
        stringToClassMap.put(getClassName(ChunkRenderDistanceCenterS2CPacket.class),ChunkRenderDistanceCenterS2CPacket.class);
        stringToClassMap.put(getClassName(ChunkSentS2CPacket.class),ChunkSentS2CPacket.class);
        stringToClassMap.put(getClassName(ClearTitleS2CPacket.class),ClearTitleS2CPacket.class);
        stringToClassMap.put(getClassName(CloseScreenS2CPacket.class),CloseScreenS2CPacket.class);
        stringToClassMap.put(getClassName(CommandSuggestionsS2CPacket.class),CommandSuggestionsS2CPacket.class);
        stringToClassMap.put(getClassName(CommandTreeS2CPacket.class),CommandTreeS2CPacket.class);
        stringToClassMap.put(getClassName(CooldownUpdateS2CPacket.class),CooldownUpdateS2CPacket.class);
        stringToClassMap.put(getClassName(CraftFailedResponseS2CPacket.class),CraftFailedResponseS2CPacket.class);
        stringToClassMap.put(getClassName(DamageTiltS2CPacket.class),DamageTiltS2CPacket.class);
        stringToClassMap.put(getClassName(DeathMessageS2CPacket.class),DeathMessageS2CPacket.class);
        stringToClassMap.put(getClassName(DifficultyS2CPacket.class),DifficultyS2CPacket.class);
        stringToClassMap.put(getClassName(EndCombatS2CPacket.class),EndCombatS2CPacket.class);
        stringToClassMap.put(getClassName(EnterCombatS2CPacket.class),EnterCombatS2CPacket.class);
        stringToClassMap.put(getClassName(EnterReconfigurationS2CPacket.class),EnterReconfigurationS2CPacket.class);
        stringToClassMap.put(getClassName(EntitiesDestroyS2CPacket.class),EntitiesDestroyS2CPacket.class);
        stringToClassMap.put(getClassName(EntityAnimationS2CPacket.class),EntityAnimationS2CPacket.class);
        stringToClassMap.put(getClassName(EntityAttachS2CPacket.class),EntityAttachS2CPacket.class);
        stringToClassMap.put(getClassName(EntityAttributesS2CPacket.class),EntityAttributesS2CPacket.class);
        stringToClassMap.put(getClassName(EntityDamageS2CPacket.class),EntityDamageS2CPacket.class);
        stringToClassMap.put(getClassName(EntityEquipmentUpdateS2CPacket.class),EntityEquipmentUpdateS2CPacket.class);
        stringToClassMap.put(getClassName(EntityPassengersSetS2CPacket.class),EntityPassengersSetS2CPacket.class);
        stringToClassMap.put(getClassName(EntityPositionS2CPacket.class),EntityPositionS2CPacket.class);
        stringToClassMap.put(getClassName(EntityS2CPacket.MoveRelative.class),EntityS2CPacket.MoveRelative.class);
        stringToClassMap.put(getClassName(EntityS2CPacket.Rotate.class),EntityS2CPacket.Rotate.class);
        stringToClassMap.put(getClassName(EntityS2CPacket.RotateAndMoveRelative.class),EntityS2CPacket.RotateAndMoveRelative.class);
        stringToClassMap.put(getClassName(EntitySetHeadYawS2CPacket.class),EntitySetHeadYawS2CPacket.class);
        stringToClassMap.put(getClassName(EntitySpawnS2CPacket.class),EntitySpawnS2CPacket.class);
        stringToClassMap.put(getClassName(EntityStatusEffectS2CPacket.class),EntityStatusEffectS2CPacket.class);
        stringToClassMap.put(getClassName(EntityStatusS2CPacket.class),EntityStatusS2CPacket.class);
        stringToClassMap.put(getClassName(EntityTrackerUpdateS2CPacket.class),EntityTrackerUpdateS2CPacket.class);
        stringToClassMap.put(getClassName(EntityVelocityUpdateS2CPacket.class),EntityVelocityUpdateS2CPacket.class);
        stringToClassMap.put(getClassName(ExperienceBarUpdateS2CPacket.class),ExperienceBarUpdateS2CPacket.class);
        stringToClassMap.put(getClassName(ExperienceOrbSpawnS2CPacket.class),ExperienceOrbSpawnS2CPacket.class);
        stringToClassMap.put(getClassName(ExplosionS2CPacket.class),ExplosionS2CPacket.class);
        stringToClassMap.put(getClassName(GameJoinS2CPacket.class),GameJoinS2CPacket.class);
        stringToClassMap.put(getClassName(GameMessageS2CPacket.class),GameMessageS2CPacket.class);
        stringToClassMap.put(getClassName(GameStateChangeS2CPacket.class),GameStateChangeS2CPacket.class);
        stringToClassMap.put(getClassName(HealthUpdateS2CPacket.class),HealthUpdateS2CPacket.class);
        stringToClassMap.put(getClassName(InventoryS2CPacket.class),InventoryS2CPacket.class);
        stringToClassMap.put(getClassName(ItemPickupAnimationS2CPacket.class),ItemPickupAnimationS2CPacket.class);
        stringToClassMap.put(getClassName(LightUpdateS2CPacket.class),LightUpdateS2CPacket.class);
        stringToClassMap.put(getClassName(LookAtS2CPacket.class),LookAtS2CPacket.class);
        stringToClassMap.put(getClassName(MapUpdateS2CPacket.class),MapUpdateS2CPacket.class);
        stringToClassMap.put(getClassName(NbtQueryResponseS2CPacket.class),NbtQueryResponseS2CPacket.class);
        stringToClassMap.put(getClassName(OpenHorseScreenS2CPacket.class),OpenHorseScreenS2CPacket.class);
        stringToClassMap.put(getClassName(OpenScreenS2CPacket.class),OpenScreenS2CPacket.class);
        stringToClassMap.put(getClassName(OpenWrittenBookS2CPacket.class),OpenWrittenBookS2CPacket.class);
        stringToClassMap.put(getClassName(OverlayMessageS2CPacket.class),OverlayMessageS2CPacket.class);
        stringToClassMap.put(getClassName(ParticleS2CPacket.class),ParticleS2CPacket.class);
        stringToClassMap.put(getClassName(PlayerAbilitiesS2CPacket.class),PlayerAbilitiesS2CPacket.class);
        stringToClassMap.put(getClassName(PlayerActionResponseS2CPacket.class),PlayerActionResponseS2CPacket.class);
        stringToClassMap.put(getClassName(PlayerListHeaderS2CPacket.class),PlayerListHeaderS2CPacket.class);
        stringToClassMap.put(getClassName(PlayerListS2CPacket.class),PlayerListS2CPacket.class);
        stringToClassMap.put(getClassName(PlayerPositionLookS2CPacket.class),PlayerPositionLookS2CPacket.class);
        stringToClassMap.put(getClassName(PlayerRemoveS2CPacket.class),PlayerRemoveS2CPacket.class);
        stringToClassMap.put(getClassName(PlayerRespawnS2CPacket.class),PlayerRespawnS2CPacket.class);
        stringToClassMap.put(getClassName(PlayerSpawnPositionS2CPacket.class),PlayerSpawnPositionS2CPacket.class);
        stringToClassMap.put(getClassName(PlaySoundFromEntityS2CPacket.class),PlaySoundFromEntityS2CPacket.class);
        stringToClassMap.put(getClassName(PlaySoundS2CPacket.class),PlaySoundS2CPacket.class);
        stringToClassMap.put(getClassName(ProfilelessChatMessageS2CPacket.class),ProfilelessChatMessageS2CPacket.class);
        stringToClassMap.put(getClassName(RemoveEntityStatusEffectS2CPacket.class),RemoveEntityStatusEffectS2CPacket.class);
        stringToClassMap.put(getClassName(RemoveMessageS2CPacket.class),RemoveMessageS2CPacket.class);
        stringToClassMap.put(getClassName(ScoreboardDisplayS2CPacket.class),ScoreboardDisplayS2CPacket.class);
        stringToClassMap.put(getClassName(ScoreboardObjectiveUpdateS2CPacket.class),ScoreboardObjectiveUpdateS2CPacket.class);
        stringToClassMap.put(getClassName(ScoreboardScoreUpdateS2CPacket.class),ScoreboardScoreUpdateS2CPacket.class);
        stringToClassMap.put(getClassName(ScoreboardScoreResetS2CPacket.class),ScoreboardScoreResetS2CPacket.class);
        stringToClassMap.put(getClassName(ScreenHandlerPropertyUpdateS2CPacket.class),ScreenHandlerPropertyUpdateS2CPacket.class);
        stringToClassMap.put(getClassName(ScreenHandlerSlotUpdateS2CPacket.class),ScreenHandlerSlotUpdateS2CPacket.class);
        stringToClassMap.put(getClassName(SelectAdvancementTabS2CPacket.class),SelectAdvancementTabS2CPacket.class);
        stringToClassMap.put(getClassName(ServerMetadataS2CPacket.class),ServerMetadataS2CPacket.class);
        stringToClassMap.put(getClassName(SetCameraEntityS2CPacket.class),SetCameraEntityS2CPacket.class);
        stringToClassMap.put(getClassName(SetTradeOffersS2CPacket.class),SetTradeOffersS2CPacket.class);
        stringToClassMap.put(getClassName(SignEditorOpenS2CPacket.class),SignEditorOpenS2CPacket.class);
        stringToClassMap.put(getClassName(SimulationDistanceS2CPacket.class),SimulationDistanceS2CPacket.class);
        stringToClassMap.put(getClassName(StartChunkSendS2CPacket.class),StartChunkSendS2CPacket.class);
        stringToClassMap.put(getClassName(StatisticsS2CPacket.class),StatisticsS2CPacket.class);
        stringToClassMap.put(getClassName(StopSoundS2CPacket.class),StopSoundS2CPacket.class);
        stringToClassMap.put(getClassName(SubtitleS2CPacket.class),SubtitleS2CPacket.class);
        stringToClassMap.put(getClassName(SynchronizeRecipesS2CPacket.class),SynchronizeRecipesS2CPacket.class);
        stringToClassMap.put(getClassName(TeamS2CPacket.class),TeamS2CPacket.class);
        stringToClassMap.put(getClassName(TitleFadeS2CPacket.class),TitleFadeS2CPacket.class);
        stringToClassMap.put(getClassName(TitleS2CPacket.class),TitleS2CPacket.class);
        stringToClassMap.put(getClassName(UnloadChunkS2CPacket.class),UnloadChunkS2CPacket.class);
        stringToClassMap.put(getClassName(UnlockRecipesS2CPacket.class),UnlockRecipesS2CPacket.class);
        stringToClassMap.put(getClassName(UpdateSelectedSlotS2CPacket.class),UpdateSelectedSlotS2CPacket.class);
        stringToClassMap.put(getClassName(VehicleMoveS2CPacket.class),VehicleMoveS2CPacket.class);
        stringToClassMap.put(getClassName(WorldBorderCenterChangedS2CPacket.class),WorldBorderCenterChangedS2CPacket.class);
        stringToClassMap.put(getClassName(WorldBorderInitializeS2CPacket.class),WorldBorderInitializeS2CPacket.class);
        stringToClassMap.put(getClassName(WorldBorderInterpolateSizeS2CPacket.class),WorldBorderInterpolateSizeS2CPacket.class);
        stringToClassMap.put(getClassName(WorldBorderSizeChangedS2CPacket.class),WorldBorderSizeChangedS2CPacket.class);
        stringToClassMap.put(getClassName(WorldBorderWarningBlocksChangedS2CPacket.class),WorldBorderWarningBlocksChangedS2CPacket.class);
        stringToClassMap.put(getClassName(WorldBorderWarningTimeChangedS2CPacket.class),WorldBorderWarningTimeChangedS2CPacket.class);
        stringToClassMap.put(getClassName(WorldEventS2CPacket.class),WorldEventS2CPacket.class);
        stringToClassMap.put(getClassName(WorldTimeUpdateS2CPacket.class),WorldTimeUpdateS2CPacket.class);
        stringToClassMap.put(getClassName(PingResultS2CPacket.class),PingResultS2CPacket.class);
        stringToClassMap.put(getClassName(QueryResponseS2CPacket.class),QueryResponseS2CPacket.class);
        //Finally Done

        stringToClassMap.forEach((name, clazz) -> classToStringMap.put(clazz,name));
    }

    public static String getClassName(Class<? extends Packet<?>> clazz) {

        MappingTree.ClassMapping classMapping = tree.getClass(
                clazz.getName().replace('.', '/'),
                INTERMEDIARY_ID
        );

        if (classMapping != null) {
            String path = classMapping.getName(NAMED_ID);
            if(path != null) {
                String[] splitPath = path.split("/");
                return splitPath[splitPath.length-1];
            } else {
                throw new IllegalArgumentException("No mapping for that field");
            }
        } else {
            throw new IllegalArgumentException("No mapping for that class");
        }
    }

    public static String getFieldName(Field field) {
        MappingTree.FieldMapping fieldMapping =  tree.getField(
                field.getDeclaringClass().getName().replace('.','/'),
                field.getName(),
                null,
                INTERMEDIARY_ID
        );

        if (fieldMapping == null) {
            fieldMapping = tree.getField(
                    field.getDeclaringClass().getSuperclass().getName().replace('.','/'),
                    field.getName(),
                    null,
                    INTERMEDIARY_ID
            );
            if(fieldMapping == null) {
                throw new IllegalArgumentException("No mapping for that field");
            }
        }
        String name = fieldMapping.getName(NAMED_ID);
        if (name != null) {
            return name;
        } else {
            throw new IllegalArgumentException("No mapping for that field");
        }
    }
}
