//Authors: Alysha Armstrong and Brandon Walters, 11/13/2019

using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Timers;
using System.Windows.Forms;


namespace TankWars
{
    /// <summary>
    /// Draws the objects stored in the World class for the TankWars form
    /// </summary>
    class DrawingPanel : Panel
    {
        /// <summary>
        /// reference to the world-which holds all the objects to draw
        /// </summary>
        private World theWorld;

        /// <summary>
        /// Timer for displaying the beam
        /// </summary>
        private System.Timers.Timer beamTimer = new System.Timers.Timer(300);
        /// <summary>
        /// beam to remove after the beamTimer has elapsed
        /// </summary>
        private Beam beamToDelete;

        // resized shot images
        private Bitmap blueShotImage = new Bitmap(new Bitmap("../../../Resources/Images/shot-blue.png"), new Size(Constants.ProjectileWidth, Constants.ProjectileHeight));
        private Bitmap redShotImage = new Bitmap(new Bitmap("../../../Resources/Images/shot-red.png"), new Size(Constants.ProjectileWidth, Constants.ProjectileHeight));
        private Bitmap yellowShotImage = new Bitmap(new Bitmap("../../../Resources/Images/shot-yellow.png"), new Size(Constants.ProjectileWidth, Constants.ProjectileHeight));
        private Bitmap greenShotImage = new Bitmap(new Bitmap("../../../Resources/Images/shot-green.png"), new Size(Constants.ProjectileWidth, Constants.ProjectileHeight));
        private Bitmap violetShotImage = new Bitmap(new Bitmap("../../../Resources/Images/shot-violet.png"), new Size(Constants.ProjectileWidth, Constants.ProjectileHeight));
        private Bitmap whiteShotImage = new Bitmap(new Bitmap("../../../Resources/Images/shot-white.png"), new Size(Constants.ProjectileWidth, Constants.ProjectileHeight));
        private Bitmap greyShotImage = new Bitmap(new Bitmap("../../../Resources/Images/shot-grey.png"), new Size(Constants.ProjectileWidth, Constants.ProjectileHeight));
        private Bitmap brownShotImage = new Bitmap(new Bitmap("../../../Resources/Images/shot-brown.png"), new Size(Constants.ProjectileWidth, Constants.ProjectileHeight));

        // the background image
        private Bitmap background = new Bitmap("../../../Resources/Images/Background.png");

        // resized tank images
        private Bitmap blueTank = new Bitmap(new Bitmap("../../../Resources/Images/BlueTank.png"), new Size(Constants.TankWidth, Constants.TankHeight));
        private Bitmap redTank = new Bitmap(new Bitmap("../../../Resources/Images/RedTank.png"), new Size(Constants.TankWidth, Constants.TankHeight));
        private Bitmap orangeTank = new Bitmap(new Bitmap("../../../Resources/Images/OrangeTank.png"), new Size(Constants.TankWidth, Constants.TankHeight));
        private Bitmap yellowTank = new Bitmap(new Bitmap("../../../Resources/Images/YellowTank.png"), new Size(Constants.TankWidth, Constants.TankHeight));
        private Bitmap darkTank = new Bitmap(new Bitmap("../../../Resources/Images/DarkTank.png"), new Size(Constants.TankWidth, Constants.TankHeight));
        private Bitmap greenTank = new Bitmap(new Bitmap("../../../Resources/Images/GreenTank.png"), new Size(Constants.TankWidth, Constants.TankHeight));
        private Bitmap lightGreenTank = new Bitmap(new Bitmap("../../../Resources/Images/LightGreenTank.png"), new Size(Constants.TankWidth, Constants.TankHeight));
        private Bitmap purpleTank = new Bitmap(new Bitmap("../../../Resources/Images/PurpleTank.png"), new Size(Constants.TankWidth, Constants.TankHeight));

        // resized turret images
        private Bitmap blueTurret = new Bitmap(new Bitmap("../../../Resources/Images/BlueTurret.png"), new Size(Constants.TurretWidth, Constants.TurretHeight));
        private Bitmap redTurret = new Bitmap(new Bitmap("../../../Resources/Images/RedTurret.png"), new Size(Constants.TurretWidth, Constants.TurretHeight));
        private Bitmap orangeTurret = new Bitmap(new Bitmap("../../../Resources/Images/OrangeTurret.png"), new Size(Constants.TurretWidth, Constants.TurretHeight));
        private Bitmap yellowTurret = new Bitmap(new Bitmap("../../../Resources/Images/YellowTurret.png"), new Size(Constants.TurretWidth, Constants.TurretHeight));
        private Bitmap darkTurret = new Bitmap(new Bitmap("../../../Resources/Images/DarkTurret.png"), new Size(Constants.TurretWidth, Constants.TurretHeight));
        private Bitmap greenTurret = new Bitmap(new Bitmap("../../../Resources/Images/GreenTurret.png"), new Size(Constants.TurretWidth, Constants.TurretHeight));
        private Bitmap lightGreenTurret = new Bitmap(new Bitmap("../../../Resources/Images/LightGreenTurret.png"), new Size(Constants.TurretWidth, Constants.TurretHeight));
        private Bitmap purpleTurret = new Bitmap(new Bitmap("../../../Resources/Images/PurpleTurret.png"), new Size(Constants.TurretWidth, Constants.TurretHeight));
        // resized wall image
        private Bitmap wallImage = new Bitmap(new Bitmap("../../../Resources/Images/WallSprite.png"), new Size(Constants.WallWidth, Constants.WallHeight));
        // resized explosion image
        private Bitmap explosionImage = new Bitmap(new Bitmap("../../../Resources/Images/DeadTank.png"), new Size(Constants.ExplosionWidth, Constants.ExplosionHeight));

        private System.Timers.Timer timer = new System.Timers.Timer();

        public DrawingPanel(World w)
        {
            DoubleBuffered = true;
            theWorld = w;
            // delete beam from the world when the beam timer elapses 
            beamTimer.Elapsed += (sender, e) => BeamTimer_Elapsed(sender, e, beamToDelete);
            beamTimer.Enabled = false;
        }

        /// <summary>
        /// Helper method for DrawObjectWithTransform
        /// </summary>
        /// <param name="size">The world (and image) size</param>
        /// <param name="w">The worldspace coordinate</param>
        /// <returns></returns>
        private static int WorldSpaceToImageSpace(int size, double w)
        {
            return (int)w + size / 2;
        }

        // A delegate for DrawObjectWithTransform
        // Methods matching this delegate can draw whatever they want using e  
        public delegate void ObjectDrawer(object o, PaintEventArgs e);

        /// <summary>
        /// This method performs a translation and rotation to drawn an object in the world.
        /// </summary>
        /// <param name="e">PaintEventArgs to access the graphics (for drawing)</param>
        /// <param name="o">The object to draw</param>
        /// <param name="worldSize">The size of one edge of the world (assuming the world is square)</param>
        /// <param name="worldX">The X coordinate of the object in world space</param>
        /// <param name="worldY">The Y coordinate of the object in world space</param>
        /// <param name="angle">The orientation of the objec, measured in degrees clockwise from "up"</param>
        /// <param name="drawer">The drawer delegate. After the transformation is applied, the delegate is invoked to draw whatever it wants</param>
        private void DrawObjectWithTransform(PaintEventArgs e, object o, int worldSize, double worldX, double worldY, double angle, ObjectDrawer drawer)
        {
            // "push" the current transform
            System.Drawing.Drawing2D.Matrix oldMatrix = e.Graphics.Transform.Clone();

            int x = WorldSpaceToImageSpace(worldSize, worldX);
            int y = WorldSpaceToImageSpace(worldSize, worldY);
            e.Graphics.TranslateTransform(x, y);
            e.Graphics.RotateTransform((float)angle);
            drawer(o, e);

            // "pop" the transform
            e.Graphics.Transform = oldMatrix;
        }

        /// <summary>
        /// Returns the color of the tank/turret to use depending on the id
        /// </summary>
        /// <param name="id">object id</param>
        /// <returns></returns>
        private Color GetColor(int id)
        {
            if (id == 0 || id % 10 == 0)
            {
                return Color.Blue;
            }

            if (id == 1 || id == 8 || id % 10 == 1 || id % 10 == 8)
            {
                return Color.Red;
            }

            if (id == 2 || id == 9 || id % 10 == 2 || id % 10 == 9)
            {
                return Color.Yellow;
            }

            if (id == 3 || id % 10 == 3)
            {
                return Color.DarkBlue;
            }

            if (id == 4 || id % 10 == 4)
            {
                return Color.Green;
            }

            if (id == 5 || id % 10 == 5)
            {
                return Color.LightGreen;
            }

            if (id == 6 || id % 10 == 6)
            {
                return Color.Orange;
            }

            if (id == 7 || id % 10 == 7)
            {
                return Color.Purple;
            }

            else
            {
                return Color.Green;
            }
        }

        /// <summary>
        /// Draw the background 
        /// </summary>
        /// <param name="e">The PaintEventArgs to access the graphics</param>
        private void DrawBackground(PaintEventArgs e)
        {
            Bitmap b2 = new Bitmap(background, new Size(theWorld.WorldSize, theWorld.WorldSize));
            e.Graphics.DrawImage(b2, new Point(0, 0));

        }

        /// <summary>
        /// Acts as a drawing delegate for DrawObjectWithTransform
        /// After performing the necessary transformation (translate/rotate)
        /// DrawObjectWithTransform will invoke this method to draw a Tank
        /// </summary>
        /// <param name="o">The tank to draw</param>
        /// <param name="e">The PaintEventArgs to access the graphics</param>
        private void TankDrawer(object o, PaintEventArgs e)
        {
            Tank t = o as Tank;
            // offset to postion correctly
            Point offset = new Point(-Constants.TankWidth / 2, -Constants.TankHeight / 2);

            e.Graphics.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.AntiAlias;

            if (GetColor(t.ID).Equals(Color.Blue))
            {
                e.Graphics.DrawImage(blueTank, offset);
            }
            else if (GetColor(t.ID).Equals(Color.Red))
            {
                e.Graphics.DrawImage(redTank, offset);
            }
            else if (GetColor(t.ID).Equals(Color.Orange))
            {
                e.Graphics.DrawImage(orangeTank, offset);
            }
            else if (GetColor(t.ID).Equals(Color.Yellow))
            {
                e.Graphics.DrawImage(yellowTank, offset);
            }
            else if (GetColor(t.ID).Equals(Color.DarkBlue))
            {
                e.Graphics.DrawImage(darkTank, offset);
            }
            else if (GetColor(t.ID).Equals(Color.Green))
            {
                e.Graphics.DrawImage(greenTank, offset);
            }
            else if (GetColor(t.ID).Equals(Color.LightGreen))
            {
                e.Graphics.DrawImage(lightGreenTank, offset);
            }
            else if (GetColor(t.ID).Equals(Color.Purple))
            {
                e.Graphics.DrawImage(purpleTank, offset);
            }
        }

        /// <summary>
        /// Acts as a drawing delegate for DrawObjectWithTransform
        /// After performing the necessary transformation (translate/rotate)
        /// DrawObjectWithTransform will invoke this method to draw a Turrett
        /// </summary>
        /// <param name="o">The turret to draw</param>
        /// <param name="e">The PaintEventArgs to access the graphics</param>
        private void TurretDrawer(object o, PaintEventArgs e)
        {
            Tank t = o as Tank;
            // offset to postion correctly
            Point offset = new Point(-Constants.TurretWidth / 2, -Constants.TurretHeight / 2);

            e.Graphics.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.AntiAlias;
            if (GetColor(t.ID).Equals(Color.Blue))
            {
                e.Graphics.DrawImage(blueTurret, offset);
            }
            else if (GetColor(t.ID).Equals(Color.Red))
            {
                e.Graphics.DrawImage(redTurret, offset);
            }
            else if (GetColor(t.ID).Equals(Color.Orange))
            {
                e.Graphics.DrawImage(orangeTurret, offset);
            }
            else if (GetColor(t.ID).Equals(Color.Yellow))
            {
                e.Graphics.DrawImage(yellowTurret, offset);
            }
            else if (GetColor(t.ID).Equals(Color.DarkBlue))
            {
                e.Graphics.DrawImage(darkTurret, offset);
            }
            else if (GetColor(t.ID).Equals(Color.Green))
            {
                e.Graphics.DrawImage(greenTurret, offset);
            }
            else if (GetColor(t.ID).Equals(Color.LightGreen))
            {
                e.Graphics.DrawImage(lightGreenTurret, offset);
            }
            else if (GetColor(t.ID).Equals(Color.Purple))
            {
                e.Graphics.DrawImage(purpleTurret, offset);
            }
        }

        /// <summary>
        /// Acts as a drawing delegate for DrawObjectWithTransform
        /// After performing the necessary transformation (translate/rotate)
        /// DrawObjectWithTransform will invoke this method to draw a hp bar for a player.
        /// </summary>
        /// <param name="o">The tank to get HP from</param>
        /// <param name="e">The PaintEventArgs to access the graphics</param>
        private void HPBarDrawer(object o, PaintEventArgs e)
        {
            Tank t = o as Tank;

            int barHeight = Constants.HPBarHeight;
            int fullBarWidth = Constants.HPFullBarWidth;

            int tankHeight = Constants.TankHeight;
            int tankWidth = Constants.TankWidth;
            using (System.Drawing.SolidBrush redBrush = new System.Drawing.SolidBrush(System.Drawing.Color.Red))
            using (System.Drawing.SolidBrush yellowBrush = new System.Drawing.SolidBrush(System.Drawing.Color.Yellow))
            using (System.Drawing.SolidBrush greenBrush = new System.Drawing.SolidBrush(System.Drawing.Color.Green))
            {
                Rectangle r;
                // If max hp, draw a green bar
                if (t.GetHP() == Constants.MaxHP)
                {
                    r = new Rectangle(-tankWidth / 3, -(tankHeight / 2) - 2 * barHeight, fullBarWidth, barHeight);
                    e.Graphics.FillRectangle(greenBrush, r);
                }
                // if hp is less than the max hp but greater than 1, draw a smaller yellow bar
                else if (t.GetHP() < Constants.MaxHP && t.GetHP() > 1)
                {
                    r = new Rectangle(-tankWidth / 3, -(tankHeight / 2) - 2 * barHeight, 2 * fullBarWidth / 3, barHeight);
                    e.Graphics.FillRectangle(yellowBrush, r);
                }
                // if hp is 1, draw an even smaller red bar
                else
                {
                    r = new Rectangle(-tankWidth / 3, -(tankHeight / 2) - 2 * barHeight, fullBarWidth / 3, barHeight);
                    e.Graphics.FillRectangle(redBrush, r);
                }
            }
        }

        /// <summary>
        /// Acts as a drawing delegate for DrawObjectWithTransform
        /// After performing the necessary transformation (translate/rotate)
        /// DrawObjectWithTransform will invoke this method to draw the player name.
        /// </summary>
        /// <param name="o">The tank to get name from</param>
        /// <param name="e">The PaintEventArgs to access the graphics</param>
        private void PlayerNameDrawer(object o, PaintEventArgs e)
        {
            Tank t = o as Tank;
            int tankHeight = Constants.TankHeight;
            int tankWidth = Constants.TankWidth;

            e.Graphics.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.AntiAlias;

            // player name and score to put below the tank
            string text = t.name + ": " + t.GetScore();

            Font font = new Font("Calibri", 12);
            SolidBrush textBrush = new SolidBrush(Color.White);
            e.Graphics.DrawString(text, font, textBrush, new Point(-tankWidth / 2, tankHeight / 2));

        }

        /// <summary>
        /// Acts as a drawing delegate for DrawObjectWithTransform
        /// After performing the necessary transformation (translate/rotate)
        /// DrawObjectWithTransform will invoke this method to draw a Wall
        /// </summary>
        /// <param name="o">The wall to draw</param>
        /// <param name="e">The PaintEventArgs to access the graphics</param>
        private void SingleWallPanelDrawer(object o, PaintEventArgs e)
        {
            e.Graphics.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.AntiAlias;
            e.Graphics.DrawImage(wallImage, new Point(-Constants.WallWidth / 2, -Constants.WallHeight / 2));
        }

        /// <summary>
        /// Draws a series of wall panels between point1 and point2 specified in the Wall object,
        /// for each wall in the world
        /// </summary>
        /// <param name="e">The PaintEventArgs to access the graphics</param>
        /// <param name="walls">The walls in the world</param>
        private void EntireWallDrawer(PaintEventArgs e, Dictionary<int, Wall> walls)
        {
            foreach (Wall wall in walls.Values)
            {
                double start;
                double end;

                // wall is vertical 
                if (wall.point1.GetX() == wall.point2.GetX())
                {
                    // Get the starting point and end point of the wall
                    if (wall.point1.GetY() < wall.point2.GetY())
                    {
                        start = wall.point1.GetY();
                        end = wall.point2.GetY();
                    }
                    else
                    {
                        start = wall.point2.GetY();
                        end = wall.point1.GetY();
                    }
                    // Draw a walls from the starting postion to the end position, incrementing by the wall width 
                    // (length between p1 and p2 will always be a multiple of the wall width)
                    for (double pos = start; pos <= end; pos += Constants.WallWidth)
                    {
                        DrawObjectWithTransform(e, wall, theWorld.WorldSize, wall.point1.GetX(), pos, 0, SingleWallPanelDrawer);
                    }
                }
                // wall is horizontal 
                else
                {
                    if (wall.point1.GetX() < wall.point2.GetX())
                    {
                        start = wall.point1.GetX();
                        end = wall.point2.GetX();
                    }
                    else
                    {
                        start = wall.point2.GetX();
                        end = wall.point1.GetX();
                    }

                    // Draw a walls from the starting postion to the end position, incrementing by the wall width 
                    // (length between p1 and p2 will always be a multiple of the wall width)
                    for (double pos = start; pos <= end; pos += Constants.WallWidth)
                    {
                        DrawObjectWithTransform(e, wall, theWorld.WorldSize, pos, wall.point1.GetY(), 0, SingleWallPanelDrawer);
                    }
                }
            }
        }

        /// <summary>
        /// Acts as a drawing delegate for DrawObjectWithTransform
        /// After performing the necessary transformation (translate/rotate)
        /// DrawObjectWithTransform will invoke this method to draw a Powerup
        /// </summary>
        /// <param name="o">The powerup to draw</param>
        /// <param name="e">The PaintEventArgs to access the graphics</param>
        private void PowerupDrawer(object o, PaintEventArgs e)
        {
            PowerUp p = o as PowerUp;

            int width = Constants.PowerupWidth;
            int height = Constants.PowerupHeight;

            e.Graphics.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.AntiAlias;

            using (System.Drawing.SolidBrush redBrush = new System.Drawing.SolidBrush(System.Drawing.Color.Red))
            using (System.Drawing.SolidBrush yellowBrush = new System.Drawing.SolidBrush(System.Drawing.Color.Yellow))
            using (System.Drawing.SolidBrush blueBrush = new System.Drawing.SolidBrush(System.Drawing.Color.Blue))
            {
                // Circles are drawn starting from the top-left corner.
                // So if we want the circle centered on the powerup's location, we have to offset it
                // by half its size to the left (-width/2) and up (-height/2)
                Rectangle smallCircle = new Rectangle(-(width / 2), -(height / 2), width, height);
                Rectangle largeCircle = new Rectangle(-width, -height, 2 * width, 2 * height);

                // draw a red circle within a blue circle if id is even
                if (p.ID % 2 == 0)
                {
                    e.Graphics.FillEllipse(blueBrush, largeCircle);
                    e.Graphics.FillEllipse(redBrush, smallCircle);
                }
                // draw a blue circle within a yellow circle if id is odd
                if (p.ID % 2 == 1)
                {
                    e.Graphics.FillEllipse(yellowBrush, largeCircle);
                    e.Graphics.FillEllipse(blueBrush, smallCircle);

                }

            }
        }

        /// <summary>
        /// Acts as a drawing delegate for DrawObjectWithTransform
        /// After performing the necessary transformation (translate/rotate)
        /// DrawObjectWithTransform will invoke this method to draw a Beam. 
        /// </summary>
        /// <param name="o">The beam to draw</param>
        /// <param name="e">The PaintEventArgs to access the graphics</param>
        private void BeamDrawer(object o, PaintEventArgs e)
        {
            e.Graphics.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.AntiAlias;

            using (System.Drawing.SolidBrush whiteBrush = new System.Drawing.SolidBrush(System.Drawing.Color.White))
            {
                Pen pen = new Pen(whiteBrush, 5);
                // Draw beam from the end of the turrett
                e.Graphics.DrawLine(pen, (float)0, (float)-Constants.TurretHeight / 2, (float)0, (float)-theWorld.WorldSize);
            }

        }

        /// <summary>
        /// Event for the Beam_Timer. Removes the beam from the world.
        /// </summary>
        /// <param name="sender">timer sender</param>
        /// <param name="e">ElapsedEventArgs from timer</param>
        /// <param name="beam">beam to be removed</param>
        private void BeamTimer_Elapsed(object sender, ElapsedEventArgs e, Beam beam)
        {
            beamTimer.Stop();
            lock (theWorld)
            {
                theWorld.RemoveBeam(beam);
            }
        }

        /// <summary>
        /// Acts as a drawing delegate for DrawObjectWithTransform
        /// After performing the necessary transformation (translate/rotate)
        /// DrawObjectWithTransform will invoke this method to draw a Projectile
        /// </summary>
        /// <param name="o">The projectile to draw</param>
        /// <param name="e">The PaintEventArgs to access the graphics</param>
        private void ProjectileDrawer(object o, PaintEventArgs e)
        {
            Projectile p = o as Projectile;

            // offset to postion correctly
            Point offset = new Point(-Constants.ProjectileWidth / 2, -Constants.ProjectileHeight / 2);

            e.Graphics.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.AntiAlias;

            // Get the color of the tank that owns this projectile
            Color color = GetColor(p.ownerID);

            if (color.Equals(Color.Blue))
            {
                e.Graphics.DrawImage(blueShotImage, offset);
            }
            else if (color.Equals(Color.Red))
            {
                e.Graphics.DrawImage(redShotImage, offset);

            }
            else if (color.Equals(Color.Orange))
            {
                e.Graphics.DrawImage(whiteShotImage, offset);

            }
            else if (color.Equals(Color.Yellow))
            {
                e.Graphics.DrawImage(yellowShotImage, offset);

            }
            else if (color.Equals(Color.DarkBlue))
            {
                e.Graphics.DrawImage(greyShotImage, offset);

            }
            else if (color.Equals(Color.Green))
            {
                e.Graphics.DrawImage(greenShotImage, offset);

            }
            else if (color.Equals(Color.LightGreen))
            {
                e.Graphics.DrawImage(brownShotImage, offset);

            }
            else if (color.Equals(Color.Purple))
            {
                e.Graphics.DrawImage(violetShotImage, offset);
            }
        }

        /// <summary>
        /// Acts as a drawing delegate for DrawObjectWithTransform
        /// After performing the necessary transformation (translate/rotate)
        /// DrawObjectWithTransform will invoke this method to draw an explosion after 
        /// a tank has died, and before it has respawned
        /// </summary>
        /// <param name="o">The tank to explode</param>
        /// <param name="e">The PaintEventArgs to access the graphics</param>
        private void ExplosionDrawer(object o, PaintEventArgs e)
        {
            e.Graphics.DrawImage(explosionImage, new Point(-Constants.ExplosionWidth / 2, -Constants.ExplosionHeight / 2));
        }

        /// <summary>
        /// Invoked when the DrawingPanel needs to be re-drawn
        /// </summary>
        /// <param name="e">PaintEventArgs to access graphics</param>
        protected override void OnPaint(PaintEventArgs e)
        {
            // Only draw if initial information from the server has been received
            if (theWorld.WorldSizeReceived == true)
            {
                // Center the player view with the tank 
                theWorld.GetTanks().TryGetValue(theWorld.GetPlayerID(), out Tank playerTank);
                if (playerTank != null)
                {
                    double playerX = playerTank.GetLocation().GetX();
                    double playerY = playerTank.GetLocation().GetY();

                    // calculate view/world size ratio
                    double ratio = (double)this.Size.Width / (double)theWorld.WorldSize;
                    int halfSizeScaled = (int)(theWorld.WorldSize / 2.0 * ratio);

                    double inverseTranslateX = -WorldSpaceToImageSpace(theWorld.WorldSize, playerX) + halfSizeScaled;
                    double inverseTranslateY = -WorldSpaceToImageSpace(theWorld.WorldSize, playerY) + halfSizeScaled;

                    e.Graphics.TranslateTransform((float)inverseTranslateX, (float)inverseTranslateY);

                }

                DrawBackground(e);

                // Draw walls
                lock (theWorld)
                {
                    EntireWallDrawer(e, theWorld.GetWalls());
                }

                // Draw projectiles
                lock (theWorld)
                {

                    foreach (Projectile p in theWorld.GetProjectiles().Values)
                    {
                        DrawObjectWithTransform(e, p, theWorld.WorldSize, p.location.GetX(), p.location.GetY(), p.orientation.ToAngle(), ProjectileDrawer);
                    }
                }

                //Draw the players
                lock (theWorld)
                {
                    foreach (Tank tank in theWorld.GetTanks().Values)
                    {
                        // Draw an explosion if the player is dead
                        if (tank.IsDead() || tank.GetHP() == 0)
                        {
                            DrawObjectWithTransform(e, tank, theWorld.WorldSize, tank.GetLocation().GetX(), tank.GetLocation().GetY(), 0, ExplosionDrawer);
                        }
                        else
                        {
                            DrawObjectWithTransform(e, tank, theWorld.WorldSize, tank.GetLocation().GetX(), tank.GetLocation().GetY(), tank.GetOrientation().ToAngle(), TankDrawer);
                            DrawObjectWithTransform(e, tank, theWorld.WorldSize, tank.GetLocation().GetX(), tank.GetLocation().GetY(), tank.GetAiming().ToAngle(), TurretDrawer);
                            DrawObjectWithTransform(e, tank, theWorld.WorldSize, tank.GetLocation().GetX(), tank.GetLocation().GetY(), 0, PlayerNameDrawer);
                            DrawObjectWithTransform(e, tank, theWorld.WorldSize, tank.GetLocation().GetX(), tank.GetLocation().GetY(), 0, HPBarDrawer);
                        }
                    }
                }

                // Draw the powerups
                lock (theWorld)
                {
                    foreach (PowerUp p in theWorld.GetPowerups().Values)
                    {
                        DrawObjectWithTransform(e, p, theWorld.WorldSize, p.origin.GetX(), p.origin.GetY(), 0, PowerupDrawer);
                    }
                }

                // Drarw the beams
                lock (theWorld)
                {
                    foreach (Beam b in theWorld.GetBeams().Values)
                    {
                        DrawObjectWithTransform(e, b, theWorld.WorldSize, b.origin.GetX(), b.origin.GetY(), b.direction.ToAngle(), BeamDrawer);
                        beamToDelete = b;
                    }
                    // If beams have been drawn, start the timer to remove them from the world
                    if (theWorld.GetBeams().Count != 0)
                    {
                        beamTimer.Start();
                    }
                }
                base.OnPaint(e);
            }
        }
    }
}
