package de.uni_leipzig.simba.saim.cytoprocess.widgetset.client.ui.graph;

import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.shape.Text;
import de.uni_leipzig.simba.saim.cytoprocess.widgetset.client.ui.VCytoprocess;
import de.uni_leipzig.simba.saim.cytoprocess.widgetset.client.ui.graph.VNode;
/**
 * @author rspeck
 */
public class VEdge {

	private final VNode node1, node2;
	public Line line;

	private Line arrow1 = null;
	private Line arrow2 = null;

	public Text label;
	public int id;

	// TODO add arrowSize to visualstyle
	final int arrowSize = 5;

	public VEdge(int id, VNode source, VNode target, Line line, Line arrow2,
			Line arrow1, Text label) {

		this.id = id;
		this.node1 = source;
		this.node2 = target;
		this.line = line;
		this.arrow1 = arrow1;
		this.arrow2 = arrow2;
		this.label = label;

		updatePosition();
	}

	public void delete(VCytoprocess vcypro) {
		vcypro.remove(line);
		vcypro.remove(label);
		if (arrow1 != null)
			vcypro.remove(arrow1);

		if (arrow2 != null)
			vcypro.remove(arrow2);
	}

	public void updatePosition() {
		line.setX1(node1.getX());
		line.setY1(node1.getY());
		line.setX2(node2.getX());
		line.setY2(node2.getY());

		label.setX(Math.round((node1.getX() + node2.getX()) / 2f));
		label.setY(Math.round((node1.getY() + node2.getY()) / 2f));

		if (arrow1 != null && arrow2 != null)
			setArrowPosition();
	}

	public static VEdge createVEdge(VVisualStyle vs, VCytoprocess vcytoprocess,	VNode source, VNode target, int id, String label, String shape,	boolean arrow) {

		if (arrow){
			vcytoprocess.noFill();
			vcytoprocess.strokeWeight(vs.EDGE_LINE_WIDTH);
			vcytoprocess.setStroke(vs.EDGE_COLOR);
			Line line       = vcytoprocess.line(0, 0, 0, 0);
			Line lineArrowA = vcytoprocess.line(0, 0, 0, 0);
			Line lineArrowB = vcytoprocess.line(0, 0, 0, 0);
			Text text       = vcytoprocess.text(label, 0, 0);
			text.setStrokeColor(vs.EDGE_LABEL_COLOR);
			text.setFontSize(Integer.valueOf(vs.EDGE_FONT_SIZE));
			text.setFontFamily(vs.EDGE_FONT_NAME);

			vcytoprocess.fill();

			VEdge vedge = new VEdge(id, source, target,
					line,
					lineArrowA,
					lineArrowB,
					text);

			return vedge;


		}else{
			vcytoprocess.noStroke();
			vcytoprocess.noFill();

			Line line       = vcytoprocess.line(0, 0, 0, 0);
			Text text       = vcytoprocess.text(label, 0, 0);

			vcytoprocess.fill();
			vcytoprocess.stroke();
			VEdge vedge = new VEdge(id, source, target,
					line,
					null,
					null,
					text);

			return vedge;
		}

	}

	// private
	private void setArrowPosition() {
		// middle
		final float toX = ((node1.getX() + node2.getX()) / 2);
		final float toY = ((node1.getY() + node2.getY()) / 2);
		// diff
		final int dx = (int) (toX - node1.getX());
		final int dy = (int) (toY - node1.getY());

		double hdx = (arrowSize * dx) / Math.sqrt(dx * dx + dy * dy);
		double hdy = (arrowSize * dy) / Math.sqrt(dx * dx + dy * dy);

		arrow1.setX1((int) (toX));
		arrow1.setY1((int) (toY));
		arrow1.setX2((int) (toX + hdx + hdy));
		arrow1.setY2((int) (toY + hdy - hdx));
		arrow2.setX1((int) (toX));
		arrow2.setY1((int) (toY));
		arrow2.setX2((int) (toX + hdx - hdy));
		arrow2.setY2((int) (toY + hdy + hdx));
	}
}
