package org.devinprogress.nohome;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

public class MainListener implements Listener{
	@EventHandler
	public void onCommandInvoke(PlayerCommandPreprocessEvent e){
		String cmd[]=e.getMessage().split(" ");
		Player p=e.getPlayer();
		if (cmd[0].equalsIgnoreCase("/home")||cmd[0].equalsIgnoreCase("/homes")||cmd[0].equalsIgnoreCase("/ehome")||cmd[0].equalsIgnoreCase("/ehomes")){
			if (!homeAvailable(p,cmd.length==1?"":cmd[1])){
				p.sendMessage("那儿不是你的家");
				e.setCancelled(true);
			}
			return;
		}else if(cmd[0].equalsIgnoreCase("/sethome")||cmd[0].equalsIgnoreCase("/esethome")){
			if(!haveHomePremit(p.getName(),p.getLocation())){
				p.sendMessage("你不能在这设置家");
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractWithBed(PlayerInteractEvent e){
		if(e.getAction()==Action.RIGHT_CLICK_BLOCK
       	&& e.getClickedBlock().getType() == Material.BED_BLOCK){
			if(!haveHomePremit(e.getPlayer().getName(),e.getClickedBlock().getLocation())){
				e.getPlayer().sendMessage("你不能在这里睡觉！");
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPearlUse(PlayerTeleportEvent e){
		if (!e.getCause().equals(TeleportCause.ENDER_PEARL))return;
		String p=e.getPlayer().getName();
		ClaimedResidence res1=Residence.getResidenceManager().getByLoc(e.getFrom());
		ClaimedResidence res2=Residence.getResidenceManager().getByLoc(e.getTo());
		if((res1!=null&&!res1.getPermissions().playerHas(p, "pearl", true))
				||(res2!=null&&!res2.getPermissions().playerHas(p, "pearl", true))){
			e.getPlayer().sendMessage("不能用末影珍珠");
			e.setCancelled(true);
		}	
	}
	
	private boolean homeAvailable(Player p,String h){
		
		try {
			String t[]=h.split(":", 2);
			if (t[0].length()==h.length()){
				User u=((Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials")).getUser(p);
				Location l=null,bed=u.getBedSpawnLocation();
				if (h.equalsIgnoreCase("bed")||(bed!=null&&h.isEmpty())||(bed!=null&&u.getHomes().size()==0)){
					l=bed;
				}else if(u.getHomes().size()==0)
					return true;
				else if(u.getHomes().size()==1)
					l=u.getHome();
				else
					l=u.getHome(h);
				return haveHomePremit(p.getName(),l);
			}
			if (t.length==0)return true;
			else{
				h=(t[1].equals(""))?"home":t[1];
				User u=((Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials")).getUser(t[0]);
				if (u==null)return true;
				Location l=u.getHome(h);
				if (h.equalsIgnoreCase("bed"))l=u.getBedSpawnLocation();
				if (u.getHomes().size()==0)l=u.getBedSpawnLocation();
				if (l==null)return true;
				return haveHomePremit(p.getName(),l);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean haveHomePremit(String p,Location l){
		ClaimedResidence res=Residence.getResidenceManager().getByLoc(l);
		if (res==null)return(true);
		return res.getPermissions().playerHas(p, "home", true);
	}
}
