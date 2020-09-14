package cs1410;

import java.util.Scanner;
import cs1410lib.Dialogs;

/**
 * This class is designed to take user input from a series of 4 popup windows, use that input to make some calculations,
 * and then output those calculations to both a popup dialog window and directly to the console. 
 * Author: Brandon Walters
 */
public class GasMileage
{
    /**
     * This main method gathers user input from 4 dialog windows, uses that data to make calculations about the user's
     * car, and then outputs those calculations both in a window and directly to the console./*
     */
    public static void main (String[] args)
    {
        String carType = Dialogs.showInputDialog("What is the year and model of your car?");

        String milesDrivenSinceGas = Dialogs
                .showInputDialog("How far, in miles, have you driven since you filled your tank?");
        int milesDrivenInt = Integer.parseInt(milesDrivenSinceGas);

        String priceOfGas = Dialogs.showInputDialog("How much does a gallon of gas cost?");
        double priceOfGasDouble = Double.parseDouble(priceOfGas);

        String gallonsToFillTank = Dialogs.showInputDialog("How many gallons of gas would you need to fill your tank?");
        double gallonsToFillDouble = Double.parseDouble(gallonsToFillTank);

        double costToFill = (priceOfGasDouble * gallonsToFillDouble);
        double mpgSinceLastFill = (milesDrivenInt / gallonsToFillDouble);
        double gasCostPerMile = (priceOfGasDouble / gallonsToFillDouble);

        String costToFillString = String.format("%.2f", costToFill);
        String mpgSinceLastFillString = String.format("%.2f", mpgSinceLastFill);
        String gasCostPerMileString = String.format("%.2f", gasCostPerMile);

        Dialogs.showMessageDialog(carType + "\n" + "Cost to fill tank: $" + costToFillString + "\n"
                + "Miles per gallon since last fill-up: " + mpgSinceLastFillString + "\n"
                + "Gas cost per mile since last fill-up: $" + gasCostPerMileString);

        System.out.println(carType);
        System.out.println("Cost to fill tank: $" + costToFillString);
        System.out.println("Miles per gallon since last fill-up: " + mpgSinceLastFillString);
        System.out.println("Gas cost per mile since last fill-up: $" + gasCostPerMileString);
    }

}
