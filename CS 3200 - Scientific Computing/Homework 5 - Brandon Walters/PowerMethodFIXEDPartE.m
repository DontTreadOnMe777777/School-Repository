function [lambda] = PowerMethodFIXEDPartE(A, x0, tolerance)

Iteration = 0;
x_prev = x0;
success = 0;

for k=1:100    
    % Perform power iteration
    y = A*x_prev;
    x = y / norm(y,inf);
    lambda=(x'*y)/(x'*x); % ADDED LINE
    x = x*sign(x(1));
    
    Iteration = Iteration + 1;
    
    % Check for convergence
    if norm(x - x_prev, inf) < tolerance
        success = 1;
        break
    else
        x_prev = x;
    end    
end

if success == 0
    disp(['Power method failed to converge in ' int2str(Iteration) ' iterations.']);
end