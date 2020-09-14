namespace TipCalculator
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
            this.EnterTotalBill = new System.Windows.Forms.Label();
            this.TotalBillTextBox = new System.Windows.Forms.TextBox();
            this.ComputeTipTextBox = new System.Windows.Forms.TextBox();
            this.ComputeTipButton = new System.Windows.Forms.Button();
            this.TipPercentLabel = new System.Windows.Forms.Label();
            this.TipPercentBox = new System.Windows.Forms.TextBox();
            this.TotalAmountTextBox = new System.Windows.Forms.TextBox();
            this.TotalAmountLabel = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // EnterTotalBill
            // 
            this.EnterTotalBill.AutoSize = true;
            this.EnterTotalBill.Location = new System.Drawing.Point(283, 95);
            this.EnterTotalBill.Name = "EnterTotalBill";
            this.EnterTotalBill.Size = new System.Drawing.Size(75, 13);
            this.EnterTotalBill.TabIndex = 0;
            this.EnterTotalBill.Text = "Enter Total Bill";
            this.EnterTotalBill.Click += new System.EventHandler(this.Label1_Click);
            // 
            // TotalBillTextBox
            // 
            this.TotalBillTextBox.Location = new System.Drawing.Point(385, 92);
            this.TotalBillTextBox.Name = "TotalBillTextBox";
            this.TotalBillTextBox.Size = new System.Drawing.Size(100, 20);
            this.TotalBillTextBox.TabIndex = 1;
            this.TotalBillTextBox.TextChanged += new System.EventHandler(this.TotalBillTextBox_TextChanged);
            // 
            // ComputeTipTextBox
            // 
            this.ComputeTipTextBox.Location = new System.Drawing.Point(385, 183);
            this.ComputeTipTextBox.Name = "ComputeTipTextBox";
            this.ComputeTipTextBox.Size = new System.Drawing.Size(100, 20);
            this.ComputeTipTextBox.TabIndex = 2;
            // 
            // ComputeTipButton
            // 
            this.ComputeTipButton.Location = new System.Drawing.Point(283, 183);
            this.ComputeTipButton.Name = "ComputeTipButton";
            this.ComputeTipButton.Size = new System.Drawing.Size(75, 23);
            this.ComputeTipButton.TabIndex = 3;
            this.ComputeTipButton.Text = "Compute Tip";
            this.ComputeTipButton.UseVisualStyleBackColor = true;
            this.ComputeTipButton.Click += new System.EventHandler(this.ComputeTipButton_Click);
            // 
            // TipPercentLabel
            // 
            this.TipPercentLabel.AutoSize = true;
            this.TipPercentLabel.Location = new System.Drawing.Point(280, 143);
            this.TipPercentLabel.Name = "TipPercentLabel";
            this.TipPercentLabel.Size = new System.Drawing.Size(92, 13);
            this.TipPercentLabel.TabIndex = 4;
            this.TipPercentLabel.Text = "Percentage of Tip";
            this.TipPercentLabel.Click += new System.EventHandler(this.Label1_Click_1);
            // 
            // TipPercentBox
            // 
            this.TipPercentBox.Location = new System.Drawing.Point(385, 140);
            this.TipPercentBox.Name = "TipPercentBox";
            this.TipPercentBox.Size = new System.Drawing.Size(100, 20);
            this.TipPercentBox.TabIndex = 5;
            this.TipPercentBox.TextChanged += new System.EventHandler(this.TipPercentBox_TextChanged);
            // 
            // TotalAmountTextBox
            // 
            this.TotalAmountTextBox.Location = new System.Drawing.Point(385, 276);
            this.TotalAmountTextBox.Name = "TotalAmountTextBox";
            this.TotalAmountTextBox.Size = new System.Drawing.Size(100, 20);
            this.TotalAmountTextBox.TabIndex = 6;
            // 
            // TotalAmountLabel
            // 
            this.TotalAmountLabel.AutoSize = true;
            this.TotalAmountLabel.Location = new System.Drawing.Point(288, 279);
            this.TotalAmountLabel.Name = "TotalAmountLabel";
            this.TotalAmountLabel.Size = new System.Drawing.Size(70, 13);
            this.TotalAmountLabel.TabIndex = 7;
            this.TotalAmountLabel.Text = "Total Amount";
            this.TotalAmountLabel.Click += new System.EventHandler(this.Label1_Click_2);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(800, 450);
            this.Controls.Add(this.TotalAmountLabel);
            this.Controls.Add(this.TotalAmountTextBox);
            this.Controls.Add(this.TipPercentBox);
            this.Controls.Add(this.TipPercentLabel);
            this.Controls.Add(this.ComputeTipButton);
            this.Controls.Add(this.ComputeTipTextBox);
            this.Controls.Add(this.TotalBillTextBox);
            this.Controls.Add(this.EnterTotalBill);
            this.Name = "Form1";
            this.Text = "Form1";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label EnterTotalBill;
        private System.Windows.Forms.TextBox TotalBillTextBox;
        private System.Windows.Forms.TextBox ComputeTipTextBox;
        private System.Windows.Forms.Button ComputeTipButton;
        private System.Windows.Forms.Label TipPercentLabel;
        private System.Windows.Forms.TextBox TipPercentBox;
        private System.Windows.Forms.TextBox TotalAmountTextBox;
        private System.Windows.Forms.Label TotalAmountLabel;
    }
}

