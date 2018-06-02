package com.mygdx.game.isometric;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

class IsometricView implements Screen {

    private Matrix4 matrix;
    private Sprite playerSprite;
    private Sprite[][] tileImg = new Sprite[Config.X_TILES][Config.Y_TILES];
    private PolygonSpriteBatch batch;
    private OrthographicCamera camera;

    //twisting the world
    private void matrixAndCameraSetup() {
        camera = new OrthographicCamera(Config.WIDTH, Config.HEIGHT);
        //shifting the camera's X to view the world at center.
        camera.position.set(Config.SCALED_TILE_SIZE * 10, 0, 0);

        matrix = new Matrix4();
        matrix.idt();
        matrix.scale((float) (Math.sqrt(2) / 2), (float) (Math.sqrt(2) / 4), 1.0f);
        matrix.rotate(0.0f, 0.0f, 1.0f, -45);

        Matrix4 invMatrix = new Matrix4(matrix.cpy());
        invMatrix.inv();
    }


    @Override
    public void show() {
        matrixAndCameraSetup();
        textureAndSpriteSetup();
        batch = new PolygonSpriteBatch();
    }

    private void textureAndSpriteSetup() {
        Texture floorTile = new Texture(Gdx.files.internal("grassTile.png"));
        Texture originTile = new Texture(Gdx.files.internal("floorTile.png"));
        Texture playerTexture = new Texture(Gdx.files.internal("monk.png"));
        playerSprite = new Sprite(playerTexture);
        playerSprite.setSize(50, 70);
        playerSprite.setFlip(true, false);

        for (int y = 0; y < Config.Y_TILES; y++) {
            for (int x = 0; x < Config.X_TILES; x++) {
                if (x == 0 && y == 0) {
                    tileImg[0][0] = new Sprite(originTile);
                } else {
                    tileImg[x][y] = new Sprite(floorTile);
                }
                tileImg[x][y].setPosition(x * Config.SCALED_TILE_SIZE * Config.SCALE, y * Config.SCALED_TILE_SIZE * Config.SCALE);
                tileImg[x][y].setSize(Config.SCALED_TILE_SIZE * Config.SCALE, Config.SCALED_TILE_SIZE * Config.SCALE);
            }
        }
        //setting player at (4th,5th) tile.
        playerSprite.setPosition(tileNumber(4, 5).x, tileNumber(4, 5).y);
    }

    private Vector3 tileNumber(int tileX, int tileY) {
        Vector3 position = new Vector3();
        position.x = tileImg[tileX][tileY].getX();
        position.y = tileImg[tileX][tileY].getY();
        position.mul(matrix);
        position.y -= 10;//adjustments due to image size issue
        position.x += 8;
        return position;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.1f, 0.12f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.setTransformMatrix(matrix);
        batch.begin();
        for (int z = 0; z < 10; z++) {
            for (int x = 0; x < 10; x++) {
                tileImg[x][z].draw(batch);
            }
        }
        batch.setTransformMatrix(new Matrix4(matrix).idt());
        playerSprite.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
