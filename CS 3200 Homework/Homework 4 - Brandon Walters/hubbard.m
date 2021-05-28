function uval  = hubbard(x)
%
   pp = length(x);
   uval = ones(pp,1);
   t = 0.25;
   %for i=1:pp
       xc = 0.30 +t;
        ds = 0.5;
       ds = 0.25;
       ds = 0.1;
       ds2 = ds*0.5;
       % second front
       xl = xc - ds2;
       xr = xc + ds2;
       % first front
       xll = xc - 5.0*ds2;
       xrr = xc - 3.0*ds2;
       %disp(sprintf(' xll  %e xrr  %e xl %e xr %e',xll, xrr ,xl,xr));
      for i = 1:pp
        uval(i)= 0.1;
       if(x(i) <= xl & x(i)>= xrr )uval(i) = 1.1; end
       if( (x(i) > xll) & (x(i)< xrr))
         z = (x(i)-xll)/ds;
         z2= z*z;
         z3= z2*z;
         z4 =z2*z2;
    uval(i) = uval(i) + z3*z3*(-252.0*z4*z + 1386.0*z4 -3080.0*z3+3465.0*z2 -1980.0*z+462.0);
       end 
       if( (x(i) > xl) & (x(i)< xr))
         z = (xr - x(i))/ds;
         z2= z*z;
         z3= z2*z;
         z4 =z2*z2;
    uval(i) = uval(i) + z3*z3*(-252.0*z4*z + 1386.0*z4 -3080.0*z3+3465.0*z2 -1980.0*z+462.0);
       end 
   end

