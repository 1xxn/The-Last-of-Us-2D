package Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.corex.thelastofus2d.Screens.PlayScreen;
import com.corex.thelastofus2d.TheLastOfUs2D;

public class Joel extends Sprite {
    public World world;
    public Body b2body;
    private TextureRegion joelStand;

    public Joel(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("joel_standing_right"));
        this.world = world;
        defineJoel();
        joelStand = new TextureRegion(getTexture(), 0, 0, 16, 16);
        setBounds(0, 0, 32 / TheLastOfUs2D.PPM, 32 / TheLastOfUs2D.PPM);
        setRegion(joelStand);
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }

    public void defineJoel() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / TheLastOfUs2D.PPM, 32 / TheLastOfUs2D.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / TheLastOfUs2D.PPM);

        fixtureDef.shape = shape;
        b2body.createFixture(fixtureDef);
    }
}
