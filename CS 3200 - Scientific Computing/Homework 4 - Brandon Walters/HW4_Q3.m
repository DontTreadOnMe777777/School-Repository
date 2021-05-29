% Gradient Descent example 
n = 8;
A=[9 -4 1 0 0 0 0 0; -4 6 -4 1 0 0 0 0; 1 -4 6 -4 1 0 0 0; 0 1 -4 6 -4 1 0 0; 0 0 1 -4 6 -4 1 0; 0 0 0 1 -4 6 -4 1; 0 0 0 0 1 -4 5 2; 0 0 0 0 0 1 -2 1];
AEig = eig(A);
b=1/8^4 * [1 1 1 1 1 1 1 1]';
x=zeros(n,1);
xReal = linsolve(A,b);
r = -A*x+b;
normVal=Inf; 
itr = 0;
tol = 1e-15;
% Algorithm: Gradient Descent%%
while normVal>tol
    xold=x;
    y = A*r;
    alpha  = (r'*r)/(r'*y);
    x = x + alpha*r;
    r = r - alpha* y;
    itr=itr+1;
    normVal=norm((xold-x),inf);
    normRes=norm(r,inf);
end
fprintf(' %i valnrm  %8.2e resnrm  %8.2e \n',itr,normVal,normRes)
fprintf(' x(1)= %8.2e x(2)= %8.2e x(3)= %8.2e \n',x(1),x(2),x(3));


% Conjugate Gradient example ! THIS ONE DOESN'T WORK !
n = 8;
A=[9 -4 1 0 0 0 0 0; -4 6 -4 1 0 0 0 0; 1 -4 6 -4 1 0 0 0; 0 1 -4 6 -4 1 0 0; 0 0 1 -4 6 -4 1 0; 0 0 0 1 -4 6 -4 1; 0 0 0 0 1 -4 5 2; 0 0 0 0 0 1 -2 1];
b=1/8^4 * [1 1 1 1 1 1 1 1]';
normVal=Inf;

[x, numIterations] = cgsolve(A, b);
residual = A*x-b;
normRes = norm(r,inf);
fprintf(' %i Resnrm  %8.2e  \n',numIterations,normRes);
fprintf(' x(1)= %8.2e x(2)= %8.2e x(3)= %8.2e \n',x(1),x(2),x(3));