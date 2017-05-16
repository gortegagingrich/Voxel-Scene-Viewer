import provided.SimplexNoise;

/**
 * Created by Gabriel on 2017/05/16.
 */
public class SimpleScene {
	private static final float EDGE_LENGTH = 16;
	private SimpleChunk[][] chunks;
	private int[][] heightMatrix;

	public SimpleScene() {
		int i,j;
		SimplexNoise sn;

		sn = new SimplexNoise(16,0.2, (int)System.currentTimeMillis());
		heightMatrix = new int[Main.CUBE_COUNT][Main.CUBE_COUNT];
		chunks = new SimpleChunk[Main.CUBE_COUNT][Main.CUBE_COUNT];

		for (i = 0; i < Main.CUBE_COUNT; i++) {
			for (j = 0; j < Main.CUBE_COUNT; j++) {
				heightMatrix[i][j] = 10 + (int)(20*sn.getNoise(i,j));//(int)sn.getNoise(i,j);
				chunks[i][j] = new SimpleChunk(i*EDGE_LENGTH,0,j*EDGE_LENGTH,EDGE_LENGTH,heightMatrix[i][j]);
			}
		}

		cleanseFaces();
	}

	public void draw() {
		int i;

		for (i = 0; i < Main.CUBE_COUNT; i++) {
			for (SimpleChunk chunk: chunks[i]) {
				chunk.draw();
			}
		}
	}

	private void cleanseFaces() {
		SimpleChunk chunk;

		for (int i = 0; i < Main.CUBE_COUNT; i++) {
			for (int j = 0; j < Main.CUBE_COUNT; j++) {
				chunk = chunks[i][j];

				if (i > 0) {
					chunk.deactivateRange(0,heightMatrix[i-1][j],Cube.LEFT);
				}
				if (i < Main.CUBE_COUNT-1) {
					chunk.deactivateRange(0,heightMatrix[i+1][j],Cube.RIGHT);
				}
				if (j > 0) {
					chunk.deactivateRange(0,heightMatrix[i][j-1],Cube.FRONT);
				}
				if (j < Main.CUBE_COUNT-1) {
					chunk.deactivateRange(0,heightMatrix[i][j+1],Cube.BACK);
				}

				chunk.setFaces();
			}
		}
	}
}
