% Timing arrays to store time values
timeVandermonde = ones(1,2);
timePoly = ones(1,2);
timeBary = ones(1,2);
timeSpline = ones(1,2);

xArray = linspace(0,2,1001);

vanderXArray6 = linspace(0,2,6);
vanderXArray11 = linspace(0,2,11);
vanderXArray21 = linspace(0,2,21);
vanderXArray41 = linspace(0,2,41);
vanderXArray81 = linspace(0,2,81);
vanderXArray161 = linspace(0,2,161);
vanderXArray321 = linspace(0,2,321);
vanderXArray641 = linspace(0,2,641);

x = linspace(0,2,3)';
expx  = exp(x);
A = fliplr(vander(x));
a = A\expx;

yplot6 = ones(1,6)*a(3); 
yplot11 = ones(1,11)*a(3); 
yplot21 = ones(1,21)*a(3); 
yplot41 = ones(1,41)*a(3); 
yplot81 = ones(1,81)*a(3); 
yplot161 = ones(1,161)*a(3); 
yplot321 = ones(1,321)*a(3);
yplot641 = ones(1,641)*a(3); % calculate polynomial at plotting points 
yplotFull = ones(1,1001)*a(3);
% Time calculations
tic
for j = 1:2   
    yplot6 = yplot6.*vanderXArray6 + a(3-j);
    yplot11 = yplot11.*vanderXArray11 + a(3-j);
    yplot21 = yplot21.*vanderXArray21 + a(3-j);
    yplot41 = yplot41.*vanderXArray41 + a(3-j);
    yplot81 = yplot81.*vanderXArray81 + a(3-j);
    yplot161 = yplot161.*vanderXArray161 + a(3-j);
    yplot321 = yplot321.*vanderXArray321 + a(3-j);
    yplot641 = yplot641.*vanderXArray641 + a(3-j);
    yplotFull = yplotFull.*xArray + a(3-j);  % note vector operations  
end
% Store linear timing
timeVandermonde(1) = toc;

chebyshevX6 = ones(1,6);  
for k = 1:6       
    chebyshevX6(k) =  -cos(pi*(k-1.0)/(6.0-1.0)) + 1;        
end

chebyshevX11 = ones(1,11);   
for k = 1:11       
    chebyshevX11(k) =  -cos(pi*(k-1.0)/(11.0-1.0)) + 1;        
end

chebyshevX21 = ones(1,21);   
for k = 1:21       
    chebyshevX21(k) =  -cos(pi*(k-1.0)/(21.0-1.0)) + 1;        
end

chebyshevX41 = ones(1,41);  
for k = 1:41       
    chebyshevX41(k) =  -cos(pi*(k-1.0)/(41.0-1.0)) + 1;        
end

chebyshevX81 = ones(1,81);   
for k = 1:81       
    chebyshevX81(k) =  -cos(pi*(k-1.0)/(81.0-1.0)) + 1;        
end

chebyshevX161 = ones(1,161);   
for k = 1:161       
    chebyshevX161(k) =  -cos(pi*(k-1.0)/(161.0-1.0)) + 1;        
end

chebyshevX321 = ones(1,321);  
for k = 1:321       
    chebyshevX321(k) =  -cos(pi*(k-1.0)/(321.0-1.0)) + 1;        
end

chebyshevX641 = ones(1,641);  
for k = 1:641       
    chebyshevX641(k) =  -cos(pi*(k-1.0)/(641.0-1.0)) + 1;        
end

chebyshevXFull = ones(1,1001);   
for k = 1:1001       
    chebyshevXFull(k) =  -cos(pi*(k-1.0)/(1001.0-1.0)) + 1;        
end

yplot6Chebyshev = ones(1,6)*a(3); 
yplot11Chebyshev = ones(1,11)*a(3); 
yplot21Chebyshev = ones(1,21)*a(3); 
yplot41Chebyshev = ones(1,41)*a(3); 
yplot81Chebyshev = ones(1,81)*a(3); 
yplot161Chebyshev = ones(1,161)*a(3); 
yplot321Chebyshev = ones(1,321)*a(3);
yplot641Chebyshev = ones(1,641)*a(3); % calculate polynomial at plotting points 
yplotFullChebyshev = ones(1,1001)*a(3);

tic

for j = 1:2   
    yplot6Chebyshev = yplot6Chebyshev.*chebyshevX6 + a(3-j);
    yplot11Chebyshev = yplot11Chebyshev.*chebyshevX11 + a(3-j);
    yplot21Chebyshev = yplot21Chebyshev.*chebyshevX21 + a(3-j);
    yplot41Chebyshev = yplot41Chebyshev.*chebyshevX41 + a(3-j);
    yplot81Chebyshev = yplot81Chebyshev.*chebyshevX81 + a(3-j);
    yplot161Chebyshev = yplot161Chebyshev.*chebyshevX161 + a(3-j);
    yplot321Chebyshev = yplot321Chebyshev.*chebyshevX321 + a(3-j);
    yplot641Chebyshev = yplot641Chebyshev.*chebyshevX641 + a(3-j);
    yplotFullChebyshev = yplotFullChebyshev.*chebyshevXFull + a(3-j);  % note vector operations  
end
% Store linear timing + Chebyshev timing
timeVandermonde(2) = timeVandermonde(1) + toc;

exponentialArray = exp(xArray);
exponentialArrayChebyshev = exp(chebyshevXFull);

tic

poly6Linear = polyinterp(vanderXArray6, yplot6, xArray);
poly11Linear = polyinterp(vanderXArray11, yplot11, xArray);
poly21Linear = polyinterp(vanderXArray21, yplot21, xArray);
poly41Linear = polyinterp(vanderXArray41, yplot41, xArray);
poly81Linear = polyinterp(vanderXArray81, yplot81, xArray);
poly161Linear = polyinterp(vanderXArray161, yplot161, xArray);
poly321Linear = polyinterp(vanderXArray321, yplot321, xArray);
poly641Linear = polyinterp(vanderXArray641, yplot641, xArray);
% Store linear poly timing
timePoly(1) = toc;

poly6Chebyshev = polyinterp(chebyshevX6, yplot6Chebyshev, chebyshevXFull);
poly11Chebyshev = polyinterp(chebyshevX11, yplot11Chebyshev, chebyshevXFull);
poly21Chebyshev = polyinterp(chebyshevX21, yplot21Chebyshev, chebyshevXFull);
poly41Chebyshev = polyinterp(chebyshevX41, yplot41Chebyshev, chebyshevXFull);
poly81Chebyshev = polyinterp(chebyshevX81, yplot81Chebyshev, chebyshevXFull);
poly161Chebyshev = polyinterp(chebyshevX161, yplot161Chebyshev, chebyshevXFull);
poly321Chebyshev = polyinterp(chebyshevX321, yplot321Chebyshev, chebyshevXFull);
poly641Chebyshev = polyinterp(chebyshevX641, yplot641Chebyshev, chebyshevXFull);
% Store linear poly timing + Chebyshev timing
timePoly(2) = timePoly(1) + toc;

tic

bary6Linear = barylag([vanderXArray6(:), yplot6(:)], xArray');
bary11Linear = barylag([vanderXArray11(:), yplot11(:)], xArray');
bary21Linear = barylag([vanderXArray21(:), yplot21(:)], xArray');
bary41Linear = barylag([vanderXArray41(:), yplot41(:)], xArray');
bary81Linear = barylag([vanderXArray81(:), yplot81(:)], xArray');
bary161Linear = barylag([vanderXArray161(:), yplot161(:)], xArray');
bary321Linear = barylag([vanderXArray321(:), yplot321(:)], xArray');
bary641Linear = barylag([vanderXArray641(:), yplot641(:)], xArray');
% Store linear bary timing
timeBary(1) = toc;

bary6Chebyshev = barylag([chebyshevX6(:), yplot6Chebyshev(:)], chebyshevXFull');
bary11Chebyshev = barylag([chebyshevX11(:), yplot11Chebyshev(:)], chebyshevXFull');
bary21Chebyshev = barylag([chebyshevX21(:), yplot21Chebyshev(:)], chebyshevXFull');
bary41Chebyshev = barylag([chebyshevX41(:), yplot41Chebyshev(:)], chebyshevXFull');
bary81Chebyshev = barylag([chebyshevX81(:), yplot81Chebyshev(:)], chebyshevXFull');
bary161Chebyshev = barylag([chebyshevX161(:), yplot161Chebyshev(:)], chebyshevXFull');
bary321Chebyshev = barylag([chebyshevX321(:), yplot321Chebyshev(:)], chebyshevXFull');
bary641Chebyshev = barylag([chebyshevX641(:), yplot641Chebyshev(:)], chebyshevXFull');
% Store linear bary timing + Chebyshev timing
timeBary(2) = timeBary(1) + toc;

tic

spline6Linear = spline(vanderXArray6, yplot6, xArray);
spline11Linear = spline(vanderXArray11, yplot11, xArray);
spline21Linear = spline(vanderXArray21, yplot21, xArray);
spline41Linear = spline(vanderXArray41, yplot41, xArray);
spline81Linear = spline(vanderXArray81, yplot81, xArray);
spline161Linear = spline(vanderXArray161, yplot161, xArray);
spline321Linear = spline(vanderXArray321, yplot321, xArray);
spline641Linear = spline(vanderXArray641, yplot641, xArray);
% Store linear spline timing
timeSpline(1) = toc;

spline6Chebyshev = spline(chebyshevX6, yplot6Chebyshev, chebyshevXFull);
spline11Chebyshev = spline(chebyshevX11, yplot11Chebyshev, chebyshevXFull);
spline21Chebyshev = spline(chebyshevX21, yplot21Chebyshev, chebyshevXFull);
spline41Chebyshev = spline(chebyshevX41, yplot41Chebyshev, chebyshevXFull);
spline81Chebyshev = spline(chebyshevX81, yplot81Chebyshev, chebyshevXFull);
spline161Chebyshev = spline(chebyshevX161, yplot161Chebyshev, chebyshevXFull);
spline321Chebyshev = spline(chebyshevX321, yplot321Chebyshev, chebyshevXFull);
spline641Chebyshev = spline(chebyshevX641, yplot641Chebyshev, chebyshevXFull);
% Store linear spline timing + Chebyshev timing
timeSpline(2) = timeSpline(1) + toc;


% Error calc
sqrth = 1.0/sqrt(1001);
errorLinear = norm((yplotFull-exponentialArray),inf); % max value of error at points 
err2Linear = sqrth*norm((yplotFull-exponentialArray),2);

errorChebyshev = norm((yplotFullChebyshev-exponentialArrayChebyshev),inf); % max value of error at points 
err2Chebyshev = sqrth*norm((yplotFullChebyshev-exponentialArrayChebyshev),2);

fprintf('Linear infinity error = %8.2e \nChebyshev infinity error = %8.2e\n\n', errorLinear, errorChebyshev);
fprintf('Linear 2-Norm error = %8.2e \nChebyshev 2-Norm error = %8.2e\n\n', err2Linear, err2Chebyshev);

% Linear errors for plotting
errorLinear6 = norm((yplot6-exp(vanderXArray6)),inf);
errorChebyshev6 = norm((yplot6Chebyshev-exp(chebyshevX6)),inf);
errorLinear81 = norm((yplot81-exp(vanderXArray81)),inf);
errorChebyshev81 = norm((yplot81Chebyshev-exp(chebyshevX81)),inf);
errorLinear641 = norm((yplot641-exp(vanderXArray641)),inf);
errorChebyshev641 = norm((yplot641Chebyshev-exp(chebyshevX641)),inf);

linearVandermondeError = [errorLinear6 errorLinear81 errorLinear641 errorLinear];
chebyshevVandermondeError = [errorChebyshev6 errorChebyshev81 errorChebyshev641 errorChebyshev];


% Poly errors for plotting
errorLinear6Poly = norm((poly6Linear-exponentialArray),inf);
errorChebyshev6Poly = norm((poly6Chebyshev-exponentialArrayChebyshev),inf);
errorLinear81Poly = norm((poly81Linear-exponentialArray),inf);
errorChebyshev81Poly = norm((poly81Chebyshev-exponentialArrayChebyshev),inf);
errorLinear641Poly = norm((poly641Linear-exponentialArray),inf);
errorChebyshev641Poly = norm((poly641Chebyshev-exponentialArrayChebyshev),inf);

linearPolyError = [errorLinear6Poly errorLinear81Poly errorLinear641Poly];
chebyshevPolyError = [errorChebyshev6Poly errorChebyshev81Poly errorChebyshev641Poly];


% Bary errors for plotting
errorLinear6Bary = norm((bary6Linear-exponentialArray),inf);
errorChebyshev6Bary = norm((bary6Chebyshev-exponentialArrayChebyshev),inf);
errorLinear81Bary = norm((bary81Linear-exponentialArray),inf);
errorChebyshev81Bary = norm((bary81Chebyshev-exponentialArrayChebyshev),inf);
errorLinear641Bary = norm((bary641Linear-exponentialArray),inf);
errorChebyshev641Bary = norm((bary641Chebyshev-exponentialArrayChebyshev),inf);

linearBaryError = [errorLinear6Bary errorLinear81Bary errorLinear641Bary];
chebyshevBaryError = [errorChebyshev6Bary errorChebyshev81Bary errorChebyshev641Bary];


% Spline errors for plotting
errorLinear6Spline = norm((spline6Linear-exponentialArray),inf);
errorChebyshev6Spline = norm((spline6Chebyshev-exponentialArrayChebyshev),inf);
errorLinear81Spline = norm((spline81Linear-exponentialArray),inf);
errorChebyshev81Spline = norm((spline81Chebyshev-exponentialArrayChebyshev),inf);
errorLinear641Spline = norm((spline641Linear-exponentialArray),inf);
errorChebyshev641Spline = norm((spline641Chebyshev-exponentialArrayChebyshev),inf);

linearSplineError = [errorLinear6Spline errorLinear81Spline errorLinear641Spline];
chebyshevSplineError = [errorChebyshev6Spline errorChebyshev81Spline errorChebyshev641Spline];


% Plot data, including errors and timing of methods
figure
xlim([0 2])
subplot(1,2,1)
plot(xArray, exponentialArray, xArray, yplotFull, vanderXArray6, yplot6, vanderXArray11, yplot11, vanderXArray21, yplot21, vanderXArray41, yplot41, vanderXArray81, yplot81, vanderXArray161, yplot161, vanderXArray321, yplot321, vanderXArray641, yplot641);
legend(' True values','1001 linear Vandermonde values',' 6 values',' 11 values',' 21 values',' 41 values',' 81 values',' 161 values',' 321 values',' 641 values');
title('Linear Vandermonde Interpolation');

subplot(1,2,2)
plot(chebyshevXFull, exponentialArrayChebyshev, chebyshevXFull, yplotFullChebyshev, chebyshevX6, yplot6Chebyshev, chebyshevX11, yplot11Chebyshev, chebyshevX21, yplot21Chebyshev, chebyshevX41, yplot41Chebyshev, chebyshevX81, yplot81Chebyshev, chebyshevX161, yplot161Chebyshev, chebyshevX321, yplot321Chebyshev, chebyshevX641, yplot641Chebyshev);
legend(' True values', ' 1001 Chebyshev Vandermonde values', ' 6 values', ' 11 values', ' 21 values', ' 41 values', ' 81 values', ' 161 values', ' 321 values', ' 641 values');
title('Chebyshev Vandermonde Interpolation');

figure
xlim([0 2])
subplot(1,3,1)
plot(xArray, exponentialArray, xArray, poly6Linear, chebyshevXFull, poly81Chebyshev);
legend(' True values',' Linear Poly', ' Chebyshev Poly');
title('Polyinterp');

subplot(1,3,2)
plot(xArray, exponentialArray, xArray, bary21Linear, chebyshevXFull, bary641Chebyshev);
legend(' True values',' Linear Bary', ' Chebyshev Bary');
title('Barylag');

subplot(1,3,3)
plot(xArray, exponentialArray, xArray, spline641Linear, chebyshevXFull, spline641Chebyshev);
legend(' True values',' Linear Spline', ' Chebyshev Spline');
title('Cubic Spline');

figure
subplot(1,2,1)
xlim([0 2])
timeX = [1 2];
ylim([0 1.5])
plot(timeX, timeVandermonde, timeX, timePoly, timeX, timeBary, timeX, timeSpline);
legend(' Linear',' Polyinterp', ' Barylag', ' Cubic Spline');
title('Time Needed');

subplot(1,2,2)
xlim([0 1001])
axis tight
errorX = [6 81 641 1001];
errorXSmall = [6 81 641];
plot(errorX, linearVandermondeError, errorX, chebyshevVandermondeError, errorXSmall, linearPolyError, errorXSmall, chebyshevPolyError, errorXSmall, linearBaryError, errorXSmall, chebyshevBaryError, errorXSmall, linearSplineError, errorXSmall, chebyshevSplineError);
ylim([0 10])
legend(' Linear Vandermonde',' Chebyshev Vandermonde', ' Linear Poly', ' Cheby Poly', ' Linear Bary', ' Cheby Bary', ' Linear Spline', ' Cheby Spline');
title('Error at varying point values');