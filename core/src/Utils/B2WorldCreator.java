package Utils;

import Sprites.Brick;
import Sprites.Coin;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.corex.thelastofus2d.TheLastOfUs2D;

public class B2WorldCreator {
    public B2WorldCreator(World world, TiledMap map) {
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        // create ground bodies/fixtures
        for (RectangleMapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = object.getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / TheLastOfUs2D.PPM, (rectangle.getY() + rectangle.getHeight() / 2) / TheLastOfUs2D.PPM);

            body = world.createBody(bodyDef);

            shape.setAsBox(rectangle.getWidth() / 2 / TheLastOfUs2D.PPM, rectangle.getHeight() / 2 / TheLastOfUs2D.PPM);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }

        // // create tree bodies/fixtures
        // for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
        //     Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
        //
        //     bodyDef.type = BodyDef.BodyType.StaticBody;
        //     bodyDef.position.set(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2);
        //
        //     body = world.createBody(bodyDef);
        //
        //     shape.setAsBox(rectangle.getWidth() / 2, rectangle.getHeight() / 2);
        //     fixtureDef.shape = shape;
        //     body.createFixture(fixtureDef);
        // }

        // create brick bodies/fixtures
        for (RectangleMapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = object.getRectangle();

            new Brick(world, map, rectangle);
        }

        // create coin bodies/fixtures
        for (RectangleMapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = object.getRectangle();

            new Coin(world, map, rectangle);
        }
    }
}
