package ics3.chess;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Model class for the player clock which is used to ensure that each player only gets a certain amount
 * of time to play the game. It is responsible for updating and displaying the time.
 *
 * @author Sukhveer Sahota
 * @version 1.0
 * @since May 20th, 2017
 */
public class Player_Clock extends JComponent {

    // The image of the clock
    private Image clockImg;
    // The last time that the System.currentTimeMillis() was called (used to determine when 1 second has passed)
    private long millisOfLastCheck;
    // Integers storing the remaining number of hours, minutes, and seconds
    private int timeRemainingHours, timeRemainingMinutes, timeRemainingSeconds;
    // Strings storing the hours, minutes, and seconds that are displayed
    private String displayedHours, displayedMinutes, displayedSeconds;

    // Font used during rendering
    private Font font;

    /**
     * Default constructor for the Player_Clock class
     */
    public Player_Clock() {
        this(0, 0, 0, 0, "", 0, "");
    }

    /**
     * Constructor for the Player_Clock class
     *
     * @param xPos      The x value of the top left corner of the clock
     * @param yPos      The y value of the top left corner of the clock
     * @param width     The width of the clock
     * @param height    The height of the clock
     * @param imgLoc    The image location of the clock
     * @param timeLimitSeconds  The time limit from which the clock must count down
     */
    public Player_Clock(int xPos, int yPos, int width, int height, String imgLoc, int timeLimitSeconds, String fontFileLoc) {
        setLocation(xPos, yPos);
        setSize(width, height);
        setDoubleBuffered(true);

        setTimeLimit(timeLimitSeconds);

        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(fontFileLoc));
            font = font.deriveFont(Font.BOLD, 45);
        } catch (FontFormatException|IOException exception) {
            exception.printStackTrace(System.err);
            font = new Font("Times New Roman", Font.BOLD, 55);
        }

        clockImg = new Image(imgLoc);
    }

    /**
     * Setter method for the time limit from which to count down
     *
     * @param timeLimitSeconds  The time limit (in seconds) from which the clock must count down
     */
    public void setTimeLimit(int timeLimitSeconds) {
        timeRemainingHours = timeLimitSeconds / 3600;
        timeLimitSeconds -= 3600 * timeRemainingHours;
        timeRemainingMinutes = timeLimitSeconds / 60;
        timeLimitSeconds-= 60 * timeRemainingMinutes;
        timeRemainingSeconds = timeLimitSeconds;
        determineDisplayedTime();
    }

    /**
     * Setter method for the time limit from which to count down (takes in the hour, minute,
     * and second numbers as parameters)
     *
     * @param hourNumber    The whole number of hours for which the clock is running
     * @param minuteNumber  The whole number of minutes for which the clock is running
     * @param secondNumber  The whole number of seconds for which the clock is running
     */
    public void setTimeLimit(int hourNumber, int minuteNumber, int secondNumber) {
        timeRemainingHours = hourNumber;
        timeRemainingMinutes = minuteNumber;
        timeRemainingSeconds = secondNumber;
        determineDisplayedTime();
    }

    /**
     * Getter method to get the remaining time (in seconds)
     *
     * @return int  The time remaining (in seconds)
     */
    public int getTimeRemainingSeconds() {
        return (3600 * timeRemainingHours) + (60 * timeRemainingMinutes) + timeRemainingSeconds;
    }

    /**
     * Update the time elapsed and the displayed time
     *
     */
    public void updateTimeElapsed() {
        // If one second has passed, then update
        if (System.currentTimeMillis() - millisOfLastCheck >= 1000) {
            // If seconds reaches 0, then decrease minutes and set seconds back to 60
            if (timeRemainingSeconds == 0) {
                // If minutes reaches 0, then decrease hours and set minutes back to 60
                if (timeRemainingMinutes == 0) {
                    timeRemainingHours--;
                    timeRemainingMinutes = 59;
                } else {
                    timeRemainingMinutes--;
                }
                timeRemainingSeconds = 59;
            } else {
                timeRemainingSeconds--;
            }
            // Update the millisOfLastCheck
            millisOfLastCheck = System.currentTimeMillis();
            // Update the displayed time
            determineDisplayedTime();
        }
    }

    /**
     * Determines the time to display on the screen (in the correct HH:MM:SS format)
     */
    private void determineDisplayedTime() {
        if (timeRemainingHours < 10) {
            displayedHours = "0" + Integer.toString(timeRemainingHours);
        } else {
            displayedHours = Integer.toString(timeRemainingHours);
        }
        if (timeRemainingMinutes < 10) {
            displayedMinutes = "0" + Integer.toString(timeRemainingMinutes);
        } else {
            displayedMinutes = Integer.toString(timeRemainingMinutes);
        }
        if (timeRemainingSeconds < 10) {
            displayedSeconds = "0" + Integer.toString(timeRemainingSeconds);
        } else {
            displayedSeconds = Integer.toString(timeRemainingSeconds);
        }
    }

    /**
     * Override the paintComponent() method to draw the clock and the time
     *
     * @param g   The graphics context with which to paint
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Draw the clock
        g2d.drawImage(clockImg.getImage(), 0, 0, getWidth(), getHeight(), null);

        // Display the remaining time
        g2d.setColor(Color.WHITE);
        g2d.setFont(font);
        g2d.drawString(displayedHours + ":" + displayedMinutes + ":" + displayedSeconds, 20 , getHeight() / 2 + font.getSize() / 4);
    }
}
