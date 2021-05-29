% Gradient Descent example 
years = [-1 -0.8 -0.6 -0.4 -0.2 0 0.2 0.4 0.6 0.8 1]';
populations = [75.995 91.972 105.711 123.203 131.669 150.697 179.323 203.212 226.505 249.633 281.422]';
condition = zeros(8,1);
condItr = 1;


for n = 4:11
    
    A = zeros(11,n);% always zero everything! 
    for jj = 1:11
        for ii = 1:n
            A(jj,ii) = years(jj)^(ii-1);%vandermodematrix        
        end
    end

    condition(condItr) = cond(A); % Store condition number
    
    C = A'; % Transpose of vandermode matrix
    yy= C*populations;% Modified RHS 
    B = C*A;% B = A^T * A = normal matrix
    x=zeros(n,1);
    r = B*x - yy; % Initial residual
    
    
    normVal=Inf; 
    itr = 0;
    tol = 1e-5;
    % Algorithm: Gradient Descent%%
    while normVal>tol
        xold=x;
        yy = B*r;
        alpha  = (r'*r)/(r'*yy);
        x = x + alpha*r;
        r = r - alpha*yy;
        itr=itr+1;
        normVal=norm((xold-x),inf);
        normRes=norm(r,inf);
    end
    fprintf('%i %i valnrm  %8.2e resnrm  %8.2e VandCond %8.2e\n', n-1,itr,normVal,normRes,condition(condItr))
    condItr = condItr+1;
end