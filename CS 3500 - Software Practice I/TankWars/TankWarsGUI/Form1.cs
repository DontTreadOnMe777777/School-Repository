using NetworkUtil;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace TankWars
{
    public partial class Form1 : Form
    {
        // World is a simple container for Players and Powerups
        // The controller owns the world, but we have a reference to it
        private World theWorld;

        // The controller contains all of the logic in the client-side implementation, and acts as a bridge between the server and the client
        private Controller controller;

        // The drawingPanel is the GUI on which we draw the game
        DrawingPanel drawingPanel;

        /// <summary>
        /// Constructor for our form, which lays out the basic GUI layout and sets some listeners manually
        /// </summary>
        /// <param name="con"></param>
        public Form1(Controller con)
        {
            InitializeComponent();
            controller = con;
            theWorld = con.GetWorld();
            controller.RegisterServerUpdateHandler(OnFrame);
            drawingPanel = new DrawingPanel(theWorld);
            drawingPanel.Location = new Point(0, 36);
            drawingPanel.Size = new Size(800, 800);

            // Set listener events for the mouse on the drawingPanel
            drawingPanel.MouseDown += mouseClicked;
            drawingPanel.MouseUp += mouseReleased;
            drawingPanel.MouseMove += pointerTracker;

            // Change the background to gray 
            this.BackColor = System.Drawing.Color.Gray;

            // Limit the player to a name of 16 characters or under
            nameTextBox.MaxLength = 16;
        }

        /// <summary>
        /// Used to invalidate the form, which invokes the OnPaint method
        /// </summary>
        private void OnFrame()
        {
            // Don't try to redraw if the window doesn't exist yet.
            // This might happen if the controller sends an update
            // before the Form has started.
            if (!IsHandleCreated)
                return;

            // Invalidate this form and all its children
            // This will cause the form to redraw as soon as it can
            MethodInvoker invokeInvalidate = new MethodInvoker(() => this.Invalidate(true));
            try
            {
                this.Invoke(invokeInvalidate);
            }
            catch { }
        }

        /// <summary>
        /// Listener for when the Connect button is pressed
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void connectButtonClick(object sender, EventArgs e)
        {
            // Disables the connection GUI at the top, sets key listeners to the drawingPanel
            this.KeyPreview = true;
            serverTextBox.Enabled = false;
            nameTextBox.Enabled = false;
            connectButton.Enabled = false;
            try
            {
                // Start connection and add the drawingPanel to the GUI
                controller.InitializeConnectToServer(serverTextBox.Text, nameTextBox.Text);
                this.Controls.Add(drawingPanel);
            }
            catch
            {
                // Take away the drawing panel, show an error message, and then re-enable the connection GUI to allow for a retry
                drawingPanel.Visible = false;
                MessageBox.Show("Error connecting to the server! Please try again.");
                drawingPanel.Visible = true;
                this.KeyPreview = false;
                serverTextBox.Enabled = true;
                nameTextBox.Enabled = true;
                connectButton.Enabled = true;
                return;
            }
        }
        /// <summary>
        /// For when the About menu is opened
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void aboutButton_Click(object sender, EventArgs e)
        {
            drawingPanel.Visible = false;
            MessageBox.Show("Tank Wars!\n\nCreated by Alysha Armstrong and Brandon Walters\n\nNovember 11th, 2019");
            drawingPanel.Visible = true;
            return;
        }
        /// <summary>
        /// For when the Controls menu is opened
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void controlsButton_Click(object sender, EventArgs e)
        {
            drawingPanel.Visible = false;
            MessageBox.Show("Controls:\n\nW - Move up\nS - Move down\nA - Move left\nD - Move right\nMouse - Aim\nLeft Click - Fire\nRight Click - Fire Beam\nQ - Quit");
            drawingPanel.Visible = true;
            return;
        }

        /// <summary>
        /// Listeners for the mouse buttons, which modify the control command for firing purposes
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void mouseClicked(object sender, MouseEventArgs e)
        {
            if (e.Button == MouseButtons.Left)
            {
                theWorld.SetCommandFire("main");
            }

            else if (e.Button == MouseButtons.Right)
            {
                theWorld.SetCommandFire("alt");
            }
        }

        /// <summary>
        /// Listeners to stop firing once the mouse button is released
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void mouseReleased(object sender, MouseEventArgs e)
        {

            theWorld.SetCommandFire("none");

        }

        /// <summary>
        /// Listeners to modify the control command for movement of the player, and to quit the game with the Q button
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void keyDown(object sender, KeyEventArgs e)
        {
            if (e.KeyData == Keys.W)
            {
                theWorld.SetCommandMoving("up");
            }

            if (e.KeyData == Keys.A)
            {
                theWorld.SetCommandMoving("left");
            }

            if (e.KeyData == Keys.S)
            {
                theWorld.SetCommandMoving("down");
            }

            if (e.KeyData == Keys.D)
            {
                theWorld.SetCommandMoving("right");
            }

            if (e.KeyData == Keys.Q)
            {
                Close();
            }
        }
        /// <summary>
        /// Listeners to modify the control command to stop the player's movement once the key is lifted
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void keyUp(object sender, KeyEventArgs e)
        {
            if (e.KeyData == Keys.W)
            {
                theWorld.SetCommandMoving("none");
            }

            if (e.KeyData == Keys.A)
            {
                theWorld.SetCommandMoving("none");
            }

            if (e.KeyData == Keys.S)
            {
                theWorld.SetCommandMoving("none");
            }

            if (e.KeyData == Keys.D)
            {
                theWorld.SetCommandMoving("none");
            }
        }

        /// <summary>
        /// Listener to track the position of the cursor when on the drawingPanel, which is used to orient the turret
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void pointerTracker(object sender, MouseEventArgs e)
        {
            int x = e.X - 400;
            int y = e.Y - 400;
            Vector2D vector = new Vector2D(x, y);
            theWorld.SetCommandTurret(vector);
        }
    }
}
