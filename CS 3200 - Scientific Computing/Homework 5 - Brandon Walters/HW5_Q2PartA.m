A = [1 2 3; 4 5 6; 7 8 9];
v = [1;1;1];
tol = 1.0e-5;

[realEigVectors, realEigValues] = eig(A);
x = PowerMethodFIXED(A, v, tol); % Eigenvalue
x2 = PowerMethod(A, v, tol); % Eigenvector

fprintf('Largest Generated Eigenvalue = %.4f, Largest Actual Eigenvalue = %.4f\n', x, realEigValues(1));