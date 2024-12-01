package me.pepperbell.continuity.client.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.pepperbell.continuity.client.resource.ResourceRedirectHandler;
import net.minecraft.resource.LifecycledResourceManagerImpl;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

@Mixin(LifecycledResourceManagerImpl.class)
abstract class LifecycledResourceManagerImplMixin {
	@Unique
	private boolean continuity$useRedirects = false;

	@Inject(method = "<init>(Lnet/minecraft/resource/ResourceType;Ljava/util/List;)V", at = @At("TAIL"))
	private void continuity$onTailInit(ResourceType type, List<ResourcePack> packs, CallbackInfo ci) {
		continuity$useRedirects = type == ResourceType.CLIENT_RESOURCES;
	}

	@ModifyVariable(method = "getResource(Lnet/minecraft/util/Identifier;)Ljava/util/Optional;", at = @At("HEAD"), argsOnly = true)
	private Identifier continuity$redirectGetResourceId(Identifier id) {
		if (continuity$useRedirects) {
			return ResourceRedirectHandler.redirect(id);
		}
		return id;
	}

	@ModifyVariable(method = "getAllResources(Lnet/minecraft/util/Identifier;)Ljava/util/List;", at = @At("HEAD"), argsOnly = true)
	private Identifier continuity$redirectGetAllResourcesId(Identifier id) {
		if (continuity$useRedirects) {
			return ResourceRedirectHandler.redirect(id);
		}
		return id;
	}
}
