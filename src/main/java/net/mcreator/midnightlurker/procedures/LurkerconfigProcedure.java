package net.mcreator.midnightlurker.procedures;

import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nullable;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class LurkerconfigProcedure {
	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		execute();
	}

	public static void execute() {
		execute(null);
	}

	private static void execute(@Nullable Event event) {
		com.google.gson.JsonObject mainjsonobject = new com.google.gson.JsonObject();
		File lurker = new File("");
		lurker = new File((FMLPaths.GAMEDIR.get().toString() + "/config/"), File.separator + "midnightlurkerconfig.json");
		if (!lurker.exists()) {
			try {
				lurker.getParentFile().mkdirs();
				lurker.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			mainjsonobject.addProperty("lurker_chase_music", false);
			mainjsonobject.addProperty("lurker_spawn_rate", 5);
			mainjsonobject.addProperty("the spawn rate can range from 1 to 5, with 1 being the lowest and 5 being the highest.", 0);
			mainjsonobject.addProperty("pop_up_jumpscare", true);
			mainjsonobject.addProperty("jumpscare_sound", true);
			mainjsonobject.addProperty("longer_lurker_chase", false);
			mainjsonobject.addProperty("spooky_ambience", true);
			mainjsonobject.addProperty("multi_spawning", false);
			mainjsonobject.addProperty("insanity_progress_effect", true);
			mainjsonobject.addProperty("insanity_countdown_time", 2);
			mainjsonobject.addProperty("the insanity countdown time determines how long the timer is for insanity. 1 is 5mins, 2 is 10mins, 3 is 20mins, and 4 is 30mins.", 0);
			{
				Gson mainGSONBuilderVariable = new GsonBuilder().setPrettyPrinting().create();
				try {
					FileWriter fileWriter = new FileWriter(lurker);
					fileWriter.write(mainGSONBuilderVariable.toJson(mainjsonobject));
					fileWriter.close();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		}
	}
}