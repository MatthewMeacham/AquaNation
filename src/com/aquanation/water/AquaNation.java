package com.aquanation.water;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;

import com.aquanation.water.comparators.StateComparator;
import com.aquanation.water.windows.MainFrame;

/**
 * 
 * @author Matthew Meacham
 * @author Nick Wilson
 * @author Lavanya Uppala
 * @author Trevor Holmes
 * @author Neil Band
 * 
 *         The main class for the AquaNation Program
 *
 */
public class AquaNation {

	// The current version of the application, this is used for updating
	public static final String VERSION = "1.0.1";

	@SuppressWarnings("unused")
	private JFrame frame;

	private Thread thread;

	private static ArrayList<State> states = new ArrayList<State>();
	public static boolean showStateNames = true;
	public static boolean showWaterValues = true;
	

	private static Font font = new Font("Dialog", Font.PLAIN, 12);
	private static Color fontColor = Color.BLACK;

	// Adjusting where the points will center so it looks nice
	private static final int CLIP_X = -30;
	private static final int CLIP_Y = -96;

	public AquaNation() {
		// First thing to do is check for updates
		new Updater();

		frame = new MainFrame();
		initStates();

		thread = new Thread(new Runnable() {
			public void run() {
				render();
			}
		});
		thread.start();
	}

	private void initStates() {
		states.add(new State("WA", "Washington", new Point(133, 151), new Point(132, 133)));
		states.add(new State("OR", "Oregon", new Point(123, 199), new Point(123, 179)));
		states.add(new State("CA", "California", new Point(122, 311), new Point(115, 289)));
		states.add(new State("ID", "Idaho", new Point(166, 208), new Point(165, 191)));
		states.add(new State("NV", "Nevada", new Point(140, 255), new Point(139, 233)));
		states.add(new State("UT", "Utah", new Point(183, 276), new Point(183, 254)));
		states.add(new State("AZ", "Arizona", new Point(175, 343), new Point(175, 316)));
		states.add(new State("MT", "Montana", new Point(215, 171), new Point(214, 149)));
		states.add(new State("WY", "Wyoming", new Point(226, 223), new Point(226, 208)));
		states.add(new State("CO", "Colorado", new Point(235, 280), new Point(235, 265)));
		states.add(new State("NM", "New Mexico", new Point(230, 343), new Point(230, 328)));
		states.add(new State("ND", "North Dakota", new Point(285, 167), new Point(285, 152)));
		states.add(new State("SD", "South Dakota", new Point(285, 211), new Point(285, 196)));
		states.add(new State("NE", "Nebraska", new Point(291, 252), new Point(291, 237)));
		states.add(new State("KS", "Kansas", new Point(298, 292), new Point(298, 277)));
		states.add(new State("OK", "Oklahoma", new Point(314, 330), new Point(314, 315)));
		states.add(new State("TX", "Texas", new Point(295, 385), new Point(295, 370)));
		states.add(new State("MN", "Minnesota", new Point(331, 195), new Point(331, 170)));
		states.add(new State("IA", "Iowa", new Point(340, 245), new Point(340, 230)));
		states.add(new State("MO", "Missouri", new Point(363, 295), new Point(355, 270)));
		states.add(new State("AR", "Arkansas", new Point(365, 348), new Point(366, 323)));
		states.add(new State("LA", "Louisiana", new Point(365, 390), new Point(365, 375)));
		states.add(new State("WI", "Wisconsin", new Point(374, 205), new Point(374, 190)));
		states.add(new State("IL", "Illinois", new Point(385, 265), new Point(384, 250)));
		states.add(new State("MS", "Mississippi", new Point(392, 367), new Point(392, 338)));
		states.add(new State("MI", "Michigan", new Point(420, 217), new Point(419, 206)));
		states.add(new State("IN", "Indiana", new Point(413, 262), new Point(412, 246)));
		states.add(new State("OH", "Ohio", new Point(440, 253), new Point(440, 236)));
		states.add(new State("KY", "Kentucky", new Point(414, 290), new Point(434, 279)));
		states.add(new State("TN", "Tennessee", new Point(404, 315), new Point(429, 309)));
		states.add(new State("AL", "Alabama", new Point(417, 368), new Point(417, 336)));
		states.add(new State("GA", "Georgia", new Point(449, 365), new Point(448, 340)));
		states.add(new State("FL", "Florida", new Point(485, 419), new Point(473, 394)));
		states.add(new State("SC", "South Carolina", new Point(476, 331), new Point(476, 317)));
		states.add(new State("NC", "North Carolina", new Point(470, 301), new Point(493, 296)));
		states.add(new State("VA", "Virginia", new Point(491, 271), new Point(491, 261)));
		states.add(new State("WV", "West Virginia", new Point(459, 268), new Point(468, 251)));
		states.add(new State("MD", "Maryland", new Point(549, 293), new Point(550, 280)));
		states.add(new State("DE", "Delaware", new Point(555, 268), new Point(556, 257)));
		states.add(new State("NJ", "New Jersey", new Point(557, 245), new Point(557, 235)));
		states.add(new State("PA", "Pennsylvania", new Point(478, 229), new Point(478, 217)));
		states.add(new State("NY", "New York", new Point(497, 193), new Point(497, 178)));
		states.add(new State("CT", "Connecticut", new Point(561, 226), new Point(561, 216)));
		states.add(new State("RI", "Rhode Island", new Point(569, 204), new Point(569, 194)));
		states.add(new State("MA", "Massachusetts", new Point(574, 178), new Point(574, 165)));
		states.add(new State("VT", "Vermont", new Point(436, 133), new Point(438, 118)));
		states.add(new State("NH", "New Hampshire", new Point(501, 107), new Point(502, 94)));
		states.add(new State("ME", "Maine", new Point(538, 138), new Point(539, 125)));
		states.add(new State("AK", "Alaska", new Point(62, 354), new Point(62, 332)));
		states.add(new State("HI", "Hawaii", new Point(219, 486), new Point(219, 471)));

		// Outline settings
		states.get(0).setOutline(new Polygon(new int[] { 110, 111, 118, 125, 125, 122, 122, 122, 125, 124, 120, 123, 126, 129, 129, 131, 129, 171, 162, 150, 128, 118, 114, 107, 107, 109, 110, 108, 109, 110, 113, 110, 109, 110 }, new int[] { 115, 113, 119, 121, 123, 124, 126, 128, 128, 131, 135, 135, 134, 129, 126, 123, 117, 130, 164, 162, 160, 158, 150, 150, 148, 148, 147, 143, 139, 135, 132, 131, 128, 114 }, 34)); // WA
		states.get(1).setOutline(new Polygon(new int[] { 108, 111, 114, 116, 117, 133, 141, 147, 151, 156, 162, 163, 162, 160, 157, 157, 154, 151, 126, 93, 94, 94, 99, 108 }, new int[] { 149, 150, 150, 152, 156, 160, 160, 161, 161, 162, 164, 171, 175, 180, 187, 193, 196, 215, 213, 204, 186, 182, 175, 151 }, 24)); // OR
		states.get(2).setOutline(new Polygon(new int[] { 93, 126, 120, 159, 159, 162, 156, 156, 155, 155, 132, 130, 131, 132, 125, 121, 121, 115, 113, 109, 103, 105, 97, 100, 99, 97, 94, 95, 100, 98, 94, 92, 90, 87, 86, 87, 87, 89, 90, 93 }, new int[] { 203, 212, 253, 315, 324, 327, 341, 343, 349, 350, 346, 344, 338, 337, 330, 329, 326, 324, 319, 318, 311, 303, 281, 279, 277, 275, 265, 263, 262, 258, 259, 258, 255, 231, 225, 222, 215, 213, 208, 204 }, 40)); // CA
		states.get(3).setOutline(new Polygon(new int[] { 171, 170, 162, 165, 156, 158, 157, 156, 154, 151, 177, 202, 204, 191, 189, 189, 190, 188, 187, 186, 186, 184, 185, 181, 180, 182, 171 }, new int[] { 131, 134, 165, 171, 189, 192, 194, 195, 197, 216, 223, 224, 196, 194, 192, 190, 189, 188, 182, 181, 176, 175, 164, 151, 143, 133, 130 }, 27)); // ID
		states.get(4).setOutline(new Polygon(new int[] { 127, 121, 159, 159, 160, 163, 166, 168, 169, 176, 127 }, new int[] { 214, 253, 314, 304, 301, 301, 303, 299, 292, 222, 213 }, 11)); // NV
		states.get(5).setOutline(new Polygon(new int[] { 176, 203, 202, 218, 214, 168, 177 }, new int[] { 221, 225, 243, 243, 297, 292, 222 }, 7)); // UT
		states.get(6).setOutline(new Polygon(new int[] { 170, 214, 209, 189, 153, 155, 156, 155, 162, 162, 159, 160, 161, 165, 168, 169 }, new int[] { 292, 297, 375, 375, 352, 349, 343, 341, 328, 327, 324, 303, 301, 303, 300, 292 }, 16)); // AZ
		states.get(7).setOutline(new Polygon(new int[] { 181, 230, 263, 264, 206, 204, 190, 189, 189, 188, 188, 187, 187, 186, 186, 184, 185, 185, 185, 184, 182, 179, 181 }, new int[] { 131, 137, 136, 188, 188, 195, 194, 192, 189, 188, 185, 184, 181, 181, 176, 175, 173, 169, 164, 161, 156, 144, 132 }, 23)); // MT
		states.get(8).setOutline(new Polygon(new int[] { 202, 206, 263, 263 }, new int[] { 243, 189, 188, 242 }, 4)); // WY
		states.get(9).setOutline(new Polygon(new int[] { 218, 280, 281, 214, 219 }, new int[] { 244, 244, 301, 302, 244 }, 5)); // CO
		states.get(10).setOutline(new Polygon(new int[] { 211, 211, 215, 215, 270, 270, 236, 235, 219, 217 }, new int[] { 374, 352, 324, 302, 304, 366, 369, 371, 370, 375 }, 10)); // NM
		states.get(11).setOutline(new Polygon(new int[] { 263, 319, 322, 324, 324, 264, 263 }, new int[] { 136, 136, 155, 161, 178, 179, 136 }, 7)); // ND
		states.get(12).setOutline(new Polygon(new int[] { 264, 324, 322, 326, 325, 323, 324, 327, 327, 329, 322, 317, 314, 264, 264 }, new int[] { 179, 179, 183, 187, 211, 212, 215, 218, 223, 225, 221, 221, 219, 218, 180 }, 15)); // SD
		states.get(13).setOutline(new Polygon(new int[] { 265, 314, 317, 323, 328, 332, 339, 281, 281, 264, 264 }, new int[] { 219, 219, 221, 222, 226, 245, 261, 261, 244, 243, 220 }, 11)); // NE
		states.get(14).setOutline(new Polygon(new int[] { 281, 338, 339, 341, 344, 344, 342, 342, 344, 348, 348, 282, 281 }, new int[] { 261, 261, 262, 262, 262, 263, 267, 269, 271, 272, 301, 301, 261 }, 13)); // KS
		states.get(15).setOutline(new Polygon(new int[] { 271, 347, 348, 349, 350, 346, 335, 332, 321, 317, 308, 304, 301, 298, 298, 270 }, new int[] { 302, 302, 305, 308, 343, 339, 345, 343, 342, 340, 338, 336, 338, 336, 309, 309 }, 16)); // OK
		states.get(16).setOutline(new Polygon(new int[] { 271, 298, 298, 300, 305, 308, 310, 313, 316, 319, 323, 333, 335, 345, 349, 355, 357, 364, 361, 361, 354, 353, 351, 351, 350, 349, 350, 342, 332, 331, 327, 325, 328, 330, 330, 325, 324, 322, 308, 302, 295, 288, 280, 272, 271, 267, 264, 254, 250, 245, 235, 271, 270 }, new int[] { 309, 310, 335, 338, 336, 339, 339, 340, 341, 341, 343, 343, 345, 340, 343, 348, 368, 381, 389, 397, 401, 401, 400, 399, 399, 401, 404, 414, 415, 418, 428, 432, 442, 447, 450, 450, 449, 449, 440, 428, 421, 403, 399, 401, 406, 405, 410, 402, 392, 387, 371, 369, 309 }, 53)); // TX
		states.get(17).setOutline(new Polygon(new int[] { 320, 335, 339, 342, 341, 342, 342, 342, 347, 352, 367, 380, 374, 368, 365, 365, 363, 362, 355, 358, 357, 358, 357, 363, 366, 370, 371, 325, 325, 322, 324, 324, 320 }, new int[] { 137, 133, 127, 128, 131, 133, 136, 138, 140, 138, 145, 145, 152, 154, 158, 162, 164, 172, 176, 178, 179, 192, 195, 197, 201, 204, 212, 212, 187, 183, 179, 158, 136 }, 33)); // MN
		states.get(18).setOutline(new Polygon(new int[] { 324, 369, 373, 371, 377, 380, 376, 374, 372, 372, 375, 376, 374, 374, 371, 372, 367, 336, 333, 328, 327, 327, 324, 325 }, new int[] { 212, 212, 216, 220, 224, 230, 234, 237, 237, 241, 242, 245, 249, 250, 253, 259, 254, 255, 247, 224, 222, 217, 215, 213 }, 24)); // IA
		states.get(19).setOutline(new Polygon(new int[] { 336, 367, 372, 372, 375, 378, 382, 384, 385, 388, 387, 386, 390, 394, 395, 396, 396, 392, 392, 389, 390, 349, 348, 348, 344, 342, 342, 344, 339, 335 }, new int[] { 254, 254, 258, 262, 266, 270, 273, 275, 276, 277, 280, 283, 289, 294, 296, 298, 300, 304, 307, 307, 303, 305, 273, 273, 272, 269, 267, 262, 262, 254 }, 30)); // MO
		states.get(20).setOutline(new Polygon(new int[] { 349, 390, 389, 391, 391, 390, 389, 389, 389, 386, 386, 384, 385, 385, 386, 356, 355, 355, 350, 351, 349, 349 }, new int[] { 306, 304, 307, 308, 313, 319, 322, 324, 327, 331, 336, 338, 346, 350, 353, 353, 353, 347, 343, 317, 307, 306 }, 22)); // AR
		states.get(21).setOutline(new Polygon(new int[] { 357, 384, 387, 386, 386, 384, 384, 382, 400, 399, 403, 400, 397, 397, 399, 401, 404, 407, 408, 405, 402, 408, 411, 413, 413, 412, 409, 407, 399, 399, 396, 395, 389, 388, 389, 388, 388, 386, 385, 386, 384, 379, 370, 361, 360, 365, 356, 356 }, new int[] { 354, 354, 363, 364, 367, 368, 371, 376, 378, 384, 388, 389, 389, 394, 393, 395, 394, 394, 397, 397, 402, 406, 406, 408, 411, 411, 409, 409, 403, 412, 406, 406, 411, 410, 407, 403, 398, 398, 401, 404, 404, 401, 397, 397, 391, 381, 368, 354 }, 48)); // LA
		states.get(22).setOutline(new Polygon(new int[] { 363, 370, 370, 398, 398, 395, 402, 402, 401, 401, 403, 377, 371, 372, 370, 370, 365, 363, 356, 358, 358, 355, 362, 362 }, new int[] { 164, 163, 166, 178, 181, 190, 182, 191, 194, 200, 220, 224, 220, 217, 213, 205, 200, 197, 195, 191, 178, 176, 172, 164 }, 24)); // WI
		states.get(23).setOutline(new Polygon(new int[] { 377, 403, 403, 406, 408, 410, 408, 410, 405, 405, 403, 400, 398, 394, 386, 387, 385, 383, 371, 372, 371, 373, 375, 375, 372, 372, 376, 379, 378, 375 }, new int[] { 224, 223, 226, 229, 246, 264, 266, 270, 284, 288, 290, 293, 291, 293, 284, 277, 277, 273, 259, 256, 253, 251, 245, 244, 240, 237, 236, 230, 226, 223 }, 30)); // IL
		states.get(24).setOutline(new Polygon(new int[] { 389, 410, 410, 414, 410, 405, 400, 400, 382, 384, 383, 384, 386, 386, 386, 385, 385, 385, 384, 386, 387, 389, 389 }, new int[] { 323, 322, 365, 384, 385, 390, 384, 378, 376, 373, 369, 366, 363, 359, 356, 353, 349, 347, 339, 337, 331, 326, 323 }, 23)); // MS
		states.get(25).setOutline(new Polygon(new int[] { 376, 380, 386, 388, 393, 389, 390, 394, 396, 397, 400, 402, 404, 411, 416, 417, 418, 418, 421, 422, 426, 421, 419, 414, 410, 407, 403, 401, 399, 398, 397, 392, 385, 380, 376, 413, 415, 415, 411, 408, 410, 410, 413, 415, 416, 420, 418, 417, 422, 425, 433, 433, 434, 434, 431, 430, 431, 434, 437, 438, 440, 442, 446, 446, 441, 441, 440, 412 }, new int[] { 168, 164, 160, 155, 153, 157, 159, 159, 159, 163, 163, 163, 161, 161, 158, 158, 158, 160, 160, 160, 166, 167, 171, 169, 170, 172, 175, 178, 182, 183, 177, 175, 171, 170, 169, 226, 221, 214, 207, 201, 193, 185, 179, 185, 180, 177, 175, 173, 171, 174, 174, 179, 184, 187, 193, 196, 198, 195, 194, 192, 192, 192, 206, 209, 214, 220, 222, 226 }, 68)); // MI
		states.get(26).setOutline(new Polygon(new int[] { 405, 408, 412, 429, 434, 430, 430, 429, 426, 422, 419, 411, 406, 409, 409, 409, 410, 405 }, new int[] { 229, 229, 227, 225, 260, 262, 267, 271, 275, 273, 279, 279, 281, 273, 270, 267, 262, 229 }, 18)); // IN
		states.get(27).setOutline(new Polygon(new int[] { 430, 441, 448, 453, 457, 457, 465, 468, 467, 466, 466, 467, 467, 466, 465, 463, 462, 462, 463, 463, 461, 460, 459, 458, 458, 458, 456, 450, 443, 441, 438, 434, 429 }, new int[] { 225, 223, 223, 223, 219, 218, 210, 230, 232, 235, 239, 241, 242, 243, 245, 245, 246, 249, 253, 254, 254, 253, 253, 253, 256, 258, 259, 260, 264, 264, 262, 261, 225 }, 33)); // OH
		states.get(28).setOutline(new Polygon(new int[] { 396, 397, 395, 395, 396, 398, 401, 401, 405, 405, 408, 413, 418, 421, 427, 429, 430, 430, 431, 435, 438, 441, 447, 450, 453, 452, 462, 462, 458, 456, 455, 410, 409, 395 }, new int[] { 301, 298, 295, 294, 293, 291, 293, 292, 289, 285, 280, 279, 279, 273, 275, 271, 269, 263, 262, 262, 263, 264, 262, 261, 261, 266, 275, 276, 282, 286, 288, 297, 299, 302 }, 34)); // KY
		states.get(29).setOutline(new Polygon(new int[] { 389, 390, 391, 391, 396, 409, 409, 468, 468, 466, 466, 462, 459, 459, 456, 456, 453, 449, 447, 446, 445, 446, 410, 389 }, new int[] { 322, 318, 314, 306, 302, 299, 297, 286, 288, 290, 292, 292, 294, 299, 301, 302, 306, 306, 310, 312, 312, 314, 321, 322 }, 24)); // TN
		states.get(30).setOutline(new Polygon(new int[] { 410, 435, 443, 445, 445, 445, 444, 444, 444, 446, 446, 447, 423, 426, 424, 422, 418, 418, 417, 415, 410, 410 }, new int[] { 321, 317, 349, 351, 353, 355, 355, 358, 361, 364, 368, 373, 377, 384, 385, 385, 380, 381, 385, 385, 364, 321 }, 22)); // AL
		states.get(31).setOutline(new Polygon(new int[] { 435, 456, 456, 463, 467, 477, 477, 485, 485, 482, 484, 483, 483, 481, 476, 447, 446, 443, 444, 445, 443, 434 }, new int[] { 318, 313, 317, 320, 327, 334, 339, 346, 350, 351, 354, 357, 358, 360, 372, 373, 364, 356, 354, 350, 349, 318 }, 22)); // GA
		states.get(32).setOutline(new Polygon(new int[] { 423, 447, 447, 475, 477, 478, 477, 477, 482, 484, 492, 494, 497, 506, 503, 495, 494, 485, 484, 476, 477, 476, 476, 472, 473, 472, 469, 465, 461, 454, 446, 436, 436, 434, 427, 427, 423 }, new int[] { 376, 372, 374, 371, 373, 372, 370, 367, 368, 376, 388, 391, 396, 427, 438, 441, 433, 429, 421, 413, 409, 408, 409, 404, 394, 389, 386, 386, 380, 380, 388, 380, 379, 379, 381, 384, 376 }, 37)); // FL
		states.get(33).setOutline(new Polygon(new int[] { 456, 457, 460, 472, 494, 504, 501, 499, 499, 493, 492, 489, 489, 484, 485, 478, 477, 461, 456, 457 }, new int[] { 317, 313, 309, 306, 307, 312, 326, 326, 329, 334, 337, 338, 340, 340, 347, 339, 335, 320, 319, 314 }, 20)); // SC
		states.get(34).setOutline(new Polygon(new int[] { 445, 445, 448, 447, 453, 456, 458, 459, 462, 465, 466, 468, 468, 520, 521, 519, 517, 516, 513, 514, 523, 529, 523, 516, 521, 521, 521, 525, 524, 518, 513, 512, 510, 505, 493, 481, 473, 461, 445 }, new int[] { 313, 313, 311, 306, 306, 301, 301, 295, 292, 293, 291, 291, 287, 273, 276, 277, 279, 280, 279, 285, 282, 287, 289, 290, 292, 294, 295, 295, 298, 301, 304, 308, 312, 313, 307, 307, 308, 308, 314 }, 39)); // NC
		states.get(35).setOutline(new Polygon(new int[] { 455, 457, 456, 462, 464, 467, 469, 469, 473, 476, 476, 482, 482, 485, 484, 489, 489, 491, 492, 496, 498, 503, 503, 508, 508, 507, 514, 516, 515, 517, 516, 520, 520, 455 }, new int[] { 288, 284, 281, 275, 277, 278, 278, 274, 274, 271, 270, 267, 263, 259, 251, 253, 247, 245, 237, 238, 237, 237, 241, 243, 246, 252, 252, 258, 259, 261, 263, 266, 271, 288 }, 34)); // VA
		states.get(36).setOutline(new Polygon(new int[] { 453, 456, 458, 458, 462, 463, 461, 461, 465, 467, 466, 467, 468, 469, 470, 492, 498, 498, 493, 493, 492, 490, 490, 482, 477, 477, 469, 469, 453, 453 }, new int[] { 261, 259, 258, 253, 251, 250, 247, 245, 245, 240, 238, 231, 231, 235, 239, 233, 235, 238, 237, 241, 245, 246, 252, 266, 272, 275, 275, 277, 267, 259 }, 30)); // WV
		states.get(37).setOutline(new Polygon(new int[] { 492, 512, 509, 510, 514, 513, 507, 508, 503, 502, 499, 491 }, new int[] { 233, 227, 240, 246, 249, 252, 253, 245, 241, 237, 236, 233 }, 12)); // MD
		states.get(38).setOutline(new Polygon(new int[] { 516, 520, 526, 521, 511 }, new int[] { 230, 237, 244, 246, 228 }, 5)); // DE
		states.get(39).setOutline(new Polygon(new int[] { 514, 524, 522, 524, 522, 520, 521, 514, 514, 517, 513, 514, 512, 514 }, new int[] { 206, 208, 214, 215, 234, 234, 230, 229, 224, 219, 214, 211, 210, 206 }, 14)); // NJ
		states.get(40).setOutline(new Polygon(new int[] { 465, 471, 471, 509, 510, 514, 514, 512, 514, 513, 518, 515, 515, 511, 511, 469, 464 }, new int[] { 210, 205, 209, 197, 202, 202, 206, 209, 212, 214, 220, 221, 223, 225, 227, 238, 210 }, 17)); // PA
		states.get(41).setOutline(new Polygon(new int[] { 470, 477, 487, 488, 493, 492, 492, 489, 494, 498, 513, 513, 515, 514, 518, 520, 522, 523, 525, 523, 526, 524, 524, 515, 512, 509, 471, 470, 474, 471, 469 }, new int[] { 191, 187, 187, 186, 181, 175, 174, 172, 167, 157, 152, 160, 161, 164, 172, 171, 188, 198, 202, 204, 210, 210, 208, 205, 203, 197, 208, 205, 198, 194, 191 }, 31)); // NY
		states.get(42).setOutline(new Polygon(new int[] { 523, 534, 536, 524, 525, 522, 522 }, new int[] { 191, 187, 196, 203, 201, 200, 192 }, 7)); // CT
		states.get(43).setOutline(new Polygon(new int[] { 533, 540, 540, 536, 535 }, new int[] { 188, 187, 194, 195, 188 }, 5)); // RI
		states.get(44).setOutline(new Polygon(new int[] { 520, 536, 538, 539, 541, 540, 540, 544, 546, 543, 543, 538, 539, 522, 521 }, new int[] { 180, 174, 172, 172, 175, 176, 178, 178, 184, 188, 191, 192, 186, 190, 179 }, 15)); // MA
		states.get(45).setOutline(new Polygon(new int[] { 513, 524, 524, 526, 522, 527, 520, 520, 517, 514, 513, 511 }, new int[] { 151, 146, 151, 153, 157, 177, 180, 171, 171, 161, 159, 150 }, 12)); // VT
		states.get(46).setOutline(new Polygon(new int[] { 525, 529, 538, 540, 539, 537, 537, 526, 524, 524, 523, 526, 524, 526, 525 }, new int[] { 142, 142, 168, 168, 172, 172, 175, 178, 166, 162, 158, 153, 145, 143, 142 }, 15)); // NH
		states.get(47).setOutline(new Polygon(new int[] { 529, 530, 532, 532, 534, 534, 541, 543, 544, 546, 550, 553, 555, 557, 560, 560, 562, 564, 561, 558, 558, 555, 555, 554, 551, 551, 550, 548, 543, 541, 541, 538, 530 }, new int[] { 141, 139, 139, 134, 133, 126, 104, 104, 106, 106, 105, 107, 111, 123, 123, 128, 128, 133, 134, 135, 138, 138, 140, 141, 141, 140, 148, 149, 155, 161, 170, 168, 142 }, 33)); // ME
		states.get(48).setOutline(new Polygon(new int[] { 36, 45, 55, 65, 68, 118, 126, 133, 139, 140, 117, 101, 90, 85, 82, 76, 75, 69, 65, 57, 47, 45, 42, 40, 36, 35, 36, 36, 35 }, new int[] { 406, 402, 395, 386, 376, 374, 381, 388, 383, 380, 365, 352, 310, 306, 307, 307, 305, 305, 303, 302, 313, 316, 317, 320, 331, 335, 342, 361, 405 }, 29)); // AK
		states.get(49).setOutline(new Polygon(new int[] { 231, 240, 259, 274, 264, 246, 226, 201, 181, 164, 161, 172, 189, 205, 219, 235, 235, 239 }, new int[] { 480, 497, 498, 482, 464, 450, 437, 424, 412, 411, 423, 435, 448, 461, 472, 487, 497, 503 }, 18)); // HI

		// FillBuckPoint settings
		states.get(0).addFillBucketPoint(new Point(118, 40)); // WA
		states.get(1).addFillBucketPoint(new Point(99, 79)); // OR
		states.get(2).addFillBucketPoint(new Point(93, 169)); // CA
		states.get(3).addFillBucketPoint(new Point(152, 91)); // ID
		states.get(4).addFillBucketPoint(new Point(123, 136)); // NV
		states.get(5).addFillBucketPoint(new Point(172, 152)); // UT
		states.get(6).addFillBucketPoint(new Point(164, 214)); // AZ
		states.get(7).addFillBucketPoint(new Point(201, 56)); // MT
		states.get(8).addFillBucketPoint(new Point(213, 107)); // WY
		states.get(9).addFillBucketPoint(new Point(228, 160)); // CO
		states.get(10).addFillBucketPoint(new Point(218, 218)); // NM
		states.get(11).addFillBucketPoint(new Point(273, 54)); // ND
		states.get(12).addFillBucketPoint(new Point(276, 93)); // SD
		states.get(13).addFillBucketPoint(new Point(284, 129)); // NE
		states.get(14).addFillBucketPoint(new Point(297, 167)); // KS
		states.get(15).addFillBucketPoint(new Point(306, 203)); // OK
		states.get(16).addFillBucketPoint(new Point(289, 255)); // TX
		states.get(17).addFillBucketPoint(new Point(323, 72)); // MN
		states.get(18).addFillBucketPoint(new Point(335, 122)); // IA
		states.get(19).addFillBucketPoint(new Point(351, 169)); // MO
		states.get(20).addFillBucketPoint(new Point(354, 211)); // AR
		states.get(21).addFillBucketPoint(new Point(359, 260)); // LA
		states.get(22).addFillBucketPoint(new Point(368, 89)); // WI
		states.get(23).addFillBucketPoint(new Point(376, 142)); // IL
		states.get(24).addFillBucketPoint(new Point(383, 234)); // MS
		states.get(25).addFillBucketPoint(new Point(415, 100)); // MI
		states.get(25).addFillBucketPoint(new Point(380, 65)); // MI
		states.get(26).addFillBucketPoint(new Point(407, 138)); // IN
		states.get(27).addFillBucketPoint(new Point(436, 128)); // OH
		states.get(28).addFillBucketPoint(new Point(426, 163)); // KY
		states.get(29).addFillBucketPoint(new Point(411, 191)); // TN
		states.get(30).addFillBucketPoint(new Point(413, 229)); // AL
		states.get(31).addFillBucketPoint(new Point(449, 226)); // GA
		states.get(32).addFillBucketPoint(new Point(476, 281)); // FL
		states.get(33).addFillBucketPoint(new Point(471, 205)); // SC
		states.get(34).addFillBucketPoint(new Point(481, 178)); // NC
		states.get(35).addFillBucketPoint(new Point(488, 148)); // VA
		states.get(36).addFillBucketPoint(new Point(459, 145)); // WV
		states.get(37).addFillBucketPoint(new Point(492, 123)); // MD
		states.get(38).addFillBucketPoint(new Point(510, 130)); // DE
		states.get(39).addFillBucketPoint(new Point(510, 110)); // NJ
		states.get(40).addFillBucketPoint(new Point(480, 110)); // PA
		states.get(41).addFillBucketPoint(new Point(495, 75)); // NY
		states.get(42).addFillBucketPoint(new Point(521, 86)); // CT
		states.get(43).addFillBucketPoint(new Point(527, 82)); // RI
		states.get(44).addFillBucketPoint(new Point(520, 76)); // MA
		states.get(45).addFillBucketPoint(new Point(509, 57)); // VT
		states.get(46).addFillBucketPoint(new Point(521, 60)); // NH
		states.get(47).addFillBucketPoint(new Point(535, 26)); // ME
		states.get(48).addFillBucketPoint(new Point(44, 222)); // AK
		states.get(49).addFillBucketPoint(new Point(228, 350)); // HI
		states.get(49).addFillBucketPoint(new Point(210, 323)); // HI
		states.get(49).addFillBucketPoint(new Point(171, 306)); // HI
		states.get(49).addFillBucketPoint(new Point(150, 296)); // HI

		// Sort the states by their postal code
		Collections.sort(states, new StateComparator());
	}

	private static void render() {
		while (true) {
			Graphics g = MainFrame.getImagePanel().getGraphics();

			g.setColor(fontColor);
			// have to create a new font in order to set the size to the resized size
			g.setFont(new Font(font.getFamily(), font.getStyle(), (int) (MainFrame.getRatioX() * font.getSize())));
			for (State state : states) {
				float marginX = (float) ((MainFrame.getImagePanel().getWidth() - MainFrame.getNewWidth()) / 2);
				float marginY = (float) ((MainFrame.getImagePanel().getHeight() - MainFrame.getNewHeight()) / 2);

				if (showStateNames) g.drawString(state.getPostalCode(), (int) ((state.getPostalCodePoint().x + CLIP_X) * MainFrame.getRatioX() + marginX), (int) ((state.getPostalCodePoint().y + CLIP_Y) * MainFrame.getRatioY() + marginY));

				if (showWaterValues && state.getWaterValues().size() > 0) {
					g.drawString(String.valueOf(state.getWaterValue(MainFrame.getSelectedDataType())), (int) ((state.getWaterValuePoint().x + CLIP_X) * MainFrame.getRatioX() + marginX), (int) ((state.getWaterValuePoint().y + CLIP_Y) * MainFrame.getRatioY() + marginY));
				} else if (showWaterValues) {
					g.drawString("null", (int) ((state.getWaterValuePoint().x + CLIP_X) * MainFrame.getRatioX() + marginX), (int) ((state.getWaterValuePoint().y + CLIP_Y) * MainFrame.getRatioY() + marginY));
				}
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * Draws a String to the image panel for the specified duration
	 * 
	 * @param string
	 *            The string to be displayed
	 * @param duration
	 *            The time to display the String in milliseconds
	 * @param color
	 *            The color that the string should be
	 */
	public static void drawString(String string, long duration, Color color) {
		Thread drawingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				long startTime = System.currentTimeMillis();
				while (System.currentTimeMillis() < startTime + duration) {
					Graphics g = MainFrame.getImagePanel().getGraphics();
					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
					g.setColor(color);
					g.setFont(new Font("Verdana", Font.PLAIN, 50));
					int stringWidth = g2.getFontMetrics().stringWidth(string);
					// Divide by 2 to center the string in the center of the width, and divide by three to place the string on a
					// reasonable height that is at eye level, don't want it directly center, but slightly higher
					g.drawString(string, MainFrame.getImagePanel().getWidth() / 2 - stringWidth / 2, MainFrame.getImagePanel().getHeight() / 3);
				}
				MainFrame.getImagePanel().repaint();
			}
		});
		drawingThread.start();
	}

	public static ArrayList<State> getStates() {
		return states;
	}

	public static Font getFont() {
		return font;
	}

	public static void setFont(Font font2) {
		font = font2;
		MainFrame.getImagePanel().repaint();
	}

	public static Color getFontColor() {
		return fontColor;
	}

	public static void setFontColor(Color color) {
		fontColor = color;
	}

	public static void setStates(ArrayList<State> states2) {
		states = states2;
	}

	public static void main(String[] args) {
		new AquaNation();
	}

}
