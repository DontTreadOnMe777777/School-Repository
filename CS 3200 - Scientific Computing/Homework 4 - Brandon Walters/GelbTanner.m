function uval  = GelbTanner(x)
%
   pp = length(x);
   uval = ones(pp,1);
   t = 0.25;
   for i=1:pp
       if x(i) < -0.5
             uval(i) = (2*exp(2*pi*(x(i)+1))-1-exp(pi))/(exp(pi)-1);
            else
             uval(i) = -sin((2*pi*x(i))/3.0 + pi/3);
            end
      end
   end

