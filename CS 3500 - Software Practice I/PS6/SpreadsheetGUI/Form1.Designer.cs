namespace SpreadsheetGUI
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.spreadsheetPanel1 = new SS.SpreadsheetPanel();
            this.menuStrip1 = new System.Windows.Forms.MenuStrip();
            this.newToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.saveToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.openToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.closeToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.helpToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.setContentsTextBox = new System.Windows.Forms.TextBox();
            this.cellContentsLabel = new System.Windows.Forms.Label();
            this.cellValueTextBox = new System.Windows.Forms.Label();
            this.cellNameTextBox = new System.Windows.Forms.Label();
            this.setContentsButton = new System.Windows.Forms.Button();
            this.cellSearchBox = new System.Windows.Forms.TextBox();
            this.cellSearchLabel = new System.Windows.Forms.Label();
            this.cellSearchButton = new System.Windows.Forms.Button();
            this.menuStrip1.SuspendLayout();
            this.SuspendLayout();
            // 
            // spreadsheetPanel1
            // 
            this.spreadsheetPanel1.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.spreadsheetPanel1.Location = new System.Drawing.Point(1, 56);
            this.spreadsheetPanel1.Name = "spreadsheetPanel1";
            this.spreadsheetPanel1.Size = new System.Drawing.Size(798, 392);
            this.spreadsheetPanel1.TabIndex = 0;
            // 
            // menuStrip1
            // 
            this.menuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.newToolStripMenuItem,
            this.saveToolStripMenuItem,
            this.openToolStripMenuItem,
            this.closeToolStripMenuItem,
            this.helpToolStripMenuItem});
            this.menuStrip1.Location = new System.Drawing.Point(0, 0);
            this.menuStrip1.Name = "menuStrip1";
            this.menuStrip1.Size = new System.Drawing.Size(800, 24);
            this.menuStrip1.TabIndex = 1;
            this.menuStrip1.Text = "menuStrip1";
            // 
            // newToolStripMenuItem
            // 
            this.newToolStripMenuItem.Name = "newToolStripMenuItem";
            this.newToolStripMenuItem.Size = new System.Drawing.Size(43, 20);
            this.newToolStripMenuItem.Text = "New";
            this.newToolStripMenuItem.Click += new System.EventHandler(this.newSpreadsheetButton_Clicked);
            // 
            // saveToolStripMenuItem
            // 
            this.saveToolStripMenuItem.Name = "saveToolStripMenuItem";
            this.saveToolStripMenuItem.Size = new System.Drawing.Size(43, 20);
            this.saveToolStripMenuItem.Text = "Save";
            this.saveToolStripMenuItem.Click += new System.EventHandler(this.saveButton_Clicked);
            // 
            // openToolStripMenuItem
            // 
            this.openToolStripMenuItem.Name = "openToolStripMenuItem";
            this.openToolStripMenuItem.Size = new System.Drawing.Size(48, 20);
            this.openToolStripMenuItem.Text = "Open";
            this.openToolStripMenuItem.Click += new System.EventHandler(this.openButton_Clicked);
            // 
            // closeToolStripMenuItem
            // 
            this.closeToolStripMenuItem.Name = "closeToolStripMenuItem";
            this.closeToolStripMenuItem.Size = new System.Drawing.Size(48, 20);
            this.closeToolStripMenuItem.Text = "Close";
            this.closeToolStripMenuItem.Click += new System.EventHandler(this.closeButton_Clicked);
            // 
            // helpToolStripMenuItem
            // 
            this.helpToolStripMenuItem.Name = "helpToolStripMenuItem";
            this.helpToolStripMenuItem.Size = new System.Drawing.Size(44, 20);
            this.helpToolStripMenuItem.Text = "Help";
            this.helpToolStripMenuItem.Click += new System.EventHandler(this.helpButton_Clicked);
            // 
            // setContentsTextBox
            // 
            this.setContentsTextBox.Location = new System.Drawing.Point(91, 27);
            this.setContentsTextBox.Name = "setContentsTextBox";
            this.setContentsTextBox.Size = new System.Drawing.Size(100, 20);
            this.setContentsTextBox.TabIndex = 2;
            this.setContentsTextBox.TextChanged += new System.EventHandler(this.setContentsTextBox_Changed);
            // 
            // cellContentsLabel
            // 
            this.cellContentsLabel.AutoSize = true;
            this.cellContentsLabel.Location = new System.Drawing.Point(13, 30);
            this.cellContentsLabel.Name = "cellContentsLabel";
            this.cellContentsLabel.Size = new System.Drawing.Size(72, 13);
            this.cellContentsLabel.TabIndex = 3;
            this.cellContentsLabel.Text = "Cell Contents:";
            // 
            // cellValueTextBox
            // 
            this.cellValueTextBox.AutoSize = true;
            this.cellValueTextBox.Location = new System.Drawing.Point(327, 30);
            this.cellValueTextBox.Name = "cellValueTextBox";
            this.cellValueTextBox.Size = new System.Drawing.Size(57, 13);
            this.cellValueTextBox.TabIndex = 4;
            this.cellValueTextBox.Text = "Cell Value:";
            // 
            // cellNameTextBox
            // 
            this.cellNameTextBox.AutoSize = true;
            this.cellNameTextBox.Location = new System.Drawing.Point(494, 30);
            this.cellNameTextBox.Name = "cellNameTextBox";
            this.cellNameTextBox.Size = new System.Drawing.Size(58, 13);
            this.cellNameTextBox.TabIndex = 5;
            this.cellNameTextBox.Text = "Cell Name:";
            // 
            // setContentsButton
            // 
            this.setContentsButton.Enabled = false;
            this.setContentsButton.Location = new System.Drawing.Point(216, 25);
            this.setContentsButton.Name = "setContentsButton";
            this.setContentsButton.Size = new System.Drawing.Size(87, 23);
            this.setContentsButton.TabIndex = 6;
            this.setContentsButton.Text = "Set Contents";
            this.setContentsButton.UseVisualStyleBackColor = true;
            this.setContentsButton.Click += new System.EventHandler(this.setContentsButton_Clicked);
            // 
            // cellSearchBox
            // 
            this.cellSearchBox.Location = new System.Drawing.Point(676, 27);
            this.cellSearchBox.Name = "cellSearchBox";
            this.cellSearchBox.Size = new System.Drawing.Size(47, 20);
            this.cellSearchBox.TabIndex = 7;
            this.cellSearchBox.TextChanged += new System.EventHandler(this.cellSearchTextBox_Changed);
            // 
            // cellSearchLabel
            // 
            this.cellSearchLabel.AutoSize = true;
            this.cellSearchLabel.Location = new System.Drawing.Point(606, 30);
            this.cellSearchLabel.Name = "cellSearchLabel";
            this.cellSearchLabel.Size = new System.Drawing.Size(64, 13);
            this.cellSearchLabel.TabIndex = 8;
            this.cellSearchLabel.Text = "Cell Search:";
            // 
            // cellSearchButton
            // 
            this.cellSearchButton.Enabled = false;
            this.cellSearchButton.Location = new System.Drawing.Point(729, 25);
            this.cellSearchButton.Name = "cellSearchButton";
            this.cellSearchButton.Size = new System.Drawing.Size(59, 23);
            this.cellSearchButton.TabIndex = 9;
            this.cellSearchButton.Text = "Search";
            this.cellSearchButton.UseVisualStyleBackColor = true;
            this.cellSearchButton.Click += new System.EventHandler(this.cellSearchButton_Clicked);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(800, 450);
            this.Controls.Add(this.cellSearchButton);
            this.Controls.Add(this.cellSearchLabel);
            this.Controls.Add(this.cellSearchBox);
            this.Controls.Add(this.setContentsButton);
            this.Controls.Add(this.cellNameTextBox);
            this.Controls.Add(this.cellValueTextBox);
            this.Controls.Add(this.cellContentsLabel);
            this.Controls.Add(this.setContentsTextBox);
            this.Controls.Add(this.spreadsheetPanel1);
            this.Controls.Add(this.menuStrip1);
            this.MainMenuStrip = this.menuStrip1;
            this.Name = "Form1";
            this.Text = "Form1";
            this.menuStrip1.ResumeLayout(false);
            this.menuStrip1.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private SS.SpreadsheetPanel spreadsheetPanel1;
        private System.Windows.Forms.MenuStrip menuStrip1;
        private System.Windows.Forms.ToolStripMenuItem newToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem saveToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem openToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem closeToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem helpToolStripMenuItem;
        private System.Windows.Forms.TextBox setContentsTextBox;
        private System.Windows.Forms.Label cellContentsLabel;
        private System.Windows.Forms.Label cellValueTextBox;
        private System.Windows.Forms.Label cellNameTextBox;
        private System.Windows.Forms.Button setContentsButton;
        private System.Windows.Forms.TextBox cellSearchBox;
        private System.Windows.Forms.Label cellSearchLabel;
        private System.Windows.Forms.Button cellSearchButton;
    }
}

