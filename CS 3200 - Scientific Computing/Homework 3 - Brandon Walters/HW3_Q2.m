clear all
% Arrays to hold all residual results
t = zeros(1,200);
normb = zeros(1,200);
normbRefined = normb;
normbSingle = normb;
normbSingleRefined = normb;
mormbb= normb;
normbre= normb;
model = zeros(1,200);
for m = 1:200
    n = m*10;
    A = rand(n,n);
    b = rand(n,1);
    x = A\b;
    % Single precision calculations
    xSingle = single(x);
    ASingle = single(A);
    bSingle = single(b);
    
    % Initial residual calculations for double and single-precision
    r = A*x-b;
    rSingle = ASingle*xSingle-bSingle;
    for i = 1:3
        % If the original residual, store in our arrays
        if i == 1
            normb(m) = norm(r,inf);
            normbSingle(m) = norm(rSingle,inf);
        end
        % If the second step, store as our refined residual
        if i == 3
            normbRefined(m) = norm(r,inf);
            normbSingleRefined(m) = norm(rSingle,inf);
        end
        % Calculate our new residuals
        d = A\r;
        dSingle = ASingle\rSingle;
        x = x - d;
        xSingle = xSingle - dSingle;
        
        r = A*x-b;
        rSingle = ASingle*xSingle-bSingle;
    end
    nt(m) = n;
end 
% Plot
semilogy(nt,normb,nt,normbRefined,nt,normbSingle,nt,normbSingleRefined)
legend('double precision residual', 'refined DP residual', 'single precision residual', 'refined SP residual')
title('Residual Values Using Single and Double Precision');