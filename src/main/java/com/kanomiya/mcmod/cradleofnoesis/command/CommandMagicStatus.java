package com.kanomiya.mcmod.cradleofnoesis.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import scala.actors.threadpool.Arrays;

import com.kanomiya.mcmod.cradleofnoesis.CradleOfNoesisAPI;
import com.kanomiya.mcmod.cradleofnoesis.magic.MagicStatus;

/**
 * @author Kanomiya
 *
 */
public class CommandMagicStatus extends CommandBase {

	@Override public String getCommandName() {
		return "magicstatus";
	}

	@Override public String getCommandUsage(ICommandSender sender) {
		return "energyway.command.energyway.usage";
	}

	@Override public List addTabCompletionOptions(ICommandSender sender, String[] option, BlockPos pos) {
		List<String> list = new ArrayList<String>();

		if (option.length == 1) {
			list.add("accept");
		}


		if (option.length == 2) {
			if (option[0].equals("accept")) {
				list.addAll(Arrays.asList(MinecraftServer.getServer().getAllUsernames()));
			}
		}

		return list;
	}

	@Override public void processCommand(ICommandSender sender, String[] args) throws CommandException
	{
		int argLength = args.length;
		if (0 < argLength)
		{
			Entity entity = getPlayer(sender, args[1]);
			MagicStatus<Entity> magicStatus = entity.getCapability(CradleOfNoesisAPI.capMagicStatus, null);

			if (magicStatus != null) {

				if (argLength == 3 && args[0].equals("accept") && args[2].matches("-*[0-9]+")) {
					int amount = Integer.valueOf(args[2]);

					magicStatus.acceptMp(amount);

					sender.addChatMessage(new ChatComponentTranslation("cradleofnoesis.command.magicstatus.accept",
							sender.getDisplayName(),
							new ChatComponentText("" +amount)));
					return ;
				}

				if (argLength == 3 && args[0].equals("accept") && args[2].matches("-*[0-9]+")) {
					int amount = Integer.valueOf(args[2]);

					magicStatus.acceptMp(amount);

					sender.addChatMessage(new ChatComponentTranslation("cradleofnoesis.command.magicstatus.accept",
							sender.getDisplayName(),
							new ChatComponentText("" +amount)));
					return ;
				}

				/* else if (argLength == 3 && args[0].equals("charge") && args[2].matches("-*[0-9]+") && entity != null && entity instanceof EntityLivingBase) {
					int amount = Integer.valueOf(args[2]);

					if (args[1].equals("item")) {
						ItemStack heldStack = ((EntityLivingBase) entity).getHeldItem();

						if (heldStack != null) {
							MagicStatus stackStatus = KMagicAPI.getMagicStatus(heldStack);

							if (stackStatus != null) {
								MagicStatus.dealMp(null, stackStatus, amount, true, false);
								sender.addChatMessage(new ChatComponentTranslation("kmagic.command.kmagic.mp", heldStack.getDisplayName(), new ChatComponentText("" +amount)));
								KMagicAPI.setMagicStatus(heldStack, stackStatus);

								success = true;

							}
						}
					}


				}*/

			}

		}

		throw new WrongUsageException("cradleofnoesis.commands.magicstatus.usage", new Object[0]);
	}

}
