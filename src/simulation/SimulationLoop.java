package simulation;

import java.util.ArrayList;
import java.util.Map;

import cell.Cell;
import cell.CellManager;
import config.XMLReader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import visualization.MakeSlider;
import visualization.VisualizeGrid;

/**
 * This class updates the GUI to simulate a CA model with parameters defined in
 * an XML file chosen by the user.
 * 
 * @author DavidTran
 *
 */
public class SimulationLoop {

	private int guiWidth;
	private int guiHeight;
	private int MAX_FRAMES_PER_SECOND = 20;
	private Stage myStage;
	private Scene myScene;
	private boolean shouldRun;
	private XMLReader xmlReader;
	private VisualizeGrid myGrid;
	private Map<Integer, Color> colorMap;
	private Map<Integer, String> stateNameMap;
	private Map<String, Double> parameterMap;
	private int[][] stateGrid;
	private String simulationType;
	private String edgeType;
	private Timeline animation;
	private KeyFrame frame;
	private MakeSlider myMakeSlider;

	private CellManager manager;

	/**
	 * Constructor, give simulation loop a scene
	 */
	public SimulationLoop() {
		frame = new KeyFrame(Duration.millis(1000 / MAX_FRAMES_PER_SECOND), e -> step());
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
	}

	public void setGUI(Stage s, Scene scene, int width, int height) {
		myStage = s;
		myScene = scene;
		guiWidth = width;
		guiHeight = height;
		shouldRun = false;
	}

	public void setMakeSlider(MakeSlider m) {
		myMakeSlider = m;
	}

	/**
	 * Initialize data and manager for each new simulation.
	 * 
	 * @param xmlReaderInput
	 */
	public void setNewSimulationParameters(XMLReader xmlReaderInput) {
		xmlReader = xmlReaderInput;
		colorMap = xmlReaderInput.createColorMap();
		stateNameMap = xmlReaderInput.createStateNameMap();
		parameterMap = xmlReaderInput.createParameterMap();
		stateGrid = xmlReaderInput.createStateGrid();
		edgeType = xmlReaderInput.setEdgeType();
		simulationType = xmlReaderInput.setSimulationType();
		
		manager = new CellManager();
		
		manager.initialize(stateGrid, edgeType, simulationType, parameterMap);
	}

	public void setVisualizeGrid(VisualizeGrid grid) {
		myGrid = grid;
	}

	/**
	 * Initializes and starts the simulation loop.
	 */
	public void start() {
		frame = new KeyFrame(Duration.millis(1000 / MAX_FRAMES_PER_SECOND), e -> step());
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
	}

	public Timeline getTimeline() {
		return animation;
	}

	/**
	 * Primary loop for running each frame of the simulation.
	 */
	public void step() {

		if (shouldRun && xmlReader != null) {
			// set index widths/height for grid

			 ArrayList<Cell> cellList = manager.cellList();
			 manager.update(); // DOES NOT UPDATE CORRECTLY
			 for (int i = 0; i < myGrid.getSize(); i++) {
				 for (int j = 0; j < myGrid.getSize(); j++) {
					 colorRectangle(i, j, colorMap.get(-1));
				 }
			 }
			 for (Cell cell: cellList) {
				 
				 int row = cell.row();
				 int col = cell.column();
				 int state = cell.state();
				
				 Color color = colorMap.get(state);
				 
				
				 colorRectangle(row, col, color);
			 }
		
		}
		
		myMakeSlider.changeSpeed(MAX_FRAMES_PER_SECOND);
	}


	/**
	 * Removes and adds a new rectangle with a color at a specified index in the
	 * grid.
	 * 
	 * @param row
	 * @param col
	 * @param color
	 */
	// There is a bug with this line
	private void colorRectangle(int row, int col, Color color) {
		Rectangle rect = (Rectangle) myGrid.getRectWithCellPosition(row, col);
		rect.setFill(color);
	}

	// start/resume the simulation
	public void play() {
		shouldRun = true;
	}

	// pause the simulation
	public void pause() {
		shouldRun = false;
	}
	
	public int getFPS() {
		return MAX_FRAMES_PER_SECOND;
	}

}