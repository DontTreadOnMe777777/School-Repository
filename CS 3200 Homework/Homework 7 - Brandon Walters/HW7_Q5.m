% QuadTX version with timing
fprintf('  tol            Q                 fcount           Time           err       ratio \n')
for  k = 7:14
    tol = 10^(-k);
    Qexact=0.53159445191300;
    tic
    [Q,fcount] = quadtx(@(x) (cos(x^3))^200,0,3,tol);
    time = toc;
    err=Q-Qexact;
    ratio = err/tol;
    fprintf('%8.0e %21.14f %7d %21.6f %13.3e %9.3f \n',tol,Q,fcount,time,err,ratio)
end

% Regular Simpson version, running on same point array as Q3
itr = 1;
for i = 3:8
    npts  =  2^(i+1)+1; nptsm1=npts-1; 

    x = linspace(0,3,npts);
    h = 3/(npts-1); %spacing of intervals 
    Simpint(i-2)= 0.0;
    
    %for p = [2 3 4 5 6 8]
        tic
        for ii =1:nptsm1 % calculate estimate of integral
            xmid= (x(ii)+x(ii+1))*0.5;  
            Simpint(i-2)  =  Simpint(i-2)+ h*(cos(x(ii)^3)^200) + (cos(x(ii+1)^3)^200)+ 4.0*(cos(xmid^3)^200)/6.0;
        end
        timingArray(itr) = toc;
        finalSimpSecond(itr)= Simpint(i-2);
        fprintf("Num of points = %7d  Time = %21.6f\n",npts,timingArray(itr));
        itr = itr + 1;
    %end
end