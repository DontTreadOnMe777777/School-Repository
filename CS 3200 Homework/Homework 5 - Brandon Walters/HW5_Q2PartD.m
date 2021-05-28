A = [1 2 3; 4 5 6; 7 8 9];
v = [1;1;1];
tol = 1.0e-5;

[realEigVectors, realEigValues] = eig(A);
x = PowerMethodForPartD(A, v, tol); % Eigenvalue using modified PowerMethod

fprintf('Smallest Generated Eigenvalue = %.4f, Smallest Actual Eigenvalue = %.4f', x, realEigValues(2));