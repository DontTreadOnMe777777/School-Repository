using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace TipCalculator
{
    public partial class Form1 : Form
    {

        private string billToUse;
        public Form1()
        {
            InitializeComponent();
        }

        private void Label1_Click(object sender, EventArgs e)
        {

        }

        // This button is officially epic
        private void ComputeTipButton_Click(object sender, EventArgs e)
        {
            if (TotalBillTextBox.Text == "" || TipPercentBox.Text == "")
            {
                ComputeTipButton.Enabled = false;
            }

            else
            {
                ComputeTipButton.Enabled = true;
                Double.TryParse(TotalBillTextBox.Text, out double billDouble);
                Double.TryParse(TipPercentBox.Text, out double tipDouble);
                double totalTip = billDouble * (tipDouble / 100);
                ComputeTipTextBox.Text = totalTip.ToString();
                TotalAmountTextBox.Text = (billDouble + totalTip).ToString();
            }
        }

        private void TotalBillTextBox_TextChanged(object sender, EventArgs e)
        {
            if (TotalBillTextBox.Text == "")
            {
                ComputeTipButton.Enabled = false;
            }

            else
            {
                ComputeTipButton.Enabled = true;
            }
        }

        private void Label1_Click_1(object sender, EventArgs e)
        {

        }

        private void Label1_Click_2(object sender, EventArgs e)
        {

        }

        private void TipPercentBox_TextChanged(object sender, EventArgs e)
        {
            if (TipPercentBox.Text == "")
            {
                ComputeTipButton.Enabled = false;
            }

            else
            {
                ComputeTipButton.Enabled = true;
            }
        }
    }
}
