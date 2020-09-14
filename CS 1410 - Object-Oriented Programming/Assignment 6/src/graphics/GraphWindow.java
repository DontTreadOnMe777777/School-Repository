package graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeMap;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import cs1410.GraphingMethods;

/**
 * Provides a GUI interface for viewing pie charts and bar graphs
 */
@SuppressWarnings("serial")
public class GraphWindow extends JFrame
{
    // JPanel where graph is drawn
    private DrawingArea area;

    // JPanel where categories are displayed and manipulated
    private CategoryArea categoryPanel;

    // Buttons for toggling between pie chart and bar graph mode
    private JRadioButton pieChart;
    private JRadioButton barGraph;

    // Control for selecting combining operation
    private JComboBox<String> operations;

    // Data being graphed.
    private TreeMap<String, ArrayList<Double>> valueMap;

    // File and color choosers
    JFileChooser chooser;

    /**
     * Constructs a custom CS 1410 graph window.
     */
    public GraphWindow ()
    {
        // Set the look and feel
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e)
        {
        }

        // Set up some preferences
        setTitle("CS 1410 Graph Window");
        setPreferredSize(new Dimension(700, 500));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Set up file chooser
        chooser = new JFileChooser(new File(System.getProperty("user.dir")));
        chooser.setFileFilter(new FileNameExtensionFilter("TEXT FILES", "txt", "text"));

        // Set look and feel
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e)
        {
        }

        // Lay out controls at top
        JPanel topControls = new JPanel();
        pieChart = new JRadioButton("Pie Chart");
        barGraph = new JRadioButton("Bar Graph");
        ButtonGroup group = new ButtonGroup();
        group.add(pieChart);
        group.add(barGraph);
        pieChart.setSelected(true);
        topControls.add(pieChart);
        topControls.add(barGraph);
        operations = new JComboBox<>(new String[] { "Sum", "Avg", "Max", "Min" });
        topControls.add(operations);
        pieChart.addActionListener( (e) -> area.repaint());
        barGraph.addActionListener( (e) -> area.repaint());
        operations.addActionListener( (e) -> area.repaint());

        // Lay out control at bottom
        JButton graphButton = new JButton("Load Data");
        graphButton.addActionListener( (e) -> loadData());

        // Lay out the category area on the right
        categoryPanel = new CategoryArea();

        // Lay out the drawing area in the middle
        area = new DrawingArea();

        // Put everything together
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(area, "Center");
        mainPanel.add(graphButton, "South");
        mainPanel.add(topControls, "North");
        mainPanel.add(categoryPanel, "East");
        setContentPane(mainPanel);

        // Complete layout
        pack();
    }

    /**
     * Gets the combining operation selected by the user
     */
    public int getOperation ()
    {
        switch ((String) operations.getSelectedItem())
        {
            case "Sum":
                return GraphingMethods.SUM;
            case "Avg":
                return GraphingMethods.AVG;
            case "Max":
                return GraphingMethods.MAX;
            case "Min":
                return GraphingMethods.MIN;
            default:
                return GraphingMethods.SUM;
        }
    }

    /**
     * Uses a JFileChooser to obtain a file of data that is used to update the data being displayed.
     */
    public void loadData ()
    {
        // Display the file chooser
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            // Try to read the data from the file
            try (Scanner dataSource = new Scanner(chooser.getSelectedFile()))
            {
                // Read the data into a map
                valueMap = GraphingMethods.readTable(dataSource);
                
                // Make sure that the data is good each time it changes
                GraphingMethods.prepareGraph(valueMap, getOperation());

                // Add the unique categories to the category panel
                categoryPanel.removeAll();
                int count = 1;
                for (String category : valueMap.keySet())
                {
                    Color color = Color.getHSBColor(1.0f / valueMap.size() * count, 0.75f, 0.75f);
                    categoryPanel.addCategory(category, color);
                    count++;
                }

                // Lay out everything again since the category panel has changed
                pack();
            }
            catch (IOException e)
            {
                JOptionPane.showMessageDialog(this, "Unable to open or read file");
            }
            catch (IllegalArgumentException e)
            {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    /**
     * Area where information about categories is displayed
     */
    private class CategoryArea extends JPanel
    {
        // The checkboxes, color buttons, and categories stored
        // in parallel arrays.
        private ArrayList<JCheckBox> checkBoxes;
        private ArrayList<JButton> colorButtons;
        private ArrayList<JLabel> categoryLabels;

        /**
         * Initialize and empty area
         */
        public CategoryArea ()
        {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            checkBoxes = new ArrayList<>();
            colorButtons = new ArrayList<>();
            categoryLabels = new ArrayList<>();
        }

        /**
         * Adds a new category
         */
        public void addCategory (String cat, Color newColor)
        {
            // A JPanel to contain the new category
            JPanel category = new JPanel();
            category.setLayout(new FlowLayout(FlowLayout.LEFT));

            // Checkbox
            JCheckBox check = new JCheckBox();
            check.setSelected(true);
            category.add(check);
            checkBoxes.add(check);
            check.addActionListener( (e) -> area.repaint());

            // Color button
            JButton color = new JButton("");
            color.setBackground(newColor);
            JPanel bPanel = new JPanel();
            bPanel.setPreferredSize(new Dimension(50, 30));
            bPanel.add(color);
            bPanel.setBackground(newColor);
            category.add(bPanel);
            colorButtons.add(color);
            color.addActionListener( (e) -> chooseColor(color));

            // Category
            JLabel label = new JLabel(cat);
            category.add(label);
            categoryLabels.add(label);

            // Lay out
            add(category);
        }

        /**
         * Removes all category information
         */
        @Override
        public void removeAll ()
        {
            super.removeAll();
            checkBoxes.clear();
            colorButtons.clear();
            categoryLabels.clear();
            area.repaint();
        }

        /**
         * Returns the selected colors
         */
        public TreeMap<String, Color> getColors ()
        {
            TreeMap<String, Color> colors = new TreeMap<>();
            for (int i = 0; i < checkBoxes.size(); i++)
            {
                if (checkBoxes.get(i).isSelected())
                {
                    colors.put(categoryLabels.get(i).getText(), colorButtons.get(i).getBackground());
                }
            }
            return colors;
        }

        /**
         * Returns the selected categories
         */
        public HashSet<String> getCategories ()
        {
            HashSet<String> categories = new HashSet<>();
            for (int i = 0; i < checkBoxes.size(); i++)
            {
                if (checkBoxes.get(i).isSelected())
                {
                    categories.add(categoryLabels.get(i).getText());
                }
            }
            return categories;
        }

        /**
         * Uses a JColorChooser to choose a new color for a category.
         */
        private void chooseColor (JButton button)
        {
            Color chosen = JColorChooser.showDialog(this, "Choose a Color", button.getBackground());
            if (chosen != null)
            {
                button.setBackground(chosen);
                button.getParent().setBackground(chosen);
                area.repaint();
            }
        }
    }

    /**
     * The area where the graph is drawn
     */
    private class DrawingArea extends JPanel
    {
        /**
         * Draws the graph
         */
        @Override
        public void paintComponent (Graphics graphics)
        {
            Graphics2D g = (Graphics2D) graphics;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.clearRect(0, 0, getWidth(), getHeight());
            HashSet<String> categories = categoryPanel.getCategories();
            if (categories.size() > 0)
            {
                TreeMap<String, Color> colors = categoryPanel.getColors();
                TreeMap<String, Double> percentages = GraphingMethods.prepareGraph(valueMap, getOperation());
                GraphingMethods.drawGraph(g, percentages, colors, pieChart.isSelected());
            }
        }
    }
}
