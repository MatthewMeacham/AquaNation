package com.aquanation.water.colorer;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.aquanation.water.AquaNation;
import com.aquanation.water.State;
import com.aquanation.water.windows.MainFrame;

/**
 * 
 * @author Matthew Meacham
 * 
 *         Performs the fill bucket operation on a certain state
 *
 */
public class Colorer {

	private List<ColorRange> colorRanges = new ArrayList<ColorRange>();

	private final int TARGET_COLOR = 0;

	public Colorer() {
		colorRanges.add(new ColorRange(0.0d, 1.0d, Color.RED.getRGB()));
		colorRanges.add(new ColorRange(1.0d, 2.0d, Color.ORANGE.getRGB()));
		colorRanges.add(new ColorRange(2.0d, 3.0d, Color.YELLOW.getRGB()));
		colorRanges.add(new ColorRange(3.0d, 4.0d, Color.GREEN.getRGB()));
		colorRanges.add(new ColorRange(4.0d, 5.0d, Color.BLUE.getRGB()));
		colorRanges.add(new ColorRange(5.0d, 6.0d, Color.MAGENTA.getRGB()));
	}

	public void fillImage(final Image image) {
		BufferedImage newImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g1 = newImage.createGraphics();
		g1.drawImage(image, 0, 0, null);
		g1.dispose();

		for (State state : AquaNation.getStates()) {
			int replacementColor = 0;

			Double waterValue = state.getWaterValue(MainFrame.getSelectedDataType());
			if (waterValue == null) continue;

			for (Point pt : state.getFillBucketPoints()) {
				if (pt == null) continue;

				for (ColorRange colorRange : colorRanges) {
					if (waterValue >= colorRange.getFrom() && waterValue < colorRange.getTo()) {
						replacementColor = colorRange.getColor();
						break;
					}
				}

				if (TARGET_COLOR == replacementColor) break;

				ArrayList<Point> points = new ArrayList<Point>();
				points.add(pt);
				for (int i = 0; i < points.size(); i++) {
					Point point = points.remove(0);
					i--;

					for (int j = -1; j <= 1; j++) {
						for (int k = -1; k <= 1; k++) {
							// ensure the point will be within the bounds of the image
							if (point.x + j >= newImage.getWidth() || point.y + k >= newImage.getHeight() || point.x + j < 0 || point.y + k < 0) continue;
							if (j == point.x && k == point.y) continue;

							if (newImage.getRGB(point.x + j, point.y + k) == TARGET_COLOR) {
								newImage.setRGB(point.x + j, point.y + k, replacementColor);
								points.add(new Point(point.x + j, point.y + k));
							}
						}
					}
				}
			}
		}

		BufferedImage resizedImage = new BufferedImage(MainFrame.getFrameWidth() / 3 * 2 - 100, 400, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = resizedImage.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(newImage, 0, 0, MainFrame.getFrameWidth() / 3 * 2 - 100, 400, null);
		g.dispose();

		MainFrame.setFullMapImage(resizedImage);
	}

	public Image fillImage(final Image image, final String dataType, final int index) {
		BufferedImage newImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g1 = newImage.createGraphics();
		g1.drawImage(image, 0, 0, null);
		g1.dispose();

		for (State state : AquaNation.getStates()) {
			int targetColor = 0;
			int replacementColor = 0;

			if (index >= state.getWaterValues(dataType).size()) continue;
			Double waterValue = state.getWaterValues(dataType).get(index);

			for (Point pt : state.getFillBucketPoints()) {

				if (waterValue == null) {
					replacementColor = 0;
				} else {
					for (ColorRange colorRange : colorRanges) {
						if (waterValue >= colorRange.getFrom() && waterValue < colorRange.getTo()) {
							replacementColor = colorRange.getColor();
							break;
						}
					}
				}

				if (pt == null) continue;
				if (targetColor == replacementColor) continue;

				ArrayList<Point> points = new ArrayList<Point>();
				points.add(pt);
				for (int i = 0; i < points.size(); i++) {
					Point point = points.remove(0);
					i--;

					for (int j = -1; j <= 1; j++) {
						for (int k = -1; k <= 1; k++) {
							// ensure the point will be within the bounds of the image
							if (point.x + j >= newImage.getWidth() || point.y + k >= newImage.getHeight() || point.x + j < 0 || point.y + k < 0) continue;

							if (newImage.getRGB(point.x + j, point.y + k) == targetColor) {
								newImage.setRGB(point.x + j, point.y + k, replacementColor);
								points.add(new Point(point.x + j, point.y + k));
							}
						}
					}
				}
			}
		}

		BufferedImage resizedImage = new BufferedImage(MainFrame.getFrameWidth() / 3 * 2 - 100, 400, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = resizedImage.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(newImage, 0, 0, MainFrame.getFrameWidth() / 3 * 2 - 100, 400, null);
		g.dispose();

		return resizedImage;
	}

	public void removeAllColorRanges() {
		colorRanges.clear();
	}

	public void removeColorRange(final double from, final double to) {
		for (int i = 0; i < colorRanges.size(); i++) {
			if (colorRanges.get(i).getFrom() == from && colorRanges.get(i).getTo() == to) {
				colorRanges.remove(i);
				break;
			}
		}
	}

	public void addColorRange(final double from, final double to, final int color) {
		for (ColorRange colorRange : colorRanges) {
			// Don't allow a range that is within one of the other ranges or the same as another
			if (colorRange.getFrom() <= from && colorRange.getTo() >= to) return;
		}
		colorRanges.add(new ColorRange(from, to, color));
	}

	public void addColorRange(final double from, final double to, final int red, final int green, final int blue, final int alpha) {
		for (ColorRange colorRange : colorRanges) {
			// Don't allow a range that is within one of the other ranges or the same as another
			if (colorRange.getFrom() <= from && colorRange.getTo() >= to) return;
		}
		addColorRange(from, to, createColor(red, green, blue, alpha));
	}

	public List<ColorRange> getColorRanges() {
		return colorRanges;
	}

	public void setColorRanges(List<ColorRange> colorRanges) {
		this.colorRanges = colorRanges;
		fillImage(MainFrame.getDefaultFullMapImage());
	}

	public int createColor(final int red, final int green, final int blue, final int alpha) {
		int color = 0;
		color = (color & ~(0xFF << 24)) | (alpha << 24);
		color = (color & ~(0xFF << 16)) | (red << 16);
		color = (color & ~(0xFF << 8)) | (green << 8);
		color = (color & ~0xFF) | blue;
		return color;
	}

}
