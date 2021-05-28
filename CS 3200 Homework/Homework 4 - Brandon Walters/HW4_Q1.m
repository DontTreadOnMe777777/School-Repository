% Solution of x in Ax=b using Jacobi Method
A=[9 -4 1 0 0 0 0 0; -4 12 -4 1 0 0 0 0; 1 -4 12 -4 1 0 0 0; 0 1 -4 12 -4 1 0 0; 0 0 1 -4 12 -4 1 0; 0 0 0 1 -4 12 -4 1; 0 0 0 0 1 -4 9 2; 0 0 0 0 0 1 -2 9];
b=1/8^4 * [1 1 1 1 1 1 1 1]';
x=[0 0 0 0]';
n=size(x,1);
normVal=Inf; %% 
% * _*Tolerence for method*_
tol=1e-15; itr=0;
% Algorithm: Jacobi Method
while normVal>tol
    x_old=x;  
    for i=1:n        
        sigma=0;
        for j=1:i-1
           sigma=sigma+A(i,j)*x_old(j);
        end
        for j=i+1:n
           sigma=sigma+A(i,j)*x_old(j);
        end
        x(i)=(1/A(i,i))*(b(i)-sigma);
    end
    itr=itr+1;
    normVal=norm((x_old-x),inf);
end
fprintf('Solution of the system is : \n%f\n%f\n%f\n%f in %d iterations\n',x,itr);



% Solution of x in Ax=b using Gauss Method
A=[9 -4 1 0 0 0 0 0; -4 12 -4 1 0 0 0 0; 1 -4 12 -4 1 0 0 0; 0 1 -4 12 -4 1 0 0; 0 0 1 -4 12 -4 1 0; 0 0 0 1 -4 12 -4 1; 0 0 0 0 1 -4 9 2; 0 0 0 0 0 1 -2 9];
b=1/8^4 * [1 1 1 1 1 1 1 1]';
x=[0 0 0 0]';
n=size(x,1);
normVal=Inf; %% 
% * _*Tolerence for method*_
tol=1e-15; itr=0;
% Algorithm: Gauss Method
while normVal>tol
    x_old=x;  
    for i=1:n        
        sigma=0;
        for j=1:i-1
           sigma=sigma+A(i,j)*x(j);
        end
        for j=i+1:n
           sigma=sigma+A(i,j)*x_old(j);
        end
        x(i)=(1/A(i,i))*(b(i)-sigma);
    end
    itr=itr+1;
    normVal=norm((x_old-x),inf);
end
fprintf('Solution of the system is : \n%f\n%f\n%f\n%f in %d iterations\n',x,itr);




% Gradient Descent example 
n = 8;
A=[9 -4 1 0 0 0 0 0; -4 12 -4 1 0 0 0 0; 1 -4 12 -4 1 0 0 0; 0 1 -4 12 -4 1 0 0; 0 0 1 -4 12 -4 1 0; 0 0 0 1 -4 12 -4 1; 0 0 0 0 1 -4 9 2; 0 0 0 0 0 1 -2 9];
b=1/8^4 * [1 1 1 1 1 1 1 1]';
x=zeros(n,1);
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