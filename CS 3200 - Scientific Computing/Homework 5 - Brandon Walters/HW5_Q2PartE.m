A = [0.3 0.3 0.3 0.1; 0.9 0 0 0; 0 0.8 0 0; 0 0 0.5 0.1];
v = [100;200;150;75];
maxit = 1000;

x = PowerMethodFIXEDPartE(A, v, 1.0e-5); % Eigenvalue using special PowerMethod with less printing
realEigValues = eig(A);

% Initial pop. counts
population1 = 100;
population2 = 200;
population3 = 150;
population4 = 75;

% Iterate to year 1000 using rates
for i = 1:1001
    population1 = 0.3*population1 + 0.3*population2 + 0.3*population3 + 0.1*population4;
    population2 = 0.9*population1;
    population3 = 0.8*population2;
    population4 = 0.5*population3 + 0.1*population4;
end

fprintf('Largest Generated Eigenvalue = %.4f, Largest Actual Eigenvalue = %.4f\n', x, realEigValues(1));
fprintf('At Year 1000 with normal rates,\nPopulation 1 = %i\nPopulation 2 = %i\nPopulation 3 = %i\nPopulation 4 = %i\n', population1, population2, population3, population4);

% Now with essentially immortal Pop 4 (death rate of 4 is now 0.01 instead of 0.9, Part IV of Part E)

A = [0.3 0.3 0.3 0.1; 0.9 0 0 0; 0 0.8 0 0; 0 0 0.5 0.99];

x = PowerMethodFIXEDPartE(A, v, 1.0e-5); % Eigenvalue using special PowerMethod with less printing
realEigValues = eig(A);

% Initial pop. counts
population1 = 100;
population2 = 200;
population3 = 150;
population4 = 75;

% Iterate to year 1000 using rates
for i = 1:1001
    population1 = 0.3*population1 + 0.3*population2 + 0.3*population3 + 0.1*population4;
    population2 = 0.9*population1;
    population3 = 0.8*population2;
    population4 = 0.5*population3 + 0.99*population4;
end

fprintf('Largest Generated Eigenvalue = %.4f, Largest Actual Eigenvalue = %.4f\n', x, realEigValues(4));
fprintf('At Year 1000 with d4 = 0.01,\nPopulation 1 = %i\nPopulation 2 = %i\nPopulation 3 = %i\nPopulation 4 = %i\n', population1, population2, population3, population4);