poly = [1 -2 -11 12];
rootCheck = roots(poly); % Root check
disp("Roots of the function are:");
disp(rootCheck);

xArray = [2.352875270 2.352841720 2.352837350 2.352836327 2.352836323];
xArrayItr = 1;

% Original Uni X points
for x = [2.352875270 2.352841720 2.352837350 2.352836327 2.352836323] %starting guess
    f = 0.0;
    n = 30;
    for i = 1:n                            %fixed number of iterations 
        f = x^3 - 2*x^2 - 11*x + 12;           %function to be zero
        dfdx = 3*x^2 - 4*x - 11;               %Jacobian matrix
        x = x - f/dfdx;                          %Newton iteration 
    end  
    fprintf('Original guess = %12.9e x = %12.3e  f %8.2e \n',xArray(xArrayItr),x,f)
    xArrayItr = xArrayItr + 1;
end

xArray = linspace(2.3528, 2.3530, 81);
xArrayItr = 1;
finalRoot = zeros(1,81);

% Linear points
for x = xArray %starting guess
    f = 0.0;
    n = 20;
    for i = 1:n                            %fixed number of iterations 
        f = x^3 - 2*x^2 - 11*x + 12;           %function to be zero
        dfdx = 3*x^2 - 4*x - 11;               %Jacobian matrix
        x = x - f/dfdx;                          %Newton iteration 
    end
    finalRoot(xArrayItr) = x;
    xArrayItr = xArrayItr + 1;
end

% Plot linear convergences
plot(xArray, finalRoot);
ylim([-5 5]);
title("Roots from linearly spaced points");
     