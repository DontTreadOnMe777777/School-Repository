% Arrays for our table
residualNormCG = zeros(1,5);
iterationsCG = zeros(1,5);
arrayItr = 1;
nArray = [4 8 12 32 40];
% CG Algorithm
for n = [4 8 12 32 40]
    A = hilb(n);
    b=ones(n,1);
    x=zeros(n,1);
    r = -A*x+b;
    tol = 0.1e-2;
    [x, niters] = cgsolve(A,b);
    resid = A*x-b;
    normRes= norm(resid,inf);
    residualNormCG(arrayItr) = normRes;
    iterationsCG(arrayItr) = niters;
    arrayItr = arrayItr + 1;
    fprintf(' %i Resnrm  %8.2e  \n',niters,normRes)
    fprintf(' x(1)= %8.2e x(2)= %8.2e x(3)= %8.2e x(4) = %8.2e\n',x(1),x(2),x(3),x(4))
end
% More arrays for table
residualNormGD = zeros(1,5);
iterationsGD = zeros(1,5);
arrayItr2 = 1;
% GD method
for n = [4 8 12 32 40]
    H = hilb(n);
    b=ones(n,1);
    x=zeros(n,1);
    realX = linsolve(H, b);
    r = -H*x+b;
    normVal=Inf; 
    itr = 0;
    tol = 0.1e-6;
    % Algorithm: Gradient Descent
    while normVal>tol
        xold=x;
        y = H*r;
        alpha  = (r'*r)/(r'*y) * 0.001;
        x = x + alpha*r;
        r = r - alpha* y;
        itr=itr+1;
        normVal=norm((xold-x),inf);
        normRes=norm(r,inf);
    end
    residualNormGD(arrayItr2) = normRes;
    iterationsGD(arrayItr2) = itr;
    arrayItr2 = arrayItr2 + 1;
    fprintf(' %i valnrm  %8.2e resnrm  %8.2e \n',itr,normVal,normRes)
    fprintf(' x(1)= %8.2e x(2)= %8.2e x(3)= %8.2e x(4) = %8.2e\n',x(1),x(2),x(3),x(4))
end
% Set up and display table
T = table(nArray(:), iterationsCG(:), residualNormCG(:), iterationsGD(:), residualNormGD(:));
uitable('Data',T{:,:},'ColumnName',{'Hilbert Matrix Size', 'Conjugate Gradient Iterations', 'Residual Norm for Conjugate Gradient', 'Gradient Descent Iterations', 'Residual Norm for Gradient Descent'},'Units', 'Normalized', 'Position',[0, 0, 1, 1]);