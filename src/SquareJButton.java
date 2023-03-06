import javax.swing.*;
import java.awt.*;

/**
 * This method is not of my own creation but by the user camickr on Stack Overflow
 */
public class SquareJButton extends JButton {
    private static Dimension maxDimension = new Dimension(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        // take the larger value
        int max = Math.max(d.width, d.height);
        // compare it against our static dimension
        if (max > maxDimension.width)
            maxDimension = new Dimension(max, max);
        // return copy so no one can change the private one
        return new Dimension(maxDimension);
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension d = super.getPreferredSize();
        int max = Math.max(d.width, d.height);
        if (max > maxDimension.width)
            maxDimension = new Dimension(max, max);
        return new Dimension(maxDimension);
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension d = super.getPreferredSize();
        int max = Math.max(d.width, d.height);
        if (max > maxDimension.width)
            maxDimension = new Dimension(max, max);
        return new Dimension(maxDimension);
    }
}
