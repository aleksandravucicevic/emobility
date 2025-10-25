package emobility.gui;

import java.awt.*;
import javax.swing.JButton;

/**
 * Utility class for creating custom-styled buttons.
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class CustomButton{
	/**
	 * Creates a custom-styled JButton.
	 * @param text text to display on the button
	 * @param width width of the button
	 * @param height height of the button
	 * @return customized JButton
	 */
	public static JButton createStyledButton(String text, int width, int height){
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width,height));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(148,125,102));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial",Font.BOLD,16));
        return button;
    }
}
