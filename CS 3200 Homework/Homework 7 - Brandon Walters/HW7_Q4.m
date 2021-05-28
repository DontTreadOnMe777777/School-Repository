fprintf('  tol            Q                 fcount           Time           err       ratio \n')
for  k = 7:14
    tol = 10^(-k);
    Qexact=0.53159445191300; % Estimate for exact, couldn't calculate
    tic
    [Q,fcount] = quadtx(@(x) (cos(x^3))^200,0,3,tol);
    time = toc; % Store timing
    err=Q-Qexact;
    ratio = err/tol;
    fprintf('%8.0e %21.14f %7d %21.6f %13.3e %9.3f \n',tol,Q,fcount,time,err,ratio)
end