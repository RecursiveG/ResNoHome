package org.devinprogress.nohome;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.bekvon.bukkit.residence.protection.FlagPermissions;

public class Main extends JavaPlugin {

	public void onEnable() {
		initResFlag();
		getServer().getPluginManager().registerEvents(new MainListener(), this);
	}
	private void initResFlag(){
		PluginManager pm = getServer().getPluginManager();
		Plugin p = pm.getPlugin("Residence");
		if(p!=null)
		{
		     if(!p.isEnabled())
		     {
		          System.out.println("<your plugin name> - Manually Enabling Residence!");
		          pm.enablePlugin(p);
		     }
		}
		else
		{
		     System.out.println("<your plugin name> - Residence NOT Installed, DISABLED!");
		     this.setEnabled(false);
		}
		FlagPermissions.addFlag("home");
		FlagPermissions.addFlag("pearl");
		
	}
	
}
