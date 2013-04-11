package game.cliente.core;

import game.cliente.components.ColorTag;
import game.cliente.components.Player;
import game.cliente.components.PlayerNetwork;
import game.cliente.components.SpatialForm;
import game.cliente.components.Transform;
import game.cliente.components.Velocity;

import org.newdawn.slick.Color;

import com.artemis.Entity;
import com.artemis.World;

/**
 * 
 * @author Michel Montenegro
 * 
 */
public class EntityFactory {
	public static Entity createPlayer(World world, Transform transform,
			int velocity, Color c, Player player) {
		Entity entityPlayer = world.createEntity();
		entityPlayer.setGroup("Group: Hero");
		entityPlayer.addComponent(new SpatialForm("Spatial: Hero"));
		entityPlayer.addComponent(new Velocity(velocity));
		entityPlayer.addComponent(transform);
		entityPlayer.addComponent(new ColorTag(c));
		entityPlayer.addComponent(player); // Só o heroi tem este component, usado
										// no "MovementPlayerHeroSystem" em seu
										// construtor
		entityPlayer.refresh();
		return entityPlayer;
	}
	
	public static Entity createPlayerNetwork(World world, Transform transform, 
			int velocity, Color c, PlayerNetwork playerNetwork) {
		Entity entityPlayerNetwork = world.createEntity();
		entityPlayerNetwork.setGroup("Group: PlayerNetWork");
		entityPlayerNetwork.addComponent(new SpatialForm("Spatial: PlayerNetWork"));
		entityPlayerNetwork.addComponent(new Velocity(velocity));
		entityPlayerNetwork.addComponent(transform);
		entityPlayerNetwork.addComponent(new ColorTag(c));
		entityPlayerNetwork.addComponent(playerNetwork); //Todos os outros Players tem este componente (indica os players da rede
		
		entityPlayerNetwork.refresh();
		return entityPlayerNetwork;
	}
}
