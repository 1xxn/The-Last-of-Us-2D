package Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.corex.thelastofus2d.Scenes.Hud;
import com.corex.thelastofus2d.TheLastOfUs2D;

public class Brick extends InteractiveTileObject {
    public Brick(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
        setCategoryFilter(TheLastOfUs2D.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "Collision");
        setCategoryFilter(TheLastOfUs2D.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
    }
}
