package Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.corex.thelastofus2d.Screens.PlayScreen;
import com.corex.thelastofus2d.TheLastOfUs2D;

public class Joel extends Sprite {
    public enum State { FALLING, JUMPING, STANDING, RUNNING };
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion joelStand;
    private Animation<TextureRegion> joelRun;
    private Animation<TextureRegion> joelJump;
    private float stateTimer;
    private boolean runningRight;

    public Joel(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("joel_sprite"));
        this.world = world;

        // initialization of state
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        // create running animation
        joelRun = createAnimation(screen.getAtlas().findRegion("joel_sprite"), 0, 4, 0.1f);

        // create jumping animation
        joelJump = createAnimation(screen.getAtlas().findRegion("joel_sprite"), 1, 2, 0.1f);

        // create standing texture
        joelStand = new TextureRegion(getTexture(), 2, 4, 16, 16);

        defineJoel();
        setBounds(0, 0, 32 / TheLastOfUs2D.PPM, 32 / TheLastOfUs2D.PPM);
        setRegion(joelStand);
    }

    // creating animations
    private Animation<TextureRegion> createAnimation(TextureRegion region, int startIndex, int endIndex, float frameDuration) {
        // array for frames
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = startIndex; i < endIndex; i++) {
            frames.add(new TextureRegion(region, i * 16, 0, 16, 16));
        }
        // return and create animation
        return new Animation<TextureRegion>(frameDuration, frames);
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        region = switch(currentState) {
            case JUMPING -> joelJump.getKeyFrame(stateTimer);
            case RUNNING -> joelRun.getKeyFrame(stateTimer, true);
            case FALLING, STANDING -> joelStand;
        };

        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        }
        else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) return State.JUMPING;
        if (b2body.getLinearVelocity().y < 0) return State.FALLING;
        else if (b2body.getLinearVelocity().x != 0) return State.RUNNING;
        else return State.STANDING;
    }

    public void defineJoel() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / TheLastOfUs2D.PPM, 32 / TheLastOfUs2D.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / TheLastOfUs2D.PPM);

        // setting the "feets" correctly for the ground
        FixtureDef fixtureDef2 = new FixtureDef();
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-2 / TheLastOfUs2D.PPM, -4 / TheLastOfUs2D.PPM), new Vector2(2 / TheLastOfUs2D.PPM, -4 / TheLastOfUs2D.PPM));
        fixtureDef2.shape = feet;
        b2body.createFixture(fixtureDef2);

        fixtureDef.shape = shape;
        b2body.createFixture(fixtureDef);
    }
}
