import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/**
 * 
 * 
 * 
 * @author panos
 */
public class WizardGame extends BasicGame {
	private TiledMap grassMap;
	private Animation sprite, up, down, left, right;
	private float x = 34f, y = 34f;
	/**
	 * The collision map indicating which tiles block movement - generated based
	 * on tile properties
	 */
	private boolean[][] blocked;
	private static final int SIZE = 34;

	public WizardGame()
	{
		super("Wizard game");
	}

	public static void main(String[] arguments) {
		try {
			AppGameContainer app = new AppGameContainer(new WizardGame());
			app.setDisplayMode(800, 600, false);
			app.start();
		}
		catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		Image[] movementUp = { new Image("data/wmg1_bk1.png"),
				new Image("data/wmg1_bk2.png") };
		Image[] movementDown = { new Image("data/wmg1_fr1.png"),
				new Image("data/wmg1_fr2.png") };
		Image[] movementLeft = { new Image("data/wmg1_lf1.png"),
				new Image("data/wmg1_lf2.png") };
		Image[] movementRight = { new Image("data/wmg1_rt1.png"),
				new Image("data/wmg1_rt2.png") };
		int[] duration = { 300, 300 };
		grassMap = new TiledMap("data/tankmap.tmx");
		/*
		 * 
		 * false variable means do not auto update the animation.
		 * 
		 * By setting it to false animation will update only when
		 * 
		 * the user presses a key.
		 */
		up = new Animation(movementUp, duration, false);
		down = new Animation(movementDown, duration, false);
		left = new Animation(movementLeft, duration, false);
		right = new Animation(movementRight, duration, false);
		// Original orientation of the sprite. It will look right.
		sprite = right;
		// build a collision map based on tile properties in the TileD map
		blocked = new boolean[grassMap.getWidth()][grassMap.getHeight()];
		for (int xAxis = 0; xAxis < grassMap.getWidth(); xAxis++) {
			for (int yAxis = 0; yAxis < grassMap.getHeight(); yAxis++) {
				int tileID = grassMap.getTileId(xAxis, yAxis, 0);
				String value = grassMap.getTileProperty(tileID, "blocked",
						"false");
				if ("true".equals(value)) {
					blocked[xAxis][yAxis] = true;
				}
			}
		}
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		Input input = container.getInput();
		if (input.isKeyDown(Input.KEY_UP)) {
			sprite = up;
			if (!isBlocked(x, y - delta * 0.1f)) {
				sprite.update(delta);
				// The lower the delta the slowest the sprite will animate.
				y -= delta * 0.1f;
			}
		} else if (input.isKeyDown(Input.KEY_DOWN)) {
			sprite = down;
			if (!isBlocked(x, y + SIZE + delta * 0.1f)) {
				sprite.update(delta);
				y += delta * 0.1f;
			}
		} else if (input.isKeyDown(Input.KEY_LEFT)) {
			sprite = left;
			if (!isBlocked(x - delta * 0.1f, y)) {
				sprite.update(delta);
				x -= delta * 0.1f;
			}
		} else if (input.isKeyDown(Input.KEY_RIGHT)) {
			sprite = right;
			if (!isBlocked(x + SIZE + delta * 0.1f, y)) {
				sprite.update(delta);
				x += delta * 0.1f;
			}
		}
	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		grassMap.render(0, 0);
		sprite.draw((int) x, (int) y);
	}

	private boolean isBlocked(float x, float y) {
		int xBlock = (int) x / SIZE;
		int yBlock = (int) y / SIZE;
		//return blocked[xBlock][yBlock];
		if(xBlock>=grassMap.getWidth()||yBlock>=grassMap.getHeight()){
			return true;
		}
		return false;
	}
}
