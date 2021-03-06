A = [2 3 2; 1 0 -2; -1 -3 -1];
v = [2;3;2];
tol = 1.0e-5;

[realEigVectors, realEigValues] = eig(A);
x = PowerMethodFIXED(A, v, tol); % Eigenvalue

fprintf('Largest Generated Eigenvalue = %.4f, Largest Actual Eigenvalue = %.4f\n', x, realEigValues(1));

% Fails because dominant eigenvalue has same magnitude as another