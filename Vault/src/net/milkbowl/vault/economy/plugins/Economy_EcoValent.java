package net.milkbowl.vault.economy.plugins;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import de.antivalent.ecovalent.EcoValent;
import de.antivalent.ecovalent.account.Account;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class Economy_EcoValent extends AbstractEconomy {

	private static final Logger log = Logger.getLogger("Minecraft");
	private final String name = "EcoValent";
	private Plugin plugin;
	private EcoValent economy = null;

	public Economy_EcoValent(Plugin plugin) {
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(new EconomyServerListener(this), plugin);
		
		// Load Plugin in case it was loaded before
		if (economy == null) {
			Plugin ec = plugin.getServer().getPluginManager().getPlugin("EcoValent");
			if (ec != null && ec.isEnabled() && ec.getClass().getName().equals("de.antivalent.ecovalent.EcoValent")) {
				economy = (EcoValent) ec;
				economy.getLogger().info(String.format("[%s][Economy] %s hooked.", plugin.getDescription().getName(), name));
			}
		}
	}
	

	public class EconomyServerListener implements Listener {
		Economy_EcoValent economy = null;

		public EconomyServerListener(Economy_EcoValent economy) {
			this.economy = economy;
		}

		@EventHandler(priority = EventPriority.MONITOR)
		public void onPluginEnable(PluginEnableEvent event) {
			if (economy.economy == null) {
				Plugin ec = event.getPlugin();

				if (ec.getDescription().getName().equals("EcoValent") && ec.getClass().getName().equals("de.antivalent.ecovalent.EcoValent")) {
					economy.economy = (EcoValent) ec;
					log.info(String.format("[%s][Economy] %s hooked.", plugin.getDescription().getName(), economy.name));
				}
			}
		}

		@EventHandler(priority = EventPriority.MONITOR)
		public void onPluginDisable(PluginDisableEvent event) {
			if (economy.economy != null) {
				if (event.getPlugin().getDescription().getName().equals("Craftconomy3")) {
					economy.economy = null;
					log.info(String.format("[%s][Economy] %s unhooked.", plugin.getDescription().getName(), economy.name));
				}
			}
		}
	}
	
	
	@Override
	public EconomyResponse bankBalance(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse bankDeposit(String name, double amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse bankHas(String name, double amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse bankWithdraw(String name, double amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse createBank(String name, String world) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createPlayerAccount(String name) {
		plugin.getLogger().info("createPlayer: " + name.toLowerCase());
		if(hasAccount(name.toLowerCase())){
			return false;
		}
		EcoValent.getInstance().getAccountManager().getAccount(name.toLowerCase());
		return true;
	}

	@Override
	public boolean createPlayerAccount(String name, String world) {
		return createPlayerAccount(name.toLowerCase());
	}

	@Override
	public String currencyNamePlural() {
		return "Gulden";
	}

	@Override
	public String currencyNameSingular() {
		return "Gulden";
	}

	@Override
	public EconomyResponse deleteBank(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse depositPlayer(String name, double amount) {
		plugin.getLogger().info("depositPlayer: " + name.toLowerCase() + " || " + amount);
		if(amount < 0.0){
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Das Einzahlen von negativen Beträgen ist nicht möglich.");
		}
		double balance;
		Account account = EcoValent.getInstance().getAccountManager().getAccount(name.toLowerCase());
		balance = account.deposite(amount);
		return new EconomyResponse(amount, balance, ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse depositPlayer(String name, String world, double amount) {
		return depositPlayer(name.toLowerCase(), amount);
	}

	@Override
	public String format(double amount) {
		return String.format("%,.2f", amount);
	}

	@Override
	public int fractionalDigits() {
		return 2;
	}

	@Override
	public double getBalance(String name) {
		plugin.getLogger().info("getBalance: " + name.toLowerCase());
		return EcoValent.getInstance().getAccountManager().getAccount(name.toLowerCase()).getBalance();
	}

	@Override
	public double getBalance(String name, String world) {
		return getBalance(name.toLowerCase());
	}

	@Override
	public List<String> getBanks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean has(String name, double amount) {
		plugin.getLogger().info("has: " + name.toLowerCase() + " || " + amount);
		return EcoValent.getInstance().getAccountManager().getAccount(name.toLowerCase()).hasEnough(amount);
	}

	@Override
	public boolean has(String name, String world, double amount) {
		return has(name.toLowerCase(), amount);
	}

	@Override
	public boolean hasAccount(String name) {
		plugin.getLogger().info("hasAccount: " + name.toLowerCase());
		return EcoValent.getInstance().getAccountManager().exists(name.toLowerCase());
	}

	@Override
	public boolean hasAccount(String name, String world) {
		return hasAccount(name.toLowerCase());
	}

	@Override
	public boolean hasBankSupport() {
		return false;
	}

	@Override
	public EconomyResponse isBankMember(String name, String playerName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse isBankOwner(String name, String playerName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEnabled() {
		if(economy != null){
			return economy.isEnabled();
		}
		return false;
	}
	
	@Override
	public EconomyResponse withdrawPlayer(String name, double amount) {
		plugin.getLogger().info("withdrawPlayer: " + name.toLowerCase() + " || " + amount);
		if(amount < 0.0){
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Das Abheben von negativen Beträgen ist nicht möglich.");
		}
		double balance;
		Account account = EcoValent.getInstance().getAccountManager().getAccount(name.toLowerCase());
		if(account.hasEnough(amount)){
			balance = account.withdraw(amount);
			return new EconomyResponse(amount, balance, ResponseType.SUCCESS, "");
		}
		return new EconomyResponse(0, getBalance(name.toLowerCase()), ResponseType.FAILURE, "Die Liquidität des Spielers ist nicht ausreichend.");
	}

	@Override
	public EconomyResponse withdrawPlayer(String name, String world, double amount) {
		return withdrawPlayer(name.toLowerCase(), amount);
	}

}
